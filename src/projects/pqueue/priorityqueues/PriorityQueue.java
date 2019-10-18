package projects.pqueue.priorityqueues;

import projects.pqueue.exceptions.InvalidPriorityException;

/**<p> A PriorityQueue is an extension of a classic FIFO queue. Instead of traditional
 * FIFO processing, a PriorityQueue inserts elements with a higher priority
 * getFirst, where "higher" is typically interpreted as "lower" in the arithmetic
 * sense. For example, a priority of 1 (one) is considered "higher" than priority 2 (two),
 * and the element with that particular priority would be processed faster by the application
 * using the PriorityQueue. Elements with the <b>same</b> priority are inserted in a FIFO fashion.</p>
 *
 * <p>You should <b>not</b> edit this interface! It is given to you as a resource for your project.</p>
 *
 * @author  <a href="https://github.com/JasonFil">Jason Filippou</a>
 *
 * @see LinearPriorityQueue
 * @see projects.pqueue.heaps.MinHeap
 * @see MinHeapPriorityQueue
 *
 */
public interface PriorityQueue<T> extends Iterable<T> {

	/**
	 * Enqueues the provided element taking into account its prority.
	 * @param element The element to enqueue.
	 * @param priority The priority of the element that will be enqueued.
	 * @throws InvalidPriorityException if the priority provided is less than 1.
	 */
	public void enqueue(T element, int priority) throws InvalidPriorityException;

	/**
	 * Returns <b>and deletes</b> the top element of the PriorityQueue.
	 * @return The element at the top of the PriorityQueue.
	 * @throws EmptyPriorityQueueException if the PriorityQueue is empty.
	 */
	public T dequeue() throws EmptyPriorityQueueException;

	/**
	 * Returns, <b>but does not delete</b> the top element of the PriorityQueue.
	 * @return The element at the top of the PriorityQueue.
	 * @throws EmptyPriorityQueueException If the PriorityQueue is empty.
	 */
	public T getFirst() throws EmptyPriorityQueueException;

	/**
	 * Returns the number of elements currently in the PriorityQueue.
	 * @return The number of elements currently in the PriorityQueue.
	 */
	public int size();

	/**
	 * Queries the PriorityQueue for emptiness.
	 * @return {@code true} if the PriorityQueue is empty, {@code false} otherwise.
	 */
	public boolean isEmpty();
}