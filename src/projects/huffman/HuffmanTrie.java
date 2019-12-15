package projects.huffman;
import projects.UnimplementedMethodException;
import sun.text.normalizer.Trie;

import java.util.*;

/**
 * <p>{@code HuffmanTrie} is a binary trie that implements huffman coding. Through the use of a priority
 * queue to maintain the order of the character according to their occurrence in string, HuffmanTrie is
 * able to provide an encoding that requires a lot less space than as if ASCII is used. </p>
 *
 * @author Yige Feng 115674202
 */

public class HuffmanTrie {
    /* ******************************************************** *
     * **************** PRIVATE DATA ELEMENTS ***************** *
     * ******************************************************** */

    private TrieNode root;
    private String inputString;
    private int count;
    private int height;
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
                occurrenceTable.put(c, 0);
                count++;
            }
        }
    }

    private void buildPriorityQueue() {
        if (!occurrenceTable.isEmpty()) {
            Enumeration<Character> enu = occurrenceTable.keys();
            while (enu.hasMoreElements()) {
                char c = enu.nextElement();
                int occ = occurrenceTable.get(c);
                CharPair cp = new CharPair(c, occ, getTimestamp());
                TrieNode node = new TrieNode(cp);
                pairPriorityQueue.add(node);
            }
        }
    }

    private void buildHuffmanTrie() {
        while (!pairPriorityQueue.isEmpty()) {
            TrieNode left = pairPriorityQueue.poll();
            left.setIsLeft(true);
            TrieNode right = pairPriorityQueue.poll();
            right.setIsLeft(false);

            int parOcc = left.getCharPair().getOccurrence() + right.getCharPair().getOccurrence();
            CharPair parent = new CharPair(parOcc, getTimestamp());
            TrieNode newNode = new TrieNode(parent, left, right);

            pairPriorityQueue.add(newNode);
            root = newNode;
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
        this.pairPriorityQueue = new PriorityQueue<>();
        buildOccurrenceTable();
        buildPriorityQueue();
        buildHuffmanTrie();
    }

    /**
     * Returns the occurrence table.
     *
     * @return empty hashtable iff inputString is empty, a hashtable otherwise:
     * {key = ascii character, value = occurrence of the character in the string}.
     * E.g. "good noon" --> {'g': 1, 'o': 4, 'n': 2, 'd': 1, ' ': 1}
     */
    public Hashtable getOccurrenceTable() {
        return occurrenceTable;
    }

    /**
     * Returns the encoding.
     *
     * @return empty hashtable iff inputString is empty, a hashtable otherwise:
     * {key = ascii character, value = encoding as a string of 0, 1}.
     * E.g. "good noon" --> {'g': "110", 'o': "0", 'n': "10", 'd': "1111", ' ': "1110"}
     */
    public Hashtable getEncoding() {
        return this.encodingTable;
    }

    /**
     * Searches the trie for a given key (an ascii character).
     *
     * @param key The input {@link char} key.
     * @return occurrence of the key if and only if key is in the trie, {@code 0} otherwise.
     */
    public int search(char key) {
        return this.occurrenceTable.get(key);
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
        throw new UnimplementedMethodException(); // ERASE THIS LINE AFTER YOU IMPLEMENT THE METHOD!
    }

    /**
     * Finds the most frequent {@link char} stored in the HuffmanTrie.
     * @return <p>The most frequent {@link char} stored in this. If the trie is empty, null should be
     * returned.
     *
     * <p>Ties should be broken in terms of <b>lexicographical order</b> of the character. For example,
     * if both 'e' with encoding "010" and 'f' with encoding '101' has the highest frequency 4, 'e'
     * should be returned.
     */
    public char getMostFrequentChar() {
        throw new UnimplementedMethodException(); // ERASE THIS LINE AFTER YOU IMPLEMENT THE METHOD!
    }
}
