package projects.pqueue.exceptions;

import java.util.ArrayList;

/**
  *<p> InvalidCapacityException is an {@link Exception} that is used by <b>non-default</b> constructors of
 * containers that receive a desired starting capacity  in our code base. For reasons of efficiency, it is
 * often important that the container is internally pre-allocated to a desired capacity so that the getFirst few
 * insertions are performed quite fast. For example, {@link ArrayList#ArrayList()} (the {@link ArrayList}'s default
 * constructor}) initializes the {@link ArrayList} with a default capacity of <b>10 (ten)</b> to avoid
 * paying for the resizing of the container for the getFirst 10 insertions of elements.</p>
 *
 * <p>In many object-oriented programming languages such as Java, there is no way to notify the
 * caller of a constructor of an underlying error in the constructor body other than throwing
 * some kind of {@link Throwable}. For example, the Java runtime would throw an {@link OutOfMemoryError}
 * if the heap runs out of memory during object creation.</p>
 *
 * <p>You should <b>not</b> edit this class! It is given to you as a resource for your project.</p>

 * @author  <a href="https://github.com/JasonFil">Jason Filippou</a>
 *
 *
 */
public class InvalidCapacityException extends Exception {
	public InvalidCapacityException(String msg){
		super(msg);
	}
}
