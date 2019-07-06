package demos.traversals;

import java.util.stream.IntStream;

import static java.lang.System.*;

/**<p>{@link JLinkedListClient} is a timing client for {@link JLinkedList}. It also monitors the throwing of
 * potential stack overflow / Out of memory exceptions.</p>

 * @author <a href = "mailto:jason.filippou@gmail.com">Jason Filippou</a>
 *
 * @see JLinkedList
 */
public class JLinkedListClient {


    private static int ELEMENTS = 100000; // Number of nodes to put in the list.

    /** Driver for the class. Times the three different traversals and reports findings.
     * @param args An array of command-line parameters the user can give.
     */
    public static void main(String[] args){
        JLinkedList l = new JLinkedList();
        IntStream.range(0, ELEMENTS).forEach(i -> l.pushBack());
        System.out.println("--------------------------------- " +
                "\n Timing iterative counting.... \n " +
                "--------------------------- \n");
        long start = currentTimeMillis();
        System.out.println("List size retrieved was: " + l.countIter() + ".");
        System.out.println("Iterative counting took " + (currentTimeMillis() - start) + " ms.\n");

        System.out.println("------------------------------------------" +
                "\n Timing custom stack counting.... \n " +
                "------------------------------------------ \n");
        try {
            start = currentTimeMillis();
            System.out.println("List size retrieved was: " + l.countStack() + ".");
            System.out.println("Custom stack-based counting took " + (currentTimeMillis() - start)  + " ms. \n");
        } catch(Throwable t){
            System.out.println("While generating the stack-based size, we received a " +
                    t.getClass().getSimpleName() + " with message: " + t.getMessage() + ".");
        }

        System.out.println("------------------------------------------ " +
                "\n Timing recursive counting.... \n " +
                "------------------------------------------\n");
        try {
            start = currentTimeMillis();
            System.out.println("List size retrieved was: " + l.countRec() + ".");
            System.out.println("System stack-based traversal took " + (currentTimeMillis() - start) + " ms.\n");
        }catch(Throwable t){
            System.out.println("While generating the recursive size, we received a " +
                    t.getClass().getSimpleName() + " with message: " + t.getMessage() + ".");
        }

        System.out.println("------------------------------------------ " +
                "\n Timing tail-recursive counting.... \n " +
                "------------------------------------------\n");
        try {
            start = currentTimeMillis();
            System.out.println("List size retrieved was: " + l.countTailRec() + ".");
            System.out.println("Tail-recursive traversal took " + (currentTimeMillis() - start) + " ms.\n");
        }catch(Throwable t){
            System.out.println("While generating the tail - recursive size, we received a " +
                    t.getClass().getSimpleName() + " with message: " + t.getMessage() + ".");
        }

    }

}
