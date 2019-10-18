package projects.pqueue.trees;

import projects.pqueue.fifoqueues.EmptyFIFOQueueException;
import projects.pqueue.fifoqueues.LinkedFIFOQueue;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * <p>{@link LinkedBinarySearchTree} is an implementation of a {@link BinarySearchTree} using links.
 * It is by far the most commonly encountered binary tree implementation. Compared to
 * an array-based implementation, there is always memory waste (even in the case of a perfect binary tree,
 * since the leaf level still has <i>2&#94;(h+1)</i> (where <i>h</i> is the tree's height) pointers
 * to {@code null}.</p>
 *
 * <p>You should <b>not</b> edit this class! It is given to you as a resource for your project.</p>
 *
 * @param <T> The {@link java.lang.Comparable} type stored by the container.
 *
 * @author  <a href="https://github.com/JasonFil">Jason Filippou</a>
 * @see BinarySearchTree
 * @see Tree
 */
public class LinkedBinarySearchTree<T extends Comparable<T>>  implements BinarySearchTree<T>{

	protected LinkedBSTNode root;
	protected int count; // To facilitate size queries.

	/**
	 * Default constructor initializes data structure.
	 */
	public LinkedBinarySearchTree(){
		root = null;
		count = 0;
	}

	/**
	 * ADT-level copy constructor initializes the tree with 
	 * all the elements of the provided tree.
	 * @param other The root of the tree to copy elements from.
	 */
	public LinkedBinarySearchTree(BinarySearchTree<T> other){
		if(other == null)
			throw new RuntimeException("Cannot copy-construct from a null reference");
		for(T el: other)
			add(el);
	}
	
	/**
	 * Standard equals() method. Returns {@code true} if the parameter object
	 * is a carbon copy of the current object.
	 * 
	 * @param other the Object to compare with this.
	 * 
	 * @return {@code true} if other and this
	 * are carbon copies of one another.
	 */
	@Override
	public boolean equals(Object other){
		if(other == null)
			return false;
		if(other.getClass() != getClass())
			return false;
		LinkedBinarySearchTree<T> ocasted = null;
		try {
			ocasted = (LinkedBinarySearchTree<T>)other;
		}catch(ClassCastException exc){
			return false;
		}
		if(ocasted.size() != size())
			return false;
		Iterator<T> currEls = iterator(), otherEls = ocasted.iterator();
		while(otherEls.hasNext())
			if(!otherEls.next().equals(currEls.next()))
				return false;
		return true;
	}
	
	
	@Override
	public T getRoot() throws EmptyTreeException{
		if(isEmpty())
			throw new EmptyTreeException("getRoot(): Tree is empty.");
		return root.getElement();
	}

	@Override
	public boolean isEmpty() {
		return count == 0;
	}


	@Override
	public int size() {
		return count;
	}

	@Override
	public T find(T element) throws EmptyTreeException {
		if(isEmpty())
			throw new EmptyTreeException("find(T element): Tree is empty.");
		T el = root.find(element);
		if(el == null)
			return null;
		return el;
	}

	@Override
	public Iterator<T> preorder() throws EmptyTreeException{
		if(root == null)
			throw new EmptyTreeException("preorder(): Tree was empty.");
		ArrayList<T> elementList = new ArrayList<T>();
		root.preorderTraversal(elementList);
		return elementList.iterator();
	}

	@Override
	public Iterator<T> inOrder() throws EmptyTreeException{
		if(root == null)
			throw new EmptyTreeException("inorder(): Tree was empty.");
		ArrayList<T> elementList = new ArrayList<T>();
		root.inorderTraversal(elementList);
		return elementList.iterator();
	}

	@Override
	public Iterator<T> postOrder() throws EmptyTreeException{
		if(root == null)
			throw new EmptyTreeException("postorder(): Tree was empty.");
		ArrayList<T> elementList = new ArrayList<T>();
		root.postorderTraversal(elementList);
		return elementList.iterator();
	}

	@Override
	public Iterator<T> levelOrder() throws EmptyTreeException{
		if(root == null)
			throw new EmptyTreeException("levelorder(): Tree was empty.");
		ArrayList<T> elementList = new ArrayList<T>();
		try {

			LinkedFIFOQueue<LinkedBSTNode> nodeQueue = new LinkedFIFOQueue<LinkedBSTNode>();
			nodeQueue.enqueue(root);
			while(!nodeQueue.isEmpty()){
				LinkedBSTNode current = nodeQueue.dequeue();
				elementList.add(current.getElement());
				if(current.left != null)
					nodeQueue.enqueue(current.left);
				if(current.right != null)
					nodeQueue.enqueue(current.right);
			}
		} catch(EmptyFIFOQueueException exc){
			// Dummy catchblock because dequeue() is complaining.
		}
		return elementList.iterator();
	} 

	@Override
	public T getMin() throws EmptyTreeException{
		if(root == null)
			throw new EmptyTreeException("getMin(): tree is empty.");
		LinkedBSTNode current = root;
		while(current.getLeft() != null)
			current = current.getLeft();
		return current.getElement();
	}


	@Override
	public T getMax() throws EmptyTreeException {
		if(root == null)
			throw new EmptyTreeException("getMax(): tree is empty.");
		LinkedBSTNode current = root;
		while(current.getRight() != null)
			current = current.getRight();
		return current.getElement();
	}

	@Override
	public void clear() {
		root = null;
		count = 0;
		System.gc();
	}

	@Override
	public Iterator<T> iterator() {
		Iterator<T> retVal = null;
		try {
			retVal = inOrder();
		} catch(EmptyTreeException exc){
			return null;
		}
		return retVal;
	}

	@Override
	public void add(T element) {
		if(root == null)
			root = new LinkedBSTNode(element);
		else
			root.add(element);
		count++;
	}

	/* Removal of a nodes consists of two things: First, finding it and returning it
	 * to the caller, and, second, the actual removal of it. The second part will be 
	 * attained by Node.delete.
	 */
	@Override
	public T delete(T element) throws EmptyTreeException{
		if(isEmpty())
			throw new EmptyTreeException("delete(): Tree is empty.");
		T retVal = root.find(element);
		if(retVal == null)
			return null;
		root = root.remove(retVal);
		count--;
		return retVal; 
	}
	
	public int height(){
		return root.height();
	}


	protected class LinkedBSTNode{
		protected T data;
		protected LinkedBSTNode left, right;

		public LinkedBSTNode(T element){
			data = element;
			left = right = null;
		}

		public LinkedBSTNode getLeft(){
			return left;
		}

		public LinkedBSTNode getRight(){
			return right;
		}

		public void setLeft(LinkedBSTNode left){
			this.left = left;
		}

		public void setRight(LinkedBSTNode right){
			this.right = right;
		}

		public T getElement(){
			return data;
		}

		public int getCount(){
			int count = 1;
			if(left != null)
				count += left.getCount();
			if(right != null)
				count += right.getCount();
			return count;
		}

		public T find(T element){
			// Preorder traversal makes most sense when wanting to find an element.
			T retVal = null;
			if(data.compareTo(element) == 0)
				retVal = data;
			else // Check recursively over elements. Could be {@code null}, eventually.
				if(element.compareTo(data) < 0 && left != null)
					retVal=left.find(element);
				else if(element.compareTo(data) > 0 && right != null)
					retVal=right.find(element);
			return retVal;
		}

		/* Binary search projects.pqueue.trees allow us to insert nodes at specific places. The
		 * new nodes inserted will always be a leaf nodes.*/
		public void add(T element){

			// Case 1: If the element to insert is smaller than the currently encountered
			// nodes, insert it at the left subtree of the nodes.
			if(element.compareTo(data) < 0){
				if(left == null)
					left = new LinkedBSTNode(element);
				else
					left.add(element);

				// Case 2: If the element to insert is larger than or equal to the currently
				// encountered nodes, insert it at the right subtree of the nodes.
			} else if(element.compareTo(data) >= 0){
				if(right == null)
					right = new LinkedBSTNode(element);
				else
					right.add(element);
			}
		}

		/* The following method removes a specific nodes from the tree
		 * and returns the nodes that replaces it. It is recursive in nature.
		 */
		public LinkedBSTNode remove(T element){
			LinkedBSTNode retVal = this;
			if(element.compareTo(data) == 0){// Current nodes should be removed

				// Case #1: LinkedBSTNode is a leaf: return {@code null}.
				if(left == null && right == null)
					retVal = null;

				// Case #2: LinkedBSTNode is a pre-leaf with left child {@code null}: return the right child.
				else if(left == null && right != null)
					retVal = right;

				// Case #3: LinkedBSTNode is a pre-leaf with right child {@code null}: return the left child.
				else if(left != null && right == null)
					retVal = left;

				// Case #4: LinkedBSTNode is an inner nodes with rooted subtrees. Find its inorder successor
				//		and replace the nodes with it. Set the left child to whatever was left before
				//		and the right child to whatever was right before.
				else {
					retVal = getInorderSuccessor(); // Guaranteed to exist, because "this" is an inner nodes.
					retVal.left = left;
					retVal.right = right;
				}
			} else if(element.compareTo(data) < 0){// Element on the left sub-tree
				if(left != null) // if left is {@code null}, then the element is simply not in the tree.
					left = left.remove(element);
			} else{ // Element on the right sub-tree
				if(right != null) // same
					right = right.remove(element);
			}
			return retVal;
		}

		// The inorder successor of an inner nodes is the leftmost nodes of its right subtree.
		private LinkedBSTNode getInorderSuccessor(){
			LinkedBSTNode current = right;
			while(current.left != null)
				current = current.left;
			right.remove(current.getElement()); // The successor is a leaf nodes, so it boils down to a previous case.
			return current;
		}

		/* Preorder, inorder and postorder traversal methods. */

		public void preorderTraversal(ArrayList<T> elements){
			elements.add(data);
			if(left != null)
				left.preorderTraversal(elements);
			if(right != null)
				right.preorderTraversal(elements);
		}

		public void inorderTraversal(ArrayList<T> elements){
			if(left != null)
				left.inorderTraversal(elements);
			elements.add(data);
			if(right != null)
				right.inorderTraversal(elements);
		}

		public void postorderTraversal(ArrayList<T> elements){
			if(left != null)
				left.postorderTraversal(elements);
			if(right != null)
				right.postorderTraversal(elements);
			elements.add(data);
		}
		
		public int height(){
			int leftHeight = 1, rightHeight = 1;
			if(left != null)
				leftHeight = left.height() + 1;
			if(right != null)
				rightHeight = right.height() + 1;
			// return the maximum of heights
			return (leftHeight > rightHeight) ? leftHeight : rightHeight;
		}
	}
}
