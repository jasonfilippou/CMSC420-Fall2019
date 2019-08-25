package projects.pqueue.heaps;

/**
 * <p>MinHeaps are <b>complete</b> binary search projects.pqueue.trees whose nodes contents are always smaller than
 * the contents of their children nodes.
 *
 * <p>You should <b>not</b> edit this interface! It is given to you as a resource for your project.</p>
 *
 * @author  <a href="https://github.com/JasonFil">Jason Filippou</a>
 *
 * @param <T> The {@link Comparable} type held by the {@link MinHeap}.
 *
 * @see projects.pqueue.trees.BinarySearchTree
 * @see LinkedMinHeap
 * @see ArrayMinHeap
 */
public interface MinHeap<T extends Comparable<T>> extends Iterable<T>{

	/**
	 * Add an element to {@code this}.
	 * @param element The element to insert into the current {@link MinHeap}.
	 */
	void insert(T element);

	/**
	 * Returns <b>and removes</b> the minimum element from {@code this}.
	 * @return The minimum element of the {@link MinHeap}.
	 * @throws EmptyHeapException if the {@link MinHeap} is empty.
	 */
	T deleteMin() throws EmptyHeapException;

	/**
	 * Returns, <b>but does not remove</b>, the minimum element of {@code this}.
	 * @return The minimum element of the {@link MinHeap}.
	 * @throws EmptyHeapException If the {@link MinHeap} is empty.
	 */
	 T getMin() throws EmptyHeapException;

	/**
	 * Returns the number of elements in {@code this}.
	 * @return The number of elements in the {@link MinHeap}.
	 */
	 int size();

	/**
	 * Queries {@code this} for emptiness.
	 * @return {@code true} if the {@link MinHeap} is empty, {@code false} otherwise.
	 */
	 boolean isEmpty();
}