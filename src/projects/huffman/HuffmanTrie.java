package projects.huffman;
import java.util.*;

/**
 * <p>{@code HuffmanTrie} is a binary trie that implements huffman coding. Through the use of a priority
 * queue to maintain the order of the character according to their occurrence in string, HuffmanTrie is
 * able to provide an encoding that requires a lot less space than as if ASCII is used. </p>
 *
 * @author Yige Feng 115674202
 * Credit: Tree visualizer from <a href ="https://github.com/JasonFil">Jason Filippou</a>
 */

public class HuffmanTrie {
    /* ******************************************************** *
     * **************** PRIVATE DATA ELEMENTS ***************** *
     * ******************************************************** */

    private TrieNode root;
    private String inputString;
    private int count;
    private int gTimestamp;
    private Hashtable<Character, Integer> occurrenceTable;
    private Hashtable<Character, String> encodingTable; // result of huffman encoding as a hashtable
    private PriorityQueue<TrieNode> pairPriorityQueue;

    /* ******************************************************** *
     * ******************* PRIVATE METHODS ******************** *
     * ******************************************************** */

    private int getTimestamp() {
        gTimestamp++;
        return gTimestamp;
    }

    private void buildOccurrenceTable() {
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);
            if (occurrenceTable.containsKey(c)) {
                int occ = occurrenceTable.remove(c);
                occurrenceTable.put(c, occ + 1);
            }
            else {
                occurrenceTable.put(c, 1);
                count++;
            }
        }
    }

    private void buildPriorityQueue() {
        if (!occurrenceTable.isEmpty()) {
            for (Map.Entry<Character, Integer> entry : occurrenceTable.entrySet()) {
                CharPair cp = new CharPair(entry.getKey(), entry.getValue(), getTimestamp());
                TrieNode node = new TrieNode(cp);
                pairPriorityQueue.add(node);
            }
        }
    }

    private void buildHuffmanTrie() {
        if (pairPriorityQueue.size() == 1) {
            TrieNode left = pairPriorityQueue.poll();
            left.setIsLeft(true);
            CharPair parent = new CharPair(left.getCharPair().getOccurrence(), getTimestamp());
            root = new TrieNode(parent, left, null);
            return;
        }
        while (pairPriorityQueue.size() > 1) {
            TrieNode left = pairPriorityQueue.poll();
            left.setIsLeft(true);
            TrieNode right = pairPriorityQueue.poll();
            right.setIsLeft(false);

            int parOcc = left.getCharPair().getOccurrence() + right.getCharPair().getOccurrence();
            CharPair parent = new CharPair(parOcc, getTimestamp());
            TrieNode newNode = new TrieNode(parent, left, right);

            pairPriorityQueue.add(newNode);
        }
        root = pairPriorityQueue.poll();
    }

    private void buildEncodingTable() {
        Stack<TrieNode> st = new Stack<>();
        StringBuilder str = new StringBuilder();
        st.push(root);

        while (!st.isEmpty()) {
            TrieNode node = st.pop();
            if (node != null) {
                st.add(node.getRight());
                st.add(node.getLeft());
                if (node != root) str.append(node.isLeft() ? "0" : "1");
                if (node.isLeaf()) {
                    encodingTable.put(node.getCharPair().getChr(), str.toString());
                    str.deleteCharAt(str.length() - 1);
                }
            }
        }
    }

    /* ******************************************************** *
     * ******************** PUBLIC METHODS ******************** *
     * ******************************************************** */
    /**
     * Constructor that takes in the inputString and initializes the HuffmanTrie.
     * @param inputString the string to encode
     */
    public HuffmanTrie(String inputString) {
        this.count = 0;
        this.gTimestamp = 0;
        this.inputString = inputString;
        this.encodingTable = new Hashtable<>();
        this.occurrenceTable = new Hashtable<>();
        this.pairPriorityQueue = new PriorityQueue<>(new TrieNodeComparator());
        buildOccurrenceTable();
        buildPriorityQueue();
        buildHuffmanTrie();
        buildEncodingTable();
    }

    /**
     * Returns the occurrence of that character.
     * [Note: Changed this method from returning the occurrence table to returning the occurrence of the character
     * I think there's more freedom in dealing with if user asks for occurrence of non-existing character in this way.]
     *
     * @return 0 if key doesn't exist, occurrence as an integer o.w.
     */
    public int getOccurrence(char key) {
        return this.occurrenceTable.containsKey(key) ? this.occurrenceTable.get(key) : 0;
    }

    /**
     * Returns the encoding of that character.
     * [Note: Changed this method from returning the encoding table to returning the encoding of the character
     * I think there's more freedom in dealing with if user asks for encoding of non-existing character in this way.]
     *
     * @return null if key doesn't exist, encoding as a string o.w.
     */
    public String getEncoding(char key) {
        return this.encodingTable.containsKey(key) ? this.encodingTable.get(key) : null;
    }

    /**
     * Searches the trie for a given key (an ascii character).
     *
     * @param key The input {@link char} key.
     * @return true iff key is in the trie, false otherwise.
     */
    public boolean search(char key) {
        return this.getOccurrence(key) != 0;
    }

    /**
     * Query the tree for emptiness. A tree is empty iff the inputString is empty.
     * @return {@code true} if the tree is empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return this.count == 0;
    }

    /**
     * Returns the number of different characters in the tree.
     * @return The number of different characters in the tree.
     */
    public int getCount() {
        return this.count;
    }

    /**
     * Returns the number of characters in the tree. Note that this is different from getCount().
     * For example, suppose the HuffmanTrie stores "good noon", it should return 5 for getCount(),
     * but 9 for getTotalOccurrence().
     *
     * @return The number of characters in the tree.
     */
    public int getTotalOccurrences() {
        return this.inputString.length();
    }

    /**
     * <p>Performs an <i>inorder traversal</i> of the HuffmanTrie. For custom testing purpose.
     *
     * @return An {@link Iterator} over the {@link char} keys stored in the trie, exposing the elements in <i>symmetric
     * order</i>.
     */
    public Iterator<CharPair> inorderTraversal() {
        return new TrieIterator();
    }

    private class TrieIterator implements Iterator<CharPair> {
        private Stack<TrieNode> stack;

        public TrieIterator() {
            stack = new Stack<>();
            TrieNode curr = root;
            if (!isEmpty()) pushByCurr(curr);
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public CharPair next() {
            TrieNode node = stack.pop();
            if (node != null && node.getRight() != null) pushByCurr(node.getRight());
            return node.getCharPair();
        }

        private void pushByCurr(TrieNode node) {
            while (node != null) {
                stack.push(node);
                node = node.getLeft();
            }
        }
    }

    /**
     * Finds the most frequent {@link char} stored in the HuffmanTrie.
     * @return <p>The most frequent {@link char} stored in this. If the trie is empty, null char should be
     * returned.
     *
     * <p>Ties should be broken in terms of <b>lexicographical order</b> of the character. For example,
     * if both 'e' with encoding "010" and 'f' with encoding '101' has the highest frequency 4, 'e'
     * should be returned.
     */
    public char getMostFrequentChar() {
        if (encodingTable.isEmpty()) {
            return (char) 0;
        }
        Map.Entry<Character, String> minSoFar = null;
        for (Map.Entry<Character, String> entry : encodingTable.entrySet()) {
            if (minSoFar == null || entry.getValue().compareTo(minSoFar.getValue()) < 0) {
                minSoFar = entry;
            }
        }
        return minSoFar.getKey();
    }

    /**
     * A simple tree description generator for VizTree/CompactVizTree. It returns a string representation for the Huffman Trie.
     * This tree representation follows jimblackler style(http://jimblackler.net/treefun/index.html).
     * To identify child-index (left/right), I use "*" as special character to indicate null leaves.
     * DO NOT EDIT!
     * @param verbose whether to print the tree description to stdout or not
     * @return An {@link ArrayList} that gives a string-fied representation of the KD-Tree.
     */
    public ArrayList<String> treeDescription(boolean verbose)
    {
        ArrayList<String> tree = new ArrayList<String>();
        this.treeDescription(root,"", tree, verbose);
        return tree;
    }
    /**
     * Private <b>recursive</b> help for treeDescription. DO NOT EDIT!
     * @param root the current subtree root
     * @param space tracks parent-child relationship
     * @param tree Arraylist containing the tree description
     * @param verbose whether to print the tree description to stdout or not
     */
    private void treeDescription(TrieNode root, String space, ArrayList<String> tree, boolean verbose)
    {
        if(root == null || root.getCharPair() == null)
        {
            tree.add(space+"*");
            return;
        }
        if (verbose) System.out.println(space + root.getCharPair().compactToString());

        tree.add(space + root.getCharPair().compactToString());
        treeDescription(root.getLeft(), space + " ", tree, verbose);
        treeDescription(root.getRight(), space + " ", tree, verbose);
    }
}
