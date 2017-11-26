import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class SiteCrawler {

    private final int MIN_THREAD_POOL_SIZE = 10;
    private final int MAX_THREAD_POOL_SIZE = 30;
    private final int THREAD_TIMEOUT_IN_SECONDS = 5;
    private final int EXECUTOR_SERVICE_TIMEOUT_IN_SECONDS = 20;
    private static final Logger logger = Logger.getLogger(SiteCrawler.class.getName());

    public Trie crawl(String homepage) throws InterruptedException {
        // set up structures for breath-first search
        Trie trie = new Trie();
        HashSet<String> seen = new HashSet<>();
        LinkedBlockingQueue<String> linkedQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Runnable> threadQueue = new LinkedBlockingQueue<>();
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(MIN_THREAD_POOL_SIZE,
                MAX_THREAD_POOL_SIZE, THREAD_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS, threadQueue);
        PageCrawler pageCrawler = new PageCrawler();

        // start crawling given link
        linkedQueue.add(homepage);
        while (!linkedQueue.isEmpty() || executorService.getActiveCount() > 0) {
            String link = linkedQueue.take();  // take head of queue; blocks until item is available
            if (!seen.contains(link)) {
                logger.info("Visiting " + link);
                trie.add(link);
                seen.add(link);
                executorService.execute(() -> {
                    linkedQueue.addAll(pageCrawler.getLinks(link, seen));
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
