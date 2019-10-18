package projects.pqueue.trees;

/**
 * <p>A {@link BinarySearchTree} is a {@link Tree} with a maximum of 2 children per node
 * which allows for adding and removing nodes in a sorted manner. As such, it will extend
 * the {@link Tree} interface but it will also restrict the objects it stores to Comparable ones.
 * There are only going to be two new methods in this interface: insert(T) and delete(T).</p>
 * 
 * <p>Following classic semantics, for every given nodes, elements in the left subtree of the nodes
 * are smaller (as defined by their compareTo() definitions) than the element at the nodes, which is
 * subsequently smaller than or equal to the nodes in the right subtree.</p>
 * 
 * <p>Note that this interface does not allow for methods that balance it. For BinarySearchTrees
 * that allow for auto-balancing, AVL projects.pqueue.trees or Red-Black projects.pqueue.trees should be used.</p>
 *
 * <p>You should <b>not</b> edit this class! It is given to you as a resource for your project.</p>
 *
 * @author  <a href="https://github.com/JasonFil">Jason Filippou</a>
 *
 * @param <T> The Comparable type held by this.
 *
 */
public interface BinarySearchTree<T extends Comparable<T>> extends Tree<T>{
	
	/**
	 * Adds an element to the tree. BinarySearchTrees impose criteria for insertion
	 * (see description of the class), whereas general Trees do not.
	 * @param element The element to insert.
	 */
	void add(T element);
	
	/**
	 * Removes the specified element from the tree and returns it.
	 * @param element The element to be removed.
	 * @throws EmptyTreeException If the tree is empty.
	 * @return the removed element or {@code null} if the element is not there.
	 */
	T delete(T element) throws EmptyTreeException;
	
	/**
	 * Returns the height of the tree.
	 * @return an int representing the height of the tree.
	 */
	int height();
	
	/**
	 * Returns the minimum element of the tree.
	 * @return The minimum element of the tree.
	 * @throws EmptyTreeException If the tree is empty.
	 */
	T getMin() throws EmptyTreeException;
	
	/**
	 * Returns the maximum element of the tree.
	 * @return The maximum element of the tree.
	 * @throws EmptyTreeException If the tree is empty.
	 */
	T getMax() throws EmptyTreeException;
}
