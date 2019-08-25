package projects.pqueue.fifoqueues.test;

import org.junit.Test;
import projects.pqueue.fifoqueues.*;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * <p>A testing framework for {@link LinearArrayFIFOQueue}.</p>
 *
 * <p>You should <b>not</b> edit this class! It is given to you as a resource for your project.</p>
 *
 * @author  <a href="https://github.com/JasonFil">Jason Filippou</a>
 *
 * @see LinkedFIFOQueueTest
 */
public class LinearArrayFIFOQueueTest {

	private int DEFAULT_CAPACITY = 20;
	private FIFOQueue<Integer> integerFIFOQueue = new LinearArrayFIFOQueue<Integer>();
	private FIFOQueue<String> stringFIFOQueue = new LinearArrayFIFOQueue<String>();
	private String[] veggies = {"Lettuce", "Cucumbers", "Apricots", "Olives", "Tomatoes", "Carrots"};
	
	@Test
	public void testExpandCapacity(){
		for(int i = 0; i < DEFAULT_CAPACITY; i++)
			integerFIFOQueue.enqueue(i);
		try {
			integerFIFOQueue.enqueue(200); // Just a random int, doesn't matter
			assertTrue(true);
		} catch(IndexOutOfBoundsException exc){// Shouldn't be throwing that
			fail("Should not be throwing an IndexOutOfBounds exception");
		}
		integerFIFOQueue.clear();
	}
	
	@Test
	public void testCopyConstructorAndEquals(){
		for(String s: veggies)
			stringFIFOQueue.enqueue(s);
		
		// Phase 1: Compare equality between projects.pqueue.fifoqueues of the same type.
		FIFOQueue<String> linearArrayFIFOQueueCopy = new LinearArrayFIFOQueue<String>(stringFIFOQueue);
		assertEquals(linearArrayFIFOQueueCopy, stringFIFOQueue);

		// Phase 2: Copy construct a CircularArrayFIFOQueue from a LinearArrayFIFOQueue.
		FIFOQueue<String> circularArrayFIFOQueueCopy = new CircularArrayFIFOQueue<String>(stringFIFOQueue);

		// The following line will trigger a warning on submit.cs, because we are explicitly requesting the
		// comparison of ostensibly unrelated types. However, our equals() methods allow for this!
		// Just check either LinkedFIFOQueue::equals() or LinearArrayFIFOQueue:equals().
		assertEquals(circularArrayFIFOQueueCopy, stringFIFOQueue);

		// Phase 3: Copy construct a LinkedFIFOQueue from a LinearArrayFIFOQueue.
		FIFOQueue<String> linkedFIFOQueueCopy = new LinkedFIFOQueue<String>(stringFIFOQueue);
		assertEquals(linkedFIFOQueueCopy, stringFIFOQueue); // same
		stringFIFOQueue.clear();
	}
	
	@Test
	public void testFirst(){
		stringFIFOQueue.enqueue("Soda");
		try {
			assertEquals(stringFIFOQueue.first(), "Soda");
			assertEquals(stringFIFOQueue.size(), 1); // Size should not've been decremented.
		} catch (EmptyFIFOQueueException e) { // This should not've been thrown
			fail("EmptyFIFOQueueException should not've been thrown.");
		}
		stringFIFOQueue.clear();
	}
	
	@Test
	public void testDequeue(){
		stringFIFOQueue.enqueue("Soda");
		try {
			assertEquals(stringFIFOQueue.dequeue(), "Soda");
			assertEquals(stringFIFOQueue.size(), 0); // Size SHOULD've been decremented.
		} catch (EmptyFIFOQueueException e) { // This should not've been thrown
			fail("EmptyFIFOQueueException should not've been thrown.");
		}
		stringFIFOQueue.clear();
		
		for(int i = 0; i < 100; i++)
			integerFIFOQueue.enqueue(i);
		
		for(int i = 0; i < 100; i++)
			try {
				integerFIFOQueue.dequeue();
			} catch (EmptyFIFOQueueException e) {
				fail("Empy queue exception should not've been thrown (i = " + i + ")");
			}
		assertTrue(integerFIFOQueue.isEmpty());
		integerFIFOQueue.clear();
	}
	
	@Test
	public void testEnqueue(){
		assertEquals(integerFIFOQueue.size(), 0);
		for(int i = 0; i < 100; i++)
			integerFIFOQueue.enqueue(i);
		assertEquals(integerFIFOQueue.size(), 100);
		try {
			assertEquals(integerFIFOQueue.first(), Integer.valueOf(0));
		} catch(EmptyFIFOQueueException exc){
			fail("EmptyFIFOQueueException should not've been thrown.");
		}
		integerFIFOQueue.clear();
	}
	
	@Test
	public void testIterator(){
		Iterator<String> its = stringFIFOQueue.iterator();
		for(String s: veggies)
			stringFIFOQueue.enqueue(s);
		try {
			its.next();
			fail("A ConcurrentModificationException should've been thrown.");
		} catch(ConcurrentModificationException cme){}
		catch(Throwable t){
			fail("Instead of a ConcurrentModificationException, a " + t.getClass() + 
					" was thrown, with message: " + t.getMessage() + ".");
		}
		its = stringFIFOQueue.iterator(); // reset
		int counter = 0;
		while(its.hasNext()) // verifying correct default output here
			assertEquals(its.next(), veggies[counter++]);

		for(int i = 10; i < 20; i++)
			integerFIFOQueue.enqueue(i);
		Iterator<Integer> iti = integerFIFOQueue.iterator();
		try {
			iti.remove();
			fail("An IllegalStateException should've been thrown at this point.");
		} catch(IllegalStateException ile){	}
		catch(Throwable t){
			fail("Instead of an IllegalStateException, a " + t.getClass() + 
					" was thrown, with message: " + t.getMessage() + ".");
		}
		iti.next();
		try{
			iti.remove();
		} catch(IllegalStateException ile){ // This should be thrown
			fail("An IllegalStateException should not have been thrown from this call.");
		}

		// Let's now check to see if removal of elements via iterators
		// results in queue consistency.
		its = stringFIFOQueue.iterator();
		its.next();
		its.remove();
		for(int i = 1; i < veggies.length; i++) // Starting from 1 because we removed one element.
			assertEquals(its.next(), veggies[i]);
		integerFIFOQueue.clear();
		stringFIFOQueue.clear();
	}
	
	@Test
	public void testEmpty(){
		assertTrue(stringFIFOQueue.isEmpty() && integerFIFOQueue.isEmpty());
		integerFIFOQueue.enqueue(3);
		assertFalse(integerFIFOQueue.isEmpty());
		integerFIFOQueue.clear();
	}
	
	@Test
	public void testToString(){
		for(String s: veggies)
			stringFIFOQueue.enqueue(s);
		assertEquals("[Lettuce, Cucumbers, Apricots, Olives, Tomatoes, Carrots]", stringFIFOQueue.toString());
		stringFIFOQueue.clear();
	}
	
}
