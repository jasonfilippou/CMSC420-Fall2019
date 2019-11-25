package projects.huffman;
import projects.UnimplementedMethodException;

import java.util.Iterator;
import java.util.Hashtable;

/**
 * <p>{@code HuffmanTrie} is a binary trie that implements huffman coding. Through the use of a priority
 * queue to maintain the order of the character according to their occurrence in string, HuffmanTrie is
 * able to provide an encoding that requires a lot less space than as if ASCII is used. </p>
 *
 * @author Yige Feng 115674202
 */

public class HuffmanTrie {

    // helper class for easy Enque/Deque in the priority queue.
    private static class CharPair {
        private char chr;
        private int occurrence;

        CharPair(char chr, int occurrence) {
            this.chr = chr;
            this.occurrence = occurrence;
        }
    }

    private static class TrieNode {
        private TrieNode left, right;
        private char chr;
        private int occurrence;

        // Default constructor
        TrieNode() {
            this.chr = (char)0; // ASCII code for null
            this.occurrence = 0;
            left = right = null;
        }

        // Non-default constructor
        TrieNode(char chr, int occurrence) {
            this.chr = chr;
            this.occurrence = occurrence;
            left = right = null;
        }
    }

    private TrieNode root;
    private Hashtable encoding; // result of huffman encoding as a hashtable

    /* ********************************************************* *
     * Write any private data elements or private methods here...*
     * ********************************************************* */



    /* ******************************************************** *
     * ************************ PUBLIC METHODS **************** *
     * ******************************************************** */
    /**
     * Constructor that takes in the inputString and initializes the HuffmanTrie.
     * @param inputString the string to encode
     */
    public HuffmanTrie(String inputString) {
        throw new UnimplementedMethodException(); // ERASE THIS LINE AFTER YOU IMPLEMENT THE METHOD!
    }

    /**
     * Returns the occurrence table.
     *
     * @return empty hashtable if HuffmanTrie is empty, a hashtable otherwise:
     * {key = ascii character, value = occurrence of the character in the string}.
     * E.g. "good noon" --> {'g': 1, 'o': 4, 'n': 2, 'd': 1, ' ': 1}
     */
    public Hashtable getOccurrenceTable() {
        throw new UnimplementedMethodException(); // ERASE THIS LINE AFTER YOU IMPLEMENT THE METHOD!
    }

    /**
     * Returns the encoding.
     *
     * @return empty hashtable if HuffmanTrie is empty, a hashtable otherwise:
     * {key = ascii character, value = encoding as a string of 0, 1}.
     */
    public Hashtable getEncoding() {
        throw new UnimplementedMethodException(); // ERASE THIS LINE AFTER YOU IMPLEMENT THE METHOD!
    }

    /**
     * Searches the trie for a given key (an ascii character).
     *
     * @param key The input {@link char} key.
     * @return occurrence of the key if and only if key is in the trie, {@code 0} otherwise.
     */
    public int search(char key) {
        throw new UnimplementedMethodException(); // ERASE THIS LINE AFTER YOU IMPLEMENT THE METHOD!
    }

    /**
     * Query the tree for emptiness. A tree is empty iff the inputString is empty.
     * @return {@code true} if the tree is empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        throw new UnimplementedMethodException();       // ERASE THIS LINE AFTER YOU IMPLEMENT THIS METHOD!
    }

    /**
     * Returns the number of different characters in the tree.
     * @return The number of different characters in the tree.
     */
    public int getCount() {
        throw new UnimplementedMethodException(); // ERASE THIS LINE AFTER YOU IMPLEMENT THE METHOD!
    }

    /**
     * Returns the number of characters in the tree. Note that this is different from getCount().
     * For example, suppose the HuffmanTrie stores "good noon", it should return 5 for getCount(),
     * but 9 for getTotalFreq().
     *
     * @return The number of characters in the tree.
     */
    public int getTotalOccurrence() {
        throw new UnimplementedMethodException(); // ERASE THIS LINE AFTER YOU IMPLEMENT THE METHOD!
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
