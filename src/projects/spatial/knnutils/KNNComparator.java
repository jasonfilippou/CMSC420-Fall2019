package projects.spatial.knnutils;

import projects.spatial.kdpoint.KDPoint;

import java.io.Serializable;
import java.util.Comparator;

/**<p>KNNComparator is a {@link Serializable} {@link Comparator} used for sorting {@link KDPoint}s
 * based on the {@link KDPoint#distanceSquared(KDPoint, KDPoint) distanceSquared} to an anchor {@link KDPoint}. It is very
 * useful for <b>testing</b> KNN queries.</p>
 * 
 * <p><b>Note: this comparator imposes orderings that are inconsistent with {@link KDPoint#equals(Object)}.</b></p>
 *
 * <p><b>YOU SHOULD ***NOT*** EDIT THIS CLASS!</b> If you do, you risk <b>not passing our tests!</b></p>
 *
 * @author <a href ="https://github.com/JasonFil/">Jason Filippou</a>
 * @param <T> A {@link KDPoint} type.
 * @see NNData
 */
public class KNNComparator<T extends KDPoint> implements Comparator<T>, Serializable {

	/**
	 * The &quot;anchor&quot;point for which we want to calculate the nearest neighbors.
	 */
	private T anchor;

	/**
	 * A default serial version ID so that the compiler doesn't complain.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Our constructor will store the anchor point that we want to base all future {@link KDPoint} comparisons on.
	 * @param arg The {@link KDPoint} object used as the basis of all future comparisons. 
	 */
	public KNNComparator(T arg) {
		anchor = arg;
	}

	@Override
	public int compare(T o1, T o2) {
		byte comparison = (byte) o1.distanceSquared(anchor).compareTo(o2.distanceSquared(anchor));
		if(comparison < 0) // o1 closer
			return -1;
		else if(comparison == 0) // same distance
			return 0;
		else // o2 closer
			return 1;

	}
}
