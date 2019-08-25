package projects.pqueue.exceptions;

/**
 *<p> InvalidPriorityException is an {@link Exception} that is thrown by {@link projects.pqueue.priorityqueues.PriorityQueue}
 * when the user supplies a priority that is 0 or below.</p>
 *
 * <p>You should <b>not</b> edit this class! It is given to you as a resource for your project.</p>

 * @author  <a href="https://github.com/JasonFil">Jason Filippou</a>
 *
 * @see InvalidCapacityException
*/
public class InvalidPriorityException extends Exception {
    public InvalidPriorityException(String msg){
        super(msg);
    }
}
