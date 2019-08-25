package projects.pqueue;

import projects.pqueue.exceptions.InvalidPriorityException;
import projects.pqueue.heaps.*;
import projects.pqueue.priorityqueues.*;

import projects.pqueue.priorityqueues.PriorityQueue;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Release testing framework for Priority Queue project. Classes tested:
 * <ul>
 * <li>{@link ArrayMinHeap}</li>
 * <li>{@link LinkedMinHeap}</li>
 * <li>{@link LinearPriorityQueue}</li>
 * <li>{@link MinHeapPriorityQueue}</li>
 * </ul>
 *
 * @author <a href="https://github.com/JasonFil">Jason Filippou</a>
 * @see ArrayMinHeap
 * @see LinkedMinHeap
 * @see LinearPriorityQueue
 * @see MinHeapPriorityQueue
 */
public class ReleaseTests {

    // Base class references for members. Are downcasted to the appropriate type
    // via private utils.
    private MinHeap<Integer> intMinHeap;
    private MinHeap<String> stringMinHeap;
    private PriorityQueue<String> greekNamesQueue;
    private PriorityQueue<Double> doubles;

    // Business logic members
    private static final String INVALID_PRIORITY_MSG =
            "We caught an InvalidPriorityException when enqueuing with valid priorities.";

    private static final int MAX_PRIORITY = 10;
    private static final int MAX_INT = 1000;
    private static final long SEED = 47;
    private static Random r = new Random(SEED);

    /* ************************************************************* */
    /* *************** PRIVATE DOWNCASTING UTILS ******************* */
    /* ************************************************************* */

    private void prepArrayMinHeapTest() {
        intMinHeap = new ArrayMinHeap<>();
        stringMinHeap = new ArrayMinHeap<>();
    }

    private void prepLinkedMinHeapTest() {
        intMinHeap = new LinkedMinHeap<>();
        stringMinHeap = new LinkedMinHeap<>();
    }

    /* ************************************************************* */
    /* ************************************************************* */
    /* ******************* ARRAY MINHEAP TESTS  ******************* */
    /* ************************************************************* */
    /* ************************************************************* */
    @Test
    public void testArrayMinHeapConstructorAndAddOneElement() {
        prepArrayMinHeapTest();
        assertEquals("Size of ArrayMinHeap should be 0 at construction.", intMinHeap.size(), 0);
        assertEquals("Size of ArrayMinHeap should be 0 at construction.", stringMinHeap.size(), 0);
        assertTrue("Size of ArrayMinHeap should be 0 at construction.", intMinHeap.isEmpty() && stringMinHeap.isEmpty());
        stringMinHeap.insert("Dibidabo");
        assertEquals("Size of ArrayMinHeap should be 1 after inserting a single element.", 1, stringMinHeap.size());
    }


    @Test
    public void testArrayMinHeapSimpleInsertAndGetMin() {
        prepArrayMinHeapTest();
        String[] names = {"George", "Gianna", "Greg"};
        Arrays.asList(names).forEach(stringMinHeap::insert);
        assertEquals("Inserting " + names.length + " elements into an ArrayMinHeap should yield a size of "
                + names.length + ".", names.length, stringMinHeap.size());
        try {
            assertEquals("ArrayMinHeap::getMin() did not return the correct element.", "George", stringMinHeap.getMin());
        } catch (EmptyHeapException e) {
            fail("An EmptyHeapException should not have been thrown by ArrayMinHeap::getMin()," +
                    " since the heap was not empty upon call.");
        }
    }

    @Test
    public void testArrayMinHeapComplexInsertAndGetMin() {
        /*
         * [5, -1, 0, 8, 3, 10] should yield:
         *
         * 					 -1
         * 				    /   \
         * 				   3     0
         *                / \   /
         *               8   5 10
         */
        prepArrayMinHeapTest();
        Integer[] ints = {5, -1, 0, 8, 3, 10};
        ;
        Arrays.asList(ints).forEach(intMinHeap::insert);
        assertEquals("Inserting " + ints.length + " elements into an ArrayMinHeap should yield a size of " +
                ints.length + ".", ints.length, intMinHeap.size());
        try {
            assertEquals(" ArrayMinHeap.getMin() did not return the correct element.",
                    Integer.valueOf(-1), intMinHeap.getMin());
        } catch (EmptyHeapException e) {
            fail("An EmptyHeapException should not have been thrown by getMin()," +
                    " since the heap was not empty upon call.");
        }
    }

    @Test
    public void testArrayMinHeapSimpleDeleteMin() {
        prepArrayMinHeapTest();
        String[] names = {"George", "Gianna", "Greg"};
        Arrays.asList(names).forEach(stringMinHeap::insert);
        try {// root element should be "George"
            assertEquals("ArrayMinHeap.deleteMin() did not return the correct element.", "George", stringMinHeap.deleteMin());
        } catch (EmptyHeapException e) {
            fail("An EmptyHHeapException should not have been thrown by deleteMin()," +
                    " since the heap was not empty at time of deletion.");
        }
        assertEquals("After deleting an element from an ArrayMinHeap of size " + names.length + ", the new size should be " +
                (names.length - 1) + ", .", names.length - 1, stringMinHeap.size());

    }

    @Test
    public void testArrayMinHeapComplexDeleteMin() {
        /*
         * [5, -1, 0, 8, 3, 10] should yield:
         *
         * 					 -1
         * 				    /   \
         * 				   3     0
         *                / \   /
         *               8   5 10
         */
        prepArrayMinHeapTest();
        Integer[] ints = {5, -1, 0, 8, 3, 10};
        Arrays.asList(ints).forEach(intMinHeap::insert);

        try {
            // Take elements off one-by-one, examine min each and every time.
            assertEquals("On the first deletion from an originally six-element ArrayMinHeap, " +
                    "deleteMin() did not return correct element.", Integer.valueOf(-1), intMinHeap.deleteMin());
            assertEquals("On the second deletion from an originally six-element ArrayMinHeap, " +
                    "deleteMin() did not return correct element.", Integer.valueOf(0), intMinHeap.deleteMin());
            assertEquals("On the third deletion from an originally six-element ArrayMinHeap, " +
                    "deleteMin() did not return correct element.", Integer.valueOf(3), intMinHeap.deleteMin());
            assertEquals("On the fourth deletion from an originally six-element ArrayMinHeap, " +
                    "deleteMin() did not return correct element.", Integer.valueOf(5), intMinHeap.deleteMin());
            assertEquals("On the fifth deletion from an originally six-element ArrayMinHeap, " +
                    "deleteMin() did not return correct element.", Integer.valueOf(8), intMinHeap.deleteMin());
            assertEquals("On the sixth deletion from an originally six-element ArrayMinHeap, " +
                    "deleteMMin() did not return correct element.", Integer.valueOf(10), intMinHeap.deleteMin());
        } catch (EmptyHeapException exc) {
            fail("Should not have thrown an EmptyHeapException at this point.");
        }

        assertEquals("Deleting all the elements of an ArrayMinHeap should make its size zero.", 0, intMinHeap.size());
        assertTrue("Deleting all the elements of an ArrayMinHeap should make it empty.", intMinHeap.isEmpty());
    }

    @Test
    public void testArrayMinHeapManyInsertions() {
        prepArrayMinHeapTest();
        List<Integer> manyInts = IntStream.range(0, MAX_INT).boxed().collect(Collectors.toList());
        Collections.shuffle(manyInts, r);
        for (int i = 0; i < manyInts.size(); i++) {
            try {
                intMinHeap.insert(manyInts.get(i));
            } catch (Throwable t) {
                fail("Caught a " + t.getClass().getSimpleName() + " during the insertion of element #" + i + ". " +
                        "The message was: " + t.getMessage() + ".");
            }
        }
        assertEquals("After " + MAX_INT + " insertions, the size of the ArrayMinHeap should be " + MAX_INT,
                MAX_INT, intMinHeap.size());

    }

    @Test
    public void testArrayMinHeapManyDeleteMins() {
        prepArrayMinHeapTest();
        List<Integer> manyInts = IntStream.range(0, MAX_INT).boxed().collect(Collectors.toList());
        Collections.shuffle(manyInts, r);
        manyInts.forEach(intMinHeap::insert);
        List<Integer> sortedInts = IntStream.range(0, MAX_INT).boxed().collect(Collectors.toList());
        for (int i = 0; i < manyInts.size(); i++) {
            try {
                assertEquals("ArrayMinHeap.deleteMin() returned the wrong element after deletion #" +
                        i + ".", sortedInts.get(i), intMinHeap.deleteMin());

            } catch (AssertionError ae) {
                throw (ae);
            } // Rethrow the AssertionError possibly thrown by assertEquals above.
            catch (Throwable t) { // And report any other Throwables with some detail.
                fail("Caught a " + t.getClass().getSimpleName() + " during the deletion of element #" + i + ". " +
                        "The message was: " + t.getMessage() + ".");
            }
        }
        assertEquals("After " + (MAX_INT + 1) + " insertions and subsequent deletions, the size of the ArrayMinHeap should be 0.",
                0, intMinHeap.size());
        assertTrue("After " + (MAX_INT + 1) + " insertions and subsequent deletions, the ArrayMinHeap should be reported empty.",
                intMinHeap.isEmpty());

    }

    @Test
    public void testArrayMinHeapIteratorAndConcurrentModifications() {
        /*
         * An input of [5, -1, 0, 8, 3, 10] was determined by the previous test
         * to be yielding:
         *
         * 					 -1
         * 				    /   \
         * 				   3     0
         *                / \   /
         *               8   5 10
         *
         *  Our goal now is to make sure that iterator() returns an Iterator which
         *  accesses the elements in ascending order, as implied by the MinHeap structure.
         */
        prepArrayMinHeapTest();
        Integer[] ints = {5, -1, 0, 8, 3, 10};
        Arrays.asList(ints).forEach(intMinHeap::insert);
        Arrays.sort(ints); // Arrays.sort always sorts in ascending order.
        int currentIndex = 0;
        for (Integer i : intMinHeap)
            assertEquals("ArrayMinHeap iterator doesn't seem to be exposing elements in proper order.", ints[currentIndex++], i);

        Iterator<Integer> it = intMinHeap.iterator();
        it.next();
        intMinHeap.insert(13);
        ConcurrentModificationException cme = null;
        try {
            it.next();
        } catch (ConcurrentModificationException cmeThrown) {
            cme = cmeThrown;
        } catch (Throwable t) {
            fail("When testing for appropriate fail-fast behavior of ArrayMinHeap's Iterator, we received a " +
                    t.getClass().getSimpleName() + " with message: " + t.getMessage() + " instead of a ConcurrentModificationException.");
        }
        assertNotNull("ArrayMinHeap iterator should have thrown a ConcurrentModificationException after a concurrent call to insert()" +
                " and a call to next().", cme);
        // Make sure it persists
        cme = null;
        try {
            it.next();
        } catch (ConcurrentModificationException cmeThrown) {
            cme = cmeThrown;
        } catch (Throwable t) {
            fail("When testing for appropriate fail-fast behavior of ArrayMinHeap's Iterator, we received a " +
                    t.getClass().getSimpleName() + " with message: " + t.getMessage() + " instead of a ConcurrentModificationException.");
        }
        assertNotNull("ArrayMinHeap iterator should have thrown a ConcurrentModificationException after a concurrent call to insert()" +
                " and 2 calls to next().", cme);
        intMinHeap = new ArrayMinHeap<>();
        it = intMinHeap.iterator(); // Reset iterator
        assertFalse("Iterator's hasNext() method should return false when " +
                "the ArrayMinHeap is empty.", it.hasNext());

    }

    /* ************************************************************* */
    /* ************************************************************* */
    /* ******************* LINKED MINHEAP TESTS  ******************* */
    /* ************************************************************* */
    /* ************************************************************* */


    @Test
    public void testLinkedMinHeapConstructorAndAddOneElement() {
        prepLinkedMinHeapTest();
        assertEquals("Size of LinkedMinHeap should be 0 at construction.", intMinHeap.size(), 0);
        assertEquals("\"Size of LinkedMinHeap should be 0 at construction.", stringMinHeap.size(), 0);
        assertTrue("Size of LinkedMinHeap should be 0 at construction.", intMinHeap.isEmpty() && stringMinHeap.isEmpty());
        stringMinHeap.insert("Dibidabo");
        assertEquals("Size of LinkedMinHeap should be 1 after inserting a single element.", 1, stringMinHeap.size());
    }

    @Test
    public void testLinkedMinHeapSimpleInsertAndGetMin() {
        prepLinkedMinHeapTest();
        String[] names = {"George", "Gianna", "Greg"};
        Arrays.asList(names).forEach(stringMinHeap::insert);
        assertEquals("Inserting " + names.length + " elements into an LinkedMinHeap should yield a size of "
                + names.length + ".", names.length, stringMinHeap.size());
        try {
            assertEquals(" LinkedMinHeap.getMin() did not return the correct element.",
                    "George", stringMinHeap.getMin());
        } catch (EmptyHeapException e) {
            fail("An EmptyHeapException should not have been thrown by getMin()," +
                    " since the heap was not empty upon call.");
        }
    }

    @Test
    public void testLinkedMinHeapComplexInsertAndGetMin() {
        /*
         * [5, -1, 0, 8, 3, 10] should yield:
         *
         * 					 -1
         * 				    /   \
         * 				   3     0
         *                / \   /
         *               8   5 10
         */
        prepLinkedMinHeapTest();
        Integer[] ints = {5, -1, 0, 8, 3, 10};
        Arrays.asList(ints).forEach(intMinHeap::insert);
        assertEquals("Inserting " + ints.length + " elements into a LinkedMinHeap should yield a size of " +
                ints.length + ".", ints.length, intMinHeap.size());
        try {
            assertEquals(" LinkedMinHeap.getMin() did not return the correct element.",
                    Integer.valueOf(-1), intMinHeap.getMin());
        } catch (EmptyHeapException e) {
            fail("An EmptyHeapException should not have been thrown by getMin()," +
                    " since the heap was not empty upon call.");
        }
    }

    @Test
    public void testLinkedMinHeapSimpleDeleteMin() {
        prepLinkedMinHeapTest();
        String[] names = {"George", "Gianna", "Greg"};
        Arrays.asList(names).forEach(stringMinHeap::insert);
        try {// root element should be "George"
            assertEquals("LinkedMinHeap.deleteMin() did not return the correct element.", "George", stringMinHeap.deleteMin());
        } catch (EmptyHeapException e) {
            fail("An EmptyHHeapException should not have been thrown by deleteMin()," +
                    " since the heap was not empty at time of deletion.");
        }
        assertEquals("After deleting an element from a LinkedMinHeap of size " + names.length + ", the new size should be " +
                (names.length - 1) + ", .", names.length - 1, stringMinHeap.size());

    }

    @Test
    public void testLinkedMinHeapComplexDeleteMin() {
        /*
         * [5, -1, 0, 8, 3, 10] should yield:
         *
         * 					 -1
         * 				    /   \
         * 				   3     0
         *                / \   /
         *               8   5 10
         */
        prepLinkedMinHeapTest();
        Integer[] ints = {5, -1, 0, 8, 3, 10};
        Arrays.asList(ints).forEach(intMinHeap::insert);

        try {
            // Take elements off one-by-one, examine min each and every time.
            assertEquals("On the first deletion, LinkedMinHeap.deleteMin() did not return correct element.", Integer.valueOf(-1), intMinHeap.deleteMin());
            assertEquals("On the second deletion, LinkedMinHeap.deleteMin() did not return correct element.", Integer.valueOf(0), intMinHeap.deleteMin());
            assertEquals("On the third deletion, LinkedMinHeap.deleteMin() did not return correct element.", Integer.valueOf(3), intMinHeap.deleteMin());
            assertEquals("On the fourth deletion, LinkedMinHeap.deleteMin() did not return correct element.", Integer.valueOf(5), intMinHeap.deleteMin());
            assertEquals("On the fifth deletion, LinkedMinHeap.deleteMin() did not return correct element.", Integer.valueOf(8), intMinHeap.deleteMin());
            assertEquals("On the sixth deletion, LinkedMinHeap.deleteMin() did not return correct element.", Integer.valueOf(10), intMinHeap.deleteMin());
        } catch (EmptyHeapException exc) {
            fail("Should not have thrown an EmptyHeapException at this point.");
        }

        assertEquals("Deleting all the elements of an LinkedMinHeap should make its size zero.", 0, intMinHeap.size());
        assertTrue("Deleting all the elements of an LinkedMinHeap should make it empty.", intMinHeap.isEmpty());
    }

    @Test
    public void testLinkedMinHeapManyInsertions() {
        prepLinkedMinHeapTest();
        List<Integer> manyInts = IntStream.range(0, MAX_INT).boxed().collect(Collectors.toList());
        Collections.shuffle(manyInts, r);
        int cntr = 0;
        for (Integer i : manyInts) {
            try {
                intMinHeap.insert(i);
            } catch (Throwable t) {
                fail("Caught a " + t.getClass().getSimpleName() + " during the insertion of element #" + (cntr++) + ". " +
                        "The message was: " + t.getMessage() + ".");
            }
        }
        assertEquals("After " + MAX_INT + " insertions, the size of the LinkedMinHeap should be " + MAX_INT,
                MAX_INT, intMinHeap.size());
    }

    @Test
    public void testLinkedMinHeapManyDeleteMins() {
        prepLinkedMinHeapTest();
        List<Integer> manyInts = IntStream.range(0, MAX_INT).boxed().collect(Collectors.toList());
        Collections.shuffle(manyInts, r);

        manyInts.forEach(intMinHeap::insert);
        List<Integer> sortedInts = IntStream.range(0, MAX_INT).boxed().collect(Collectors.toList());
        for (int cntr = 0; cntr < manyInts.size(); cntr++) {
            try {
                assertEquals("LinkedMinHeap.deleteMin() returned the wrong element after deletion #" +
                        cntr + ".", sortedInts.get(cntr), intMinHeap.deleteMin());

            } catch (AssertionError ae) {
                throw (ae); // Rethrow the AssertionError possibly thrown by assertEquals above.
            } catch (Throwable t) { // And report any other Throwables with some detail.
                fail("Caught a " + t.getClass().getSimpleName() + " during the deletion of element #" + cntr + ". " +
                        "The message was: " + t.getMessage() + ".");
            }
        }
        assertEquals("After " + (MAX_INT + 1) + " insertions and subsequent deletions, the size of the LinkedMinHeap should be 0.",
                0, intMinHeap.size());
        assertTrue("After " + (MAX_INT + 1) + " insertions and subsequent deletions, the LinkedMinHeap should be reported empty.",
                intMinHeap.isEmpty());

    }

    @Test
    public void testLinkedMinHeapIteratorAndConcurrentModifications() {
        /*
         * An input of [5, -1, 0, 8, 3, 10] was determined by the previous test
         * to be yielding:
         *
         * 					 -1
         * 				    /   \
         * 				   3     0
         *                / \   /
         *               8   5 10
         *
         *  Our goal now is to make sure that iterator() returns an Iterator which
         *  accesses the elements in ascending order, as implied by the MinHeap structure.
         */

        prepLinkedMinHeapTest();
        Integer[] ints = {5, -1, 0, 8, 3, 10};
        Arrays.asList(ints).forEach(intMinHeap::insert);
        Arrays.sort(ints); // Arrays.sort always sorts in ascending order.
        int currentIndex = 0;
        for (Integer i : intMinHeap)
            assertEquals("LinkedMinHeap iterator doesn't seem to be exposing elements in proper order.", ints[currentIndex++], i);

        Iterator<Integer> it = intMinHeap.iterator();
        it.next();
        intMinHeap.insert(13);
        ConcurrentModificationException cme = null;
        try {
            it.next();
        } catch (ConcurrentModificationException cmeThrown) {
            cme = cmeThrown;
        } catch (Throwable t) {
            fail("When testing for appropriate fail-fast behavior of LinkedMinHeap's Iterator, we received a " +
                    t.getClass().getSimpleName() + " with message: " + t.getMessage() + " instead of a ConcurrentModificationException.");
        }
        assertNotNull("LinkedMinHeap iterator should have thrown a ConcurrentModificationException", cme);
        intMinHeap = new LinkedMinHeap<>();
        it = intMinHeap.iterator(); // Reset iterator
        assertFalse("Iterator's hasNext() method should return false when " +
                "the LinkedMinHeap is empty.", it.hasNext());

    }

    /* ************************************************************* */
    /* *************** PRIVATE DOWNCASTING UTILS ******************* */
    /* ************************************************************* */

    private void prepLinearPQTest() {
        greekNamesQueue = new LinearPriorityQueue<>();
        doubles = new LinearPriorityQueue<>();
    }

    private void prepMinHeapPQTest() {
        greekNamesQueue = new MinHeapPriorityQueue<>();
        doubles = new MinHeapPriorityQueue<>();
    }

    /* ************************************************************* */
    /* ************************************************************* */
    /* ********************* LINEAR PQ TESTS  ********************** */
    /* ************************************************************* */
    /* ************************************************************* */


    @Test
    public void testLinearPQConstructorAndSize() {
        prepLinearPQTest();
        assertTrue("After construction, a LinearPriorityQueue should be empty.",
                greekNamesQueue.isEmpty());
        assertEquals("After construction, a LinearPriorityQueue's size should be 0.", 0,
                greekNamesQueue.size());
    }

    @Test
    public void testLinearPQClear() throws InvalidPriorityException {
        prepLinearPQTest();

        greekNamesQueue.enqueue("Alexandrou", 8);
        greekNamesQueue = new LinearPriorityQueue<>();
        assertTrue("After resetting it, a LinearPriorityQueue should be empty.", greekNamesQueue.isEmpty());
        assertEquals("After resetting it, a LinearPriorityQueue's size should be 0.", 0, greekNamesQueue.size());

    }

    @Test
    public void testLinearPQSimpleEnqueueDifferentPriorities() throws InvalidPriorityException {
        prepLinearPQTest();

        greekNamesQueue.enqueue("Filippou", 2);
        greekNamesQueue.enqueue("Alexandrou", 3);
        greekNamesQueue.enqueue("Costakis", 1);

        try {
            assertEquals("LinearPriorityQueue.getFirst() did not return the correct element.",
                    "Costakis", greekNamesQueue.getFirst());
        } catch (EmptyPriorityQueueException ignored) {
            fail("Since the queue was not empty upon call to LinearPriorityQueue.getFirst(), an " +
                    "EmptyPriorityQueueException should not have been thrown.");
        }

    }

    @Test
    public void testLinearPQInvalidPriorities() {
        prepLinearPQTest();

        // Priority of 0...
        InvalidPriorityException exc = null;
        try {
            greekNamesQueue.enqueue("Grigoris", 0);
        } catch (InvalidPriorityException excThrown) {
            exc = excThrown;
        } catch (Throwable t) {
            fail("When enqueueing an element with priority -1, we expected an InvalidPriorityException, "
                    + "but we instead got a " + t.getClass() + " with message: " + t.getMessage() + ".");
        }
        assertNotNull("After supplying a priority of 0 in an enqueueing, we were expecting enqueue() "
                + " to throw an InvalidPriorityException", exc);

        // Priority of -1...
        exc = null;
        try {
            greekNamesQueue.enqueue("Grigoris", -1);
        } catch (InvalidPriorityException excThrown) {
            exc = excThrown;
        } catch (Throwable t) {
            fail("When enqueueing an element with priority -1, we expected an InvalidPriorityException, "
                    + "but we instead got a " + t.getClass() + " with message: " + t.getMessage() + ".");
        }
        assertNotNull("After supplying a priority of -1 in an enqueueing, we were expecting enqueue() "
                + " to throw an InvalidPriorityException", exc);

        // And a bunch of random priorities...
        List<Integer> priorities = IntStream.range(1, MAX_PRIORITY + 1).boxed().collect(Collectors.toList());
        Collections.shuffle(priorities, r);
        priorities.forEach(pr -> {
            try {
                doubles.enqueue(r.nextDouble(), pr);
            } catch (InvalidPriorityException ignored) { //
                fail(INVALID_PRIORITY_MSG);
            }
        });
    }

    @Test
    public void testLinearPQSimpleEnqueueSamePriorities() throws InvalidPriorityException {
        prepLinearPQTest();

        greekNamesQueue.enqueue("Filippou", 1);
        greekNamesQueue.enqueue("Alexandrou", 1);
        greekNamesQueue.enqueue("Costakis", 1);

        try {
            assertEquals("LinearPriorityQueue.getFirst() did not return the correct element. " +
                    "Are you treating same priorities correctly?", "Filippou", greekNamesQueue.getFirst());
        } catch (EmptyPriorityQueueException ignored) {
            fail("Since the queue was not empty upon call to LinearPriorityQueue.getFirst(), an " +
                    "EmptyPriorityQueueException should not have been thrown.");
        }

    }

    @Test
    public void testLinearPQComplexEnqueuesAndDequeues() throws InvalidPriorityException {
        prepLinearPQTest();

        greekNamesQueue.enqueue("Filippou", 2);

        assertEquals("After inserting a single element, a LinearPriorityQueue should have a size of 1", 1,
                greekNamesQueue.size());

        greekNamesQueue.enqueue("Alexandrou", 10);
        assertEquals("After enqueueing 2 elements, the queue should have a size of 2.", 2, greekNamesQueue.size());

        greekNamesQueue.enqueue("Vasilakopoulos", 5);
        assertEquals("After enqueueing 3 elements, the queue should have a size of 3.", 3, greekNamesQueue.size());
        try {
            assertEquals("LinearPriorityQueue.getFirst() did not return the correct element.",
                    "Filippou", greekNamesQueue.getFirst());
        } catch (EmptyPriorityQueueException ignored) {
            fail("Since the queue was not empty upon call to LinearPriorityQueue.getFirst(), an " +
                    "EmptyPriorityQueueException should not have been thrown.");
        }
        try {
            assertEquals("LinearPriorityQueue.dequeue() did not return the correct element.",
                    "Filippou", greekNamesQueue.dequeue());
        } catch (EmptyPriorityQueueException ignored) {
            fail("Since the queue was not empty upon call to LinearPriorityQueue.dequeue(), an " +
                    "EmptyPriorityQueueException should not have been thrown.");
        }
        try {
            assertEquals(" After a prior dequeueing, LinearPriorityQueue.getFirst() did not return the " +
                    "correct element.", "Vasilakopoulos", greekNamesQueue.getFirst());

        } catch (EmptyPriorityQueueException ignored) {
            fail("Despite a prior dequeue-ing, the queue was still not empty upon call to " +
                    "LinearPriorityQueue.getFirst(), so an EmptyPriorityQueueException should not have been thrown.");
        }
        greekNamesQueue.enqueue("Papandreou", 1);
        greekNamesQueue.enqueue("Mitsotakis", 1);

        assertEquals("After 3 enqueueings, 1 dequeueing and 2 enqueueings, the size of the LinearPriorityQueue should be 4.",
                4, greekNamesQueue.size());
        try {
            assertNotEquals("LinearPriorityQueue.dequeue() returned an element that used to be the top element, " +
                    "but is not any more after some recent enqueueings.", greekNamesQueue.dequeue(), "Vasilakopoulos"); // No longer the first.
            assertEquals("LinearPriorityQueue.dequeue() did not return the correct element. Are you treating same priorities correctly?", greekNamesQueue.dequeue(), "Mitsotakis");
            assertEquals("LinearPriorityQueue.dequeue() did not return the correct element. Are you treating same priorities correctly?", greekNamesQueue.dequeue(), "Vasilakopoulos");
            assertEquals("LinearPriorityQueue.dequeue() did not return the correct element. Are you treating same priorities correctly?", greekNamesQueue.dequeue(), "Alexandrou");
        } catch (EmptyPriorityQueueException ignored) {
            fail("Despite a prior dequeue-ing, the queue was still not empty upon call to LinearPriorityQueue.dequeue(), " +
                    "so an EmptyPriorityQueueException should not have been thrown.");
        }

        assertEquals("After dequeue-ing every element, the LinearPriorityQueue should have a size of 0",
                0, greekNamesQueue.size());
        assertTrue("After dequeue-ing every element, the LinearPriorityQueue should be empty.",
                greekNamesQueue.isEmpty());
    }

    @Test
    public void testLinearPQManyEnqueues() {
        prepLinearPQTest();
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
        assertEquals("After enqueueing " + MAX_PRIORITY + " elements, the size of the LinearPriorityQueue " +
                "should be " + MAX_PRIORITY + ".", MAX_PRIORITY, doubles.size());
    }


    @Test
    public void testLinearPQManyDequeues() {
        prepLinearPQTest();
        List<Integer> priorities = IntStream.range(1, MAX_PRIORITY + 1).boxed().collect(Collectors.toList());
        Collections.shuffle(priorities, r);
        priorities.forEach(pr -> {
            try {
                doubles.enqueue(r.nextDouble(), pr);
            } catch (InvalidPriorityException ignored) { //
                fail(INVALID_PRIORITY_MSG);
            } // Insert a bunch of doubles with randomly shuffled priorities
        });
        // Dequeue them all
        for (int cnt = 0; cnt < MAX_PRIORITY; cnt++) {
            try {
                doubles.dequeue();
            } catch (Throwable t) {
                fail("During the dequeueing of element #" + cnt + ", we caught a " + t.getClass().getSimpleName()
                        + " with message " + t.getMessage());
            }
        }
        assertEquals("After dequeueing all the elements of the LinearPriorityQueue, its size should be 0.",
                0, doubles.size());
        assertTrue("After dequeueing all the elements of the LinearPriorityQueue, its size should be 0.",
                doubles.isEmpty());

    }


    @Test
    public void testLinearPQIteratorAndConcurrentModifications() throws InvalidPriorityException {
        prepLinearPQTest();

        String[] strings = {"Karathodori", "Stergiou", "Tasou", "Pipinis", "Papandreou", "Mitsotakis"};
        for (int i = 0; i < strings.length; i++)
            greekNamesQueue.enqueue(strings[i], strings.length - i); // Reverse order of priority. such that Mitsotakis is first.
        Iterator<String> it = greekNamesQueue.iterator();
        assertTrue("Since we have some elements in the MinHeapPriorityQueue, the iterator's hasNext()" +
                " method should return true.", it.hasNext());


        for (int i = strings.length - 1; i > -1; i--)
            assertEquals("The iterator's next() method did not return the expected element.", strings[i], it.next());
        assertFalse("After looping through *all* the elements with next(), the iterator's hasNext() method" +
                " should return false.", it.hasNext());

        greekNamesQueue = new LinearPriorityQueue<>();

        // Now we will also check iterations over a queue that has
        // non-singleton FIFO fifoqueues in it

        // Give the first 4 people in that array a priority of 2, and the last
        // 2 people a priority of 1:

        for (int i = 0; i < strings.length; i++)
            greekNamesQueue.enqueue(strings[i], i < 4 ? 2 : 1);
        it = greekNamesQueue.iterator();

        assertTrue("Before looping through the LinearPriorityQueue, its iterator's hasNext() method should " +
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

    }

    /* ************************************************************* */
    /* ************************************************************* */
    /* ********************* MINHEAP PQ TESTS  ********************** */
    /* ************************************************************* */
    /* ************************************************************* */
    @Test
    public void testMinHeapPQConstructorAndSize() {
        prepMinHeapPQTest();
        assertTrue("After construction, a MinHeapPriorityQueue should be empty.",
                greekNamesQueue.isEmpty());
        assertEquals("After construction, a MinHeapPriorityQueue's size should be 0.", 0,
                greekNamesQueue.size());
    }

    @Test
    public void testMinHeapPQClear() throws InvalidPriorityException {
        prepMinHeapPQTest();
        greekNamesQueue.enqueue("Alexandrou", 8);
        greekNamesQueue = new MinHeapPriorityQueue<>();
        assertTrue("After resetting it, a MinHeapPriorityQueue should be empty.", greekNamesQueue.isEmpty());
        assertEquals("After resetting it, a MinHeapPriorityQueue's size should be 0.", 0, greekNamesQueue.size());
    }

    @Test
    public void testMinHeapPQSimpleEnqueueDifferentPriorities() throws InvalidPriorityException {
        prepMinHeapPQTest();

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
    }

    @Test
    public void testMinHeapPQInvalidPriorities() {
        prepMinHeapPQTest();
        InvalidPriorityException exc = null;
        try {
            greekNamesQueue.enqueue("Grigoris", 0);
        } catch (InvalidPriorityException excThrown) {
            exc = excThrown;
        } catch (Throwable t) {

            fail("When enqueueing an element with priority -1, we expected an InvalidPriorityException, "
                    + "but we instead got a " + t.getClass() + " with message: " + t.getMessage() + ".");
        }
        assertNotNull("Was expecting an InvalidPriorityException when enqueueing with a priority of 0.", exc);
        exc = null;
        try {
            greekNamesQueue.enqueue("Grigoris", -1);
        } catch (InvalidPriorityException excThrown) {
            // ok, expected behavior.
        } catch (Throwable t) {
            fail("When enqueueing an element with priority -1, we expected an InvalidPriorityException, "
                    + "but we instead got a " + t.getClass() + " with message: " + t.getMessage() + ".");
        }
    }

    @Test
    public void testMinHeapPQSimpleEnqueueSamePriorities() throws InvalidPriorityException {
        prepMinHeapPQTest();

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

    }

    @Test
    public void testMinHeapPQComplexEnqueuesAndDequeues() throws InvalidPriorityException {
        prepMinHeapPQTest();

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

    }


    @Test
    public void testMinHeapPQManyEnqueues() {
        prepMinHeapPQTest();
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
        prepMinHeapPQTest();
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
    public void testMinHeapPQIteratorAndConcurrentModifications() throws InvalidPriorityException {
        prepMinHeapPQTest();

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

        greekNamesQueue = new MinHeapPriorityQueue<>();

        // Now we will also check iterations over a queue that has
        // non-singleton FIFO fifoqueues in it

        // Give the first 4 people in that array a priority of 2, and the last
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

        // Finally, check the proper throwing of a ConcurrentModificationException instance

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

    }
}