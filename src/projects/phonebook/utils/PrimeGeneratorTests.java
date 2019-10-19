package projects.phonebook.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * <p>A jUnit testing framework for {@link PrimeGenerator}.</p>
 *
 * <p><b>YOU DO NOT HAVE TO EDIT THIS CLASS! IT HAS BEEN PROVIDED AS A HELPFUL RESOURCE.</b></p>
 *
 * @see PrimeGenerator
 * @author <a href="https://github.com/JasonFil">Jason Filippou</a>
 */
public class PrimeGeneratorTests {

    /* ********************************* */
    /* Private data fields  and methods  */
    /* ********************************* */

    private PrimeGenerator pg;

    private static String format(Throwable t){
        return "Caught a " + t.getClass().getSimpleName()+ " with message: " + t.getMessage() + ".";
    }


    /* *************/
    /* Unit tests  */
    /* *************/

    /**
     * Instantiates the {@link PrimeGenerator} instance that will be tested. This @Before - annotated methood
     * runs before every @Test-annotated method.
     */
    @Before
    public void setUp(){
        pg = new PrimeGenerator();
    }

    /**
     * Throws away the {@link PrimeGenerator} instance tested.
     */
    @After
    public void tearDown(){
        pg = null; // Can be garbage - collected.
    }
    /**
     * Tests the <b>default</b> behavior of {@link PrimeGenerator#getCurrPrime()}, which should be returning 13.
     */

    @Test
    public void testCurrentPrimeDefault() {
        assertEquals("Expected 7 by default.", 7, pg.getCurrPrime());
    }

    /**
     * Tests the behavior of {@link PrimeGenerator#getNextPrime()}.
     */
    @Test
    public void testGetNextPrime() {
        assertEquals("Expected 13 after first call to getNextPrime().", 13, pg.getNextPrime());
        assertEquals("Expected 13 to be returned by getCurrPrime() after a successful call to getNextPrime().",
                13, pg.getCurrPrime());
        assertEquals("Expected 23 to be returned by this method after a successful second call to getNextPrime().",
                23, pg.getNextPrime());
    }


    /**
     * Tests the behavior of {@link PrimeGenerator#getPreviousPrime()}.
     */
    @Test
    public void testGetPreviousPrime() {
        assertEquals("Expected 5 after first call to getPreviousPrime().", 5, pg.getPreviousPrime());
        assertEquals("Expected 5 to be returned by getCurrPrime() after a successful call to getPreviousPrime().",
                5, pg.getCurrPrime());
        assertEquals("Expected 3 after second call to getPreviousPrime().", 3, pg.getPreviousPrime());
        assertEquals("Expected 3 to be returned by getCurrPrime() after a successful second call to getPreviousPrime().",
                3, pg.getCurrPrime());
    }

    /**
     * Tests whether the {@link PrimeGenerator} instance points to 7 after a call to
     * {@link PrimeGenerator#getNextPrime()} followed by a call to {@link PrimeGenerator#getPreviousPrime()}.
     */
    @Test
    public void testGetNextAndThenPreviousPrime(){
        pg.getNextPrime();
        pg.getPreviousPrime();
        assertEquals("Expected getCurrPrime() to return  7 after a call to getNextPrime() and a call to " +
                "getPreviousPrime().", 7, pg.getCurrPrime());
    }


    /**
     * Tests whether the {@link PrimeGenerator} instance points to 7 after a call to
     * {@link PrimeGenerator#getPreviousPrime()} followed by a call to {@link PrimeGenerator#getNextPrime()}.
     */
    @Test
    public void testgetPreviousAndThenNextPrime(){
        pg.getPreviousPrime();
        pg.getNextPrime();
        assertEquals("Expected getCurrPrime() to return 7 after a call to getPreviousPrime() and a call to " +
                "getNextPrime().", 7, pg.getCurrPrime());
    }

    /** Tests for edge cases of {@link PrimeGenerator} which <b>should</b> be throwing {@link NoMorePrimesException}
     * instances.
     */
    @Test
    public void testExceptionsThrown(){
        pg.getPreviousPrime(); // 5
        pg.getPreviousPrime(); // 3
        pg.getPreviousPrime(); // 2
        RuntimeException rexc = null;
        try {
            pg.getPreviousPrime(); // Should throw
        } catch(NoMorePrimesException nmpe){
            rexc = nmpe;
        } catch(Throwable t){
            fail(format(t));
        }
        assertNotNull("A call to getPreviousPrime() when getCurrentPrime() returns 2 should have thrown a NoMorePrimesException", rexc);

        // The biggest prime number we store is 7907. We start making calls to getNextPrime() which will *more than double*
        // the current prime number every time. 2^12 = 4096 and 2^13 = 8192. Without even looking at my list of primes
        // I know that in at most 12 calls to getNextPrime() (the first one gives me 13) I am reaching a number that is definitely
        // beyond my list of current primes.
        rexc = null;
        int iterThrown = 0;
        for(int i = 0; i < 15; i++){
            // Embedding the try block inside the for loop makes the loop very slow, but allows for the index 'i' to be
            // visible to the scope of the catch-blocks. This in turn allows us to throw an AssertionError with information
            // about exactly *which* iteration failed.

            try {
                pg.getNextPrime();
            } catch(NoMorePrimesException nmpe){
                rexc = nmpe;
                iterThrown = i;
            } catch(Throwable t){
                fail("Call to getNextPrime() #" + (i + 1) + ": " + format(t) );
            }
        }
        assertNotNull("Was expecting a NoMorePrimesException after " + (iterThrown + 1) + " calls to getNextPrime()", rexc);
    }

    /**
     * Tests the behavior of {@link PrimeGenerator#reset()}.
     */
    @Test
    public void testReset(){

        // Initial test
        pg.reset();
        assertEquals("Expected getCurrPrime() to return  7 after a call to reset()", 7, pg.getCurrPrime());

        // Make a small, yet random number of calls to getNextPrime() and getPreviousPrime(),
        // then call reset(), see whether it behaves as advertised.

        Random r = new Random(47);
        int callsToGetNextPrime = r.nextInt(3) + 6; // At least 6 calls (you'll see why later).
        for(int ignored = 0; ignored < callsToGetNextPrime; ignored++)
            pg.getNextPrime();

        // Give me up to 6 calls to getPrevPrime().
        int callsToGetPrevPrime = r.nextInt(6);
        for(int ignored = 0; ignored < callsToGetPrevPrime; ignored++)
            pg.getPreviousPrime();

        pg.reset();
        assertEquals("After " + callsToGetNextPrime + " calls to getNextPrime() and " + callsToGetPrevPrime +
                " calls to getPreviousPrime() and one call to reset(), we did not get 7 as the current prime number.",
                7, pg.getCurrPrime());
    }
}
