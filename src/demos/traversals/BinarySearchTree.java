package demos.traversals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/** <p> BinarySearchTree is a simple container class of <a href ="https://docs.oracle.com/javase/8/docs/api/java/lang/Comparable.html">Comparable</a>s. It is likely going to be used
 * in the second lecture, to illustrate some points about inorder traversal. </p>
 * @author Jason
 */
public class BinarySearchTree<T extends Comparable<T>> {
  class Node{
    T key;
    Node left, right;

    Node(T key){
      left = right = null;
      this.key = key;
    }

    // This will return the node's inorder successor in the tree.
    Node inSucc(){
      assert right != null; // Otherwise the caller is making the wrong application
      Node curr = right;
      while(curr.left != null)
        curr = curr.left;
      return curr;
    }

    // This will deleteRec the inorder successor of a given node from the tree.
    // It is assumed that the inorder successor exists (i.e the argument to this method is non-null

    void delInSucc(){
      assert right != null;  // Otherwise the caller is making the wrong application
      Node prev = this, curr = right;
      while(curr.left != null){
        prev = curr;
        curr = curr.left;
      }
      prev.left = null; // Throw away the leftmost child in the subtree rooted at this.right;
    }
  };


  private Node root = null;
  private int count = 0;


  /**
   * A recursive implementation of inorder traversal.
   * @param visited A List that will store the nodes as they are being visited.
   * @see #inorderTraversalWithStack(List)
   */
  public void inorderTraversalRec(List<T> visited){
    inorderTraversalRec(root, visited);
  }

  private void inorderTraversalRec(Node n, List<T> visited){
    if(n==null)
      return;
    inorderTraversalRec(n.left, visited);
    visited.add(n.key);
    inorderTraversalRec(n.right, visited);
  }

  /**
   * A non-recursive implementation of inorder traversal, which uses a user-provided stack.
   * @param visited A List that will store the nodes as they are being visited.
   * @see #inorderTraversalRec(List)
   */
  public void inorderTraversalWithStack(List<T> visited){
    Stack<Node> s = new Stack<Node>();
    Node curr = root;
    while(!s.isEmpty() || curr != null){ // TODO: Remember to ask the students why the second check is important.
      // If you're null, you have to visit your parent. They might be one level above you, they
      // might be many levels above you!
      if(curr == null) {
        curr = s.pop(); // Important: this is why the stack needs to be a stack of nodes, not just Ts!
        visited.add(curr.key);
        curr = curr.right;
        // Otherwise, go as left as you can. This is inorder traversal, after all!
      } else {
        s.push(curr);
        curr = curr.left;
      }
    }
  }

  /**
   * Non-recursive insertion routine! Insertion doesn't even need a stack!
   * @param key The {@link java.lang.Comparable} key to add to the tree.
   */
  public  void insert(T key){
    if(root == null) {
      root = new Node(key);
      count++;
      return;
    }
    boolean left = false, right = false;
    Node curr = root, prev = null;
    while(curr != null){
      prev = curr;
      if(key.compareTo(curr.key) < 0) {
        curr = curr.left;
        left = true;
        right = false;
      } else {
        curr = curr.right;
        left = false;
        right = true;
      }
    }
    if(left)
      prev.left = new Node(key);
    else
      prev.right = new Node(key);
    count++;
  }

  /**
   * Returns the number of keys stored in the tree.
   * @return The number of nodes in the tree.
   */
  public int getCount(){
    return count;
  }

  /**
   * Search for key in the binary search tree. Non-recursive.
   * @param key The key to search for in the tree.
   * @return key, if it is found, null otherwise.
   */
  public T search(T key){
    Node curr = root;
    while(curr != null){
      if(curr.key.compareTo(key) == 0)
        return key;
      else if(curr.key.compareTo(key) > 0)
        curr = curr.left;
      else
        curr = curr.right;
    }
    return null;
  }

  /**
   * Performs a range search on the binary tree. The search is <b>inclusive</b> on both ends and is implemented
   * recursively. If min and  max are the same, {@link #search(Comparable)} is called.
   * @param min the left end of the range search.
   * @param max the right end of the range search.
   * @return An {@link Iterator} that exposes the elements that satisfy the search in <b>ascending order</b>.
   * @throws IllegalArgumentException if min &gt; max.
   */
  public Iterator<T> rangeSearch(T min, T max) throws IllegalArgumentException{
    ArrayList<T> list = new ArrayList<>();
    if(min.compareTo(max) > 0)
      throw new IllegalArgumentException("rangeSearch(): Undefined range. Given min = " + min + " max = " + max + ".");
    else if(min.compareTo(max) == 0){
      T key = search(min);
      if(key != null)
        list.add(key);
    } else {
      rangeSearch(root, min, max, list);
    }
    return list.iterator();
  }

  private void rangeSearch(Node n, T min, T max, List<T> list){
    assert min.compareTo(max) <= 0 : "Min ought to be smaller than or equal to max.";
    if(n == null)
      return;
    if(n.key.compareTo(min) > 0 && n.key.compareTo(max) < 0){
      rangeSearch(n.left, min, n.key, list);
      list.add(n.key);
      rangeSearch(n.right, n.key, max, list);
    } else if(n.key.compareTo(min) == 0) {
      list.add(n.key);
      rangeSearch(n.right, n.key, max, list);
    } else if(n.key.compareTo(min) < 0) {
      rangeSearch(n.right, n.key, max, list);
    } else if(n.key.compareTo(max) == 0 ) {
      rangeSearch(n.left, min, n.key, list);
      list.add(n.key); // ! If you want the range sorted, the addition should be after the recursive call.
    } else if(n.key.compareTo(max) > 0 ) {
      rangeSearch(n.left, min, n.key, list);
    }
  }

  /**
   * Deletes key from the tree if it's there, otherwise does nothing.
   * @param key The {@link java.lang.Comparable} key to deleteRec from the tree.
   */
  public void delete(T key){
    // root = deleteRec(root, key); // call to private recursive method, implemented below.
    deleteIter(key); // call to private iterative method, implemented below.
  }

  /* Private deletion routines. One recursive, one iterative. */

  private Node deleteRec(Node curr, T key){
    if(curr == null)
      return null;
    if(curr.key.compareTo(key) > 0)
      curr.left = deleteRec(curr.left, key);
    else if(curr.key.compareTo(key) < 0)
      curr.right =  deleteRec(curr.right, key);
    else { // All actual deletion cases will be implemented here.
      if((curr.right == null) && (curr.left == null)) {// pure leaf;
        curr = null;
        count--;
      }
      else if (curr.right == null){ // Has a left subtree - return that
        curr = curr.left;
        count--;
      }
      else { // Has a right subtree. Swap with inorder successor.
        curr.key = curr.inSucc().key;
        curr.right = deleteRec(curr.right, curr.key);
      }
    }
    return curr;
  }

  private void deleteIter(T key){
    Node curr = root, prev = null;
    boolean found = false;
    while(curr != null){
      if(curr.key.compareTo(key) > 0){ // Gotta go left
        prev = curr;
        curr = curr.left;
      }
      else if(curr.key.compareTo(key) < 0) { // Gotta go right
        prev = curr;
        curr = curr.right;
      }
      else { // Found it!
        found = true;
        if(count == 1) { // deleting root
          root = null;
          count--;
          return; // I got your structured programming guidelines right here
        }
        if(curr.left == null && curr.right == null) { // leaf, throw it away
          if(prev.left == curr) prev.left = null;
          else prev.right = null;
          curr = null;
        } else if(curr.right == null) { // Null right subtree, alive and kicking left one.
          if (prev.right == curr) prev.right = curr.left;
          else prev.left = curr.left;
        } else { // Have to swap with inorder successor.
          curr.key = curr.inSucc().key;
          curr.delInSucc();
        }
        break;
      }
    }
    if(found)
      count--;
  }



  /**
   * Queries the BST for emptiness.
   * @return true if, and only if, there are zero keys in the tree.
   */
  public boolean isEmpty(){
    return (count == 0);
  }
}