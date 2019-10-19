package projects.phonebook;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import projects.phonebook.hashes.*;
import projects.phonebook.utils.NoMorePrimesException;
import projects.phonebook.utils.Probes;
//import sun.plugin.perf.PluginRollup;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import static org.junit.Assert.*;

import static projects.phonebook.hashes.CollisionResolver.*;

/**
 * <p> {@code StudentTests} is a place for you to write your tests for {@link Phonebook} and all the various
 * {@link HashTable} instances.</p>
 *
 * @author YOUR NAME HERE!
 * @see Phonebook
 * @see HashTable
 * @see SeparateChainingHashTable
 * @see LinearProbingHashTable
 * @see QuadraticProbingHashTable
 */
public class StudentTests {

    private CollisionResolver[] resolvers = {SEPARATE_CHAINING, LINEAR_PROBING, ORDERED_LINEAR_PROBING, QUADRATIC_PROBING};
    private HashMap<String, String> testingPhoneBook;
    private static final long SEED = 47;
    private static final Random RNG = new Random(SEED);
    private static final int NUMS = 1000;
    private static final int UPPER_BOUND = 100;

    private String format(String error, CollisionResolver namesToPhones, CollisionResolver phonesToNames) {
        return error + "Collision resolvers:" + namesToPhones + ", " + phonesToNames + ".";
    }


    private String errorData(Throwable t) {
        return "Received a " + t.getClass().getSimpleName() + " with message: " + t.getMessage() + ".";
    }

    @Before
    public void setUp() {
        testingPhoneBook = new HashMap<>();
        testingPhoneBook.put("Arnold", "894-59-0011");
        testingPhoneBook.put("Tiffany", "894-59-0011");
        testingPhoneBook.put("Jessie", "705-12-7500");
        testingPhoneBook.put("Mary", "888-1212-3340");
    }

    @After
    public void tearDown() {
        testingPhoneBook.clear();
    }


    // Make sure that all possible phonebooks we can create will report empty when beginning.
    @Test
    public void testBehaviorWhenEmpty() {
        for (CollisionResolver namesToPhones : resolvers) {
            for (CollisionResolver phonesToNames : resolvers) {
                Phonebook pb = new Phonebook(namesToPhones, phonesToNames);
                assertTrue(format("Phonebook should be empty", namesToPhones, phonesToNames), pb.isEmpty());
            }
        }
    }

    // See if all of our hash tables cover the simple example from the writeup.
    @Test
    public void testOpenAddressingResizeWhenInsert() {
        SeparateChainingHashTable sc = new SeparateChainingHashTable();
        LinearProbingHashTable lp = new LinearProbingHashTable();
        QuadraticProbingHashTable qp = new QuadraticProbingHashTable();
        assertEquals("Separate Chaining hash should have a capacity of 7 at startup.", 7, sc.capacity());
        assertEquals("Linear Probing hash should have a capacity of 7 at startup.", 7, lp.capacity());
        assertEquals("Quadratic Probing hash should have a capacity of 7 at startup.", 7, qp.capacity());
        for (Map.Entry<String, String> entry : testingPhoneBook.entrySet()) { // https://docs.oracle.com/javase/10/docs/api/java/util/Map.Entry.html
            sc.put(entry.getKey(), entry.getValue());
            lp.put(entry.getKey(), entry.getValue());
            qp.put(entry.getKey(), entry.getValue());
        }
        assertEquals("Separate Chaining hash should have a capacity of 7 after inserting 4 elements.", 7, sc.capacity());
        assertEquals("Linear Probing hash should have a capacity of 7 after inserting 4 elements.", 7, lp.capacity());
        assertEquals("Quadratic Probing hash should have a capacity of 7 after inserting 4 elements.", 7, qp.capacity());

        sc.put("DeAndre", "888-1212-3340");
        assertEquals("Separate Chaining hash should still have a capacity of 7 after inserting 5 elements.", 7, sc.capacity());
        sc.enlarge();
        assertEquals("Separate Chaining hash should have a capacity of 13 after first call to enlarge().", 13, sc.capacity());
        sc.enlarge();
        assertEquals("Separate Chaining hash should have a capacity of 23 after second call to enlarge().", 23, sc.capacity());
        sc.shrink();
        assertEquals("Separate Chaining hash should have a capacity of 13 after two calls to enlarge() and one to shrink().",
                13, sc.capacity());
        sc.shrink();
        assertEquals("Separate Chaining hash should have a capacity of 7 after two calls to enlarge() and two to shrink().",
                7, sc.capacity());
        lp.put("DeAndre","888-1212-3340" );
        assertEquals("Linear Probing hash should have a capacity of 13 after inserting 5 elements.",
                13, lp.capacity());
        qp.put("DeAndre","888-1212-3340" );
        assertEquals("Quadratic Probing hash should have a capacity of 13 after inserting 5 elements.",
                13, qp.capacity());

        // The following two deletions should both fail and thus not affect capacity.

        lp.remove("Thomas");
        assertEquals("Linear Probing hash with starting capacity of 7 should have a capacity of 13 after " +
                "five insertions and a failed deletion.", 13, lp.capacity());
        qp.remove("Thomas" );
        assertEquals("Quadratic Probing hash with starting capacity of 7 should have a capacity of 13 after " +
                "five insertions and a failed deletion.", 13, qp.capacity());
    }

    // An example of a stress test to catch any insertion errors that you might get.
    @Test
    public void insertionStressTest() {
        HashTable sc = new SeparateChainingHashTable();
        HashTable lp = new LinearProbingHashTable();
        HashTable qp = new QuadraticProbingHashTable();
        for (int i = 0; i < NUMS; i++) {
            String randomNumber = Integer.toString(RNG.nextInt(UPPER_BOUND));
            String randomNumber2 = Integer.toString(RNG.nextInt(UPPER_BOUND));
            try {
                sc.put(randomNumber, randomNumber2);
            } catch (NoMorePrimesException ignored) {
                // To have this exception thrown is not a problem; we have a finite #primes to generate resizings for.
            } catch (Throwable t) {
                fail("Separate Chaining hash failed insertion #" + i + ". Error message: " + errorData(t));
            }

            try {
                lp.put(randomNumber, randomNumber2);
            } catch (NoMorePrimesException ignored) {
                // To have this exception thrown is not a problem; we have a finite #primes to generate resizings for.
            } catch (Throwable t) {
                fail("Linear Probing hash failed insertion #" + i + ". Error message: " + errorData(t));
            }


            try {
                qp.put(randomNumber, randomNumber2);
            } catch (NoMorePrimesException ignored) {
                // To have this exception thrown is not a problem; we have a finite #primes to generate resizings for.
            } catch (Throwable t) {
                fail("Quadratic Probing hash failed insertion #" + i + ". Error message: " + errorData(t));
            }
        }

    }

    @Test
    public void testSCProbes() {
        SeparateChainingHashTable sc = new SeparateChainingHashTable();

        assertEquals(1, sc.put("Arnold", "894-59-0011").getProbes());
        assertEquals(1, sc.put("Tiffany", "894-59-0011").getProbes());
        assertEquals(1, sc.put("Jessie", "705-12-7500").getProbes());
        assertEquals(1, sc.put("Mary", "888-1212-3340").getProbes());

        assertEquals(2, sc.get("Arnold").getProbes());
        assertEquals("894-59-0011", sc.get("Arnold").getValue());
        assertEquals(1, sc.get("Tiffany").getProbes());
        assertEquals(1, sc.get("Jessie").getProbes());
        assertEquals(1, sc.get("Mary").getProbes());

        // Search fail
        assertEquals(2, sc.get("Jerry").getProbes());
        assertEquals(2, sc.remove("Jerry").getProbes());
        assertNull(sc.remove("Jerry").getValue());

        assertEquals(2, sc.remove("Arnold").getProbes());
        assertEquals(1, sc.remove("Tiffany").getProbes());
        assertEquals(1, sc.remove("Jessie").getProbes());
        assertEquals(1, sc.remove("Mary").getProbes());

    }


    @Test
    public void testLProbes() {

        LinearProbingHashTable lp = new LinearProbingHashTable();

        assertEquals(1, lp.put("Arnold", "894-59-0011").getProbes());
        assertEquals(1, lp.put("Tiffany", "894-59-0011").getProbes());
        assertEquals(2, lp.put("Jessie", "705-12-7500").getProbes());
        assertEquals(1, lp.put("Mary", "888-1212-3340").getProbes());


        assertEquals(1, lp.get("Arnold").getProbes());
        assertEquals("894-59-0011", lp.get("Arnold").getValue());
        assertEquals(1, lp.get("Tiffany").getProbes());
        assertEquals(2, lp.get("Jessie").getProbes());
        assertEquals(1, lp.get("Mary").getProbes());

        // Search fail
        assertEquals(2, lp.get("Jerry").getProbes());
        assertEquals(2, lp.remove("Jerry").getProbes());
        assertEquals(null, lp.remove("Jerry").getValue());

        assertEquals(2, lp.remove("Jessie").getProbes());
        assertEquals(1, lp.remove("Arnold").getProbes());
        assertEquals(1, lp.remove("Tiffany").getProbes());
        assertEquals(1, lp.remove("Mary").getProbes());



    }

    @Test
    public void testResizeSoftLProbes() {

        LinearProbingHashTable lp = new LinearProbingHashTable(true);
        String[] add1 = new String[]{"Tiffany", "Helen", "Alexander", "Paulette", "Jason", "Money", "Nakeesha", "Ray", "Jing", "Amg"};
        String[] remove1 = new String[]{"Helen", "Alexander", "Paulette", "Jason", "Money", "Nakeesha", "Ray", "Jing", "Amg"};
        String[] add2 = new String[]{"Christine", "Carl"};

        for(String s: add1) {
            lp.put(s, s);
        }

        for (String s: remove1) {
            lp.remove(s);
        }

        for(String s: add2) {
            lp.put(s, s);
        }

        assertEquals("After additions and deletions, and additions again, the capacity should be 23, but get " + lp.capacity() + ".", 23, lp.capacity());

        lp.put("Terry", "new");
        assertEquals("After additions and deletions, and additions again, resize should be triggered and the capacity should be shrink to 7, but get " + lp.capacity() + ".", 7, lp.capacity());

    }


}
