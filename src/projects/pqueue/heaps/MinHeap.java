package projects.pqueue.heaps;

import java.util.Iterator;

/**
 * <p>MinHeaps are <b>complete</b> binary search projects.pqueue.trees whose nodes contents are always smaller than
 * the contents of their children nodes.
 *
 * <p>You should <b>not</b> edit this interface! It is given to you as a resource for your project.</p>
 *
 * @author  <a href="mailto:jasonfil@cs.umd.edu">Jason Filippou</a>
 *
 * @param <T> The {@link Comparable} type held by the MinHeap.
 *
 * @see projects.pqueue.trees.BinarySearchTree
 * @see LinkedMinHeap
 * @see ArrayMinHeap
 */
public interface MinHeap<T extends Comparable<T>> extends Iterable<T>{

	/**
	 * Add an element in the MinHeap.
	 * @param element The element to insert to the MinHeap.
	 */
	void insert(T element);

	/**
	 * Returns <b>and removes</b> the minimum element from the MinHeap.
	 * @return The minimum element of the MinHeap.
	 * @throws EmptyHeapException if the MinHeap is empty.
	 */
	T deleteMin() throws EmptyHeapException;

	/**
	 * Returns, <b>but does not remove</b>, the minimum element of the MinHeap.
	 * @return The minimum element of the MinHeap.
	 * @throws EmptyHeapException If the MinHeap is empty.
	 */
	 T getMin() throws EmptyHeapException;

	/**
	 * Returns the number of elements in the MinHeap.
	 * @return The number of elements in the MinHeap.
	 */
	 int size();

	/**
	 * Queries the MinHeap for emptiness.
	 * @return true if the MinHeap is empty, false otherwise.
	 */
	 boolean isEmpty();

	/**
	 * Clears the MinHeap of all elements.
	 */
	 void clear();

	/**
	 * MinHeaps are endowed with fail-fast {@link Iterator}s which return the elements
	 * in <b>ascending</b> order.
	 * @return A <b>fail-fast</b> {@link Iterator} which loops through the MinHeap's contents in ascending
	 * order.
	 */
	Iterator<T> iterator();
}