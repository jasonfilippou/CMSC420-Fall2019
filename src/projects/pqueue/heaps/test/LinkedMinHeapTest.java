package projects.pqueue.heaps.test;

import projects.pqueue.heaps.*;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * <p>A testing framework for {@link LinkedMinHeap}.</p>
 *
 * @author  <a href="mailto:jasonfil@cs.umd.edu">Jason Filippou</a>
 *
 * @see ArrayMinHeapTest
 */
public class LinkedMinHeapTest {

    private MinHeap<Integer> intMinHeap = new LinkedMinHeap<Integer>();
    private MinHeap<String> stringMinHeap = new LinkedMinHeap<String>();
    private static final int MAX_INT = 1000;
    private static final long SEED=47;
    private static Random r = new Random(SEED);

    @Test
    public void testLinkedMinHeapClear(){

        IntStream.of(20, 10, 89, 20, 110).forEach(intMinHeap::insert);
        intMinHeap.clear();
        assertTrue("Clearing an LinkedMinHeap should make it empty.", intMinHeap.isEmpty());
    }


    @Test
    public void testLinkedMinHeapConstructorAndAddOneElement(){
        assertEquals("Size of LinkedMinHeap should be 0 at construction.", intMinHeap.size(), 0);
        assertEquals("\"Size of LinkedMinHeap should be 0 at construction.", stringMinHeap.size(), 0);
        assertTrue("Size of LinkedMinHeap should be 0 at construction.", intMinHeap.isEmpty() && stringMinHeap.isEmpty());
        stringMinHeap.insert("Dibidabo");
        assertEquals("Size of LinkedMinHeap should be 1 after inserting a single element.", 1, stringMinHeap.size());
    }

    @Test
    public void testLinkedMinHeapSimpleInsertAndGetMin(){
        String[] names = {"George", "Gianna", "Greg"};
        for(String s : names)
            stringMinHeap.insert(s);
        assertEquals("Inserting " + names.length + " elements into an LinkedMinHeap should yield a size of "
                + names.length + ".", names.length, stringMinHeap.size());
        try {
            assertEquals(" LinkedMinHeap.getMin() did not return the correct element.", "George", stringMinHeap.getMin());
        } catch (EmptyHeapException e) {
            fail("An EmptyHeapException should not have been thrown by getMin()," +
                    " since the heap was not empty upon call.");
        }
    }

    @Test
    public void testLinkedMinHeapComplexInsertAndGetMin(){
        /*
         * [5, -1, 0, 8, 3, 10] should yield:
         *
         * 					 -1
         * 				    /   \
         * 				   3     0
         *                / \   /
         *               8   5 10
         */

        int[] ints = {5, -1, 0, 8, 3, 10};
        for(Integer i: ints)
            intMinHeap.insert(i);
        assertEquals("Inserting " + ints.length + " elements into an LinkedMinHeap should yield a size of " +
                ints.length + ".", ints.length, intMinHeap.size());
        try {
            assertEquals(" LinkedMinHeap.getMin() did not return the correct element.",
                    new Integer(-1), intMinHeap.getMin());
        } catch (EmptyHeapException e) {
            fail("An EmptyHeapException should not have been thrown by getMin()," +
                    " since the heap was not empty upon call.");
        }
    }

    @Test
    public void testLinkedMinHeapSimpleDeleteMin(){
        String[] names = {"George", "Gianna", "Greg"};
        for(String s : names)
            stringMinHeap.insert(s);
        try {// root element should be "George"
            assertEquals("LinkedMinHeap.deleteMin() did not return the correct element.", "George", stringMinHeap.deleteMin());
        }catch(EmptyHeapException e){
            fail("An EmptyHHeapException should not have been thrown by deleteMin()," +
                    " since the heap was not empty at time of deletion.");
        }
        assertEquals("After deleting an element from an LinkedMinHeap of size " + names.length + ", the new size should be " +
                (names.length - 1) + ", .", names.length - 1,stringMinHeap.size());

        try {
            while(!stringMinHeap.isEmpty())
                stringMinHeap.deleteMin();
        } catch(EmptyHeapException e){
            fail("An EmptyHeapException should not have been thrown by deleteMin() here.");
        }

        assertEquals("Deleting all the elements of an LinkedMinHeap should make its size zero.", 0, stringMinHeap.size());
        assertTrue("After deleting all the elements of an LinkedMinHeap, it should be considered empty.", stringMinHeap.isEmpty());


    }

    @Test
    public void testLinkedMinHeapComplexDeleteMin(){
        /*
         * [5, -1, 0, 8, 3, 10] should yield:
         *
         * 					 -1
         * 				    /   \
         * 				   3     0
         *                / \   /
         *               8   5 10
         */

        int[] ints = {5, -1, 0, 8, 3, 10};
        for(Integer i: ints)
            intMinHeap.insert(i);

        try{
            // Take elements off one-by-one, examine min each and every time.
            assertEquals("On the first deletion, LinkedMinHeap.deleteMin() did not return correct element.",  new Integer(-1), intMinHeap.deleteMin());
            assertEquals("On the second deletion, LinkedMinHeap.deleteMin() did not return correct element.", new Integer(0), intMinHeap.deleteMin());
            assertEquals("On the third deletion, LinkedMinHeap.deleteMin() did not return correct element.", new Integer(3), intMinHeap.deleteMin());
            assertEquals("On the fourth deletion, LinkedMinHeap.deleteMin() did not return correct element.", new Integer(5), intMinHeap.deleteMin());
            assertEquals("On the fifth deletion, LinkedMinHeap.deleteMin() did not return correct element.", new Integer(8), intMinHeap.deleteMin());
            assertEquals("On the sixth deletion, LinkedMinHeap.deleteMin() did not return correct element.", new Integer(10), intMinHeap.deleteMin());
        } catch(EmptyHeapException exc){
            fail("Should not have thrown an EmptyHeapException at this point.");
        }

        assertEquals("Deleting all the elements of an LinkedMinHeap should make its size zero.", 0, intMinHeap.size());
        assertTrue("Deleting all the elements of an LinkedMinHeap should make it empty.", intMinHeap.isEmpty());
    }

    @Test
    public void testLinkedMinHeapManyInsertions(){
        List<Integer> manyInts = IntStream.range(0, MAX_INT).boxed().collect(Collectors.toList());
        Collections.shuffle(manyInts, r);
        int cntr = 0;
        for(Integer i: manyInts){
            try {
                intMinHeap.insert(i);
            }
            catch(Throwable t){
                fail("Caught a " + t.getClass().getSimpleName() + " during the insertion of element #" + cntr + ". " +
                        "The message was: " + t.getMessage() + ".");
            }
        }
        assertEquals("After " + MAX_INT  + " insertions, the size of the LinkedMinHeap should be " + MAX_INT,
                MAX_INT, intMinHeap.size());
    }

    @Test
    public void testLinkedMinHeapManyDeleteMins(){
        List<Integer> manyInts = IntStream.range(0, MAX_INT).boxed().collect(Collectors.toList());
        Collections.shuffle(manyInts, r);

        for(Integer i: manyInts)
            intMinHeap.insert(i);

        List<Integer> sortedInts = IntStream.range(0, MAX_INT).boxed().collect(Collectors.toList());
        for(int cntr = 0; cntr < manyInts.size(); cntr++){
            try {
                assertEquals("LinkedMinHeap.deleteMin() returned the wrong element after deletion #" +
                        cntr + ".", sortedInts.get(cntr), intMinHeap.deleteMin());

            } catch(AssertionError ae){ throw(ae); } // Rethrow the AssertionError possibly thrown by assertEquals above.
            catch(Throwable t){ // And report any other Throwables with some detail.
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
    public void testLinkedMinHeapIteratorAndConcurrentModifications(){
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

        Integer[] ints = {5, -1, 0, 8, 3, 10};
        for(Integer i : ints)
            intMinHeap.insert(i);
        Arrays.sort(ints); // Arrays.sort always sorts in ascending order.
        int currentIndex = 0;
        for(Integer i : intMinHeap)
            assertEquals("LinkedMinHeap iterator doesn't seem to be exposing elements in proper order.", ints[currentIndex++], i);

        Iterator<Integer> it = intMinHeap.iterator();
        it.next();
        intMinHeap.insert(13);
        ConcurrentModificationException cme = null;
        try {
            it.next();
        } catch(ConcurrentModificationException cmeThrown){ cme = cmeThrown;}
        catch(Throwable t) {
            fail("When testing for appropriate fail-fast behavior of LinkedMinHeap's Iterator, we received a " +
                    t.getClass().getSimpleName() + " with message: " + t.getMessage() + " instead of a ConcurrentModificationException.");
        }
        assertNotNull("LinkedMinHeap iterator should have thrown a ConcurrentModificationException",cme);
        intMinHeap.clear();
        it = intMinHeap.iterator(); // Reset iterator
        assertFalse( "Iterator's hasNext() method should return false when " +
                "the LinkedMinHeap is empty.", it.hasNext());

    }

}