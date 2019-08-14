package projects.pqueue.trees.test;

import org.junit.Test;
import projects.pqueue.trees.BinarySearchTree;
import projects.pqueue.trees.EmptyTreeException;
import projects.pqueue.trees.LinkedBinarySearchTree;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * <p>A testing framework for {@link LinkedBinarySearchTree}.</p>
 *
 * <p>You should <b>not</b> edit this class! It is given to you as a resource for your project.</p>
 *
 * @author  <a href="https://github.com/JasonFil">Jason Filippou</a>
 *
 */
public class LinkedBinarySearchTreeTest {

	private BinarySearchTree<Integer> intTree= new LinkedBinarySearchTree<Integer>();

	@Test
	public void testEmptiness(){
		assertTrue(intTree.isEmpty());
		try {
			intTree.getRoot();
		} catch (EmptyTreeException e) { // This exception should be thrown
			assertTrue(true);
		}
		assertEquals(intTree.size(), 0);
		intTree.clear();
	}

	@Test
	public void testFirstTree(){
		/*                    
		 * 	Adding these numbers sequentially: {5, 9, -1, 2, 0, 4} should yield the tree
		 * 							   5
		 * 						      / \
		 *                           /   \
		 *                          -1    9
		 *                           \
		 *                            \
		 *                             2
		 *                            / \
		 *                           /   \
		 *                          0     4
		 */

		int[] nums = {5, 9, -1, 2, 0, 4};
		for(Integer n: nums)
			intTree.add(n);
		assertFalse(intTree.isEmpty());
		assertEquals(intTree.size(), nums.length);
		try {
			assertEquals(intTree.getMin(), Integer.valueOf(-1));
			assertEquals(intTree.getMax(), Integer.valueOf(9));
		} catch (EmptyTreeException e) {
			fail("getMin() and/or getMax() reported an empty tree, which shouldn't be the case.");
		}

		// Try to delete different types of nodes, see what happens.
		try {
			intTree.delete(0);
			assertEquals(intTree.getMin(), Integer.valueOf(-1));// Min and Max shouldn't change
			assertEquals(intTree.getMax(), Integer.valueOf(9));
			assertEquals(intTree.size(), nums.length - 1); // 1 less
		} catch(EmptyTreeException exc){
			fail("delete() threw an unexpected EmptyTreeException when removing a leaf nodes.");
		}

		try {
			intTree.delete(2); // After removal of 0, 2 is still a pre-leaf. Check what happens.
			assertEquals(intTree.getMin(), Integer.valueOf(-1));// Min and Max shouldn't change
			assertEquals(intTree.getMax(), Integer.valueOf(9));
			assertEquals(intTree.size(), nums.length - 2); // 1 less
		} catch(EmptyTreeException exc){
			fail("delete() threw an unexpected EmptyTreeException when removing a pre-leaf nodes.");
		}

		try {
			intTree.delete(-1); // Remove the tree's smallest element. 4 should now be the minimum element.
			assertEquals(intTree.getMin(), Integer.valueOf(4));
			assertEquals(intTree.getMax(), Integer.valueOf(9));
			assertEquals(intTree.size(), nums.length - 3); // 1 less
		} catch(EmptyTreeException exc){
			fail("delete() threw an unexpected EmptyTreeException when removing a pre-leaf nodes.");
		}

		try {
			intTree.delete(9); // Remove the tree's largest element. 5 (the root) should now be the largest element.
			assertEquals(intTree.getMin(), Integer.valueOf(4));
			assertEquals(intTree.getMax(), Integer.valueOf(5));
			assertEquals(intTree.size(), nums.length - 4); // 1 less
		} catch(EmptyTreeException exc){
			fail("delete() threw an unexpected EmptyTreeException when removing a leaf nodes.");
		}

		// Two sequential root deletions should provide us with an empty tree.
		try {
			intTree.delete(5);
			intTree.delete(4);
			assertTrue(intTree.isEmpty());
		} catch(EmptyTreeException exc){
			fail("delete() threw an unexpected EmptyTreeException when removing the root.");
		}
		intTree.clear(); // Should not affect the emptiness of the tree 
		assertTrue(intTree.isEmpty());
	}

	@Test
	public void testSecondTree(){
		/*
		 * Inserting the following integers sequentially: [-10, 9, -2, 0, -2, 3, 5, 6, -12] should yield:
		 * 
		 * 
		 * 					-10
		 *                  /  \
		 *                -12   9
		 *                     /
		 *                    -2
		 *                      \ 
		 *                       0
		 *                      / \
		 *                    -2   3
		 *                          \
		 *                           5 
		 *                            \ 
		 *                              6
		 */

		int[] nums = {-10, 9, -2, 0, -2, 3, 5, 6, -12};
		for(Integer n: nums)
			intTree.add(n);
		try {
			// Removing the root should make -2 the new root.
			intTree.delete(-10);
			assertEquals(intTree.getRoot(), Integer.valueOf(-2));
		} catch(EmptyTreeException exc){
			fail("delete() threw an unexpected EmptyTreeException when removing the root.");
		}

		// Removing the same nodes should throw an exception, because there's no duplicates
		// of the former root nodes in the tree.

		try {
			intTree.delete(-10);
		} catch(EmptyTreeException exc){
			fail("Should not have throuwn an EmptyTreeException at this point.");
		}

		// However, removing the nodes -2 twice should not throw an exception, because
		// there were two instances of this nodes in the tree, the second one being the current root.

		try {
			intTree.delete(-2);
		} catch(EmptyTreeException exc){
			fail("Threw an EmptyTreeException for removal of nodes from a non-empty tree.");
		}

		// Finally, removing the root nodes up until we get an EmptyTreeException should leave us with
		// an empty tree.

		boolean exceptionThrown = false;
		while(true){
			try {
				intTree.delete(intTree.getRoot());
			} catch(EmptyTreeException exc){
				exceptionThrown = true;
				break;
			}
		}
		if(!(exceptionThrown && intTree.isEmpty()))
			fail("When the EmptyTreeException was thrown, the tree should also be empty.");
		intTree.clear();
	}

	@Test
	public void testTraversalsSimple() throws EmptyTreeException{
		/*
		 * Let's use a small "stub" - like tree for this simple test:
		 * 
		 * 				0
		 *            /   \
		 *           -1    1
		 */
		intTree.add(0);
		intTree.add(-1);
		intTree.add(1);
		Iterator<Integer> preorder, inorder, postorder, levelorder;
	
		// Test the traversals one at a time.
		preorder = intTree.preorder();
		assertTrue(preorder.hasNext());
		assertEquals(preorder.next(), Integer.valueOf(0));
		assertEquals(preorder.next(), Integer.valueOf(-1));
		assertEquals(preorder.next(), Integer.valueOf(1));
		try {
			preorder.next(); // Should throw a NoSuchElementException
			fail("Should've thrown a NoSuchElementException.");
		} catch(NoSuchElementException exc){
			// dummy catchblock
		}

		inorder = intTree.inOrder();
		assertTrue(inorder.hasNext());
		assertEquals(inorder.next(), Integer.valueOf(-1));
		assertEquals(inorder.next(), Integer.valueOf(0));
		assertEquals(inorder.next(), Integer.valueOf(1));
		try {
			inorder.next(); 
			fail("Should've thrown a NoSuchElementException.");
		} catch(NoSuchElementException exc){}

		postorder = intTree.postOrder();
		assertTrue(postorder.hasNext());
		assertEquals(postorder.next(), Integer.valueOf(-1));
		assertEquals(postorder.next(), Integer.valueOf(1));
		assertEquals(postorder.next(), Integer.valueOf(0));
		try {
			postorder.next(); 
			fail("Should've thrown a NoSuchElementException.");
		}catch(NoSuchElementException exc) {}

		levelorder = intTree.levelOrder();
		assertTrue(levelorder.hasNext());
		assertEquals(levelorder.next(), Integer.valueOf(0));
		assertEquals(levelorder.next(), Integer.valueOf(-1));
		assertEquals(levelorder.next(), Integer.valueOf(1));
		try {
			levelorder.next(); 
			fail("Should've thrown a NoSuchElementException.");
		}catch(NoSuchElementException exc) {}

		intTree.clear();
	}

	@Test
	public void testTraversalsComplex() throws EmptyTreeException{
		/*
		 * We will use the same tree used in the "testFirstTree()" test:
		 * 	  						   5
		 * 						      / \
		 *                           /   \
		 *                          -1    9
		 *                           \
		 *                            \
		 *                             2
		 *                            / \
		 *                           /   \
		 *                          0     4
		 */
		int[] nums = {5, 9, -1, 2, 0, 4};
		for(Integer n: nums)
			intTree.add(n);
		Iterator<Integer> preorder, inorder, postorder, levelorder;
		
		// preorder
		preorder = intTree.preorder();
		assertEquals(preorder.next(), Integer.valueOf(5));
		assertEquals(preorder.next(), Integer.valueOf(-1));
		assertEquals(preorder.next(), Integer.valueOf(2));
		assertEquals(preorder.next(), Integer.valueOf(0));
		assertEquals(preorder.next(), Integer.valueOf(4));
		assertEquals(preorder.next(), Integer.valueOf(9));
		
		// inorder
		inorder = intTree.inOrder();
		assertEquals(inorder.next(), Integer.valueOf(-1));
		assertEquals(inorder.next(), Integer.valueOf(0));
		assertEquals(inorder.next(), Integer.valueOf(2));
		assertEquals(inorder.next(), Integer.valueOf(4));
		assertEquals(inorder.next(), Integer.valueOf(5));
		assertEquals(inorder.next(), Integer.valueOf(9));
		
		// postorder
		postorder = intTree.postOrder();
		assertEquals(postorder.next(), Integer.valueOf(0));
		assertEquals(postorder.next(), Integer.valueOf(4));
		assertEquals(postorder.next(), Integer.valueOf(2));
		assertEquals(postorder.next(), Integer.valueOf(-1));
		assertEquals(postorder.next(), Integer.valueOf(9));
		assertEquals(postorder.next(), Integer.valueOf(5));
		
		// levelorder
		levelorder = intTree.levelOrder();
		assertEquals(levelorder.next(), Integer.valueOf(5));
		assertEquals(levelorder.next(), Integer.valueOf(-1));
		assertEquals(levelorder.next(), Integer.valueOf(9));
		assertEquals(levelorder.next(), Integer.valueOf(2));
		assertEquals(levelorder.next(), Integer.valueOf(0));
		assertEquals(levelorder.next(), Integer.valueOf(4));
		intTree.clear();
	}
	
	@Test
	public void testHeight(){
		/*
		 * First tree:
		 * 	  						   5
		 * 						      / \
		 *                           /   \
		 *                          -1    9
		 *                           \
		 *                            \
		 *                             2
		 *                            / \
		 *                           /   \
		 *                          0     4
		 */
		int[] nums = {5, 9, -1, 2, 0, 4};
		for(Integer n: nums)
			intTree.add(n);
		assertEquals(4, intTree.height());
		intTree.clear();
		/* Second tree:
		 * 					 10
		 *                  /  \
		 *                -12   9
		 *                     /
		 *                    -2
		 *                      \ 
		 *                       0
		 *                      / \
		 *                    -2   3
		 *                          \
		 *                           5 
		 *                            \ 
		 *                              6
		 */
		int[] nums2 = {-10, 9, -2, 0, -2, 3, 5, 6, -12};
		for(Integer n: nums2)
			intTree.add(n);
		assertEquals(7, intTree.height());
		intTree.clear();
		
	}

}
