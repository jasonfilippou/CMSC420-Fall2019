package projects.spatial.knnutils;

import java.math.BigDecimal;

/**
 * <p>A PriorityQueueNode is a {@link Comparable} type which is used to wrap around
 * the (data, priority) pairs in a {@link PriorityQueue}. Its overriding of the
 * {@link Comparable#compareTo(Object)} method allows the {@link PriorityQueue} to disambiguate between
 * the same priority elements, thus establishing a strict <em>natural ordering</em> inside the data structure.</p>
 *
 * <p><b>YOU SHOULD ***NOT*** EDIT THIS CLASS!</b> If you do, you risk <b>not passing our tests!</b></p>
 *
 * @author <a href="https://github.com/JasonFil/">Jason Filippou</a>
 *
 * @param <T> The type of element contained in the PriorityQueueNode.
 *
 * @see PriorityQueue
 * @see BoundedPriorityQueue
 */
public class PriorityQueueNode<T> implements Comparable<PriorityQueueNode<T>>{

	private T data;
	private BigDecimal priority;
	private int orderInserted;

	/**
	 * 3-arg constructor.
	 * @param data The element of type T held by the container.
	 * @param priority The element's priority, as provided by the caller. <b>&quot;Smaller&quot;priorities are considered &quot;higher.</b> The accuracy of
	 *                 priorities is {@link BigDecimal}, a data type that represents arbitrary precision floating point numbers.
	 * @param insertionOrder The order that the element was inserted in, as provided by the caller. This parameter is important
	 *                       because, in a {@link PriorityQueue}, tie-breakers between elements with the same priority are
	 *                       determined by their insertion order in the {@link PriorityQueue}. <b>It is the caller's responsibility
	 *                       to ensure that this parameter is passed correctly.</b>
	 * @see BigDecimal
	 */
	public PriorityQueueNode(T data, BigDecimal priority, int insertionOrder){
		this.data = data;
		this.priority = priority;
		orderInserted = insertionOrder;
	}

	/**
	 * 1-arg constructor.
	 * @param data The element of type T held by the container.
	 */
	public PriorityQueueNode(T data){
		this(data, new BigDecimal(-1), 0);
	}

	/**
	 * Default constructor.
	 */
	public PriorityQueueNode(){
		this(null);
	}

	@Override
	public int compareTo(PriorityQueueNode<T> o) {
		// Remember that a numerically smaller priority
		// is actually considered larger in priority
		// queue terms. Also recall that we are using a 
		// MinHeap, so the smallest elements ascend to the top,
		// not the largest.
		if(priority.compareTo(o.priority) < 0)
			return -1;
		else if(priority.compareTo(o.priority) > 0)
			return 1;
		else {
			if(orderInserted < o.orderInserted)
				return -1;
			else // Covers equality but it's not like we'll ever have equality of two elements.
				return 1;
		}
	}

	/**
	 * Simple getter for contained data element.
	 * @return The element of type T contained by this.
	 */
	public T getData(){
		return data;
	}

	/**
	 * Simple getter for element's priority.
	 * @return The contained element's priority. <b>&quot;Smaller&quot; {@link BigDecimal} instances have higher priorities</b>.
	 */
	public BigDecimal getPriority(){
		return priority;
	}

}