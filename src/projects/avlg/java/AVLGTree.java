package projects.avlg.java;

import projects.avlg.java.exceptions.EmptyTreeException;
import projects.avlg.java.exceptions.InvalidBalanceException;

/** <p>An AVL-G Tree is an AVL Tree with a relaxed balance condition. Its constructor receives a strictly
 * positive parameter which controls the <b>maximum</b> imbalance allowed on any subtree of the tree which
 * it creates. So, for example:</p>
 *  <ul>
 *      <li>An AVL-1 tree is a classic AVL tree, which only allows for perfectly balanced binary
 *      subtrees (imbalance of 0 everywhere), or subtrees with a maximum imbalance of 1 (somewhere). </li>
 *      <li>An AVL-2 tree relaxes the criteria of AVL-1 trees, by also allowing for subtrees
 *      that have an imbalance of 2.</li>
 *      <li>AVL-3 trees allow an imbalance of 3</li>
 *      <li>...</li>
 *  </ul>
 *
 *  <p>The idea behind AVL-G trees is that rotations cost time, so maybe we would be willing to
 *  accept bad search performance now and then if it would mean less rotations.</p>
 *
 * @author <a href="https://github.com/JasonFil">Jason Filippou</a>
 */
public class AVLGTree<T extends Comparable<T>> {

    /* *********************************************************************
     ************************* PRIVATE FIELDS  *****************************
     **********************************************************************/

    private Node root;
    private int maxImbalance;
    private int count;

    /* Definition of  private node class */
    private class Node {

        /* Data members */
        private int height;
        private T key;
        private Node left, right;


        /* Constructor */
        Node(T key) { // This is really only useful for the root.
            this.key = key;
            left = right = null;
        }
    } // End of inner node class definition

    /* *********************************************************************
     ************************* PRIVATE METHODS *****************************
     **********************************************************************/

    /* Height of a node. */
    private int getHeight(Node n) {
        return (n == null) ? -1 : n.height;
    }

    /* Retrieve the inorder successor of a given node. Initial invariant:
     the right subtree of the node is not null. */
    private Node getInorderSuccessor(Node n) {
        Node current = n.right;
        assert current != null : "Inorder successor searches " +
                "can only happen for nodes with non-null right children.";
        while (current.left != null)
            current = current.left; // Go as far left as you can
        return current;
    }

    /* Return the balance of the current node, defined as the difference
     * between the height of the right subtree and the left subtree.
     *
     * POSITIVE BALANCE: Left-heavy subtree.
     * ZERO BALANCE: Perfectly balanced subtree
     * NEGATIVE BALANCE: Right-heavy subtree.
     */
    private int balance(Node n) {
        return ( n == null) ? 0 : getHeight(n.left) - getHeight(n.right);
    }

    // TODO: Fix the height updates in those methods.
    private Node rotateRight(Node n, boolean isInsertion) {
        Node x = n.left;
        n.left = x.right;
        x.right = n;
       x.height = n.height;
        if (isInsertion)
            n.height--;
        else
            n.height -= 2;
        return x;
    }

    private Node rotateLeft(Node n, boolean isInsertion) {
        Node x = n.right;
        n.right = x.left;
        x.left = n;
        x.height = n.height;
        if (isInsertion)
            n.height--;
        else
            n.height -= 2;
        return x;
    }

    /* Recursive insertion method. */
    private Node insert(Node n, T key) {
        if (n == null)
            return new Node(key);
        if (key.compareTo(n.key) < 0) {
            n.left = insert(n.left, key);

            // Did our insertion cause an imbalance?
            if (Math.abs(balance(n)) > maxImbalance) {
                // What was the source of the imbalance? To find it,
                // we need to understand exactly in which subtree
                // we inserted the key; left-left or left-right?
                if (key.compareTo(n.left.key) <= 0) // Right Rotation
                    n = rotateRight(n, true);
                else {
                    n.left = rotateLeft(n.left, true); // LR Rotation
                    n = rotateRight(n, true);
                }
            }
        } else { // Symmetric cases
            n.right = insert(n.right, key);

            // Any imbalances caused?
            if (Math.abs(balance(n)) > maxImbalance) {
                if (key.compareTo(n.right.key) >= 0) // Left Rotation
                    n = rotateLeft(n, true);
                else {
                    n.right = rotateRight(n.right, true); // RL Rotation
                    n = rotateLeft(n, true);
                }
            }
        }
        // Update the current node's height. This is relevant whether we have rotated or not.
        int rightHeight = getHeight(n.right);
        int leftHeight = getHeight(n.left);
        int maxHeight = (rightHeight > leftHeight) ? rightHeight : leftHeight;
        n.height = maxHeight + 1;
        return n;
    }

    /* Recursive deletion method. Splits cases across leaf deletions
     * as well as inner node deletions. Most complex method of data structure.
     */
    private Node delete(Node n, T key) {

        // Case 0: Node is null. Could not find the node to delete.
        if(n == null)
            return null;

        // Case 1: Found the key
        if (n.key.compareTo(key) == 0) {

            // Case  1.a: Null right subtree; simply return left subtree (might be null)
            if (n.right == null) {
                return n.left;
            // Case 1.b: Non-null right subtree. Gotta find inorder successor, copy
            // and recursively delete him.
            } else {

                Node inSucc = getInorderSuccessor(n);
                assert inSucc != null : "The inorder successor should never be null here!";
                n.key = inSucc.key;
                n.right = delete(n.right, n.key); // recursively

                // Maybe the deletion of the node on the right caused a positive imbalance.
                // If that's the case, we will need to take corrective action, via rotations!
                if (Math.abs(balance(n)) > maxImbalance) {

                    /* Since it is a deletion from the *right* subtree
                     * that caused the imbalance, it is n's *left* subtree
                     * that is causing us grief. To figure out whether we need to do a
                     * right rotation about n or an LR rotation about n, we need to query
                     * its left subtree about whether it is left heavy, balanced or right heavy.
                     */
                    int leftBalance = balance(n.left);

                    if (leftBalance >= 0) { // Left-leaning or balanced left subtree. Right rotation about n.
                        n = rotateRight(n, false);
                    } else { // Right-leaning left subtree. LR rotation about n.
                        n.left = rotateLeft(n.left, false);
                        n = rotateRight(n, false);
                    }
                }

            }
        }
        // Case #2: key might be on the left
        else if (key.compareTo(n.key) < 0) {
            n.left = delete(n.left, key);

            // Do we need to re-balance? If so, check the right subtree's balance
            // to figure out appropriate rotation!

            if (Math.abs(balance(n)) > maxImbalance) {

                int rightBalance = balance(n.right);
                if (rightBalance <= 0) { // Right-leaning or balanced right subtree. Left rotation about n.
                    n = rotateLeft(n, false);
                } else { // Left-leaning right subtree. RL rotation about n.
                    n.right = rotateRight(n.right, false);
                    n = rotateLeft(n, false);
                }
            }
        // Case #3: key might be on the right
        } else {
            // The rest of this code is the same as the inorder successor deletion case.
            n.right = delete(n.right, key);
            if (Math.abs(balance(n)) > maxImbalance) {

                int leftBalance = balance(n.left);
                if (leftBalance >= 1) { // Left-leaning left subtree. Right rotation about n.
                    n = rotateRight(n, false);
                } else { // Right-leaning left subtree. LR rotation about n.
                    n.left = rotateLeft(n.left, false);
                    n = rotateRight(n, false);
                }
            }
        }
        // Before we return the node, we need to adjust its height appropriately,
        // taking into consideration the heights of its children subtrees.
        int rightHeight =  getHeight(n.right);
        int leftHeight = getHeight(n.left);
        int maxHeight = (rightHeight >= leftHeight) ? rightHeight : leftHeight;
        n.height = maxHeight + 1;
        return n;
    }


    /* Check if the subtree obeys the BST property. */
    private boolean isBST(Node n) {
        if (n == null || (n.left == null && n.right == null)) { // Empty trees and leaves are trivially BSTs )
            return true;
        } else if (n.left != null && n.right == null) { // Non-null left child, null right child
            if (n.left.key.compareTo(n.key) >= 0)
                return false;
            else
                return isBST(n.left);
        } else if (n.left == null && n.right != null) { // Null left child, non-null right child.
            if (n.right.key.compareTo(n.key) < 0)
                return false;
            else
                return isBST(n.right);
        } else { // Non-null left and right child
            return isBST(n.left) && isBST(n.right);
        }
    }

    /* Check if the subtree is balanced, based on the maxImbalance allowed. */
    private boolean isAVLGBalanced(Node n) {
        if (n == null || (n.left == null && n.right == null)) // empty subtrees & leaves are trivially balanced
            return true;
        return (Math.abs(balance(n)) <= maxImbalance)
                && isAVLGBalanced(n.left) && isAVLGBalanced(n.right);
    }


    /* *********************************************************************
     ************************* PUBLIC (INTERFACE) METHODS *******************
     **********************************************************************/

    /**
     * The class constructor provides the tree with the maximum imbalance allowed.
     * @param maxImbalance The maximum imbalance allowed by the AVL-G Tree.
     * @throws InvalidBalanceException if maxImbalance is a value smaller than 1.
     */
    public AVLGTree(int maxImbalance) throws InvalidBalanceException {
        if (maxImbalance < 1)
            throw new InvalidBalanceException("AVLGTree constructor: Imbalance value has to be " +
                    "at least 1 (provided: " + maxImbalance + ").");
        this.maxImbalance = maxImbalance;
        count = 0;
    }

    /**
     * Insert key in the tree.
     * @param key The key to insert in the tree.
     */
    public void insert(T key) {
        if (isEmpty())
            root = new Node(key);
        else
            root = insert(root, key);
        count++;
    }

    /**
     * Delete the key from the data structure and return it to the caller.
     * @param key The key to delete from the structure.
     * @return The key that was removed, or null if the key was not found.
     * @throws EmptyTreeException if the tree is empty.
     */
    public T delete(T key) throws EmptyTreeException {
        /* While it is surely not the most efficient thing to do, we will first
         * search for the key in the tree and only delete it if it's found in the tree.
         * This makes successful deletions slower by a logarithmic parameter, yet
         * it also makes for cleaner deletion code. It also speeds up unsuccessful
         * deletions, i.e deletions of keys that are not in the tree (but any reasonable
         * application would search keys first anyway).
         */
        if (isEmpty())
            throw new EmptyTreeException("AVLGTree.delete(): Cannot delete from an empty tree.");
        T retVal = search(key);
        if (retVal != null) {
            root = delete(root, key);
            count--;
        }
        return retVal; // null or otherwise.
    }

    /**
     * <p>Search for key in the tree. Return a reference to it if it's in there,
     * or null otherwise.</p>
     * @param key The key to search for.
     * @return key if key is in the tree, or null otherwise.
     * @throws EmptyTreeException if the tree is empty.
     */
    public T search(T key) throws EmptyTreeException {
        if (isEmpty())
            throw new EmptyTreeException("AVLGTree::search(T key): Tree is empty!");
        else { // Do it iteratively.
            Node current = root;
            while(current != null) {
                if(current.key.compareTo(key) == 0)
                    return current.key;
                else if(current.key.compareTo(key) > 0)
                    current = current.left;
                else
                    current = current.right;
            }
            return null;
        }
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
     * @return The height of the tree. If the tree is empty, returns -1.
     */
    public int getHeight() {
        return getHeight(root);
    }

    /**
     * Query the tree for emptiness. A tree is empty iff it has zero keys stored.
     * @return true if the tree is empty, false otherwise.
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Return the key at the tree's root node.
     * @return The key at the tree's root node.
     * @throws  EmptyTreeException if the tree is empty.
     */
    public T getRoot() throws EmptyTreeException{
        if(root == null || isEmpty())
            throw new EmptyTreeException("AVL-"+maxImbalance + " tree is empty!");
        return root.key;
    }


    /**
     * <p>Establishes whether the AVL-G tree <em>globally</em> satisfies the BST condition. This method is
     * <b>terrifically useful for testing!</b></p>
     * @return true if the tree satisfies the Binary Search Tree property,
     * false otherwise.
     */
    public boolean isBST() {
        return isBST(root);
    }


    /**
     * <p>Establishes whether the AVL-G tree <em>globally</em> satisfies the AB condition. This method is
     * <b>terrifically useful for testing!</b></p>
     * @return true if the tree satisfies the balance requirements of an AVLG tree, false
     * otherwise.
     */
    public boolean isAVLGBalanced() {
        return isAVLGBalanced(root);
    }

    /**
     * <p>Empties the AVL-G Tree of all its elements. After a call to this method, the
     * tree should have <b>0</b> elements.</p>
     */
    public void clear(){
        root = null;
        // System.gc();
        count = 0;
    }


    /**
     * <p>Return the number of elements in the tree.</p>
     * @return  The number of elements in the tree.
     */
    public int getCount(){
        return count;
    }
}
