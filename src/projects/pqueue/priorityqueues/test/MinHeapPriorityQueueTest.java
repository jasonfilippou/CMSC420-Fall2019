package projects.pqueue.priorityqueues.test;
import org.junit.Test;
import projects.pqueue.exceptions.InvalidPriorityException;
import projects.pqueue.priorityqueues.*;
import projects.pqueue.priorityqueues.PriorityQueue;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static org.junit.Assert.*;

/**
 * <p>A testing framework for {@link MinHeapPriorityQueue}.</p>
 *
 * @author  <a href="mailto:jasonfil@cs.umd.edu">Jason Filippou</a>
 *
 * @see MinHeapPriorityQueueTest
 */
public class MinHeapPriorityQueueTest {


    private PriorityQueue<String> greekNamesQueue =
            new MinHeapPriorityQueue<String>();
    private PriorityQueue<Double> doubles = new MinHeapPriorityQueue<Double>();

    private static final String INVALID_PRIORITY_MSG =
            "We caught an InvalidPriorityException when enqueuing with valid priorities.";

    private static final int MAX_PRIORITY = 10;
    private static final long SEED = 47;
    private Random r = new Random(SEED);

    @Test
    public void testMinHeapPQConstructorAndSize() {
        assertTrue("After construction, a MinHeapPriorityQueue should be empty.",
                greekNamesQueue.isEmpty());
        assertEquals("After construction, a MinHeapPriorityQueue's size should be 0.", 0,
                greekNamesQueue.size());
    }

    @Test
    public void testMinHeapPQClear() {
        try {
            greekNamesQueue.enqueue("Alexandrou", 8);
            greekNamesQueue.clear();
            assertTrue("After clearing, a MinHeapPriorityQueue should be empty.", greekNamesQueue.isEmpty());
            assertEquals("After clearing, a MinHeapPriorityQueue's size should be 0.", 0, greekNamesQueue.size());
        } catch (InvalidPriorityException ignored) {
            fail(INVALID_PRIORITY_MSG);
        }
    }

    @Test
    public void testMinHeapPQSimpleEnqueueDifferentPriorities() {
        try {
            greekNamesQueue.enqueue("Filippou", 2);
            greekNamesQueue.enqueue("Alexandrou", 3);
            greekNamesQueue.enqueue("Costakis", 1);

            try {
                assertEquals("MinHeapPriorityQueue.getFirst() did not return the correct element.",
                        "Costakis", greekNamesQueue.getFirst());
            } catch (EmptyPriorityQueueException ignored) {
                fail("Since the queue was not empty upon call to MinHeapPriorityQueue.getFirst(), an " +
                        "EmptyPriorityQueueException should not have been thrown.");
            }
        } catch (InvalidPriorityException ignored) {
            fail(INVALID_PRIORITY_MSG);
        }
    }

    @Test
    public void testMinHeapPQInvalidPriorities() {
        try {
            greekNamesQueue.enqueue("Grigoris", 0);
        } catch(InvalidPriorityException exc) {
            // ok, expected behavior.
        }catch (Throwable t) {

            fail("When enqueueing an element with priority -1, we expected an InvalidPriorityException, "
                    + "but we instead got a " + t.getClass() + " with message: " + t.getMessage() + ".");
        }try {
            greekNamesQueue.enqueue("Grigoris", -1);
        } catch (InvalidPriorityException ignored) {
            // ok, expected behavior.
        } catch (Throwable t) {
            fail("When enqueueing an element with priority -1, we expected an InvalidPriorityException, "
                    + "but we instead got a " + t.getClass() + " with message: " + t.getMessage() + ".");
        }
    }

    @Test
    public void testMinHeapPQSimpleEnqueueSamePriorities() {
        try {
            greekNamesQueue.enqueue("Filippou", 1);
            greekNamesQueue.enqueue("Alexandrou", 1);
            greekNamesQueue.enqueue("Costakis", 1);

            try {
                assertEquals("MinHeapPriorityQueue.getFirst() did not return the correct element. " +
                        "Are you treating same priorities correctly?", "Filippou", greekNamesQueue.getFirst());
            } catch (EmptyPriorityQueueException ignored) {
                fail("Since the queue was not empty upon call to MinHeapPriorityQueue.getFirst(), an " +
                        "EmptyPriorityQueueException should not have been thrown.");
            }
        } catch (InvalidPriorityException ignored) {
            fail(INVALID_PRIORITY_MSG);
        }
    }

    @Test
    public void testMinHeapPQComplexEnqueuesAndDequeues() {
        try {
            greekNamesQueue.enqueue("Filippou", 2);

            assertEquals("After inserting a single element, a MinHeapPriorityQueue should have a size of 1", 1,
                    greekNamesQueue.size());

            greekNamesQueue.enqueue("Alexandrou", 10);
            assertEquals("After enqueueing 2 elements, the queue should have a size of 2.", 2, greekNamesQueue.size());

            greekNamesQueue.enqueue("Vasilakopoulos", 5);
            assertEquals("After enqueueing 3 elements, the queue should have a size of 3.", 3, greekNamesQueue.size());
            try {
                assertEquals("MinHeapPriorityQueue.getFirst() did not return the correct element.",
                        "Filippou", greekNamesQueue.getFirst());
            } catch (AssertionError ae) {
                throw (ae);
            } catch (EmptyPriorityQueueException ignored) {
                fail("Since the queue was not empty upon call to MinHeapPriorityQueue.getFirst(), an " +
                        "EmptyPriorityQueueException should not have been thrown.");
            }
            try {
                assertEquals("MinHeapPriorityQueue.dequeue() did not return the correct element.",
                        "Filippou", greekNamesQueue.dequeue());
            } catch (EmptyPriorityQueueException ignored) {
                fail("Since the queue was not empty upon call to MinHeapPriorityQueue.dequeue(), an " +
                        "EmptyPriorityQueueException should not have been thrown.");
            }
            try {
                assertEquals(" After a prior dequeueing, MinHeapPriorityQueue.getFirst() did not return the " +
                        "correct element.", "Vasilakopoulos", greekNamesQueue.getFirst());

            } catch (AssertionError ae) {
                throw (ae);
            } catch (EmptyPriorityQueueException ignored) {
                fail("Despite a prior dequeue-ing, the queue was still not empty upon call to " +
                        "MinHeapPriorityQueue.getFirst(), so an EmptyPriorityQueueException should not have been thrown.");
            }
            greekNamesQueue.enqueue("Papandreou", 1);
            greekNamesQueue.enqueue("Mitsotakis", 1);

            assertEquals("After 3 enqueueings, 1 dequeueing and 2 enqueueings, the size of the MinHeapPriorityQueue should be 4.",
                    4, greekNamesQueue.size());
            try {
                assertNotEquals("MinHeapPriorityQueue.dequeue() returned an element that used to be the top element, " +
                        "but is not any more after some recent enqueueings.", greekNamesQueue.dequeue(), "Vasilakopoulos"); // No longer the first.
                assertEquals("MinHeapPriorityQueue.dequeue() did not return the correct element. Are you treating same priorities correctly?", greekNamesQueue.dequeue(), "Mitsotakis");
                assertEquals("MinHeapPriorityQueue.dequeue() did not return the correct element. Are you treating same priorities correctly?", greekNamesQueue.dequeue(), "Vasilakopoulos");
                assertEquals("MinHeapPriorityQueue.dequeue() did not return the correct element. Are you treating same priorities correctly?", greekNamesQueue.dequeue(), "Alexandrou");
            } catch (AssertionError ae) {
                throw (ae);
            } catch (EmptyPriorityQueueException ignored) {
                fail("Despite a prior dequeue-ing, the queue was still not empty upon call to MinHeapPriorityQueue.dequeue(), " +
                        "so an EmptyPriorityQueueException should not have been thrown.");
            }

            assertEquals("After dequeue-ing every element, the MinHeapPriorityQueue should have a size of 0",
                    0, greekNamesQueue.size());
            assertTrue("After dequeue-ing every element, the MinHeapPriorityQueue should be empty.",
                    greekNamesQueue.isEmpty());
        } catch (InvalidPriorityException ignored) {
            fail("We caught an InvalidPriorityException when enqueuing with valid priorities.");
        }
    }


    @Test
    public void testMinHeapPQManyEnqueues() {

        List<Integer> priorities = IntStream.range(1, MAX_PRIORITY + 1).boxed().collect(Collectors.toList());
        Collections.shuffle(priorities, r);
        for (int cnt = 0; cnt < MAX_PRIORITY; cnt++) {
            try {
                doubles.enqueue(r.nextDouble(), priorities.get(cnt));
            } catch (Throwable t) {
                fail("During the enqueueing of element #" + cnt + ", we caught a " + t.getClass().getSimpleName()
                        + " with message " + t.getMessage());
            }
        }
        assertEquals("After enqueueing " + MAX_PRIORITY + " elements, the size of the MinHeapPriorityQueue " +
                "should be " + MAX_PRIORITY + ".", MAX_PRIORITY, doubles.size());
    }


    @Test
    public void testMinHeapPQManyDequeues() {

        List<Integer> priorities = IntStream.range(1, MAX_PRIORITY + 1).boxed().collect(Collectors.toList());
        Collections.shuffle(priorities, r);

        priorities.forEach(pr -> {
            try {
                doubles.enqueue(r.nextDouble(), pr);
            } catch (InvalidPriorityException ignored) { //
                fail(INVALID_PRIORITY_MSG);
            } // Insert a bunch of doubles with randomly shuffled priorities
        });
        for (int cnt = 0; cnt < MAX_PRIORITY; cnt++) {
            try {
                doubles.dequeue();
            } catch (Throwable t) {
                fail("During the dequeueing of element #" + cnt + ", we caught a " + t.getClass().getSimpleName()
                        + " with message " + t.getMessage());
            }
        }
        assertEquals("After dequeueing all the elements of the MinHeapPriorityQueue, its size should be 0.",
                0, doubles.size());
        assertTrue("After dequeueing all the elements of the MinHeapPriorityQueue, its size should be 0.",
                doubles.isEmpty());

    }



    @Test
    public void testMinHeapPQIteratorAndConcurrentModifications() {
        try {
            String[] strings = {"Karathodori", "Stergiou", "Tasou", "Pipinis", "Papandreou", "Mitsotakis"};
            for (int i = 0; i < strings.length; i++)
                greekNamesQueue.enqueue(strings[i], strings.length - i); // Reverse order of priority. such that Mitsotakis is first.
            Iterator<String> it = greekNamesQueue.iterator();
            assertTrue("Since we have some elements in the MinHeapPriorityQueue, the iterator's hasNext()" +
                    " method should return true.", it.hasNext());


            for (int i = strings.length - 1; i > -1; i--)
                assertEquals("The iterator's next() method did not return the expected element.", strings[i], it.next());
            assertFalse("After looping through all the elements with next(), the iterator's hasNext() method" +
                    " should return false.", it.hasNext());

            greekNamesQueue.clear();

            // Now we will also check iterations over a queue that has
            // non-singleton FIFO fifoqueues in it

            // Give the getFirst 4 people in that array a priority of 2, and the last
            // 2 people a priority of 1:

            for (int i = 0; i < strings.length; i++) {
                if (i < 4)
                    greekNamesQueue.enqueue(strings[i], 2);
                else
                    greekNamesQueue.enqueue(strings[i], 1);
            }
            it = greekNamesQueue.iterator();

            assertTrue("Before looping through the MinHeapPriorityQueue, its iterator's hasNext() method should " +
                    " return true.", it.hasNext());
            assertEquals("The iterator's next() method did not return the expected element.", "Papandreou", it.next());
            assertEquals("The iterator's next() method did not return the expected element.", "Mitsotakis", it.next());
            for (int i = 0; i < 4; i++)
                assertEquals("The iterator's next() method did not return the expected element.", it.next(), strings[i]);
            assertFalse("After looping through all the elements with next(), the iterator's hasNext() method" +
                    " should return false.", it.hasNext());

            // Finally, check the proper throwing of a ConcurrentModificationException

            it = greekNamesQueue.iterator();
            it.next();
            greekNamesQueue.enqueue("Stamatopoulos", 9);
            ConcurrentModificationException cme = null;
            try {
                it.next();
            } catch (ConcurrentModificationException cmeThrown) {
                cme = cmeThrown;
            } catch (Throwable t) {
                fail("Instead of a ConcurrentModificationException, we were thrown a " + t.getClass().getSimpleName() +
                        " with message: " + t.getMessage() + ".");
            }
            assertNotNull("MinHeapPQ Iterator should have thrown a ConcurrentModificationException. ", cme);
        }catch(InvalidPriorityException ignored){
            fail(INVALID_PRIORITY_MSG);
        }
    }

}