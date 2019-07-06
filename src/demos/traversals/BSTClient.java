package demos.traversals;

import java.util.LinkedList;
import java.util.Random;

/**<p>BSTClient is a client program for {@link BinarySearchTree}. It will be used mostly for timing
 * and for benchmarking.</p>
 * @author Jason
 */
public class BSTClient {


  public static void main(String[] args) {
    Random r = new Random();
    BinarySearchTree<Integer> tree = new BinarySearchTree<Integer>();
    tree.insert(2);
    tree.insert(1);
    tree.insert(3);
    LinkedList<Integer> visited = new LinkedList<Integer>();
    tree.inorderTraversalWithStack(visited);
    System.out.println(visited);
  }

}