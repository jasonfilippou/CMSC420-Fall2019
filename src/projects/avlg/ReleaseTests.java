
package projects.avlg;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import projects.avlg.exceptions.EmptyTreeException;
import projects.avlg.exceptions.InvalidBalanceException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

/** <p>A testing suite for the second project of CMSC420, Data Structures,
 * in CS UMD, Fall 2019. The theme of the project is AVL-G Trees.
 * The structure is tested against {@link Integer}s, with easily tune-able values
 * for <i>G</i> as well as tune-able stress-testing of a pre-defined number of pairwise
 * distinct Integers.</p>
 *
 * <p>The {@code testBalancedInsertionsAVLx()} and {@code testBalancedInsertionsAVLx()}
 * tests <b>all</b> test against expected behavior of the following eight operations: </p>
 *
 * <ol>
 *     <li>Single left rotation about the root.</li>
 *     <li>Single right rotation about the root.</li>
 *     <li>RL rotation about the root.</li>
 *     <li>LR rotation about the root.</li>
 *     <li>LR rotation about the root's left child.</li>
 *     <li>RL rotation about the root's left child.</li>
 *     <li>LR rotation about the root's right child.</li>
 *     <li>RL rotation about the root's right child.</li>
 * </ol>
 *
 * @author <a href="https://github.com/jasonfil">Jason Filippou</a>
 * @see AVLGTree
 * @see EmptyTreeException
 * @see InvalidBalanceException
 * @see StudentTests
 */
public class ReleaseTests {

    // TODO: Use the jUnit4 solution of Exception.ExpectedException to simplify the
    // TODO: pieces of the code that *expect* certain specific Exceptions to be thrown.

    /* ******************************/
    /* ****** PRIVATE FIELDS ********/
    /* ******************************/

    private ArrayList<AVLGTree<Integer>> trees = new ArrayList<>(MAX_IMBALANCE);
    private static final Random RNG = new Random(47);
    private static final int MAX_IMBALANCE=10;
    private static final Integer ZERO = 0;
    private static final int NUMS = 5000;


    /* *******************************/
    /* ****** PRIVATE METHODS ********/
    /* *******************************/


    /* Ensure that the given tree is empty in terms of both isEmpty()
     * and count(). Return false otherwise.  */
    private boolean ensureTreeEmpty(AVLGTree<?> tree){
        assert tree!= null : "ensureTreeEmpty() expects non-null trees";
        return tree.isEmpty() || tree.getCount() == 0;
    }

    /* Ensure that the given tree has the provided arguments as height,
     * count and root respectively. Return false in any other case.  */
    private boolean ensureHeightCountAndRoot(AVLGTree<Integer> tree,
                                             int expectedHeight, int expectedCount,
                                             Object expectedRoot){
        assert tree!= null : "ensureHeightAndCount() expects non-null trees";
        try {
            assertEquals("Root mismatch!", expectedRoot , tree.getRoot());
        } catch(EmptyTreeException exc ){
            return (expectedCount == 0); // True only if we were dealing with an empty tree.
        } catch(AssertionError ignored){
            return false;
        }
        return ( expectedHeight == tree.getHeight() ) &&
                (expectedCount == tree.getCount());

    }

    /* Ensure that the given tree obeys *both* the BST *and* AVL-G
     * global invariants. Null trees obey those trivially. */
    private boolean ensureAVLGBST(AVLGTree<?> tree){
        return (tree == null) || (tree.isBST() && tree.isAVLGBalanced());
    }

    /* Insert all the Integers provided in the stream in the given tree.
     * Afterwards, check height, count and root against expected values. */
    private void insertAndTest(IntStream stream,
                               AVLGTree<Integer> tree, int expectedHeight,
                               int expectedCount, Object expectedRoot) throws AssertionError{

        assert stream != null && tree != null: "insertAndTest() expects a valid IntStream and tree.";
        assert expectedCount >= 0 && expectedHeight >= -1 : "insertAndTest() expects " +
                "non-negative count parameter and a height parameter of at least -1";

        tree.clear();
        List<Integer> keys = stream.boxed().collect(Collectors.toList()); // Only for printing purposes
        keys.forEach(tree::insert);

        if(!ensureHeightCountAndRoot(tree, expectedHeight, expectedCount, expectedRoot))
            throw new AssertionError("After inserting the key sequence " + keys +
                    " in an initially empty AVL-" + tree.getMaxImbalance() + " tree, the height, count and/or new " +
                    "root were different from what was expected.");
        if(!ensureAVLGBST(tree))
            throw new AssertionError("After inserting the key sequence " + keys +
                    " in an initially empty AVL-" + tree.getMaxImbalance() + " the tree was " +
                    "found to not obey either the BST and/or the AVL-" + tree.getMaxImbalance() +
                    "invariants.");
        tree.clear();
    }

    /* Delete the key provided from the tree provided and test against
     expected values of height, count and root after the operation. */
    private void deleteAndTest(Integer toDel, AVLGTree<Integer> tree,
                               int expectedHeight, int expectedCount, Object expectedRoot) throws AssertionError{

        assert (toDel != null) && (tree != null) : "deleteAndTest():" +
                " Need non-null tree *and* object to delete";
        try {
            tree.delete(toDel);
        } catch(EmptyTreeException exc){
            throw new RuntimeException("AVLGTree:delete() threw an EmptyTreeException " +
                    "with message: " + exc.getMessage() + ".");
        }

        if(!ensureHeightCountAndRoot(tree, expectedHeight, expectedCount, expectedRoot))
            throw new AssertionError("After deleting the key " + toDel +
                    " in an AVL-" + tree.getMaxImbalance() + " tree, the height, count" +
                    " and/or new root were different from what was expected.");
        if(!ensureAVLGBST(tree))
            throw new AssertionError("After deleting the key " + toDel +
                    " in an AVL-" + tree.getMaxImbalance() + " the tree was " + "found to " +
                    "not obey either the BST and/or the AVL-" + tree.getMaxImbalance() +
                    "invariants.");
        tree.clear();
    }


    /* *****************************/
    /* ****** UNIT TESTS ***********/
    /* *****************************/

    /**
     * Set-up the trees that we will use for our tests.
     */
    @Before
    public void setUp() {
        assert MAX_IMBALANCE >=1 : "MAX_IMBALANCE had best be at least one!";
        IntStream.rangeClosed(1, MAX_IMBALANCE).forEach(imb ->
        {
            try {
                trees.add(new AVLGTree<>(imb));
            } catch (InvalidBalanceException i) {
                throw new RuntimeException(i.getMessage());
            }
        });
    }

    /**
     * Clear the contents of the trees used in our tests.
     */
    @After
    public void tearDown(){
        trees.forEach(AVLGTree::clear);
        trees.clear();
    }

    /**
     * Test whether a {@link InvalidBalanceException} is thrown when initializing
     * AVL-G trees with various invalid choices for the imbalance parameter <i>G</i>.
     */
    @Test
    public void testInvalidImbalances(){
        IntStream.range(0, NUMS).forEach(imb->
        {
            InvalidBalanceException expected = null;
            try {
                new AVLGTree<Integer>(-imb); // Zero or negative imbalance
            } catch(InvalidBalanceException thrown){
                expected = thrown;
            } catch(Throwable thr) {
                fail("While initializing an AVL-" + imb + " tree we caught a "
                        + thr.getClass().getSimpleName() + " with message: " +
                        thr.getMessage() + " instead of an InvalidBalanceException.");
            }
            assertNotNull("Initializing an AVL-G tree with an imbalance parameter of " +
                    + -imb + " should have thrown an InvalidBalanceException.", expected);
        });
    }

    /**
     * Make sure that an empty tree is reported as such by the code, in terms of both
     * {@link AVLGTree#isEmpty()} and {@link AVLGTree#getCount()}.
     */
    @Test
    public void testEmptyTree(){
        trees.forEach(t->
        {
            assertTrue("Upon creation, an AVL-"+t.getMaxImbalance() +
                    " tree should be empty.", t.isEmpty());
            assertEquals("Upon creation, an AVL-"+t.getMaxImbalance() +
                    " tree should have a height of -1.", -1, t.getHeight());
            assertEquals("Upon creation, an AVL-"+t.getMaxImbalance() +
                    " tree should have a count of 0.", 0, t.getCount());
            EmptyTreeException expected = null;
            try {
                t.getRoot();
            } catch(EmptyTreeException thrown){
                expected = thrown;
            } catch(Throwable thr){
                fail("While calling getRoot() in an empty AVL-" + t.getMaxImbalance() +
                        " tree, instead of an EmptyTreeException, we caught a "
                        + thr.getClass().getSimpleName() + " with message: " +
                        thr.getMessage() + " instead of an EmptyTreeException.");
            }
            assertNotNull("Upon creation, retrieving the root of " +
                    "an AVL-" + t.getMaxImbalance() + " tree should throw an " +
                    "EmptyTreeException.", expected);
        });
    }

    /**
     * A simple test which inserts two keys and then deletes them. Tests for height, count,
     * emptiness, and even polls the root after all 4 different operations.
     */
    @Test
    public void testTwoInsertionsAndDeletions(){
        trees.forEach(t->
        {
            // Make an insertion and test everything.

            Integer firstKey = RNG.nextInt();
            t.insert(firstKey);
            assertFalse("After inserting a key, the AVL-" +
                            t.getMaxImbalance()+ " tree should no longer be empty.",
                    t.isEmpty());
            assertEquals("After inserting a key, the AVL-" +
                            t.getMaxImbalance()+ " tree's new height should be 0.",
                    0, t.getHeight());
            assertEquals("After inserting a key, the AVL-" +
                            t.getMaxImbalance()+ " tree's new count should be 1.",
                    1, t.getCount());
            try {
                assertEquals("After inserting a key in an AVL-" +
                                t.getMaxImbalance()+ " tree, a search for it should be successful.",
                        firstKey, t.search(firstKey));
            }catch(EmptyTreeException ignored){
                fail("When searching for the only key in an AVL-" + t.getMaxImbalance() + "" +
                        " tree, an EmptyTreeException was thrown.");
            }catch(Throwable thr){
                fail("While searching for the only key of a non- empty AVL-" +
                        t.getMaxImbalance() + " tree,  we caught a " + thr.getClass().getSimpleName()
                        + " with message: " +  thr.getMessage() + ".");
            }
            try {
                assertEquals("After inserting a key in a previously empty AVL-" +
                                t.getMaxImbalance()+ " tree, it should be the tree's root.",
                        firstKey, t.getRoot());
            } catch(EmptyTreeException ignored){
                fail("getRoot() should *not* have thrown an EmptyTreeException at this point.");
            } catch(Throwable thr){
                fail("While retrieving the root of an AVL-" +  t.getMaxImbalance() +
                        " tree with a single key,  we caught a " + thr.getClass().
                        getSimpleName() + " with message: " +  thr.getMessage() + ".");
            }

            // Insert a second node and test everything

            Integer secondKey = RNG.nextInt();
            while(secondKey.equals(firstKey))
                secondKey = RNG.nextInt();
            t.insert(secondKey); // Will either be left or right of root. No rotations irrespective of parameter G.
            assertFalse("After inserting a second key, the AVL-" +
                            t.getMaxImbalance()+ " tree should still *not* be empty.",
                    t.isEmpty());
            assertEquals("After inserting a second key, the AVL-" +
                            t.getMaxImbalance()+ " tree's new height should be 1",
                    1, t.getHeight());// Irrespective of G, this tree will have a height of 1.
            assertEquals("After inserting a key, the AVL-" +
                            t.getMaxImbalance()+ " tree's new count should be 2.",
                    2, t.getCount());
            try {
                assertEquals("After inserting a second key in an AVL-" +
                        t.getMaxImbalance()+ " tree, a search for the first one should" +
                        "*still* be successful.", firstKey, t.search(firstKey));
                assertEquals("After inserting a second key in an AVL-" +
                        t.getMaxImbalance()+ " tree, a search for the new key should" +
                        "be successful.", secondKey, t.search(secondKey));
            } catch(EmptyTreeException ignored){
                fail("search(Key) on an AVL-" + t.getMaxImbalance() + " tree should *not* have " +
                        "thrown an EmptyTreeException at this point.");
            } catch(Throwable thr) {
                fail("While retrieving the root of an AVL-" + t.getMaxImbalance() + " tree " +
                        " with two stored keys, we caught a " + thr.getClass().getSimpleName()
                        + " with message: " + thr.getMessage() + ".");
            }
            try {
                assertEquals("After the second key is inserted in an AVL-" + t.getMaxImbalance() +
                        "tree, the first key should still be at the root.", firstKey, t.getRoot()); // Irrespective of G, the root should *not* have changed. This is not a splay tree.
            } catch(EmptyTreeException ignored){
                fail("getRoot() on an AVL-" + t.getMaxImbalance() + " tree should *not* have " +
                        "thrown an EmptyTreeException at this point since we had another " +
                        "insertion and the tree should *still* be non-empty.");
            } catch(Throwable thr){
                fail("While retrieving the root of an AVL-" +  t.getMaxImbalance() + " tree " +
                        " with two stored keys, we caught a " + thr.getClass().getSimpleName()
                        + " with message: " +  thr.getMessage() + ".");
            }

            // Delete the root and see what happens.

            try {
                assertEquals("After deleting the key " + firstKey + " from an AVL-" + t.getMaxImbalance() +
                        " tree, we expected delete() to return the key itself.", firstKey, t.delete(firstKey));
            } catch(EmptyTreeException ignored){
                fail("delete(Key) threw an EmptyTreeException when deleting the root key " + firstKey +
                        " of an AVL-"+ t.getMaxImbalance() + " tree.");
            } catch(Throwable thr){
                fail("delete(Key) threw a " + thr.getClass().getSimpleName() +
                        " with message: " + thr.getMessage() + " when deleting" +
                        "the root key " + firstKey + " of an AVL-" + t.getMaxImbalance() + " tree.");
            }
            assertEquals("After deleting one of 2 keys available in an AVL-" +
                            t.getMaxImbalance() + " tree, we expected the new height to be 0.",
                    0, t.getHeight());
            assertEquals("After deleting one of 2 keys available in an AVL-" +
                            t.getMaxImbalance() + " tree, we expected the new count to be 1.",
                    1, t.getCount());
            assertFalse("After deleting one of 2 keys available in an AVL-" +
                    t.getMaxImbalance() + " tree, we *still* expect the tree " +
                    "to *not* be considered empty.", t.isEmpty());

            try {
                assertEquals("After deleting the root of an AVL-" + t.getMaxImbalance() +
                                " tree, we expected the new root to be the other key.",
                        secondKey, t.getRoot());
            } catch(EmptyTreeException ignored){
                fail("getRoot() on an AVL-" + t.getMaxImbalance() + " tree should *not* have " +
                        "thrown an EmptyTreeException since the deletion of the first key" +
                        " did not leave the tree empty.");
            }catch(Throwable thr){
                fail("After we deleted the root key of an AVL-" + t.getMaxImbalance() + " tree "
                        + "with two keys in it, getRoot() threw a " + thr.getClass().getSimpleName() +
                        " with message: " + thr.getMessage() + ".");
            }

            try {
                assertEquals("We searched for the AVL-" + t.getMaxImbalance() + "'s tree only key and failed.",
                        secondKey, t.search(secondKey));
            } catch(EmptyTreeException ignored){
                fail("search() on an AVL-" + t.getMaxImbalance() + " tree threw " +
                        "an EmptyTreeException when searching an AVL-" + t.getMaxImbalance() +
                        " tree for its single key.");
            } catch(Throwable thr){
                fail("search() threw a " + thr.getClass().getSimpleName() + " when searching" +
                        "the root key " + secondKey + " of an AVL-" + t.getMaxImbalance() +
                        " tree with another key in it.");
            }


            // Delete the second key, making the tree empty, and see what happens.

            try {
                assertEquals("When deleting the key " + secondKey + " from an AVL-" + t.getMaxImbalance() +
                        " tree, we expected delete() to return the key itself.", secondKey, t.delete(secondKey));
            } catch(EmptyTreeException ignored){
                fail("delete(Key) threw an EmptyTreeException when deleting key " + secondKey +
                        " from an AVL-"+ t.getMaxImbalance() + " tree.");
            } catch(Throwable thr){
                fail("delete(Key) threw a " + thr.getClass().getSimpleName() + " with message: "
                        + thr.getMessage() + " when deleting key " + secondKey +
                        " from an AVL-" + t.getMaxImbalance() + " tree.");
            }
            assertEquals("After deleting the last key of an AVL-" + t.getMaxImbalance() +
                    " tree, we expected its height to be -1.", -1, t.getHeight());
            assertEquals("After deleting the last key of an AVL-" + t.getMaxImbalance() +
                    " tree, we expected its count to be 0.", 0, t.getCount());
            assertTrue("After deleting the last key of an AVL-" + t.getMaxImbalance() +
                    " tree, we expected it to be empty.", t.isEmpty());

            // TODO: The following is a perfect example of code that could be
            // TODO: simplified using jUnit's Exception.ExpectedException rule.
            EmptyTreeException expected = null;
            try {
                t.getRoot();
            } catch(EmptyTreeException thrown){
                expected = thrown;
            } catch(Throwable thr){
                fail("After deleting the last key in an AVL-" +  t.getMaxImbalance()
                        + " tree, instead of an EmptyTreeException we caught a " +
                        thr.getClass().getSimpleName() + " with message: " +
                        thr.getMessage() + " while retrieving the root.");
            }
            assertNotNull("After deleting the last key of an AVL-" + t.getMaxImbalance() +
                    " tree, we expect getRoot() to throw an EmptyTreeException" , expected);

            expected = null;
            try {
                t.search(secondKey);
            } catch(EmptyTreeException thrown){
                expected = thrown;
            } catch(Throwable thr){
                fail("After deleting the last keys in an AVL-" +  t.getMaxImbalance()
                        + " tree  we caught a " + thr.getClass().getSimpleName()
                        + " with message: " +  thr.getMessage() + "" +
                        " instead of an EmptyTreeException");
            }
            assertNotNull("After deleting the last key of an AVL-" + t.getMaxImbalance() +
                    " tree, we expect search() to throw an EmptyTreeException" , expected);

            expected = null;
            try {
                t.delete(RNG.nextInt());
            } catch(EmptyTreeException thrown){
                expected = thrown;
            } catch(Throwable thr){
                fail("After deleting a randomly generated key from an *empty* AVL-" +  t.getMaxImbalance()
                        + " tree  we caught a " + thr.getClass().getSimpleName()
                        + " with message: " +  thr.getMessage() + "" +
                        " instead of an EmptyTreeException.");
            }
            assertNotNull("After deleting the last key of an AVL-" + t.getMaxImbalance() +
                    " tree, we expect delete() to throw an EmptyTreeException" , expected);
        });
    }

    /**
     * Generate 8 different insertion orders, each of which
     * should lead to the 8 operations described in the class' docs.
     * Perform tests against a maximum absolute imbalance parameter of 1.
     */
    @Test
    public void testBalancedInsertionsAVL1(){

        AVLGTree<Integer> tree = trees.get(0);
        try {
            // (1) A single left rotation about the root.
            insertAndTest(IntStream.rangeClosed(0, 2), tree,
                    1, 3, 1);

            // (2) A single right rotation about the root.
            insertAndTest(IntStream.of(0, -1, -2), tree,
                    1, 3, -1);

            // (3) A R-L rotation about the root.
            insertAndTest(IntStream.of(1, 3, 2), tree,
                    1, 3, 2);

            // (4) A L-R rotation about the root.
            insertAndTest(IntStream.of(3, 1, 2), tree,
                    1, 3, 2);

            // (5) A L-R rotation about the root's left child.
            insertAndTest(IntStream.of(5, -5, 10, -10, -8), tree,
                    2, 5, 5);

            // (6) A R-L rotation about the root's left child.
            insertAndTest(IntStream.of(5, -5, 10, 0, -2), tree,
                    2, 5, 5);

            // (7) A L-R rotation about the root's right child.
            insertAndTest(IntStream.of(5, 10, 0, 7, 8), tree,
                    2, 5, 5);

            // (8) A R-L rotation about the root's right child
            insertAndTest(IntStream.of(5, 10, 0, 13, 12), tree,
                    2, 5, 5);
        }catch(AssertionError error){
            fail("Received an AssertionError with message: " + error.getMessage() + ".");
        }
    }

    /**
     * Generate 8 different insertion orders, each of which
     * should lead to the 8 operations described in the class' docs.
     * Perform tests against a maximum absolute imbalance parameter of 2.
     */
    @Test
    public void testBalancedInsertionsAVL2(){

        AVLGTree<Integer> tree = trees.get(1);
        try {
            // (1) A single left rotation about the root.
            insertAndTest(IntStream.rangeClosed(0, 3), tree,
                    2, 4, 1);

            // (2) A single right rotation about the root.
            insertAndTest(IntStream.of(0, -1, -2, -3), tree,
                    2, 4, -1);

            // (3) A R-L rotation about the root.
            insertAndTest(IntStream.of(1, 4, 2, 3), tree,
                    2, 4, 2);

            // (4) A L-R rotation about the root.
            insertAndTest(IntStream.of(4, 1, 2, 3), tree,
                    2, 4, 2);

            // (5) A L-R rotation about the root's left child.
            insertAndTest(IntStream.of(5, -5, 10, -10, -8, -7), tree,
                    3, 6, 5);

            // (6) A R-L rotation about the root's left child.
            insertAndTest(IntStream.of(5, -5, 10, 0, -2, -3 ), tree,
                    3, 6, 5);

            // (7) A L-R rotation about the root's right child.
            insertAndTest(IntStream.of(5, 10, 0, 7, 8, 9), tree,
                    3, 6, 5);

            // (8) A R-L rotation about the root's right child
            insertAndTest(IntStream.of(5, 10, 0, 13, 12, 11), tree,
                    3, 6, 5);
        }catch(AssertionError error){
            fail("Received an AssertionError with message: " + error.getMessage() + ".");
        }
    }

    /**
     * Generate 8 different insertion orders, each of which
     * should lead to the 8 operations described in the class' docs.
     * Perform tests against a maximum absolute imbalance parameter of 3.
     */
    @Test
    public void testBalancedInsertionsAVL3() {
        AVLGTree<Integer> tree = trees.get(2);
        try {
            // (1) A single left rotation about the root.
            insertAndTest(IntStream.rangeClosed(0, 4), tree,
                    3, 5, 1);

            // (2) A single right rotation about the root.
            insertAndTest(IntStream.of(0, -1, -2, -3, -4), tree,
                    3, 5, -1);

            // (3) A R-L rotation about the root.
            insertAndTest(IntStream.of(1, 5, 2, 3, 4), tree,
                    3, 5, 2);

            // (4) A L-R rotation about the root.
            insertAndTest(IntStream.of(5, 1, 2, 3, 4), tree,
                    3, 5, 2);

            // (5) A L-R rotation about the root's left child.
            insertAndTest(IntStream.of(5, -5, 10, -10, -8, -7, -6), tree,
                    4, 7, 5);

            // (6) A R-L rotation about the root's left child.
            insertAndTest(IntStream.of(5, -5, 10, 0, -2, -3, -4), tree,
                    4, 7, 5);

            // (7) A L-R rotation about the root's right child.
            insertAndTest(IntStream.of(5, 11, 0, 7, 8, 9, 10), tree,
                    4, 7, 5);

            // (8) A R-L rotation about the root's right child
            insertAndTest(IntStream.of(5, 9, 0, 13, 12, 11, 10), tree,
                    4, 7, 5);
        } catch (AssertionError error) {
            fail("Received an AssertionError with message: " + error.getMessage() + ".");
        }
    }


    /**
     * Generate 8 different insertion &amp; deletion orders, each of which
     * should lead to the 8 operations described in the class' docs.
     * Perform tests against a maximum absolute imbalance parameter of 1.
     */
    @Test
    public void testBalancedDeletionsAVL1(){

        AVLGTree<Integer> tree = trees.get(0);
        try {
            // (1) A single left rotation about the root.
            IntStream.of(1, 3, -1, 5).forEach(tree::insert);
            deleteAndTest(-1, tree, 1, 3, 3);

            // (2) A single right rotation about the root.
            IntStream.of(5, 4, 6, 3).forEach(tree::insert);
            deleteAndTest(6, tree, 1, 3, 4);

            // (3) A R-L rotation about the root.
            IntStream.of(1, -1, 3, 2).forEach(tree::insert);
            deleteAndTest(-1, tree, 1, 3, 2);

            // (4) A L-R rotation about the root.
            IntStream.of(5, 3, 6, 4).forEach(tree::insert);
            deleteAndTest(6, tree, 1, 3, 4);

            // (5) A L-R rotation about the root's left child.
            IntStream.of(20, 5, 30, 0, 10, 25, 2).forEach(tree::insert);
            deleteAndTest(10, tree, 2, 6, 20);

            // (6) A R-L rotation about the root's left child.
            IntStream.of(5, 0, 10, -5, 2, 18, 1).forEach(tree::insert);
            deleteAndTest(-5, tree, 2, 6, 5);

            // (7) A L-R rotation about the root's right child.
            IntStream.of(20, 10, 40, 5, 30, 50, 35).forEach(tree::insert);
            deleteAndTest(50, tree, 2, 6, 20);

            // (8) A R-L rotation about the root's right child
            IntStream.of(20, 10, 30, 5, 25, 40, 35).forEach(tree::insert);
            deleteAndTest(25, tree, 2, 6, 20);

        }catch(AssertionError error){
            fail("Received an AssertionError with message: " + error.getMessage() + ".");
        }
    }

    /**
     * Generate 8 different insertion &amp; deletion orders, each of which
     * should lead to the 8 operations described in the class' docs.
     * Perform tests against a maximum absolute imbalance parameter of 2.
     */
    @Test
    public void testBalancedDeletionsAVL2(){
        AVLGTree<Integer> tree = trees.get(1);
        try {
            // (1) A single left rotation about the root.
            IntStream.of(5, 10, 15, 20, 25).forEach(tree::insert);
            deleteAndTest(5,tree,2, 4, 15 );

            // (2) A single right rotation about the root.
            IntStream.of(10, 5, 15, 0, -5).forEach(tree::insert);
            deleteAndTest(15,tree,2, 4, 5 );

            // (3) A R-L rotation about the root.
            IntStream.of(10, 5, 15, 11, 12).forEach(tree::insert);
            deleteAndTest(5, tree,2, 4, 11);

            // (4) A L-R rotation about the root.
            IntStream.of(10, 5, 15, 8, 6).forEach(tree::insert);
            deleteAndTest(15, tree,2, 4, 8);

            // (5) A L-R rotation about the root's left child.
            IntStream.of(40, 20, 50, 10, 30, 60, 12, 14).forEach(tree::insert);
            deleteAndTest(30, tree,3, 7, 40);

            // (6) A R-L rotation about the root's left child.
            IntStream.of(40, 20, 50, 10, 30, 60, 25, 23).forEach(tree::insert);
            deleteAndTest(10, tree,3, 7, 40);

            // (7) A L-R rotation about the root's right child.
            IntStream.of(20, 10, 40, 0, 30, 60, 35, 38).forEach(tree::insert);
            deleteAndTest(60, tree,3, 7, 20);

            // (8) A R-L rotation about the root's right child
            IntStream.of(20, 10, 40, 0, 30, 60, 50, 45).forEach(tree::insert);
            deleteAndTest(30, tree,3, 7, 20);
        }catch(AssertionError error){
            fail("Received an AssertionError with message: " + error.getMessage() + ".");
        }
    }

    /**
     * Generate 8 different insertion &amp; deletion orders, each of which
     * should lead to the 8 operations described in the class' docs.
     * Perform tests against a maximum absolute imbalance parameter of 3.
     */
    @Test
    public void testBalancedDeletionsAVL3() {
        AVLGTree<Integer> tree = trees.get(2);
        try {
            // (1) A single left rotation about the root.
            IntStream.of(1, 0, 2, 3, 4, 5).forEach(tree::insert);
            deleteAndTest(0, tree,3, 5, 2);

            // (2) A single right rotation about the root.
            IntStream.of(1, 2, 0, -1, -2, -3).forEach(tree::insert);
            deleteAndTest(2, tree,3, 5, 0);

            // (3) A R-L rotation about the root.
            IntStream.of(20, 10, 40, 30, 35, 37).forEach(tree::insert);
            deleteAndTest(10, tree,3, 5, 30);

            // (4) A L-R rotation about the root.
            IntStream.of(20, 0, 40, 5, 10, 15).forEach(tree::insert);
            deleteAndTest(40, tree,3, 5, 5);

            // (5) A L-R rotation about the root's left child.
            IntStream.of(40, 20, 50, 10, 25, 60, 12, 13, 14).forEach(tree::insert);
            deleteAndTest(25, tree,4, 8, 40);

            // (6) A R-L rotation about the root's left child.
            IntStream.of(40, 20, 50, 10, 25, 60, 24, 23, 22).forEach(tree::insert);
            deleteAndTest(10, tree,4, 8, 40);

            // (7) A L-R rotation about the root's right child.
            IntStream.of(20, 10, 40, 0, 30, 60, 32, 34, 36 ).forEach(tree::insert);
            deleteAndTest(60, tree, 4, 8, 20);

            // (8) A R-L rotation about the root's right child
            IntStream.of(40, 20, 60, 10, 50, 80, 75, 70, 65 ).forEach(tree::insert);
            deleteAndTest(50, tree, 4, 8, 40);
        }catch(AssertionError error){
            fail("Received an AssertionError with message: " + error.getMessage() + ".");
        }
    }


    /* ******************* /
     * ** STRESS TESTS  ** /
     * ******************* /
     */

    /**
     * <p>Test a bunch of insertions on many different AVL-G trees, with the
     * parameter <i>G</i> varied as well.</p>
     */
    @Test
    public void testManyInsertions(){
        List<Integer> keys = IntStream.range(0, NUMS).boxed().collect(Collectors.toList());
        Collections.shuffle(keys, RNG);
        trees.forEach(t->keys.forEach(k-> {
            try {
                t.insert(k);
            } catch(Throwable thr){
                fail("Caught a " + thr.getClass().getSimpleName() + " when inserting the key " + k +
                        " into a tree with maxImbalance parameter " + t.getMaxImbalance() + ".");
            }
            assertTrue("After inserting the key " + k + ", which was the key #" +
                    keys.indexOf(k) + " in the insertion sequence, in an AVL-" + t.getMaxImbalance() + " tree," +
                    " we determined that the tree did not globally satisfy " +
                    "the AVLG and/or BST properties",  ensureAVLGBST(t));
        }));
    }

    /**
     * <p>Test a bunch of insertions and subsequent deletions of all elements
     * on many different AVL-G trees, with the parameter <i>G</i> varied as well.</p>
     */
    @Test
    public void testManySuccessfulDeletions(){
        List<Integer> keys = IntStream.range(0, NUMS).boxed().collect(Collectors.toList());
        Collections.shuffle(keys, RNG);
        trees.forEach(t->keys.forEach(t::insert)); // Since we've already tested those above...
        Collections.shuffle(keys, RNG); // Re-shuffle to ensure non-dependency on insertion order.
        trees.forEach(t->keys.forEach(k-> {
            try {
                assertEquals("When deleting the key " + k + ", which was #" + keys.indexOf(k) +
                        "in the insertion sequence, from an AVL-" + t.getMaxImbalance()+
                        " tree, we expected delete() to return the key itself.", k, t.delete(k));
            } catch(Throwable thr) {
                fail("Caught a " + thr.getClass().getSimpleName() + " when deleting the key " + k +
                        " from an AVL-" + t.getMaxImbalance() + " tree.");
            }
            assertTrue("After deleting the key " + k + ", which was the key #" +
                            keys.indexOf(k) + " in the insertion sequence, we determined that the" +
                            " tree did not globally satisfy the AVLG and/or BST properties",
                    ensureAVLGBST(t));

        }));
    }

    /**
     * <p>Test a bunch of searches on varied <i>G</i> AVL-G trees, all of which
     * <b>should</b> succeed.</p>
     */
    @Test
    public void testManySuccessfulSearches(){
        List<Integer> keys = IntStream.range(0, NUMS).boxed().collect(Collectors.toList());
        Collections.shuffle(keys, RNG);
        trees.forEach(t->keys.forEach(t::insert)); // Since we've already tested those above...
        Collections.shuffle(keys, RNG); // Re-shuffle just to avoid dependence on insertion order.
        trees.forEach(t->{
            keys.forEach(k->{
                try {
                    assertEquals("Key " + k + " should have been found in the AVL-"
                            + t.getMaxImbalance() + " tree.",  k, t.search(k));
                } catch(EmptyTreeException ignored){
                    fail("search(Key) threw an EmptyTreeException when searching for key " + k +
                            ", which was the key #"+keys.indexOf(k)+ " inside a non-empty AVL-"
                            + t.getMaxImbalance() + " tree, with " + k + " guaranteed to be in the tree.");
                } catch(Throwable thr){
                    fail("search(Key) threw a " + thr.getClass().getSimpleName() + " when searching " +
                            "for key " + k + ", which was the key #" +keys.indexOf(k)+ " in a non-empty AVL-"
                            + t.getMaxImbalance() +" tree, with " + k + " guaranteed to be in the tree.");
                }
            });
        });
    }

    /**
     * <p>Test a bunch of searches on varied <i>G</i> AVL-G trees, <b>none</b> of which
     * <b>should</b> succeed!</p>
     */
    @Test
    public void testManyFailedSearches(){

        List<Integer> keys = IntStream.rangeClosed(1, NUMS + 1).boxed().collect(Collectors.toList());
        Collections.shuffle(keys, RNG);
        trees.forEach(t->keys.forEach(t::insert)); // Since we've already tested those above...
        Collections.shuffle(keys, RNG); // Re-shuffle just to avoid dependence on insertion order.
        trees.forEach(t-> keys.forEach(k->{
            try {
                // Have to subtract 1 from the generated index below because I filter out zero
                // and I would be "one off" in the stderr reporting.
                assertEquals("While looking for the negative of key " + k + "," +
                                "which was the key #" + keys.indexOf(k)  + "in the search & delete sequence, we were " +
                                "expecting to not be able to find it in an AVL-" + t.getMaxImbalance() + " tree.",
                        null, t.search(-k));
                try {
                    t.delete(k);  
                } catch(EmptyTreeException ignored){
                    fail("delete(Key) threw an EmptyTreeException when deleting key " + k +
                            ", which was the key #" + keys.indexOf(k)   + " in the search & delete sequence, in an AVL-"+
                            t.getMaxImbalance() + " tree.");
                }
                if(!t.isEmpty()) {
                    assertEquals("After deleting key " + k + ", which was key#" + keys.indexOf(k)
                                    + " in the search & delete sequence, from an AVL-" + t.getMaxImbalance() + " tree,"
                                    + " we no longer expect searches for it to succeed (no duplicates inserted).",
                            null, t.search(k));
                    assertEquals("After deleting key " + k + ", which was key#" + keys.indexOf(k)
                            + " in the search & delete sequence, from an AVL-" + t.getMaxImbalance() + " tree,"
                            + " we *still* do *not* expect searches for its *negative* key to succeed " +
                            "(no duplicates inserted).", null, t.search(-k));
                }
            } catch(EmptyTreeException ignored){
                fail("search(Key) or delete(Key) threw an EmptyTreeException when searching for key " + k +
                        ", which was the key #" + keys.indexOf(k) + " in the sequence, inside " +
                        "an AVL-"+ t.getMaxImbalance() + " tree.");
            } catch(Throwable thr){
                fail("search(Key) or delete(Key) threw a " + thr.getClass().getSimpleName() + " with message " +
                        thr.getMessage() + " while searching for or deleting key " + k + ", which was the key #" +
                        keys.indexOf(k)  + " in the sequence, in an AVL-" + t.getMaxImbalance() + " tree.");

            }
        }));
    }

    /**
     * <p>Test the behavior of clearing (calling clear()), count(), and
     * calling delete() until the tree is empty (instead of calling clear().</p>
     */
    @Test
    public void testCountDeleteAndClear(){
        List<Integer> keys = IntStream.range(0, NUMS).boxed().collect(Collectors.toList());
        Collections.shuffle(keys, RNG);
        trees.forEach(t->{
            keys.forEach(t::insert);
            Collections.shuffle(keys, RNG); // Re-shuffle just to avoid dependence on insertion order.
            assertEquals("When inserting a collection of keys in an AVL-" + t.getMaxImbalance()
                    + " tree, we expect the tree's count to be the same as the collection's "
                    + "size.", keys.size(), t.getCount());
            t.clear();
            assertTrue("After clearing an AVL-" + t.getMaxImbalance() +
                    " tree, either the tree wasn't empty or its count was 0.", ensureTreeEmpty(t));

            // Re-insert and delete() everything explicitly, instead of using clear()
            keys.forEach(t::insert);
            Collections.shuffle(keys, RNG); // Re-shuffle just to avoid dependence on insertion order.
            keys.forEach(k->{
                try {
                    t.delete(k);
                } catch(EmptyTreeException ignored){
                    fail("When deleting key " + k + ", which was #" + keys.indexOf(k) + " in the sequence " +
                            " from a non-empty AVL-" + t.getMaxImbalance() +" tree, we caught an " +
                            "EmptyTreeException.");
                } catch(Throwable thr) {
                    fail("When deleting key " + k + ", which was #" + keys.indexOf(k) + " in the sequence " +
                            " from a non-empty AVL- " + t.getMaxImbalance() + " tree, we caught a " + thr.getClass().getSimpleName() + " with message " +
                            thr.getMessage() + ".");
                }
                assertTrue("After deleting key " + k + ", which was key #" +
                                keys.indexOf(k) + " in the list, the AVL-" + t.getMaxImbalance() + "" +
                                " tree was found to not obey either the BST and/or the AVL-G properties.",
                        ensureAVLGBST(t));

            });
            assertTrue("After explicitly deleting all keys from an AVL-" + t.getMaxImbalance() +
                    " tree, either the tree wasn't empty or its count was 0.", ensureTreeEmpty(t));
        });
    }


}
