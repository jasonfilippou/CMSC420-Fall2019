package projects.spatial.knnutils;

import projects.spatial.kdpoint.KDPoint;

import java.math.BigDecimal;

/**
 * <p>{@link PriorityQueue} is an interface specification for <i>Priority Queues</i>. Those are queues which, instead
 * of operating in a FIFO manner, will insert elements according to a provided priority, which is <b>not necessarily an integer</b>
 * and where <b>lower number means better priority</b>.</p>
 *
 * <p>Minor detail: since {@link PriorityQueue} is an <b>interface</b>, all of its methods are implicitly public, so the explicit
 * scope modifier is <b>not needed</b> in the source.</p>
 *
 *<p><b>YOU SHOULD ***NOT*** EDIT THIS CLASS!</b> If you do, you risk <b>not passing our tests!</b></p>
 *
 * @author <a href="https://github.com/JasonFil/">Jason Filippou</a>
 *
 * @see PriorityQueueNode
 * @see BoundedPriorityQueue
 */
public interface PriorityQueue<T> extends Iterable<T>{
	
	/**
	 * <p>Insert element in the {@link PriorityQueue} according to its priority.
	 * <b>Lower is better.</b> We allow for <b>non-integer priorities</b> such that the {@link PriorityQueue}
	 * can be used for orderings where the prioritization is <b>not</b> rounded to integer quantities, such as
	 * Euclidean Distances in KNN queries. </p>
	 *
	 * @param element The element to insert in the queue.
	 * @param priority The priority of the element. Encoded as an arbitrary precision {@link BigDecimal}.
	 *
	 * @see projects.spatial.kdpoint.KDPoint#distanceSquared(KDPoint)
	 * @see BigDecimal
	 */
	void enqueue(T element, BigDecimal priority);
	
	/**
	 * Return the <b>minimum priority element</b> in the queue, <b>simultaneously removing it</b> from the structure.
	 * @return The minimum priority element in the queue, or null if the queue is empty.
	 */
	T dequeue();
	
	/**
	 * Return, <b>but don't remove</b>, the <b>minimum priority element</b> from the queue.
	 * @return The minimum priority element of the queue, or null if the queue is empty.
	 */
	T first();
	
	/**
	 * Query the queue about its size. <b>Empty queues have a size of 0.</b>
	 * @return The size of the queue. Returns 0 if the queue is empty.
	 */
	int size();
	
	/**
	 * Query the queue about emptiness. A queue is empty <b>iff</b> it contains <b>0 (zero)</b> elements.
	 * @return true iff the queue contains <b>0 (zero)</b> elements.
	 */
	boolean isEmpty();
}

