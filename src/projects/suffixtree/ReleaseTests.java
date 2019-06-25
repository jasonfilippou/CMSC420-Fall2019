package projects.suffixtree;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

//@SuppressWarnings("unused")

/**
 * A jUnit test suite for {@link SuffixTree}.
 *
 * @author <a href = "https://github.com/JasonFil">Jason Filippou</a> &amp; <a href ="mailto:pchiang@cs.umd.edu">Ping Chiang</a>
*/
public class ReleaseTests {


    /************************************** Testing each function ****************************/
    @Test public void testConstructor() {
        SuffixTree trie = new SuffixTree("aabbcc");
        try {
            trie = new SuffixTree(""); //should throw illegal argument exception
            fail("Expected an IllegalArgumentException to be thrown when calling the constructor with an empty string");
        }
        catch(Exception e){
            assertEquals("Expected an IllegalArgumentException to be thrown when calling the constructor with an null string, but got something else",IllegalArgumentException.class, e.getClass());
        }
        try {
            trie = new SuffixTree(null); //should throw illegal argument exception
            fail("Expected an IllegalArgumentException to be thrown when calling the constructor with an empty string");
        }
        catch(Exception e){
            assertEquals("Expected an IllegalArgumentException to be thrown when calling the constructor with an empty string, but got something else",IllegalArgumentException.class, e.getClass());
        }
        try {
            trie = new SuffixTree("b$b"); //should throw illegal argument exception
            fail("Expected an IllegalArgumentException to be thrown when calling the constructor with a string including $ sign");
        }
        catch(Exception e){
            assertEquals("Expected an IllegalArgumentException to be thrown when calling the constructor with a string including $ sign",IllegalArgumentException.class, e.getClass());
        }
    }
    @Test public void testMatches() {
        //case 1: trying to find matches that exists
        SuffixTree tree = new SuffixTree("aabbaab");

        List<Integer> actual = tree.matches("ab");
        Collections.sort(actual);
        List<Integer> expected = new ArrayList<>(Arrays.asList(1,5));
        assertEquals("searched for a valid substring",expected, actual);

        actual = tree.matches("ba");
        Collections.sort(actual);
        expected = new ArrayList<>(Arrays.asList(3));
        assertEquals("searched for a valid substring",expected, actual);

        actual = tree.matches("a");
        Collections.sort(actual);
        expected = new ArrayList<>(Arrays.asList(0,1,4,5));
        assertEquals("searched for a valid substring",expected, actual);

        actual = tree.matches("b");
        Collections.sort(actual);
        expected = new ArrayList<>(Arrays.asList(2,3,6));
        assertEquals("searched for a valid substring",expected, actual);

        actual = tree.matches("bb");
        Collections.sort(actual);
        expected = new ArrayList<>(Arrays.asList(2));
        assertEquals("searched for a valid substring",expected, actual);

        actual = tree.matches("aa");
        Collections.sort(actual);
        expected = new ArrayList<>(Arrays.asList(0,4));
        assertEquals("searched for a valid substring", expected, actual);


        //case 2: trying to find matches that doesn't exists
        actual = tree.matches("cc");
        Collections.sort(actual);
        expected = new ArrayList<>();
        assertEquals("searched for an invalid match", expected, actual);

        actual = tree.matches("abbb");
        Collections.sort(actual);
        expected = new ArrayList<>();
        assertEquals("searched for an invalid match", expected, actual);

        actual = tree.matches("b$");
        Collections.sort(actual);
        expected = new ArrayList<>();
        assertEquals("searched for a valid substring that includes the $ sign", expected, actual);

        //case 3: trying to find matches of an empty string
        try {
            actual = tree.matches(""); //should throw illegal argument exception
            fail("Expected an IllegalArgumentException to be thrown when calling matches method with an empty string");
        }
        catch(Exception e){
            assertEquals("Expected an IllegalArgumentException to be thrown when calling matches method with an empty string, but got something else",IllegalArgumentException.class, e.getClass());
        }
        try {
            actual = tree.matches(null); //should throw illegal argument exception
            fail("Expected an IllegalArgumentException to be thrown when calling matches method with a null string");
        }
        catch(Exception e){
            assertEquals("Expected an IllegalArgumentException to be thrown when calling matches method with a null string, but got something else",IllegalArgumentException.class, e.getClass());
        }
    }
    @Test public void testIsSubstring() {
        SuffixTree tree = new SuffixTree("aabbccdaab");
        //case 1: checking for a valid substring
        assertTrue("isSubstring should return true, but got false instead", tree.isSubstring("a"));
        assertTrue("isSubstring should return true, but got false instead", tree.isSubstring("aa"));
        assertTrue("isSubstring should return true, but got false instead", tree.isSubstring("ab"));
        assertTrue("isSubstring should return true, but got false instead", tree.isSubstring("bb"));
        assertTrue("isSubstring should return true, but got false instead", tree.isSubstring("bc"));
        assertTrue("isSubstring should return true, but got false instead", tree.isSubstring("cc"));
        assertTrue("isSubstring should return true, but got false instead", tree.isSubstring("cd"));
        assertTrue("isSubstring should return true, but got false instead", tree.isSubstring("da"));
        assertTrue("isSubstring should return true, but got false instead", tree.isSubstring("aabb"));
        assertTrue("isSubstring should return true, but got false instead", tree.isSubstring("bbcc"));
        assertTrue("isSubstring should return true, but got false instead", tree.isSubstring("aabbcc"));
        assertTrue("isSubstring should return true, but got false instead", tree.isSubstring("aabbccd"));
        assertTrue("isSubstring should return true, but got false instead", tree.isSubstring("aabbccdaab"));

        //case 2: checking for an invalid substring
        assertFalse("isSubstring should return false, but got true instead", tree.isSubstring("e"));
        assertFalse("isSubstring should return false, but got true instead", tree.isSubstring("aabbcd"));
        assertFalse("isSubstring should return false for a query including $, but got true instead", tree.isSubstring("ccdaab$"));

        //case 3: checking for an empty substring
        try {
            tree.isSubstring(""); //should throw illegal argument exception
            fail("Expected an IllegalArgumentException to be thrown when calling isSubstring method with an empty string");
        }
        catch(Exception e){
            assertEquals("Expected an IllegalArgumentException to be thrown when calling isSubstring method with an empty string, but got something else",IllegalArgumentException.class, e.getClass());
        }
        try {
            tree.isSubstring(null); //should throw illegal argument exception
            fail("Expected an IllegalArgumentException to be thrown when calling isSubstring method with an null string");
        }
        catch(Exception e){
            assertEquals("Expected an IllegalArgumentException to be thrown when calling isSubstring method with a null string, but got something else",IllegalArgumentException.class, e.getClass());
        }
    }
    @Test public void testNumTimesOccurs() {
        SuffixTree tree = new SuffixTree("aabbccdaab");
        //case 1: the string appears once or more
        assertEquals("numTimesOccurs method return incorrect nodeCount", 1, tree.numTimesOccurs("aabb"));
        assertEquals("numTimesOccurs method return incorrect nodeCount", 2, tree.numTimesOccurs("aa"));
        assertEquals("numTimesOccurs method return incorrect nodeCount", 4, tree.numTimesOccurs("a"));
        assertEquals("numTimesOccurs method return incorrect nodeCount", 1, tree.numTimesOccurs("bbcc"));
        assertEquals("numTimesOccurs method return incorrect nodeCount", 1, tree.numTimesOccurs("cd"));
        assertEquals("numTimesOccurs method return incorrect nodeCount", 3, tree.numTimesOccurs("b"));

        //case 2: the string does not appear
        assertEquals("numTimesOccurs method return incorrect nodeCount", 0, tree.numTimesOccurs("e"));
        assertEquals("numTimesOccurs method return incorrect nodeCount", 0, tree.numTimesOccurs("ac"));
        assertEquals("numTimesOccurs method return incorrect nodeCount", 0, tree.numTimesOccurs("bd"));
        assertEquals("numTimesOccurs method return incorrect nodeCount", 0, tree.numTimesOccurs("ca"));
        assertEquals("numTimesOccurs method return incorrect nodeCount for a query including $", 0, tree.numTimesOccurs("b$"));

        //case 3: the string is empty
        try {
            tree.numTimesOccurs(""); //should throw illegal argument exception
            fail("Expected an IllegalArgumentException to be thrown when calling numTimesOccurs method with an empty string");
        }
        catch(Exception e){
            assertEquals("Expected an IllegalArgumentException to be thrown when calling numTimesOccurs method with an empty string, but got something else",IllegalArgumentException.class, e.getClass());
        }
        try {
            tree.numTimesOccurs(null); //should throw illegal argument exception
            fail("Expected an IllegalArgumentException to be thrown when calling numTimesOccurs method with a null string");
        }
        catch(Exception e){
            assertEquals("Expected an IllegalArgumentException to be thrown when calling numTimesOccurs method with a null string, but got something else",IllegalArgumentException.class, e.getClass());
        }
    }
    @Test public void testLongestRepeatedSubstring() {
        SuffixTree tree;
        //case 1: check for cases where there is no ties
        tree = new SuffixTree("aabbaab");
        assertEquals("The longestRepeatedSubstring() method does not return the correct substring",
                tree.longestRepeatedSubstring(), "aab");
        tree = new SuffixTree("aabbaabbaab");
        assertEquals("The longestRepeatedSubstring() method does not return the correct substring",
                tree.longestRepeatedSubstring(), "aabbaab");
        tree = new SuffixTree("aabbccaabbccaab");
        assertEquals("The longestRepeatedSubstring() method does not return the correct substring",
                tree.longestRepeatedSubstring(), "aabbccaab");
        tree = new SuffixTree("abcdefghi");
        assertEquals("The longestRepeatedSubstring() method does not return the correct substring",
                tree.longestRepeatedSubstring(), "");
        tree = new SuffixTree("abcdefghia");
        assertEquals("The longestRepeatedSubstring() method does not return the correct substring",
                tree.longestRepeatedSubstring(), "a");

        //case 2: check for cases where there is ties, tie break based on lexicographical order
        tree = new SuffixTree("aabbaabaaaa");
        assertEquals("The longestRepeatedSubstring() method does not return the correct substring",
                tree.longestRepeatedSubstring(), "aaa");
                //"aaa" is returned as opposed to "aab" because "aaa" comes before "aab" in lexicographical order
        tree = new SuffixTree("aabbaabbaabcccccccc");
        assertEquals("The longestRepeatedSubstring() method does not return the correct substring",
                tree.longestRepeatedSubstring(), "aabbaab");
        tree = new SuffixTree("abcdabcdefghefgh");
        assertEquals("The longestRepeatedSubstring() method does not return the correct substring",
                tree.longestRepeatedSubstring(), "abcd");
        tree = new SuffixTree("fffff");
        assertEquals("The longestRepeatedSubstring() method does not return the correct substring",
                tree.longestRepeatedSubstring(), "ffff");
    }
    @Test public void testShortestRepeatedSubstring() {
        SuffixTree tree;
        //case 1: when there is ties, tie break based on lexicographical order
        tree = new SuffixTree("ffeeddccbbaa");
        assertEquals("The shortestRepeatedSubstring() method does not return the correct substring",
                tree.shortestRepeatedSubstring(1), "a");
        assertEquals("The shortestRepeatedSubstring() method does not return the correct substring",
                tree.shortestRepeatedSubstring(2), null);
        assertEquals("The shortestRepeatedSubstring() method does not return the correct substring",
                tree.shortestRepeatedSubstring(3), null);

        tree = new SuffixTree("bbaabbaa");
        assertEquals("The shortestRepeatedSubstring() method does not return the correct substring",
                tree.shortestRepeatedSubstring(1), "a");
        assertEquals("The shortestRepeatedSubstring() method does not return the correct substring",
                tree.shortestRepeatedSubstring(2), "aa");
        assertEquals("The shortestRepeatedSubstring() method does not return the correct substring",
                tree.shortestRepeatedSubstring(3), "baa");
        assertEquals("The shortestRepeatedSubstring() method does not return the correct substring",
                tree.shortestRepeatedSubstring(4), "bbaa");
        assertEquals("The shortestRepeatedSubstring() method does not return the correct substring",
                tree.shortestRepeatedSubstring(5), null);

        tree = new SuffixTree("cccaacccaa");
        assertEquals("The shortestRepeatedSubstring() method does not return the correct substring",
                tree.shortestRepeatedSubstring(1), "a");
        assertEquals("The shortestRepeatedSubstring() method does not return the correct substring",
                tree.shortestRepeatedSubstring(2), "aa");
        assertEquals("The shortestRepeatedSubstring() method does not return the correct substring",
                tree.shortestRepeatedSubstring(3), "caa");
        assertEquals("The shortestRepeatedSubstring() method does not return the correct substring",
                tree.shortestRepeatedSubstring(4), "ccaa");
        assertEquals("The shortestRepeatedSubstring() method does not return the correct substring",
                tree.shortestRepeatedSubstring(5), "cccaa");
        assertEquals("The shortestRepeatedSubstring() method does not return the correct substring",
                tree.shortestRepeatedSubstring(6), null);
        assertEquals("The shortestRepeatedSubstring() method does not return the correct substring",
                tree.shortestRepeatedSubstring(7), null);

        //case 2: check for cases where the length argument is greater than the length of the base string
        tree = new SuffixTree("cccaacccaa");
        assertEquals("The shortestRepeatedSubstring() should return null",
                tree.shortestRepeatedSubstring(20), null);

        //case 3: check for cases where the minLength argument is invalid (equal to or smaller than 0)
        tree = new SuffixTree("cccaacccaa");
        try {
            tree.shortestRepeatedSubstring(0); //should throw illegal argument exception
            fail("Expected an IllegalArgumentException to be thrown when calling shortestRepeatedSubstring method with length zero");
        }
        catch(Exception e){
            assertEquals("Expected an IllegalArgumentException to be thrown when calling shortestRepeatedSubstring method with length zero, but got something else",IllegalArgumentException.class, e.getClass());
        }
        try {
            tree.shortestRepeatedSubstring(-1); //should throw illegal argument exception
            fail("Expected an IllegalArgumentException to be thrown when calling shortestRepeatedSubstring method with length zero");
        }
        catch(Exception e){
            assertEquals("Expected an IllegalArgumentException to be thrown when calling shortestRepeatedSubstring method with length zero, but got something else",IllegalArgumentException.class, e.getClass());
        }
    }
    @Test public void testCount() {
        SuffixTree tree;
        //case 1: every suffix splits immediately
        tree = new SuffixTree("abcdef");
        assertEquals("The nodeCount() method does not return the correct number",
                tree.nodeCount(), 6+2+0);

        //case 2: some repeated strings
        tree = new SuffixTree("aab");
        assertEquals("The nodeCount() method does not return the correct number",
                tree.nodeCount(), 3+2+1);
        tree = new SuffixTree("aaabb");
        assertEquals("The nodeCount() method does not return the correct number",
                tree.nodeCount(), 5+2+3);
        tree = new SuffixTree("cccccc");
        assertEquals("The nodeCount() method does not return the correct number",
                tree.nodeCount(), 6+2+5);
        tree = new SuffixTree("aaabbb");
        assertEquals("The nodeCount() method does not return the correct number",
                tree.nodeCount(), 6+2+4);
    }
    @Test public void testinOrderTraversal() {
        SuffixTree tree;
        List<String> actual;
        String testString;

        //case 1: no ties
        testString = "abdcef";
        tree = new SuffixTree(testString);
        actual = new ArrayList<>();
        for (int i = 0; i < (testString.length()); i++) {
            actual.add(testString.substring(i));
        }
        Collections.sort(actual);
        assertEquals("Testing inOrderTraversal() method",
                actual, tree.inOrderTraversal());

        testString = "fedcdba";
        tree = new SuffixTree(testString);
        actual = new ArrayList<>();
        for (int i = 0; i < (testString.length()); i++) {
            actual.add(testString.substring(i));
        }
        Collections.sort(actual);
        assertEquals("Testing inOrderTraversal() method",
                actual, tree.inOrderTraversal());

        //case 2: tie break based on lexicographical order
        testString = "bcabbcde";
        tree = new SuffixTree(testString);
        actual = new ArrayList<>();
        for (int i = 0; i < (testString.length()); i++) {
            actual.add(testString.substring(i));
        }
        Collections.sort(actual);
        assertEquals("Testing inOrderTraversal() method",
                actual, tree.inOrderTraversal());

        testString = "bbcdebca";
        tree = new SuffixTree(testString);
        actual = new ArrayList<>();
        for (int i = 0; i < (testString.length()); i++) {
            actual.add(testString.substring(i));
        }
        Collections.sort(actual);
        assertEquals("Testing inOrderTraversal() method",
                actual, tree.inOrderTraversal());
    }
}