package projects.pqueue.lists;


/** <p>A {@link Exception} thrown by methods that take an index into an element of a {@link List}, when
 * the index is invalid with respect to the current state of the {@link List}.</p>
 *
 * <p>You should <b>not</b> edit this class! It is given to you as a resource for your project.</p>

 * @author  <a href="https://github.com/JasonFil">Jason Filippou</a>
 *
 * @see EmptyListException
 */
public class IllegalListAccessException extends Exception {
	public IllegalListAccessException(String msg){
		super(msg);
	}
}
