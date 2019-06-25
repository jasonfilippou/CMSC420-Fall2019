package projects.suffixtree;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * A jUnit test suite for {@link SuffixTree}.
 *
 * @author <a href = "https://github.com/JasonFil">Jason Filippou</a> &amp; <a href ="mailto:pchiang@cs.umd.edu">Ping Chiang</a>
 *  */
public class StudentTests {

    @Test public void testConstructor() {
        //the constructor should throw illegal argument exception when the argument is an empty string, null, or a string with $ sign
        try {
            SuffixTree trie = new SuffixTree(""); //should throw illegal argument exception
            fail("Expected an IllegalArgumentException to be thrown when calling the constructor with an empty string");
        }
        catch(Exception e){
            assertEquals("Expected an IllegalArgumentException to be thrown when calling the constructor with an null string, but got something else",IllegalArgumentException.class, e.getClass());
        }
    }

    @Test public void testMatches() {
        //case 1: trying to find matches that exists
        SuffixTree tree = new SuffixTree("aabbaab");

        List<Integer> actual = tree.matches("ab");
        Collections.sort(actual);
        List<Integer> expected = new ArrayList<>(Arrays.asList(1,5));
        assertEquals("searched for a valid substring",expected, actual);

        //case 2: trying to find matches that doesn't exists
        actual = tree.matches("cc");
        Collections.sort(actual);
        expected = new ArrayList<>();
        assertEquals("searched for an invalid match", expected, actual);

        actual = tree.matches("b$");
        Collections.sort(actual);
        expected = new ArrayList<>();
        assertEquals("searched for a valid substring that includes the $ sign", expected, actual);

        //case 3: trying to find matches of an empty string
        // the method should throw illegal argument exception when the argument is null OR an empty string
        // this one only checks it for null
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
        assertEquals("isSubstring should return true, but got false instead", true, tree.isSubstring("aa"));

        //case 2: checking for an invalid substring
        assertEquals("isSubstring should return false, but got true instead", false, tree.isSubstring("e"));
        assertEquals("isSubstring should return false for a query including $, but got true instead", false, tree.isSubstring("ccdaab$"));

        //case 3: checking for an empty substring
        // the method should throw illegal argument exception when the argument is null or an empty string
        // this one only checks it for null
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

        //case 2: the string does not appear
        assertEquals("numTimesOccurs method return incorrect nodeCount", 0, tree.numTimesOccurs("e"));
        assertEquals("numTimesOccurs method return incorrect nodeCount for a query including $", 0, tree.numTimesOccurs("b$"));

        //case 3: the string is empty
        // the method should throw illegal argument exception when the argument is null or an empty string
        // this one only checks it for null
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

        //case 1: check for cases where there is ties, tie break based on lexicographical order
        tree = new SuffixTree("aabbaabaaaa");
        assertEquals("The longestRepeatedSubstring() method does not return the correct substring",
                tree.longestRepeatedSubstring(), "aaa");
        //"aaa" is returned as opposed to "aab" because "aaa" comes before "aab" in lexicographical order
    }
    @Test public void testShortestRepeatedSubstring() {
        SuffixTree tree;
        //case 1: when there is ties, tie break based on lexicographical order
        tree = new SuffixTree("ffeeddccbbaa");
        assertEquals("The shortestRepeatedSubstring() method does not return the correct substring",
                tree.shortestRepeatedSubstring(1), "a");
        assertEquals("The shortestRepeatedSubstring() method does not return the correct substring",
                tree.shortestRepeatedSubstring(2), null);

        //case 2: check for cases where the minLength argument is invalid (equal to or smaller than 0)
        // the method should throw an illegal argument exception when minLength is smaller or equal to 0
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
                tree.nodeCount(), 8);

        //case 2: some repeated strings
        tree = new SuffixTree("aab");
        assertEquals("The nodeCount() method does not return the correct number",
                tree.nodeCount(), 6);
    }
    @Test public void testinOrderTraversal() {
        SuffixTree tree;
        List<String> actual;
        String testString;

        //case 2: tie break based on lexicographical order
        testString = "bcabbcde";
        tree = new SuffixTree(testString);
        actual = new ArrayList<>();
        actual.add("abbcde");
        actual.add("bbcde");
        actual.add("bcabbcde");
        actual.add("bcde");
        actual.add("cabbcde");
        actual.add("cde");
        actual.add("de");
        actual.add("e");
        assertEquals("Testing inOrderTraversal() method",
                actual, tree.inOrderTraversal());
    }

}