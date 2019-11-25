package projects.huffman;
import org.junit.Test;
import java.util.Hashtable;

import static org.junit.Assert.*;

/**
 * A jUnit test suite for {@link HuffmanTrie}.
 *
 * @author Yige Feng 115674202
 */
public class StudentTests {
    private HuffmanTrie trie1 = new HuffmanTrie("good noon");
    private HuffmanTrie trie2 = new HuffmanTrie("");

    @Test public void testEmptiness() {
        assertFalse("A HuffmanTrie with non-empty inputString should not be empty", trie1.isEmpty());
        assertTrue("A HuffmanTrie with empty inputString should be empty", trie2.isEmpty());
    }

    @Test public void testSearch() {
        assertEquals("Search for 'g' in empty trie2 should return 0", 0, trie2.search('g'));
        assertEquals("Search for ' ' in non-empty trie1 should return 1", 1, trie1.search(' '));
        assertEquals("Search for 'g' in non-empty trie1 should return 1", 1, trie1.search('g'));
        assertEquals("Search for 'o' in non-empty trie1 should return 4", 4, trie1.search('o'));
    }

    @Test public void testGetCount() {
        assertEquals("A HuffmanTrie with inputString 'good noon' should return 5 for getCount()", 5, trie1.getCount());
        assertEquals("A HuffmanTrie with empty inputString should return 0 for getCount()", 0, trie2.getCount());
    }

    @Test public void testGetTotalOccurrence() {
        assertEquals("A HuffmanTrie with inputString 'good noon' should return 9 for getTotalOccurrence()", 9, trie1.getTotalOccurrence());
        assertEquals("A HuffmanTrie with empty inputString should return 0 for getTotalOccurrence()", 0, trie2.getTotalOccurrence());
    }

    @Test public void testOccurrenceTable() {
        Hashtable table1 = trie1.getOccurrenceTable();
        Hashtable table2 = trie2.getOccurrenceTable();
        assertEquals("A HuffmanTrie with empty inputString should return null", null, table2);
        assertEquals("Occurrences of 'o' should be ", 4, table1.get('o'));
        assertEquals("Occurrences of 'n' should be ", 2, table1.get('b'));
        assertEquals("Occurrences of 'g' should be ", 1, table1.get('g'));
        assertEquals("Occurrences of ' ' should be ", 1, table1.get(' '));
        assertEquals("Occurrences of 'd' should be ", 1, table1.get('d'));
    }

    @Test public void testGetEncoding() {
        Hashtable encoding1 = trie1.getEncoding();
        Hashtable encoding2 = trie2.getEncoding();
        assertEquals("A HuffmanTrie with empty inputString should return null", null, encoding2);
        assertEquals("Encoding of 'o' should be ", "0", encoding1.get('o'));
        assertEquals("Encoding of 'n' should be ", "10", encoding1.get('n'));
        assertEquals("Encoding of 'g' should be ", "110", encoding1.get('g'));
        assertEquals("Encoding of ' ' should be ", "1110", encoding1.get(' '));
        assertEquals("Encoding of 'd' should be ", "1111", encoding1.get('d'));
    }

    @Test public void testGetMostFrequent() {
        assertEquals("A HuffmanTrie with inputString 'good noon' should return 'o' for getMostFrequentChar()", 'o', trie1.getMostFrequentChar());
        assertEquals("A HuffmanTrie with empty inputString should return null for getMostFrequentChar()", null, trie2.getMostFrequentChar());
    }
}