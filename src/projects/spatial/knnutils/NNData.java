package projects.spatial.knnutils;

import java.math.BigDecimal;

/**<p>{@link NNData} is a simple "struct-like" class that stores
 * intermediate results of nearest neighbor queries. </p>
 *
 * <p><b>YOU SHOULD ***NOT*** EDIT THIS CLASS!</b> If you do, you risk <b>not passing our tests!</b></p>
 *
 * @author <a href = "https://github.com/JasonFil/">Jason Filippou</a>
 *
 * @param <T> The type of {@link Object} held by the container.
 *
 * @see KNNComparator
 */
public class NNData<T> {
	
	/**
	 * The current best guess about the element closest to an anchor element in nearest-neighbor
	 * searching. Declared public to facilitate access by client code.
	 */
	public T bestGuess;
	
	/**
	 * The distance between the current best guess and
	 * the anchor element. Declared public to facilitate access by client code.
	 */
	public BigDecimal bestDist;
	
	/**
	 * Simple constructor that stores its arguments.
	 * @param bestGuess The current best guess.
	 * @param bestDist The distanceSquared between the current best guess and the &quot;anchor&quot; element.
	 */
	public NNData(T bestGuess, BigDecimal bestDist){
		this.bestGuess = bestGuess;
		this.bestDist = bestDist;
	}

}
