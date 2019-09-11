package projects.avlg;

import org.junit.Test;
import projects.avlg.exceptions.EmptyTreeException;
import projects.avlg.exceptions.InvalidBalanceException;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * <p>{@link StudentTests} is an example class that contains some basic unit tests. You may write your own tests here.
 * It is <b>very important</b> that you write your own tests! </p>
 *
 * @author --- YOUR NAME HERE! -----
 * @see AVLGTree
 * @see EmptyTreeException
 * @see InvalidBalanceException
 */
public class StudentTests {

    private static final int NUMS = 1000;
    private static final long SEED = 47; // Static seed for experiment reproducibility. You can set it to whatever you want.
    private Random r  = new Random(SEED);
    private AVLGTree<Integer> tree;

    /* This test creates a simple AVL-1 tree and inserts three integers in descending order (like our first class example).
     * In this scenario, your code should perform a right rotation about the root. So the new root should be the former left
     * child, while the tree's total height should be 1.
     */
    @Test
    public void testAVL1RightRotation() throws InvalidBalanceException, EmptyTreeException {
        tree = new AVLGTree<>(1);
        tree.insert(20);
        tree.insert(10);
        tree.insert(5); // Should trigger a right rotation about the root.
        // The new root should be 10, and the total height should be 1.
        assertEquals("In an AVL-1 tree with three descending order integers inserted, " +
                "the new root was not the expected one.", Integer.valueOf(10), tree.getRoot());
        assertEquals("In an AVL-1 tree with three descending order integers inserted, " +
                "the new height was not the expected one.", 1, tree.getHeight());
    }

    /* The following test creates an AVL-1 tree, but this time the insertion sequence is like our second in-class example,
     * which creates a "zig-zag" pattern. To restore balance, a left-right rotation about the root is required!
     */
    @Test
    public void testAVL1LeftRotation() throws InvalidBalanceException, EmptyTreeException {
        tree = new AVLGTree<>(1);
        tree.insert(20);
        tree.insert(10);
        tree.insert(15); // Should trigger a left-right rotation about the root.
        // The new root should be 15, and the total height should be 1.
        assertEquals("In an AVL-1 tree where we inserted 20, 10 and 15 in sequence, " +
                "the new root was not the expected one.", Integer.valueOf(15), tree.getRoot());
        assertEquals("In an AVL-1 tree  where we inserted 20, 10 and 15 in sequence, " +
                "the new height was not the expected one.", 1, tree.getHeight());
    }

    /* Now we take our first test, but this time we employ an AVL-2 tree! Nothing should change in the tree
     * after the three insertions!
     */
    @Test
    public void testAVL2Unchanged() throws InvalidBalanceException, EmptyTreeException {
        tree = new AVLGTree<>(2);
        tree.insert(20);
        tree.insert(10);
        tree.insert(5); // Should *NOT* trigger a right rotation about the root or any rotation for that matter!
        assertEquals("In an AVL-2 tree with three descending order integers inserted, " +
                "the new root was not the expected one.", Integer.valueOf(20), tree.getRoot()); // Root should be unchanged
        assertEquals("In an AVL-2 tree with three descending order integers inserted, " +
                "the new height was not the expected one.", 2, tree.getHeight()); // Height of 2 expected
    }

    /* Let's also build a test with an AVL-2 tree where we *expect* the root and height to change. */
    @Test
    public void testAVLChanged() throws InvalidBalanceException, EmptyTreeException {
        tree = new AVLGTree<>(2);
        tree.insert(20);
        tree.insert(10);
        tree.insert(5);
        tree.insert(1); // Should trigger a right rotation about the root.
        assertEquals("In an AVL-2 tree with four descending order integers inserted, " +
                "the new root was not the expected one.", Integer.valueOf(10), tree.getRoot()); // Root should change to 10
        assertEquals("In an AVL-2 tree with four descending order integers inserted, " +
                "the new height was not the expected one.", 2, tree.getHeight()); // Height of 2 expected
    }
    
    
    /* In class, we mentioned an interesting special case of deletion. Namely, when we delete from a subtree and it 
     * turns out that we have to balance, to determine which kind of rotation we have to do, we have to check the balance
     * of the *opposite* subtree. If this balance is 0, then either a double rotation or a single rotation will do the 
     * trick for us. Exactly because *both* are possible, there is no reason to go for the double rotation and your 
     * code *should* perform a single one! The following test checks for exactly this by making sure that the correct
     * node is elevated as the root of the AVL-1 tree.
     */
    @Test
    public void testCorrectRotationAfterDelete() throws InvalidBalanceException, EmptyTreeException{
        tree = new AVLGTree<>(1);
        tree.insert(5);
        tree.insert(2);
        tree.insert(7);
        tree.insert(6);
        tree.insert(8);

        /* The tree should then look like this:
        *
        *                           5
        *                          / \
        *                         /   \
        *                        2     7
        *                             / \
        *                            /   \
        *                           6     8
        *
        *  We will delete (2) and check to see if the code will perform a left rotation about the root (as it should),
        *  which would put 7 as the new root, or a right-left rotation about the root (which it should *not* do), which
        *  would elevate 6 as the new root.
        *
        *  Deletion is the most complex operation that you will have to implement, which in turn means that your implementation
        *  might initially encounter all kinds of problems and throw all kinds of exceptions. In Java, any Object that can be thrown
        *  implements the interface Throwable. So if you catch Throwables, you catch anything. Have a look at how we
        *  encapsulate the deletion call within a try-catch block and use the information from the thrown Exception to determine
        *  what exactly happened and elevate that information to the user. fail() is a method in org.junit.Assert which we statically
        *  import at the top of this file. This method essentially fails the test with the provided message. This is how we give you your
        *  messages on the submit server whenever you fail a test!
        */
        try {
            tree.delete(2);
        }  catch(Throwable t){
            fail("While deleting 2 in an AVL-1 tree, we encountered a " + t.getClass().getSimpleName() + " with message: " +
                    t.getMessage() + ".");
        }

        assertEquals("In an AVL-1 tree where we deleted the root's singleton left child, we encountered an unexpected new root.",
                Integer.valueOf(7), tree.getRoot());
        assertEquals("In an AVL-1 tree where we deleted the root's singleton left child,  the tree's new height was not the expected one.",
                2, tree.getHeight());

    }

    /* This stress test generates NUMS-many random integers, inserts them into an AVL-3 tree, and queries it for
     * being a standard BST. Employing randomization in your tests is an excellent idea for stress-testing your
     * implementations. You can easily generalize this test (and the next) to higher-order AVL Trees with another for loop
     * that iterates over several imbalance parameters.
     */
    @Test
    public void testEnsureBST() throws InvalidBalanceException {
        tree = new AVLGTree<>(3);
        for(int i = 0; i < NUMS; i++)
            tree.insert(r.nextInt());
        assertTrue("After inserting " + NUMS + " - many random elements, it was determined that our AVL-3 tree" +
                " did not satisfy the BST property!", tree.isBST());
    }


    /* The following test is identical to the previous one, with the only caveat that it checks whether the
     * tree satisfies the AVL-3 condition, instead of the BST condition.
     */

    @Test
    public void testEnsureAVLG() throws InvalidBalanceException {
        tree = new AVLGTree<>(3);
        for(int i = 0; i < NUMS; i++)
            tree.insert(r.nextInt());
        assertTrue("After inserting " + NUMS + " - many random elements, it was determined that our AVL-3 tree" +
                " did not satisfy the AVL-3 property!", tree.isAVLGBalanced());
    }
}
