import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestSiteCrawler {

    private SiteCrawler siteCrawler = new SiteCrawler();

    @Test
    public void siteCrawlerDoesNotReturnNullGivenURL() throws InterruptedException {
        Trie trie = siteCrawler.crawl("http://www.google.com");
        assertTrue(trie != null);
    }

    @Test
    public void siteCrawlerReturnsNonEmptyTrieGivenURL() throws InterruptedException {
        Trie trie = siteCrawler.crawl("http://www.google.com");
        assertFalse(trie.isEmpty());
    }

}
