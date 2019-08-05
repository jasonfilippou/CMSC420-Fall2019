package projects.pqueue.priorityqueues; // ******* <---  DO NOT ERASE THIS LINE!!!! *******


/* *****************************************************************************************
 * THE FOLLOWING IMPORTS WILL BE NEEDED BY YOUR CODE, BECAUSE WE REQUIRE THAT YOU USE
 * ANY ONE OF YOUR EXISTING MINHEAP IMPLEMENTATIONS TO IMPLEMENT THIS CLASS. TO ACCESS
 * YOUR MINHEAP'S METHODS YOU NEED THEIR SIGNATURES, WHICH ARE DECLARED IN THE MINHEAP
 * INTERFACE. ALSO, SINCE THE PRIORITYQUEUE INTERFACE THAT YOU EXTEND IS ITERABLE, THE IMPORT OF ITERATOR
 * IS NEEDED IN ORDER TO MAKE YOUR CODE COMPILABLE.
 ** ********************************************************************************** */

import projects.pqueue.exceptions.InvalidPriorityException;
import projects.pqueue.heaps.MinHeap;
import java.util.Iterator;
import projects.pqueue.heaps.EmptyHeapException;
import projects.pqueue.heaps.LinkedMinHeap;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * <p>MinHeapPriorityQueue is a {@link PriorityQueue} implemented using a {@link MinHeap}.</p>
 *
 * <p>You  <b>must</b> implement the methods in this file! To receive <b>any credit</b> for the unit tests
 * related to this class, your implementation <b>must</b> use <b>whichever</b> {@link MinHeap} implementation
 * among the two that you should have implemented you choose!</p>
 *
 * @author  ---- YOUR NAME HERE ----
 *
 * @param <T> The Type held by the container.
 *
 * @see LinearPriorityQueue
 * @see MinHeap
 */
public class MinHeapPriorityQueue<T> implements PriorityQueue<T>{

	private MinHeap<PriorityQueueNode> dataHeap;

	/**
	 * Simple default constructor.
	 */
	public MinHeapPriorityQueue(){
		dataHeap = new LinkedMinHeap<>(); // Any MinHeap can be used.
	}

	@Override
	public void enqueue(T element, int priority) throws InvalidPriorityException {
		if(priority < 1)
			throw new InvalidPriorityException("priorty cannot be smaller than 1");
		dataHeap.insert(new PriorityQueueNode(element, priority));
	}

	@Override
	public T dequeue() throws EmptyPriorityQueueException {
		try {
			return dataHeap.deleteMin().element;
		} catch (EmptyHeapException e) {
			throw new EmptyPriorityQueueException("dequeue(): FIFOQueue is empty!");
		}
	}

	@Override
	public T getFirst() throws EmptyPriorityQueueException {
		try {
			return dataHeap.getMin().element;
		}catch(EmptyHeapException e){
			throw new EmptyPriorityQueueException("getFirst(): FIFOQueue is empty!");
		}
	}

	@Override
	public int size() {
		return dataHeap.size();
	}

	@Override
	public boolean isEmpty() {
		return dataHeap.size() == 0;
	}

	@Override
	public void clear() {
		dataHeap.clear();
	}

	@Override
	public Iterator<T> iterator() {
		return new MinHeapPriorityQueueIterator();
	}

	class MinHeapPriorityQueueIterator implements Iterator<T> {
		private Iterator<PriorityQueueNode> dataHeapItr;

		MinHeapPriorityQueueIterator(){
			dataHeapItr = dataHeap.iterator();
		}

		@Override
		public boolean hasNext() {
			return dataHeapItr.hasNext();
		}

		@Override
		public T next() throws ConcurrentModificationException, NoSuchElementException {
			try {
				return dataHeapItr.next().element;
			} catch (NoSuchElementException e){
				throw new NoSuchElementException("next(): queue is empty.");
			} catch (ConcurrentModificationException e) {
				throw new ConcurrentModificationException("next(): Attempted to traverse a modified priority queue.");
			}
		}

		@Override
		public void remove() throws IllegalStateException, UnsupportedOperationException {
			try {
				dataHeapItr.remove();
			} catch (IllegalStateException e) {
				throw new IllegalStateException("Need at least one call to next() prior to removal.");
			} catch (UnsupportedOperationException e) {
				throw new UnsupportedOperationException("Arbitrary data removal is not allowed for a priority queue.");
			}
		}
	}

	/**
	 * A PriorityQueueNode is a Comparable type which is used to wrap around
	 * the (element, priority) pairs. Its overriding of the compareTo() method
	 * allows the contained MinHeap in the priority queue to disambiguate between
	 * the same priority elements, thus establishing a strict ordering in the heap,
	 * such that the root is always uniquely defined.
	 * 
	 * @author Jason Filippou (jasonfil@cs.umd.edu)
	 */
	private class PriorityQueueNode implements Comparable<PriorityQueueNode>{
		private T element;
		private int priority;
		private long timeCreated;

		PriorityQueueNode(T element, int priority){
			this.element = element;
			this.priority = priority;
			timeCreated = System.nanoTime();
		}

		@Override
		public int compareTo(PriorityQueueNode o) {
			// Remember that a numerically smaller priority
			// is actually considered larger in priority
			// queue terms. Also recall that we are using a 
			// MinHeap, so the smallest elements ascend to the top,
			// not the largest.
			boolean flag = priority < o.priority || (priority == o.priority && timeCreated < o.timeCreated);
			return flag ? -1 : 1;
		}
	}
}
