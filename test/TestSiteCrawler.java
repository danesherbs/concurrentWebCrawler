import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestSiteCrawler {

    private final String TEST_WEBSITE = "http://tomblomfield.com/";
    private SiteCrawler siteCrawler = new SiteCrawler();

    @Test
    public void siteCrawlerDoesNotReturnNullGivenURL() throws InterruptedException {
        Trie trie = siteCrawler.crawl(TEST_WEBSITE);
        assertTrue(trie != null);
    }

    @Test
    public void siteCrawlerReturnsNonEmptyTrieGivenURL() throws InterruptedException {
        Trie trie = siteCrawler.crawl(TEST_WEBSITE);
        assertFalse(trie.isEmpty());
    }

}
