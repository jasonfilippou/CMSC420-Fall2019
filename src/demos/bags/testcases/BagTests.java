package demos.bags.testcases;



import demos.bags.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Random;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * Created by jason on 3/22/17.
 */
public class BagTests {

    private Bag<Integer> staticBag, randomAccessBag, shuffledBag;
    private IntStream thousand;
    private IntStream tenthousand;
    private static int NUM_ITERS=10;
    private Random r;
    private static long DEFAULT_SEED=47;

    @Before
    public void setUp() {
        staticBag = new StaticallyPerturbedBag<Integer>();
        randomAccessBag = new RandomAccessBag<Integer>();
        shuffledBag = new DynamicallyShuffledBag<Integer>();


        thousand = IntStream.rangeClosed(1, 1000);
        tenthousand = IntStream.rangeClosed(1, 10000);


        r = new Random();
        r.setSeed(DEFAULT_SEED); // Comment out for actual pseudorandomness
    }


    @After
    public void tearDown(){
        staticBag = randomAccessBag = shuffledBag = null;
        thousand = tenthousand = null;
        r = null;
        System.gc();
    }

    @Test
    public void add() throws Exception {
        try {
            testAdditions(thousand, staticBag);
            thousand = IntStream.rangeClosed(1, 1000); // Can't reuse Streams :(
            testAdditions(thousand, shuffledBag);
            thousand = IntStream.rangeClosed(1, 1000);
            testAdditions(thousand, randomAccessBag);

            // Clear and reset the bags pretty quick
            tearDown();
            setUp();

            testAdditions(tenthousand, staticBag);
            tenthousand = IntStream.rangeClosed(1, 10000);
            testAdditions(tenthousand, shuffledBag);
            tenthousand = IntStream.rangeClosed(1, 10000);
            testAdditions(tenthousand, randomAccessBag);
        }
        catch(Exception e){
            fail("Failed  BagTests::add() with message: " + e.getMessage());
        }
    }


    // If you're gonna stream the additions, gotta make the Bag's inner implementation
    // thread-safe, and declare as such in the documentation.
    private void testAdditions(IntStream ints, Bag b){
        try {
            ints.forEach(l -> b.add(l));
        } catch(Exception e){
            System.err.println("Caught an " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void isEmpty() throws Exception {
        assertTrue("Statically Perturbed Bag should be empty.", staticBag.isEmpty());
        assertTrue("Dynamically Shuffled Bag should be empty.", shuffledBag.isEmpty());
        assertTrue("Random Access Bag should be empty.", randomAccessBag.isEmpty());

        assertTrue("Statically Perturbed Bag should should have a size of 0.", staticBag.size() == 0);
        assertTrue("Dynamically Shuffled Bag should should have a size of 0.", shuffledBag.size() == 0);
        assertTrue("Random Access Bag should should have a size of 0.", randomAccessBag.size() == 0);

        staticBag.add(r.nextInt());
        shuffledBag.add(r.nextInt());
        randomAccessBag.add(r.nextInt());

        // Make sure that after addition, you are no longer empty *and* your size is 1.

        assertFalse("Statically Perturbed Bag should not be empty.", staticBag.isEmpty());
        assertFalse("Dynamically Shuffled Bag should not be empty.", shuffledBag.isEmpty());
        assertFalse("Random Access Bag should not be empty.", randomAccessBag.isEmpty());

        assertTrue("Statically Perturbed Bag should should have a size of 1.", staticBag.size() == 1);
        assertTrue("Dynamically Shuffled Bag should should have a size of 1.", shuffledBag.size() == 1);
        assertTrue("Random Access Bag should should have a size of 1.", randomAccessBag.size() == 1);
    }

    @Test
    public void shake() throws Exception {
        // Shake() unit tests are tricky... some things should clearly not happen regardless of the shaking.
        // Let's start with those.

        for(int i = 0; i < NUM_ITERS; i++){
            IntStream.rangeClosed(0, 300).forEach(new IntConsumer() {
                @Override
                public void accept(int value) {
                    staticBag.add(value);
                    shuffledBag.add(value);
                    randomAccessBag.add(value);
                }
            });
            for(int j = 0; j < 300; j++){
                staticBag.add(j);
                try {
                    staticBag.shake();
                } catch(Exception e){
                    fail("While adding integer " + j + " to staticBag, we received an " + e.getClass() + " with message " + e.getMessage());
                }

                staticBag.add(j);
                try {
                    staticBag.shake();
                } catch(Exception e){
                    fail("While adding integer " + j + " to staticBag, we received an " + e.getClass() + " with message " + e.getMessage());
                }

                randomAccessBag.add(j);
                try {
                    randomAccessBag.shake();
                } catch(Exception e){
                    fail("While adding integer " + j + " to staticBag, we received an " + e.getClass() + " with message " + e.getMessage());
                }
            }

        }

        // Also, shaking should not make objects disappear, or new ones appear!

        tearDown();
        setUp();

        // The following will depend on iterator() working properly, but we can have this information
        // from the unit test that follows anyway.
        for(int i = 0; i < 100; i++){
            staticBag.add(i);
            staticBag.shake();
            if(!found(staticBag, i))
                fail("After adding integer " + i + " in staticBag and shaking it, we could no longer find " + i + " in staticBag.");

            shuffledBag.add(i);
            shuffledBag.shake();
            if(!found(shuffledBag, i))
                fail("After adding integer " + i + " in shuffledBag and shaking it, we could no longer find " + i + " in shuffledBag.");


            randomAccessBag.add(i);
            randomAccessBag.shake();
            if(!found(randomAccessBag, i))
                fail("After adding integer " + i + " in randomAccessBag and shaking it, we could no longer find " + i + " in randomAccessBag.");
        }
    }

    /* Note that I have to assign the looping reference of the for-each loop to an Object,
     because Bags are Iterables, but Bags can take any Object. I don't want to be doing downcasts if I can.
     Let the equals() chips fall where they may.
      */
    private boolean found(Bag b, Object o){
        for(Object ob: b)
            if(ob.equals(o))
                return true;
        return false;
    }

    @Test
    public void iterator() throws Exception {

        // Add a bunch of elements to all the bags

        for(int i = 0; i > -1000; i--){
            staticBag.add(i);
            shuffledBag.add(i);
            randomAccessBag.add(i);
        }


        if(!iteratorTraversesOk(staticBag))
            fail("itStatic is not looping correctly.");

        if(!iteratorTraversesOk(shuffledBag))
            fail("itShuffled is not looping correctly.");

        if(!iteratorTraversesOk(randomAccessBag))
            fail("itRandomAccess is not looping correctly.");

        // Make an additional check to make sure that the Iterators produced are fail-fast!

        if(!testIteratorFailFast(staticBag))
            fail("itStatic is not fail-fast!");

        if(!testIteratorFailFast(shuffledBag))
            fail("itShuffled is not fail-fast!");

        if(!testIteratorFailFast(randomAccessBag))
            fail("itRandom is not fail-fast!");


    }

    private boolean iteratorTraversesOk(Bag<Integer> b){
        Iterator<Integer> it = b.iterator();
        if(!it.hasNext())
            return false;
        while(it.hasNext()) {
            try {
                it.next();
            } catch (Exception e) {
                System.err.println("iteratorOk method: received an " + e.getClass() + " with message " +
                        e.getMessage() + " while accessing next() on Iterator.");
                return false;
            }
        }

        return true;
    }

    private boolean testIteratorFailFast(Bag<Integer> b){
        Iterator<Integer> it = b.iterator();
        b.add(2);
        try {
            it.next();
        } catch(ConcurrentModificationException ce){
            return true;
        } catch(Exception e){
            System.err.println("Caught an exception of type " + e.getClass() + " with message " + e.getMessage() +
                    ". Was expecting a " + new ConcurrentModificationException().getClass());
            return false;
        }
        return false;

    }

}