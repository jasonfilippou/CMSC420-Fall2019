package projects.pqueue.heaps; // ******* <---  DO NOT ERASE THIS LINE!!!! *******

/* *****************************************************************************************
 * THE FOLLOWING IMPORT IS NECESSARY FOR THE ITERATOR() METHOD'S SIGNATURE. FOR THIS
 * REASON, YOU SHOULD NOT ERASE IT! YOUR CODE WILL BE UNCOMPILABLE IF YOU DO!
 * ********************************************************************************** */
import java.util.*;

/**
 * <p>A {@link LinkedMinHeap} is a tree (specifically, a <b>complete</b> binary tree) where every node is
 * smaller than or equal to its descendants (as defined by the {@link Comparable#compareTo(Object)} overridings of the type T).
 * Percolation is employed when the root is deleted, and insertions guarantee maintenance of the heap property in logarithmic time. </p>
 *
 * <p>You <b>must</b> edit this class! To receive <b>any</b> credit for the unit tests related to this class,
 * your implementation <b>must</b> be a &quot;linked&quot;, <b>non-contiguous storage</b> implementation based on a
 * binary tree of nodes and references. Use the skeleton code we have provided to your advantage, but always remember
 * that the only functionality our tests can test is {@code public} functionality.</p>
 * 
 * @author --- YOUR NAME HERE! ---
 *
 * @param <T> The {@link Comparable} type of object held by {@code this}.
 *
 * @see MinHeap
 * @see ArrayMinHeap
 */
public class LinkedMinHeap<T extends Comparable<T>> implements MinHeap<T> {
	private MinHeapNode root;
	private int size;
	private boolean modified;

	/**
	 * Default constructor sets pointers to null and size to 0.
	 */
	public LinkedMinHeap() {
		size = 0;
	}

	/**
	 * Second constructor creates a root node with the data provided
	 * as the content.
	 *
	 * @param rootElement the data to create the root with.
	 */
	public LinkedMinHeap(T rootElement) {
		root = new MinHeapNode(rootElement);
		size = 1;
	}

	/**
	 * Copy constructor initializes the current MinHeap as a carbon
	 * copy of the parameter.
	 *
	 * @param other The MinHeap to copy the elements from.
	 */
	public LinkedMinHeap(MinHeap<T> other) {
		if (other == null)
			size = 0;
		else
			for (T el : other)
				insert(el); // MinHeaps have been made Iterable.
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public int size() {
		return size;
	}

	/* To insert an element in the heap, we insert it as the last leaf, and then we move the element upward until
	 * the heap identity is maintained.
	 */
	@Override
	public void insert(T element) {
		Stack<MinHeapNode> path = getPathToPosition(++size); // Stores the path to the parent node of the new node about to be inserted.
		MinHeapNode curr = new MinHeapNode(element);
		try {
			MinHeapNode parent = path.pop();
			if (parent.lChild == null) parent.lChild = curr;
			else parent.rChild = curr;

			while (parent != null && parent.data.compareTo(curr.data) > 0) { // Percolate upward
				T temp = curr.data;
				curr.data = parent.data;
				parent.data = temp;
				curr = parent;
				parent = path.isEmpty() ? null : path.pop();			// If the stack is empty, we percolated all the way up.
			}
		} catch (EmptyStackException e) {
			root = curr;
		}
		modified = true;
	}

	private Stack<MinHeapNode> getPathToPosition(int position) {
		int level = (int) Math.ceil(Math.log(position + 1) / Math.log(2)) - 1;
		int posInLevel = position - (int) Math.pow(2, level) + 1;
		int lenOfLevel = (int) Math.pow(2, level);

		Stack<MinHeapNode> path = new Stack<>();
		MinHeapNode curr = root;
		while (path.size() < level) {
			path.push(curr);
			curr = posInLevel <= lenOfLevel / 2 ? curr.lChild : curr.rChild;
			lenOfLevel = lenOfLevel / 2;
			posInLevel -= (posInLevel <= lenOfLevel ? 0 : lenOfLevel);
		}
		return path;
	}

	@Override
	public T getMin() throws EmptyHeapException {
		if (root == null) throw new EmptyHeapException("getMin(): tree is empty.");
		return root.data;
	}

	@Override
	public T deleteMin() throws EmptyHeapException {
		if (size == 0) throw new EmptyHeapException("removeMax(): Tree is empty.");

		T minElement = root.data;
		// In a minheap, the root contains the minimal datum.To delete it,
		// we make the last leaf node the new root, and then "push" this 
		// node downward until the minheap identity is maintained;
		try {
			Stack<MinHeapNode> path = getPathToPosition(size--);
			MinHeapNode parent = path.peek(); // Will throw EmptyStackException if stack is empty, handled below.
			if (parent.rChild != null) {
				root.data = parent.rChild.data;
				parent.rChild = null;
			} else {
				root.data = parent.lChild.data;
				parent.lChild = null;
			}
			// Ee-order the heap by "pushing down" (data exchange) the new root as appropriate.
			MinHeapNode curr = root, child = root.getChildToSwap();
			while (child != null) {
				T temp = curr.data;
				curr.data = child.data;
				child.data = temp;
				curr = child;
				child = child.getChildToSwap();
			}
		} catch (EmptyStackException e) {
			root = null;
		}
		modified = true;
		return minElement;
	}

	@Override
	public Iterator<T> iterator() {
		return new LinkedMinHeapIterator();
	}

	/**
	 * Standard equals() method.
	 *
	 * @return true If the parameter Object and the current MinHeap
	 * are identical Objects.
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof MinHeap))
			return false;
		Iterator itThis = iterator();
		Iterator itOther = ((MinHeap) other).iterator();
		while (itThis.hasNext())
			if (!itThis.next().equals(itOther.next()))
				return false;
		return true;
	}

	private List<T> levelOrder() {
		Queue<MinHeapNode> nodeQueue = new LinkedList<>();
		List<T> elementList = new ArrayList<>();
		if(root != null)
			nodeQueue.add(root);
		MinHeapNode curr;
		while (!nodeQueue.isEmpty()) {
			curr = nodeQueue.remove();
			elementList.add(curr.data);
			if(curr.lChild!= null)
				nodeQueue.add(curr.lChild);
			if(curr.rChild!= null)
				nodeQueue.add(curr.rChild);
		}
		return elementList;
	}

	/**
	 * {@link MinHeapNode} is a class representing a minheap's node. It contains recursive methods
	 * typical of heap operations.
	 * @author Jason Filippou (https://github.com/JasonFil)
	 */
	private class MinHeapNode{
		private T data;
		private MinHeapNode lChild, rChild;   // HeapNodes contain references to "offspring" nodes.

		MinHeapNode(T data){
			lChild = rChild = null;
			this.data = data;
		}

		private MinHeapNode getChildToSwap(){
			if(lChild == null) { // Because the heap is a complete tree, this should subsume rChild == null.
				assert rChild == null : "Heap is a complete tree, so when left child is null, so should the right child.";
				return null;
			}
			if(rChild == null) // Only rChild is null
				return data.compareTo(lChild.data) <= 0 ? null : lChild;

			// Need to check both children
			if(data.compareTo(lChild.data) <= 0 && data.compareTo(rChild.data) <= 0)
				return null;
			return lChild.data.compareTo(rChild.data) < 0 ? lChild : rChild;
		}
	}// inner class HeapNode

	/**
	 * {@link LinkedMinHeapIterator} is an inner class representing a fail-fast {@link Iterator} over the heap's elements.
	 * The elements are returned in "proper" order.
	 */
	class LinkedMinHeapIterator implements Iterator<T>{
		private LinkedMinHeap<T> tempHeap = new LinkedMinHeap<>();
		private T curr;

		/**
		 * Constructor builds a temporary heap internally because every time {@link #next()} is called, we internally
		 * call {@link #deleteMin()} on the temporary heap.
		 */
		LinkedMinHeapIterator(){
			modified = false;
			if(size == 0) return;
			List<T> elementList = levelOrder();
			List<MinHeapNode> newHeap = new ArrayList<>();
			newHeap.add(new MinHeapNode(elementList.get(0)));
			for(int i = 1; i < size; i++){
				newHeap.add(new MinHeapNode(elementList.get(i)));
				if(i % 2 == 1)
					newHeap.get((i-1)/2).lChild = newHeap.get(i);
				else
					newHeap.get((i-1)/2).rChild = newHeap.get(i);
			}
			tempHeap.root = newHeap.get(0);
			tempHeap.size = size;
		}
		
		@Override
		public boolean hasNext() {
			return !tempHeap.isEmpty(); // also covers the case of the root being null.
		}

		@Override
		public T next() throws ConcurrentModificationException, NoSuchElementException {
			if(modified)
				throw new ConcurrentModificationException("next(): attempted to traverse the heap through an Iterator after extraneous modifications.");
			try {
				curr = tempHeap.deleteMin();
				return curr;
			} catch (EmptyHeapException e) {
				throw new NoSuchElementException("next(): Heap is empty!");
			}
		}

		/**
		 * Removing an arbitrary datum from the heap is an unsupported
		 * operation. We only allow the user to explicitly call {@link #deleteMin()}
		 * in order to delete the maximum datum (the root).
		 * @throws UnsupportedOperationException
		 * @throws IllegalStateException if {@link #next()} has not been called prior to calling this method.
		 */
		@Override
		public void remove() throws UnsupportedOperationException, IllegalStateException {
			if(curr == null)
				throw new IllegalStateException("Need at least one call to next() prior to removal.");
			if(curr != root.data)
				throw new UnsupportedOperationException("Arbitrary data removal is not allowed for MinHeaps.");
			try {
				deleteMin();
				curr = null;
				modified = false;
			} catch (EmptyHeapException ignored) {
				throw new RuntimeException("Tried to call remove() on an empty heap.");
			}
		}
	}
} // outer class LinkedMinHeap
