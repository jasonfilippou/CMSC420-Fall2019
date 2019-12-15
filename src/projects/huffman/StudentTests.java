package projects.huffman;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;
import projects.visualization.CompactVizTree;

/**
 * A jUnit test suite for {@link HuffmanTrie}.
 *
 * @author Yige Feng 115674202
 */
public class StudentTests {
    private String path = "src/projects/huffman/";
    private HuffmanTrie trie1 = new HuffmanTrie("good noon");
    private HuffmanTrie trie2 = new HuffmanTrie("");

    @Test public void testEmptiness() {
        assertFalse("A HuffmanTrie with non-empty inputString should not be empty", trie1.isEmpty());
        assertTrue("A HuffmanTrie with empty inputString should be empty", trie2.isEmpty());
    }

    @Test public void testSearch() {
        assertFalse("Search for 'g' in empty trie2 should return false", trie2.search('g'));
        assertTrue("Search for ' ' in non-empty trie1 should return true", trie1.search(' '));
        assertTrue("Search for 'g' in non-empty trie1 should return true", trie1.search('g'));
        assertTrue("Search for 'o' in non-empty trie1 should return true", trie1.search('o'));
    }

    @Test public void testGetCount() {
        assertEquals("A HuffmanTrie with inputString 'good noon' should return 5 for getCount()", 5, trie1.getCount());
        assertEquals("A HuffmanTrie with empty inputString should return 0 for getCount()", 0, trie2.getCount());
    }

    @Test public void testGetTotalOccurrence() {
        assertEquals("A HuffmanTrie with inputString 'good noon' should return 9 for getTotalOccurrence()", 9, trie1.getTotalOccurrences());
        assertEquals("A HuffmanTrie with empty inputString should return 0 for getTotalOccurrence()", 0, trie2.getTotalOccurrences());
    }

    @Test public void testGetOccurrence() {
        assertEquals("A HuffmanTrie with empty inputString should return 0", 0, trie2.getOccurrence(' '));
        assertEquals("Occurrences of 'o' should be ", 4, trie1.getOccurrence('o'));
        assertEquals("Occurrences of 'n' should be ", 2, trie1.getOccurrence('n'));
        assertEquals("Occurrences of 'g' should be ", 1, trie1.getOccurrence('g'));
        assertEquals("Occurrences of ' ' should be ", 1, trie1.getOccurrence(' '));
        assertEquals("Occurrences of 'd' should be ", 1, trie1.getOccurrence('d'));
    }

    @Test public void testGetEncoding() {
        assertEquals("A HuffmanTrie with empty inputString should return null", null, trie2.getEncoding(' '));
        assertEquals("Encoding of 'o' should be ", "0", trie1.getEncoding('o'));
        assertEquals("Encoding of 'n' should be ", "10", trie1.getEncoding('n'));
        assertEquals("Encoding of 'g' should be ", "110", trie1.getEncoding('g'));
        assertEquals("Encoding of ' ' should be ", "1110", trie1.getEncoding(' '));
        assertEquals("Encoding of 'd' should be ", "1111", trie1.getEncoding('d'));
    }

    @Test public void testHuffmanTrie() {
        ArrayList<String> trieDescription1 = trie1.treeDescription(false);
        CompactVizTree visualizer1 = new CompactVizTree(120,40,10);
        visualizer1.drawBinaryTreeToFile(trieDescription1,path + "HuffmanTrie1");

        ArrayList<String> trieDescription2 = trie2.treeDescription(false);
        CompactVizTree visualizer2 = new CompactVizTree(120,40,10);
        visualizer2.drawBinaryTreeToFile(trieDescription2,path + "HuffmanTrie2");
    }

    @Test public void testGetMostFrequent() {
        assertEquals("A HuffmanTrie with inputString 'good noon' should return 'o' for getMostFrequentChar()", 'o', trie1.getMostFrequentChar());
        assertEquals("A HuffmanTrie with empty inputString should return null for getMostFrequentChar()", (char) 0, trie2.getMostFrequentChar());
    }

    @Test public void testIterator() {
        Iterator<CharPair> it = trie1.inorderTraversal();
        ArrayList<String> expectedRes = new ArrayList<>(Arrays.asList("(o, 4)", "(NULL, 9)", "(n, 2)", "(NULL, 5)", "(g, 1)", "(NULL, 3)", "(SPC, 1)", "(NULL, 2)", "(d, 1)"));
        for (String s : expectedRes) {
            assertTrue(it.hasNext());
            assertEquals(it.next().compactToString(), s);
        }
    }

    @Test public void testOneChar() {
        HuffmanTrie trie3 = new HuffmanTrie("g");
        ArrayList<String> trieDescription3 = trie3.treeDescription(false);
        CompactVizTree visualizer3 = new CompactVizTree(120,40,10);
        visualizer3.drawBinaryTreeToFile(trieDescription3,path + "HuffmanTrie3");

        assertFalse("A HuffmanTrie with non-empty inputString should not be empty", trie3.isEmpty());
        assertEquals("A HuffmanTrie with inputString 'g' should return 1 for getCount()", 1, trie3.getCount());
        assertEquals("A HuffmanTrie with inputString 'g' should return 1 for getTotalOccurrence()", 1, trie3.getTotalOccurrences());
        assertEquals("A HuffmanTrie with inputString 'g' should return 'g' for getMostFrequentChar()", 'g', trie3.getMostFrequentChar());

        assertTrue("Search for 'g' in trie3 should return true", trie3.search('g'));
        assertFalse("Search for ' ' in trie3 should return false", trie3.search(' '));

        assertEquals("Occurrences of 'g' should be ", 1, trie3.getOccurrence('g'));
        assertEquals("Occurrences of 'n' should be ", 0, trie3.getOccurrence(' '));

        assertEquals("Encoding of 'g' should be ", "0", trie3.getEncoding('g'));
        assertEquals("Encoding of 'o' should be ", null, trie3.getEncoding(' '));
    }

    @Test public void testTwoChar() {
        HuffmanTrie trie4 = new HuffmanTrie("gg");
        ArrayList<String> trieDescription4 = trie4.treeDescription(false);
        CompactVizTree visualizer4 = new CompactVizTree(120,40,10);
        visualizer4.drawBinaryTreeToFile(trieDescription4,path + "HuffmanTrie4");

        assertFalse("A HuffmanTrie with non-empty inputString should not be empty", trie4.isEmpty());
        assertEquals("A HuffmanTrie with inputString 'gg' should return 1 for getCount()", 1, trie4.getCount());
        assertEquals("A HuffmanTrie with inputString 'gg' should return 2 for getTotalOccurrence()", 2, trie4.getTotalOccurrences());
        assertEquals("A HuffmanTrie with inputString 'gg' should return 'g' for getMostFrequentChar()", 'g', trie4.getMostFrequentChar());

        assertTrue("Search for 'g' in trie4 should return true", trie4.search('g'));
        assertFalse("Search for ' ' in trie4 should return false", trie4.search(' '));

        assertEquals("Occurrences of 'g' should be ", 2, trie4.getOccurrence('g'));
        assertEquals("Occurrences of 'n' should be ", 0, trie4.getOccurrence(' '));

        assertEquals("Encoding of 'g' should be ", "0", trie4.getEncoding('g'));
        assertEquals("Encoding of 'o' should be ", null, trie4.getEncoding(' '));
    }
}