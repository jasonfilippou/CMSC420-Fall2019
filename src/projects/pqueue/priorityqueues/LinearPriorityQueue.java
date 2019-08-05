package projects.pqueue.priorityqueues; // ******* <---  DO NOT ERASE THIS LINE!!!! *******

/* *****************************************************************************************
 * THE FOLLOWING IMPORTS ARE HERE ONLY TO MAKE THE JAVADOC AND iterator() method signature
 * "SEE" THE RELEVANT CLASSES. SOME OF THOSE IMPORTS MIGHT *NOT* BE NEEDED BY YOUR OWN
 * IMPLEMENTATION, AND IT IS COMPLETELY FINE TO ERASE THEM. THE CHOICE IS YOURS.
 * ********************************************************************************** */
import projects.pqueue.exceptions.InvalidPriorityException;
import projects.pqueue.exceptions.InvalidCapacityException;
import projects.pqueue.fifoqueues.FIFOQueue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.*;

/**
 * <p>LinearPriorityQueue is a priority queue implemented as a linear {@link java.util.Collection}
 * of common {@link FIFOQueue}s, where the {@link FIFOQueue}s themselves hold objects
 * with the same priority (in the order they were inserted).</p>
 *
 * <p>You  <b>must</b> implement the methods in this file! To receive <b>any credit</b> for the unit tests related to this class, your implementation <b>must</b>
 * use <b>whichever</b> linear {@link java.util.Collection} you want (e.g {@link ArrayList}, {@link java.util.LinkedList},
 * {@link java.util.Queue}), or even the various {@link projects.pqueue.lists.List} and {@link FIFOQueue} implementations that we
 * provide for you. It is also possible to use <b>raw</b> arrays.</p>
 *
 * @param <T> The type held by the container.
 *
 * @author  ---- YOUR NAME HERE ----
 *
 * @see MinHeapPriorityQueue
 *
 */
public class LinearPriorityQueue<T> implements PriorityQueue<T> {

	private ArrayList<PriorityQueueNode> dataList; // See below for the implementation of the type PriorityQueueNode

	/**
	 * Default constructor initializes the element structure with
	 * a default capacity. This default capacity will be the default capacity of the
	 * underlying element structure that you will choose to use to implement this class.
	 */
	public LinearPriorityQueue(){
		dataList = new ArrayList<>();
	}

	/**
	 * Non-default constructor initializes the element structure with
	 * the provided capacity. This provided capacity will need to be passed to the default capacity
	 * of the underlying element structure that you will choose to use to implement this class.
	 * @see #LinearPriorityQueue()
	 * @param capacity The initial capacity to endow your inner implementation with.
	 * @throws InvalidCapacityException if the capacity provided is negative.
	 */
	public LinearPriorityQueue(int capacity) throws InvalidCapacityException{
		if(capacity < 0)
			throw new InvalidCapacityException("Invalid capacity provided!");
		dataList = new ArrayList<>(capacity);
	}

	@Override
	public void enqueue(T element, int priority) throws InvalidPriorityException{
		if(priority < 1)
			throw new InvalidPriorityException("priorty cannot be smaller than 1");
		for(int i = 0; i <= dataList.size(); i++)
			if(i == dataList.size() || priority < dataList.get(i).priority) { // FIFO
				dataList.add(i, new PriorityQueueNode(element, priority));
				break;
			}
	}

	@Override
	public T dequeue() throws EmptyPriorityQueueException {
		if(isEmpty())
			throw new EmptyPriorityQueueException("dequeue(): queue is empty!");
		return dataList.remove(0).element;
	}

	@Override
	public T getFirst() throws EmptyPriorityQueueException {
		if(isEmpty())
			throw new EmptyPriorityQueueException("dequeue(): queue is empty!");
		return dataList.get(0).element;
	}

	@Override
	public int size() {
		return dataList.size();
	}

	@Override
	public boolean isEmpty() {
		return dataList.size() == 0;
	}

	@Override
	public void clear() {
		dataList.clear();
	}

	@Override
	public Iterator<T> iterator() {
		return new LinearPriorityQueueIterator();
	}

	class LinearPriorityQueueIterator implements Iterator<T> {
		private ListIterator<PriorityQueueNode> dataListItr;

		LinearPriorityQueueIterator(){
			dataListItr = dataList.listIterator();
		}
		
		@Override
		public boolean hasNext() {
			return dataListItr.hasNext();
		}
		
		@Override
		public T next() throws ConcurrentModificationException, NoSuchElementException {
			try {
				return dataListItr.next().element;
			} catch (ConcurrentModificationException e) {
				throw new ConcurrentModificationException("next(): Attempted to traverse a modified priority queue.");
			} catch (NoSuchElementException e) {
				throw new NoSuchElementException("next(): queue is empty.");
			}
		}
		
		@Override
		public void remove() throws UnsupportedOperationException, IllegalStateException {
			if(dataListItr.previousIndex() != 0)
				throw new UnsupportedOperationException("Removal of arbitrary elements is not supported for priority queues.");
			try {
				dataListItr.remove();
			} catch (IllegalStateException e) {
				throw new IllegalStateException("Need at least one call to next() prior to removal.");
			}
		}
	}

	private class PriorityQueueNode {
		private int priority;
		private T element;

		/**
		 * Constructor that supplies the element and priority for the PriorityQueueNode.
		 * @param element the element element of the PriorityQueueNode
		 * @param priority the priority of the PriorityQueueNode in the priority queue
		 */
		PriorityQueueNode(T element, int priority){
			this.priority = priority;
			this.element = element;
		}
	}
}