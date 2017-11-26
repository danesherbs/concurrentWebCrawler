public class Main {

    public static void main(String[] args) throws InterruptedException {

        SiteCrawler siteCrawler = new SiteCrawler();
        Trie trie = siteCrawler.crawl("https://www.imperial.ac.uk/");
        System.out.println(trie);

    }

}
