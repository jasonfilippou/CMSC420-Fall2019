/* DO NOT ERASE THESE THREE LINES OR YOUR CODE WON'T COMPILE! */
package projects.avlg;
import projects.avlg.exceptions.EmptyTreeException;
import projects.avlg.exceptions.InvalidBalanceException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/** <p>An <tt>AVL-G Tree</tt> is an AVL Tree with a relaxed balance condition. Its constructor receives a strictly
 * positive parameter which controls the <b>maximum</b> imbalance allowed on any subtree of the tree which
 * it creates. So, for example:</p>
 *  <ul>
 *      <li>An AVL-1 tree is a classic AVL tree, which only allows for perfectly balanced binary
 *      subtrees (imbalance of 0 everywhere), or subtrees with a maximum imbalance of 1 (somewhere). </li>
 *      <li>An AVL-2 tree relaxes the criteria of AVL-1 trees, by also allowing for subtrees
 *      that have an imbalance of 2.</li>
 *      <li>AVL-3 trees allow an imbalance of 3.</li>
 *      <li>...</li>
 *  </ul>
 *
 *  <p>The idea behind AVL-G trees is that rotations cost time, so maybe we would be willing to
 *  accept bad search performance now and then if it would mean less rotations.</p>
 *
 * <p>You <b>must</b> implement this class! You should <b>not</b> move this class' file to
 * a different position in the code tree. You <b>are</b> allowed to add any classes, enums, interfaces,
 * abstract classes, packages, etc. that you think you might need.</p>
 *
 * @author YOUR NAME HERE!
 */
public class AVLGTree<T extends Comparable<T>> {
    public static void main(String[] args) throws Exception{
        AVLGTree<Integer> tree = new AVLGTree<>(1);
        List ints = Arrays.asList(IntStream.range(1, 6).boxed().toArray());
        Collections.shuffle(ints);
        ints.forEach(intNum -> tree.insert((Integer) intNum));
        System.out.println(tree.levelOrderTraversal());

    }

    /***************************************************************************
     ************** PLACE YOUR PRIVATE METHODS AND FIELDS HERE: ****************
     ***************************************************************************/

    private int maxImbalance, count;
    private Node root;

    private class Node {
        T key;
        int height;
        Node lChild, rChild;

        Node(T key){
            this.key = key;
            this.height = 0;
        }

        Node(Node other){
            this.key = other.key;
            this.height = other.height;
        }

        public String toString(){
            return key.toString() + " | " + height;
        }
    }

    /* *********************************************************************
     ************************* PUBLIC (INTERFACE) METHODS *******************
     **********************************************************************/

    /**
     * The class constructor provides the tree with its maximum imbalance allowed.
     * @param maxImbalance The maximum imbalance allowed by the AVL-G Tree.
     * @throws InvalidBalanceException if <tt>maxImbalance</tt> is a value smaller than 1.
     */
    public AVLGTree(int maxImbalance) throws InvalidBalanceException {
        if(maxImbalance < 1)
            throw new InvalidBalanceException("maxImbalance cannot be smaller than 1");
        this.maxImbalance = maxImbalance;
        this.count = 0;
    }

    /**
     * Insert <tt>key</tt> in the tree.
     * @param key The key to insert in the tree.
     */
    public void insert(T key) {
        root = insert(root, key);
    }

    private Node insert(Node curr, T key){
        if(curr == null) {
            count++;
            return new Node(key);
        }

        if(key.compareTo(curr.key) == 0)
            return curr;

        if(key.compareTo(curr.key) < 0) {
            curr.lChild = insert(curr.lChild, key);
            if(getHeight(curr.lChild) - getHeight(curr.rChild) > maxImbalance) {
                if(key.compareTo(curr.lChild.key) > 0)
                    curr.lChild = rotateLeft(curr.lChild);
                curr = rotateRight(curr);
            }
        } else {
            curr.rChild = insert(curr.rChild, key);
            if(getHeight(curr.rChild) - getHeight(curr.lChild) > maxImbalance) {
                if (key.compareTo(curr.rChild.key) < 0)
                    curr.rChild = rotateRight(curr.rChild);
                curr = rotateLeft(curr);
            }
        }

        if(curr.lChild != null)
            curr.lChild.height = Math.max(getHeight(curr.lChild.lChild), getHeight(curr.lChild.rChild)) + 1;
        if(curr.rChild != null)
            curr.rChild.height = Math.max(getHeight(curr.rChild.lChild), getHeight(curr.rChild.rChild)) + 1;
        curr.height = Math.max(getHeight(curr.lChild), getHeight(curr.rChild)) + 1;

        return curr;
    }

    private Node rotateRight(Node curr){
        Node temp = curr.lChild;
        curr.lChild = temp.rChild;
        temp.rChild = curr;
        return temp;
    }

    private Node rotateLeft(Node curr){
        Node temp = curr.rChild;
        curr.rChild = temp.lChild;
        temp.lChild = curr;
        return temp;
    }

    /**
     * Delete the key from the data structure and return it to the caller.
     * @param key The key to delete from the structure.
     * @return The key that was removed, or <tt>null</tt> if the key was not found.
     * @throws EmptyTreeException if the tree is empty.
     */
    public T delete(T key) throws EmptyTreeException {
        if (isEmpty())
            throw new EmptyTreeException("AVLGTree.delete(): Cannot delete from an empty tree.");
        T retVal = search(key);
        if (retVal != null) {
            root = delete(root, key);
            count--;
        }
        return retVal; // null or otherwise.
    }

    public Node delete(Node curr, T key) {
        if(curr == null)
            return null;

        if(key.compareTo(curr.key) == 0){
            if(curr.lChild == null)
                return curr.rChild;
            if(curr.rChild == null)
                return curr.lChild;
            curr.key = getInSuccessor(curr).key;
            curr.rChild = delete(curr.rChild, curr.key);
        } else if (key.compareTo(curr.key) < 0) {
            curr.lChild = delete(curr.lChild, key);
        } else {
            curr.rChild = delete(curr.rChild, key);
        }

        int heightDiff = getHeight(curr.lChild) - getHeight(curr.rChild);
        if (heightDiff > maxImbalance) {
            if(getHeight(curr.lChild.rChild) > getHeight(curr.lChild.lChild))
                curr.lChild = rotateLeft(curr.lChild);
            curr = rotateRight(curr);
            curr.lChild.height = Math.max(getHeight(curr.lChild.lChild), getHeight(curr.lChild.rChild)) + 1;
            curr.rChild.height = Math.max(getHeight(curr.rChild.lChild), getHeight(curr.rChild.rChild)) + 1;
        } else if (heightDiff < -maxImbalance) {
            if (getHeight(curr.rChild.lChild) > getHeight(curr.rChild.rChild))
                curr.rChild = rotateRight(curr.rChild);
            curr = rotateLeft(curr);
            curr.lChild.height = Math.max(getHeight(curr.lChild.lChild), getHeight(curr.lChild.rChild)) + 1;
            curr.rChild.height = Math.max(getHeight(curr.rChild.lChild), getHeight(curr.rChild.rChild)) + 1;
        }
        curr.height = Math.max(getHeight(curr.lChild), getHeight(curr.rChild)) + 1;
        return curr;
    }

    private Node getInSuccessor(Node n) {
        Node current = n.rChild;
        assert current != null : "Inorder successor searches can only happen for nodes with non-null right children.";
        while (current.lChild != null)
            current = current.lChild; // Go as far left as you can
        return current;
    }
    /**
     * <p>Search for <tt>key</tt> in the tree. Return a reference to it if it's in there,
     * or <tt>null</tt> otherwise.</p>
     * @param key The key to search for.
     * @return <tt>key</tt> if <tt>key</tt> is in the tree, or <tt>null</tt> otherwise.
     * @throws EmptyTreeException if the tree is empty.
     */
    public T search(T key) throws EmptyTreeException {
        if (isEmpty()) throw new EmptyTreeException("Tree is empty!");
         // Do it iteratively.
        Node current = root;
        while (current != null) {
            if (current.key.compareTo(key) == 0)
                return current.key;
            if (current.key.compareTo(key) > 0)
                current = current.lChild;
            else
                current = current.rChild;
        }
        return null;
    }

    /**
     * Retrieves the maximum imbalance parameter.
     * @return The maximum imbalance parameter provided as a constructor parameter.
     */
    public int getMaxImbalance(){
        return maxImbalance;
    }

    /**
     * <p>Return the height of the tree. The height of the tree is defined as the length of the
     * longest path between the root and the leaf level. By definition of path length, a
     * stub tree has a height of 0, and we define an empty tree to have a height of -1.</p>
     * @return The height of the tree.
     */
    public int getHeight() {
        return getHeight(root);
    }

    private int getHeight(Node n) {
        return (n == null) ? -1 : (n.height);
    }

    /**
     * Query the tree for emptiness. A tree is empty iff it has zero keys stored.
     * @return <tt>true</tt> if the tree is empty, <tt>false</tt> otherwise.
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * <p>Return the key at the tree's root node if it exists, or throws an
     * <tt>EmptyTreeException</tt> otherwise.</p>
     * @throws EmptyTreeException if the tree is empty.
     * @return The key at the tree's root node.
     */
    public T getRoot() throws EmptyTreeException{
        if(root == null)
            throw new EmptyTreeException("The tree is empty");
        return root.key;
    }

    /**
     * <p>Establishes whether the AVL-G tree <em>globally</em> satisfies the BST condition.
     * This method is <b>terrifically useful for testing!</b></p>
     * @return <tt>true</tt> if the tree satisfies the Binary Search Tree property,
     * <tt>false</tt> otherwise.
     */
    public boolean isBST() {
        return isBST(root);
    }

    private boolean isBST(Node curr){
        if (curr == null || (curr.lChild == null && curr.rChild == null)) // Empty trees and leaves are trivially BSTs )
            return true;
        if (curr.lChild != null && curr.rChild == null) // Non-null left child, null right child
            return curr.lChild.key.compareTo(curr.key) < 0 && isBST(curr.lChild);
        if (curr.lChild == null) // Null left child, non-null right child.
            return curr.rChild.key.compareTo(curr.key) >= 0 && isBST(curr.rChild);
         // Non-null left and right child
        return curr.lChild.key.compareTo(curr.key) < 0 && curr.rChild.key.compareTo(curr.key) >= 0
                && isBST(curr.lChild) && isBST(curr.rChild);
    }

    /**
     * <p>Establishes whether the AVL-G tree <em>globally</em> satisfies the AVL-G condition.
     * This method is <b>terrifically useful for testing!</b></p>
     * @return <tt>true</tt> if the tree satisfies the Binary Search Tree property,
     * <tt>false</tt> otherwise.
     */
    public boolean isAVLGBalanced() {
        return isAVLGBalanced(root);
    }

    private boolean isAVLGBalanced(Node curr) {
        if (curr == null || (curr.lChild == null && curr.rChild == null)) // empty subtrees & leaves are trivially balanced )
            return true;
        return isBalanced(curr) && isAVLGBalanced(curr.lChild) && isAVLGBalanced(curr.rChild);
    }

    private boolean isBalanced(Node curr) {
        return curr == null || Math.abs(getHeight(curr.lChild) - getHeight(curr.rChild)) <= maxImbalance;
    }

    /**
     * <p>Empties the <tt>AVLGTree</tt> of all its elements. After a call to this method, the
     * tree should have <b>0</b> elements.</p>
     */
    public void clear(){
        count = 0;
        root = null;
    }

    /**
     * <p>Return the number of elements in the tree.</p>
     * @return  The number of elements in the tree.
     */
    public int getCount(){
        return count;
    }

    public List<Node> inOrderTraversal(){
        List<Node> list = new ArrayList<>();
        inOrderTraversal(root, list);
        return list;
    }

    private void inOrderTraversal(Node curr, List<Node> list){
        if(curr == null) return;
        inOrderTraversal(curr.lChild, list);
        list.add(curr);
        inOrderTraversal(curr.rChild, list);
    }

    public List<Node> levelOrderTraversal(){
        Queue<Node> queue = new LinkedList<>();
        List<Node> list = new ArrayList<>();
        queue.add(root);
        while(!queue.isEmpty()){
            Node curr = queue.remove();
            list.add(curr);
            if(curr.lChild != null)
                queue.add(curr.lChild);
            if(curr.rChild != null)
                queue.add(curr.rChild);
        }
        return list;
    }
}
