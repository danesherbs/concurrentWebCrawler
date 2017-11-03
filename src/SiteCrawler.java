import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class SiteCrawler {

    private final int MIN_THREAD_POOL_SIZE = 0;
    private final int MAX_THREAD_POOL_SIZE = 30;
    private final int THREAD_TIMEOUT_IN_SECONDS = 5;
    private final int EXECUTOR_SERVICE_TIMEOUT_IN_SECONDS = 20;
    private static final Logger logger = Logger.getLogger(SiteCrawler.class.getName());
    private int maxNumberOfLinksInSitemap;

    public SiteCrawler() {
        this.maxNumberOfLinksInSitemap = -1;
    }

    public SiteCrawler(int maxNumberOfLinksInSitemap) {
        this.maxNumberOfLinksInSitemap = maxNumberOfLinksInSitemap;
    }

    public Trie crawl(String homepage) throws InterruptedException {
        // set up structures for breath-first search
        Trie trie = new Trie();
        BlockingQueue<String> linkQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Runnable> threadQueue = new LinkedBlockingQueue<>();
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(MIN_THREAD_POOL_SIZE,
                MAX_THREAD_POOL_SIZE, THREAD_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS, threadQueue);
        PageCrawler pageCrawler = new PageCrawler();

        // start crawling given link
        linkQueue.add(homepage);
        while (!linkQueue.isEmpty() || executorService.getPoolSize() > 0) {
            // stop if maximum number of links exceeded
            if (maxNumberOfLinksInSitemap > 0 && linkQueue.size() > maxNumberOfLinksInSitemap) {
                break;
            }
            String link = linkQueue.poll();
            // if no link in queue but will be in the future, wait until one becomes available
            if (link == null) {
                Thread.sleep(1);  // wait 1ms for queue to be populated
            } else if (!trie.contains(link)) {
                logger.info("Visiting " + link);
                trie.add(link);
                executorService.execute(() -> {
                    linkQueue.addAll(pageCrawler.getLinks(link));
                });
            }
        }

        // shutdown ExecutorService after current threads finish
        executorService.shutdown();

        // wait for EXECUTOR_SERVICE_TIMEOUT_IN_MINUTES before returning partially-complete trie
        try {
            executorService.awaitTermination(EXECUTOR_SERVICE_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("Timeout exceeded; returning partially-complete trie.");
        }

        return trie;
    }

}
