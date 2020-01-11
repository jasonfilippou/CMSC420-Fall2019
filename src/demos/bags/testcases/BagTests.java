package demos.bags.testcases;

import demos.bags.Bag;
import demos.bags.DynamicallyShuffledBag;
import demos.bags.RandomAccessBag;
import demos.bags.StaticallyPerturbedBag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * <p>Test suite for the various implementations of {@link Bag}. Classes tested:</p>
 * <ul>
 *     <li>{@link StaticallyPerturbedBag}</li>
 *     <li>{@link DynamicallyShuffledBag}</li>
 *     <li>{@link RandomAccessBag}</li>
 * </ul>
 *
 * @see StaticallyPerturbedBag
 * @see DynamicallyShuffledBag
 * @see RandomAccessBag
 * @see Bag
 * @see demos.bags.clients.IntegerTimingClient
 *
 * @author <a href = "https://github.com/JasonFil">Jason Filippou</a>
 */
public class BagTests {

    private Bag<Integer> staticBag, randomAccessBag, shuffledBag;
    private IntStream thousand;
    private IntStream tenThousand;
    private Random r;
    private static final int NUM_ITERS=10;
    private static final long DEFAULT_SEED=47;

    @Before
    public void setUp() {
        staticBag = new StaticallyPerturbedBag<>();
        randomAccessBag = new RandomAccessBag<>();
        shuffledBag = new DynamicallyShuffledBag<>();
        thousand = IntStream.rangeClosed(1, 1000);
        tenThousand = IntStream.rangeClosed(1, 10000);
        r = new Random();
        r.setSeed(DEFAULT_SEED); // Comment out for actual pseudo-randomness
    }


    @After
    public void tearDown(){
        staticBag = randomAccessBag = shuffledBag = null;
        thousand = tenThousand = null;
        r = null;
        System.gc();
    }

    @Test
    public void testAdd()  {
        try {
            testAdditions(thousand, staticBag);
            thousand = IntStream.rangeClosed(1, 1000); // Can't reuse Streams :(
            testAdditions(thousand, shuffledBag);
            thousand = IntStream.rangeClosed(1, 1000);
            testAdditions(thousand, randomAccessBag);

            // Clear and reset the bags pretty quick
            tearDown();
            setUp();
            testAdditions(tenThousand, staticBag);
            tenThousand = IntStream.rangeClosed(1, 10000);
            testAdditions(tenThousand, shuffledBag);
            tenThousand = IntStream.rangeClosed(1, 10000);
            testAdditions(tenThousand, randomAccessBag);
        }
        catch(Throwable t){
            fail("Caught a " + t.getClass().getSimpleName() + " with message: "
                    + t.getMessage());
        }
    }

    private void testAdditions(IntStream ints, Bag<Integer> b){
        try {
            ints.forEach(b::add);
        } catch(Throwable t){
            fail("Caught a " + t.getClass().getSimpleName() + " with message: "
                    + t.getMessage());
        }
    }

    @Test
    public void testIsEmpty() {
        assertTrue("Statically Perturbed Bag should be empty.", staticBag.isEmpty());
        assertTrue("Dynamically Shuffled Bag should be empty.", shuffledBag.isEmpty());
        assertTrue("Random Access Bag should be empty.", randomAccessBag.isEmpty());

        assertEquals("Statically Perturbed Bag should should have a size of 0.", 0, staticBag.size());
        assertEquals("Dynamically Shuffled Bag should should have a size of 0.", shuffledBag.size(), 0);
        assertEquals("Random Access Bag should should have a size of 0.", randomAccessBag.size(), 0);

        staticBag.add(r.nextInt());
        shuffledBag.add(r.nextInt());
        randomAccessBag.add(r.nextInt());

        // Make sure that after addition, you are no longer empty *and* your size is 1.

        assertFalse("Statically Perturbed Bag should not be empty.", staticBag.isEmpty());
        assertFalse("Dynamically Shuffled Bag should not be empty.", shuffledBag.isEmpty());
        assertFalse("Random Access Bag should not be empty.", randomAccessBag.isEmpty());

        assertEquals("Statically Perturbed Bag should should have a size of 1.", 1, staticBag.size());
        assertEquals("Dynamically Shuffled Bag should should have a size of 1.", 1, shuffledBag.size());
        assertEquals("Random Access Bag should should have a size of 1.", 1, randomAccessBag.size());
    }

    @Test
    public void testShake() {
        // Shake() unit tests are tricky... some things should clearly not happen regardless of the shaking.
        // Let's start with those.

        for(int i = 0; i < NUM_ITERS; i++){
            IntStream.rangeClosed(0, 300).forEach(value -> {
                staticBag.add(value);
                shuffledBag.add(value);
                randomAccessBag.add(value);
            });
            for(int j = 0; j < 300; j++){
                staticBag.add(j);
                try {
                    staticBag.shake();
                } catch(Throwable t){
                    fail("While adding integer " + j + " to staticBag, we received an " + t.getClass().getSimpleName() + " with message " + t.getMessage());
                }

                shuffledBag.add(j);
                try {
                    shuffledBag.shake();
                } catch(Throwable t){
                    fail("While adding integer " + j + " to staticBag, we received an " + t.getClass().getSimpleName() + " with message " + t.getMessage());
                }

                randomAccessBag.add(j);
                try {
                    randomAccessBag.shake();
                } catch(Throwable t){
                    fail("While adding integer " + j + " to staticBag, we received an " + t.getClass().getSimpleName() + " with message " + t.getMessage());
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
    private boolean found(Bag<Integer> b, Integer i){
        for(Integer ob:b)
            System.out.println("Derp");
        for(Integer ob: b)
            if(ob != null && ob.equals(i))
                return true;
        return false;
    }

    @Test
    public void testIterator()  {

        for(int i = 0; i > -1000; i--){
            staticBag.add(i);
            shuffledBag.add(i);
            randomAccessBag.add(i);
        }
        // Add 0, -1, -2, ... -999 to all bags
        IntStream.range(0, 1000).boxed().forEach(v->{
            staticBag.add(-v);
            shuffledBag.add(-v);
            randomAccessBag.add(-v);
        });

        if(!iteratorTraversesOk(staticBag))
            fail("itStatic is not looping correctly.");

        if(!iteratorTraversesOk(shuffledBag))
            fail("itShuffled is not looping correctly.");

        if(!iteratorTraversesOk(randomAccessBag))
            fail("itRandomAccess is not looping correctly.");

        // Make an additional check to make sure that the
        // Iterators produced are fail-fast!
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
            } catch (Throwable t) {
                System.out.println("iteratorOk method: received an " + t.getClass().getSimpleName() + " with message " +
                        t.getMessage() + " while accessing next() on Iterator.");
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
        } catch(Throwable t){
            System.out.println("Caught an exception of type " + t.getClass().getSimpleName() + " with message " + t.getMessage() +
                    ". Was expecting a " + ConcurrentModificationException.class);
            return false;
        }
        return false;

    }

}