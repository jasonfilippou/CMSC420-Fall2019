package projects.spatial.knnutils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;


/**
 * <p>{@link BoundedPriorityQueue} is a priority queue whose number of elements
 * is bounded. Insertions are such that if the queue's provided capacity is surpassed,
 * its length is not expanded, but rather the maximum priority element is ejected
 * (which could be the element just attempted to be enqueued).</p>
 *
 * <p><b>YOU ***** MUST ***** IMPLEMENT THIS CLASS!</b></p>
 *
 * @author  <a href = "https://github.com/JasonFil">Jason Filippou</a>
 *
 * @see PriorityQueue
 * @see PriorityQueueNode
 */
public class BoundedPriorityQueue<T> implements PriorityQueue<T>{

	/**
	 * Since this class is a generic, we will opt for an {@link ArrayList}
	 * instead of a raw {@link Object} array for our static storage. This avoids
	 * downcasting problems with {@link Object}s and Ts.
	 */
	private ArrayList<PriorityQueueNode<T>> elements;

	/**
	 * The {@link BoundedPriorityQueue} will be provided a maximum size at construction time.
	 * Refer to the course slides about how the BPQ should be treated.
	 */
	private int queueSize;

	/**
	 * The order in which an element is inserted will be useful for tie-breaking
	 * in the case of common priorities.
	 */
	private int orderInserted;

	/**
	 * modificationFlag is useful for making the {@link Iterator} returned by {@link #iterator()}
	 * <b>fail-fast</b>.
	 */
	private boolean modificationFlag; // Will be useful for our iterator.

	/**
	 * Constructor that specifies the size of our queue.
	 * @param size The static size of the {@link BoundedPriorityQueue}. Has to be a positive integer.
	 * @throws RuntimeException if size is not a strictly positive integer.
	 */
	public BoundedPriorityQueue(int size){
		if(size <= 0)
			throw new RuntimeException("Invalid size for BPQ: " + size + " provided.");
		elements = new ArrayList<PriorityQueueNode<T>>(size);
		queueSize = size;
		orderInserted = 0;
		modificationFlag = false;
	}

	/**
	 * <p>Enqueueing elements for BoundedPriorityQueues works a little bit differently from general case
	 * PriorityQueues. If the queue is not at capacity, the element is inserted at its
	 * appropriate location in the sequence. On the other hand, if the object is at capacity, the element is
	 * inserted in its appropriate spot in the sequence (if such a spot exists, based on its priority) and
	 * the maximum priority element is ejected from the structure.</p>
	 * 
	 * @param element The element to insert in the queue.
	 * @param priority The priority of the element to insert in the queue.
	 */
	@Override
	public void enqueue(T element, BigDecimal priority) {

		// If our element store is empty, just insert the element.
		if(elements.isEmpty()){
			elements.add(new PriorityQueueNode<T>(element, priority, orderInserted++));
			modificationFlag = true;
			return;
		}

		// Else, find the position to insert the element at.
		boolean inserted = false;
		for(int i = 0; i < elements.size(); i++){
			if(elements.get(i).getPriority().compareTo(priority) > 0) {
				inserted = true;
				if(elements.size() == queueSize)
					elements.remove(queueSize - 1); // Pop the last element
				elements.add(i, new PriorityQueueNode<T>(element, priority, orderInserted++));
				modificationFlag = true;
				break;
			}
		}
		// If you didn't find an appropriate spot to insert the element and the queue
		// is not at capacity, feel free to insert the element at the end of our queue.
		if(!inserted && elements.size() < queueSize){
			elements.add(new PriorityQueueNode<>(element, priority, orderInserted++));
			modificationFlag = true;
		}
	}

	@Override
	public T dequeue() {
		if(isEmpty())
			return null;
		else {
			T retVal = elements.remove(0).getData();
			modificationFlag = true;
			return retVal;
		}
	}

	@Override
	public T first() {
		return isEmpty() ? null : elements.get(0).getData();
	}
	
	/**
	 * Returns the last element in the queue. Useful for cases where we want to 
	 * compare the priorities of a given quantity with the maximum priority of 
	 * our stored quantities. In a minheap-based implementation of any {@link PriorityQueue},
	 * this operation would scan O(n) nodes and O(nlogn) links. In an array-based implementation,
	 * it takes constant time.
	 * @return The maximum priority element in our queue, or null if the queue is empty.
	 */
	public T last() {
		return isEmpty()? null : elements.get(elements.size() - 1).getData();
	}

	@Override
	public int size() {
		elements.trimToSize(); // To avoid dubious answers.
		return elements.size();
	}

	@Override
	public boolean isEmpty() {
		return elements.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return new BoundedPriorityQueueIterator();
	}
	
	// Iterator class. We will implement this as a fail-fast Iterator.
	private class BoundedPriorityQueueIterator implements Iterator<T>{

		private int currIndex;
		
		public BoundedPriorityQueueIterator(){
			currIndex = 0;
			modificationFlag = false; // So, if an enqueueing or dequeuing happens while our iterator is living, we will know.
		}
		
		@Override
		public boolean hasNext() {
			return currIndex < elements.size();
		}

		@Override
		public T next() throws ConcurrentModificationException{
			if(modificationFlag)
				throw new ConcurrentModificationException("BoundedPriorityQueueIterator.next(): Attempted to "
						+ "call \"next()\" on the queue after it was modified.");
			T retVal = elements.get(currIndex).getData();
			currIndex++;
			return retVal;
		}

		@Override
		public void remove() throws IllegalStateException, ConcurrentModificationException{
			if(currIndex == 0)
				throw new IllegalStateException("BoundedPriorityQueueIterator.remove(): Need at least one call to \"next()\" before removal.");
			if(modificationFlag)
				throw new ConcurrentModificationException("BoundedPriorityQueueIterator.remove(): Attempted to "
						+ "call \"remove()\" on the queue after it was modified.");
			elements.remove(currIndex - 1);
			currIndex--;
		}
		
	}
}
