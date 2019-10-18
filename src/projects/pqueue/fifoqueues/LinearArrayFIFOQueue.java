package projects.pqueue.fifoqueues;


import projects.pqueue.exceptions.InvalidCapacityException;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
/**
 *<p> LinearArrayFIFOQueue is a {@link FIFOQueue} based on a simple linear array.
 * It is a very inefficient queue implementation, because depending on whether we choose the
 * getFirst or last "filled-in" cell of the array to hold the "front" element of the queue, either
 * enqueuing or dequeuing will take linear time. In this queue, we will assume a queue where enqueuings
 * are much more common than dequeueings. This means that the inner implementation has the "top" element at
 * the getFirst address of the array, and calls to {@link #dequeue()} will result in (linear-time)
 * shifting of all the array elements. However, the assumption is that those calls are relatively infrequent.</p>
 *
 * <p>You should <b>not</b> edit this class! It is given to you as a resource for your project.</p>
 *
 * @author  <a href="https://github.com/JasonFil">Jason Filippou</a>
 *
 * @param <T> The type of element contained by this queue.
 *
 * @see FIFOQueue
 * @see LinkedFIFOQueue
 * @see CircularArrayFIFOQueue
 */
@SuppressWarnings("unchecked")
public class LinearArrayFIFOQueue<T> implements FIFOQueue<T> {


	private T[] data;
	private int rear; // rear integer is sufficient for counting the elements.
	private final int INIT_CAPACITY = 50;
	protected boolean modificationFlag;

	/* Expand the current queue's capacity by double its current capacity. */
	private void expandCapacity(){
		int newSize = 2*data.length;
		T[] newData = (T[])(new Object[newSize]);
		for(int i = 0; i < rear; i++)
			newData[i] = data[i];
		data = newData;
	}

	/* Shift all elements one space to the left (required by dequeueing operation). */

	private void shiftLeft(){
		for(int i = 1; i < rear; i++)
			data[i - 1] = data[i];
	}

	/**
	 * Default constructor initializes data structure with its default capacity.
	 */
	public LinearArrayFIFOQueue(){
		data = (T[])(new Object[INIT_CAPACITY]);
		rear = 0;
		modificationFlag = false;
	}

	/**
	 * Second constructor initializes data structure with provided capacity.
	 * @param capacity The original capacity to provide our LinearArrayFIFOQueue with.
	 * @throws InvalidCapacityException If the capacity provided is negative.
	 */
	public LinearArrayFIFOQueue(int capacity) throws InvalidCapacityException{
		if(capacity < 0)
			throw new InvalidCapacityException("Invalid capacity provided!");
		data = (T[])(new Object[capacity]);
		rear = 0;
		modificationFlag = false;
	}

	/**
	 * Copy constructor. Initializes the queue through the queue passed as the parameter.
	 * @param oqueue The queue on which we base the current queue.
	 */
	public LinearArrayFIFOQueue(FIFOQueue<T> oqueue){
		data = (T[])(new Object[oqueue.size()]);
		for(T el: oqueue)
			enqueue(el);
		modificationFlag = false;
	}

	/**
	 * Standard equals() method. Checks whether two Queues have the exact same elements
	 * at the same order.
	 * @param other The Object to compare this queue to.
	 * @return {@code true} If the two projects.pqueue.fifoqueues are equal as per the contract established above.
	 */
	@Override
	public boolean equals(Object other){
		if(other == null)
			return false;
		if(!(other instanceof FIFOQueue<?>))
			return false;
		FIFOQueue<T> oQueue = (FIFOQueue<T>)other;
		if(size() != oQueue.size())
			return false;
		Iterator<T> ito = oQueue.iterator(), itc = iterator();
		while(ito.hasNext())
			if(!ito.next().equals(itc.next()))
				return false;
		return true;
	}

	/**
	 * Standard toString() method. Returns all the elements of the queue in a Stringified representation.
	 * @return A String-like representation of the object.
	 */
	@Override
	public String toString(){
		StringBuilder accum = new StringBuilder();
		accum.append("[");
		for(int i = 0; i < rear; i++){
			accum.append(data[i]);
			if(i + 1 < rear)// Not second-to-last element
				accum.append(", ");
		}
		accum.append( "]");
		return accum.toString();
	}

	/** Overriding of the iterator() method, required for Iterables.
	 * In this implementation of iterator(), the accessors of the method returned may throw
	 * a ConcurrentModificationException if the user tries to further traverse the queue
	 * after removing an element. This is similar to what the Java Standard Library Collections are
	 * doing and it's there because typically removal of an element leaves it in an inconsistent state
	 * w.r.t further traversing its elements.
	 * @return An {@link Iterator} which can be used to scan the elements in the queue in a linear order.
	 */

	@Override
	public Iterator<T> iterator() {
		return new LinearArrayQueueIterator();
	}

	class LinearArrayQueueIterator implements Iterator<T>{

		private int current;
		
		public LinearArrayQueueIterator(){
			current = 0;
			modificationFlag = false;
		}

		@Override
		public boolean hasNext(){
			return current < rear;
		}

		@Override
		public T next() throws ConcurrentModificationException{
			if(modificationFlag)
				throw new ConcurrentModificationException("next(): Attempted to traverse queue after element removal.");
			return data[current++];
		}

		@Override
		public void remove() throws IllegalStateException {
			if(current == 0)
				throw new IllegalStateException("Need at least one call to next() before removal.");
			for(int i = current; i < rear; i++) // Shift elements to the right
				data[i - 1] = data[i];
			data[rear - 1] = null;
			current--;
			rear--;
		}
	}


	@Override
	public void enqueue(T element) {
		// In this queue, we enqueue at the high index.
		if(size() == data.length)
			expandCapacity();
		data[rear++] = element;
		modificationFlag = true;
	}

	@Override
	public T dequeue() throws EmptyFIFOQueueException {
		if(isEmpty())
			throw new EmptyFIFOQueueException("dequeue(): FIFOQueue is empty");
		T el = data[0];
		shiftLeft();
		rear--;
		modificationFlag = true;
		return el;
	}

	@Override
	public T first() throws EmptyFIFOQueueException {
		if(isEmpty())
			throw new EmptyFIFOQueueException("getFirst(): FIFOQueue is empty");
		return data[0];
	}

	@Override
	public int size() {
		return rear;
	}

	@Override
	public boolean isEmpty() {
		return rear == 0;
	}

	@Override
	public void clear(){
		for(int i = 0; i < rear; i++)
			data[i] = null;
		rear = 0;
		System.gc();
		modificationFlag = true;
	}

}
