import org.junit.Test;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;


public class TestTrie {

    private Trie trie = new Trie();

    @Test
    public void initialisesToEmptyTrie() {
        assertTrue(trie.isEmpty());
    }

    @Test
    public void addingEmptyStringToTrieDoesntAddEmptyStringAsChild() {
        trie.add("");
        assertTrue(trie.isEmpty());
    }

    @Test
    public void addingNonEmptyStringToTrieAddsANonEmptyStringAsAChild() {
        trie.add("hello");
        assertFalse(trie.isEmpty());
    }

    @Test
    public void containsReturnsTrueForElementsInTrie() {
        trie.add("google.com/");
        trie.add("google.com/help/");
        trie.add("google.com/help/blog/");
        assertTrue(trie.contains("google.com/"));
        assertTrue(trie.contains("google.com/help/"));
        assertTrue(trie.contains("google.com/help/blog/"));
    }

    @Test
    public void containsReturnsFalseForElementsNotInTrie() {
        trie.add("google.com/");
        assertFalse(trie.contains("reddit.com/"));
    }

}
