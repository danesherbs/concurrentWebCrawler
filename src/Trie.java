import java.util.HashMap;

public class Trie {

    private String root;
    private HashMap<String, Trie> children = new HashMap<>();

    /* trie initialised with empty root node and children */
    public Trie() {
        this.root = "";
    }

    /* only called in recursive case within trie */
    private Trie(String root) {
        this.root = root;
    }

    public boolean isEmpty() {
        return root.equals("") && this.children.isEmpty();
    }

    public void add(String elem) {
        if (elem.equals(root)) {
            return; // elem already inserted
        } else if (elem.startsWith(root)) {
            String remaining = elem.substring(root.length()); // trim root off head of elem
            for (Trie child : children.values()) {
                if (remaining.startsWith(child.root)) {
                    child.add(remaining);
                    return; // recursive insertion
                }
            }
            children.put(remaining, new Trie(remaining)); // add new child
        }
    }

    public boolean contains(String elem) {
        if (elem.equals(root)) {
            return true;
        } else {
            String remaining = elem.substring(root.length()); // trim root off head of elem
            for (Trie child : children.values()) {
                if (remaining.startsWith(child.root)) {
                    return child.contains(remaining);
                }
            }
            return false;
        }
    }

}
