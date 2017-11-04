public class Main {

    public static void main(String[] args) throws InterruptedException {

        SiteCrawler siteCrawler = new SiteCrawler();
        Trie trie = siteCrawler.crawl("http://tomblomfield.com/");
        System.out.println(trie);

    }

}
