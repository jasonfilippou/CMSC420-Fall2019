package demos.traversals;

import java.util.Stack;

/**<p>{@link JLinkedList} is a simple Linked List which offers four ways to compute its count:</p>
 * <ol>
 *     <li>Purely iteratively.</li>
 *     <li>&quot;Recursion&quot; with a custom stack</li>
 *     <li>Recursion.</li>
 *     <li>Tail recursion.</li>
 * </ol>
 * <p>The list isn't even implemented as a generic since its nodes are empty; the class' intent is
 * purely demonstrative of the different ways that we can implement an otherwise simple procedure.</p>
 *
 * @author <a href = "mailto:jason.filippou@gmail.com">Jason Filippou</a>
 */
public class JLinkedList {

    // Dummy node without any data fields besides a pointer to the next Node instance.
    private class Node {
        Node next;
    }

    private Node head;

    /**
     * Adds another node at the end of the list.
     */
    public void pushBack(){
        if(head == null)
            head = new Node();
        Node curr = head;
        while(curr.next != null)
            curr = curr.next;
        curr.next = new Node();
    }

    /* Counts */

    /**
     * Standard iterative counting.
     * @return The number of elements in the list.
     */
    public int countIter(){
        int count = -1;
        Node curr = head;
        while(curr != null) {
            curr = curr.next;
            count++;
        }
        return count;
    }

    /**
     * An iterative procedure which pushes every node accessed on a {@link java.util.Stack}. Meant to emulate
     * recursion.
     * @return The number of elements in the list.
     */
    public int countStack(){
        Stack<Node> s = new Stack<>();
        Node curr = head;
        int count = -1;
        while(curr != null){
            s.push(curr);
            curr = curr.next;
            count++;
        }
        while(!s.isEmpty()) // If you're going to emulate recursion, you need to emulate explicit popping.
            s.pop();
        return count;
    }

    /**
     * Recursive counting. Will probably make the system's stack blow up.
     * @return The number of elements in the list.
     */
    public int countRec(){
        return countRec(head);
    }

    private int countRec(Node n){
        if(n == null)
            return -1;
        else
            return 1 + countRec(n.next);
    }

    /**
     * Tail-recursive counting. Let's see if the JVM will do appropriate tail recursion here.
     * @return The number of elements in the list.
     */
    public int countTailRec(){
        return (head == null) ? 0 : countTailRec(1, head);
    }

    private int countTailRec(int count, Node curr){
        return (curr == null) ? count : countTailRec(count + 1, curr.next);
    }


}
