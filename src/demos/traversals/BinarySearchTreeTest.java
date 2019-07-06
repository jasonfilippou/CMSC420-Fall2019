package demos.traversals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

/** A jUnit4 (not Arquillian) testing framework for {@link BinarySearchTree}s. All public methods
 * will be tested.
 * @author Jason
 * @see BSTClient
 * @see BinarySearchTree
 */
public class BinarySearchTreeTest {

  private BinarySearchTree<Integer> tree;
  private Random r;
  private static long SEED = 47;
  private static int NUM_INTS = 1000;
  private IntStream ints;

  @Before
  public void setUp() throws Exception {
    tree = new BinarySearchTree<Integer>();
    ints = IntStream.range(0, NUM_INTS);
    r = new Random(SEED);
  }

  @After
  public void tearDown() throws Exception {
    tree = null;
    ints = null;
    r = null; // TODO: How feasible is it for a weakly compiled language to be able to do tree = ints = r = null? Compiler should intuitively not complain. Recall: Python is weakly typed.
    System.gc(); // HAHA THIS WILL ACTUALLY DO SOMETHING
  }

  @Test
  public void inorderTraversalRec() throws Exception {
    List<Integer> intList = ints.boxed().collect(Collectors.toList());
    List<Integer> copy = new LinkedList<Integer>(intList);
    Collections.shuffle(copy);
    copy.forEach(tree::insert);

    LinkedList<Integer> visited = new LinkedList<Integer>();
    tree.inorderTraversalRec(visited);

    ListIterator<Integer> intIt = intList.listIterator(), visitedIt = visited.listIterator();

    while(intIt.hasNext() && visitedIt.hasNext())
      assertTrue(intIt.next().equals(visitedIt.next()));
    assertTrue(!intIt.hasNext() && !visitedIt.hasNext());

  }

  @Test
  public void inorderTraversalWithStack() throws Exception {

    List<Integer> intList = ints.boxed().collect(Collectors.toList());
    List<Integer> copy = new LinkedList<Integer>(intList);
    Collections.shuffle(copy);
    copy.forEach(tree::insert);

    LinkedList<Integer> visited = new LinkedList<Integer>();
    tree.inorderTraversalWithStack(visited);

    ListIterator<Integer> intIt = intList.listIterator(), visitedIt = visited.listIterator();

    while(intIt.hasNext() && visitedIt.hasNext())
      assertTrue(intIt.next().equals(visitedIt.next()));
    assertTrue(!intIt.hasNext() && !visitedIt.hasNext());
  }

  @Test
  public void insert() throws Exception {

    // Some easy stuff first
    tree.insert(100);
    assertEquals(1, tree.getCount());
    IntStream.range(0, 100).filter(i -> i%2 ==1).forEach(tree::insert); // 50 odds in {0, 1, 2, ..., 99}
    assertEquals(51, tree.getCount());

    // Now pummel it
    try {
      r.ints(0, 5000).limit(NUM_INTS).forEach(tree::insert);
    } catch(Exception e){
      fail("Insertion tests received an exception " + e.getClass().getSimpleName() + " with message: " + e.getMessage() + ".");
    }
  }

  @Test
  public void getCount() throws Exception {
    assertTrue(tree.getCount() == 0);
    assertFalse(tree.getCount() != 0);
  }

  @Test
  public void search() throws Exception{
    List<Integer> myInts =IntStream.range(0, NUM_INTS).boxed().collect(Collectors.toList());
    Collections.shuffle(myInts, r);
    for(Integer i: myInts)
      tree.insert(i);
    for(Integer i: myInts)
      assertEquals("When searching the tree for the key " + i + ", we instead received a " + tree.search(i), i, tree.search(i)); // I don't care enough that I search twice for just an assertion.
  }

  @Test
  public void deleteSimple() throws Exception {
    Integer[] arr = { 5, 6, 3, 4};
    for (Integer i : arr)
      tree.insert(i);
    assertEquals("After inserting " + arr.length + " elements, we would have expected that amount to be reflected" +
            " by our tree.", arr.length, tree.getCount());
    tree.delete(3);
    assertEquals("After deleting the key 3, we should not " +
            "be able to find it in the tree.", null, tree.search(3));
    assertEquals("Count after deleting the key 3 is not right.", 3, tree.getCount());

    tree.delete(5);
    assertEquals("After deleting the key 5, we should not " +
            "be able to find it in the tree.", null, tree.search(5));
    assertEquals("Count after deleting the key 5 is not right.", 2, tree.getCount());

    tree.delete(-1); // not in the tree
    assertEquals("After attempting to remove a key that does not exist in the tree, " +
            "we should most definitely not be finding the key in our tree!", null, tree.search(5));
    assertEquals("Count after deleting a key not in the tree is not right.", 2, tree.getCount());

  }

  @Test
  public void deleteStress() throws Exception{
    List<Integer> all = IntStream.range(0, NUM_INTS).boxed().collect(Collectors.toList());
    List<Integer> odds = IntStream.range(0, NUM_INTS).filter(i-> (i % 2 == 1)).boxed().collect(Collectors.toList()); // odds
    List<Integer> evens = IntStream.range(0, NUM_INTS).filter(i-> (i % 2 == 0)).boxed().collect(Collectors.toList()); // evens

    BinarySearchTree<Integer> allTree = new BinarySearchTree<Integer>(),
            oddTree = new BinarySearchTree<Integer>(),
            evenTree = new BinarySearchTree<Integer>();

    all.forEach(allTree::insert);
    odds.forEach(oddTree::insert);
    evens.forEach(evenTree::insert);

    int allCount = allTree.getCount();
    int oddsCount = oddTree.getCount();
    int evensCount = evenTree.getCount();

    assertEquals("Size of collection of numbers and relevant tree mismatch.", all.size(), allTree.getCount());
    assertEquals("Size of collection of odd numbers and relevant tree mismatch.", odds.size(), oddTree.getCount());
    assertEquals("Size of collection of even numbers and relevant tree mismatch.",evens.size(), evenTree.getCount());

    Collections.shuffle(all, r);
    Collections.shuffle(evens, r);
    Collections.shuffle(odds, r);

    for(Integer i : evens)
      oddTree.delete(i); // This should not affect oddTree at all.
    assertEquals("Odd Tree should not have been affected by these deletions.", oddsCount, oddTree.getCount());

    for(Integer i : odds)
      evenTree.delete(i);
    assertEquals("Even Tree should not have been affected by these deletions.", evensCount, evenTree.getCount());

    for(Integer i : evens)
      allTree.delete(i);

    assertEquals("After deleting all evens from the global tree, we should " +
            "be getting the exact number of odds.", oddTree.getCount(), allTree.getCount());

    int counter = allTree.getCount();
    for(Integer i: odds){
      allTree.delete(i);
      assertEquals("After deleting " + i + ", we found out that the BST's node size was not accurate.", counter - 1, allTree.getCount());
      counter--;
    }

    assertTrue("We should have exhausted all the integers in allTree by now.", allTree.isEmpty());
    assertTrue("We should have exhausted all the integers in allTree by now.", allTree.getCount() == 0);

  }

  @Test
  public void testRangeSearch(){
    List<Integer> is = IntStream.rangeClosed(1, 10).boxed().collect(Collectors.toList());
    Collections.shuffle(is, r);
    is.forEach(tree::insert);
    Collections.sort(is);
    Iterator<Integer> it1, it2, it3, it4, it5, it6, it7, it8, it9;
    it1 = tree.rangeSearch(1, 10);
    assertTrue("it1 did not return expected range.", testRange(it1, 1, 10, is));
    it2 = tree.rangeSearch(0, 10);
    assertTrue("it2 did not return expected range.", testRange(it2, 1, 10, is));
    it3 = tree.rangeSearch(1, 11);
    assertTrue("it3 did not return expected range.", testRange(it3, 1, 10, is));
    it4 = tree.rangeSearch(0, 11);
    assertTrue("it4 did not return expected range.", testRange(it4, 1, 10, is));
    it5 = tree.rangeSearch(2, 8);
    assertTrue("it5 did not return expected range.", testRange(it5, 2, 8, is));
    it6 = tree.rangeSearch(3, 7);
    assertTrue("it6 did not return expected range.", testRange(it6, 3, 7, is));
    it7 = tree.rangeSearch(4, 6);
    assertTrue("it7 did not return expected range.", testRange(it7, 4, 6, is));
    it8 = tree.rangeSearch(5, 5);
    assertTrue("it8 did not return expected range.", testRange(it8, 5, 5, is));
    try {
      tree.rangeSearch(6, 4);
    }catch(IllegalArgumentException ignored){
      // Do nothing
    } catch(Throwable t){
      fail("testRangeSearch(): Caught a " + t.getClass().getSimpleName() + " while supplying an invalid range search to our tree. Message was: " + t.getMessage() + ".");
    }
  }

  // Exhaustively search the elements accessed by the iterator
  private boolean testRange(Iterator<Integer> it, Integer min, Integer max, List<Integer> list){
    int i = min;
    while(it.hasNext()){
      if(!it.next().equals(i))
        return false;
      i++;
    }
    assert i == max : "i should be the right end of the range after we are done!";
    return true;
  }

}