package projects.spatial.trees;
import projects.spatial.kdpoint.KDPoint;
import projects.spatial.knnutils.BoundedPriorityQueue;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * <p>{@link SpatialQuerySolver} is an interface that declares methods for range and k-NN queries over {@link KDPoint}s.</p>
 *
 *  <p>Minor detail: since {@link SpatialQuerySolver} is an <b>interface</b>, all of its methods are implicitly
 *  public, so the explicit scope modifier is <b>not needed</b> in the source.</p>
 *
 * <p><b>YOU SHOULD ***NOT*** EDIT THIS INTERFACE!</b> If you do, you risk <b>not passing our tests!</b></p>
 *
 * @author <a href="https://github.com/JasonFil">Jason Filippou</a>
 *
 * @see KDPoint
 * @see BoundedPriorityQueue
 * @see SpatialDictionary
 */
public interface SpatialQuerySolver {

    /**
     * Performs a range query. Returns all the {@link KDPoint}s whose {@link KDPoint#distanceSquared(KDPoint) distanceSquared} from
     * p is at most range, <b>INCLUSIVE</b>.
     * @param p The query {@link KDPoint}.
     * @param range The maximum {@link KDPoint#distanceSquared(KDPoint, KDPoint) distance} from p
     * that we allow a {@link KDPoint} to have if it should be part of the solution.
     * @return A {@link Collection} over all {@link KDPoint}s which satisfy our query. The
     * {@link Collection} will be empty if there are no points which satisfy the query.
     * @see KDPoint
     * @see Collection
     */
    Collection<KDPoint> range(KDPoint p, BigDecimal range);

    /** Performs a nearest neighbor query. Returns the {@link KDPoint} which is closest to
     * p, as dictated by {@link KDPoint#distanceSquared(KDPoint) distanceSquared(KDPoint p)}.
     * @param p The query {@link KDPoint}.
     * @return The solution to the nearest neighbor query. This method will return null if
     * there are no points other than p in the tree.
     * @see KDPoint
     */
    KDPoint nearestNeighbor(KDPoint p);

    /**
     * Performs a k-nearest neighbors query on the SpatialTree. Returns the <em>k</em>
     * {@link KDPoint}s which are nearest to p, as dictated by
     * {@link KDPoint#distanceSquared(KDPoint)}.
     * The {@link KDPoint}s are sorted in ascending order of distanceSquared.
     * @param k A positive integer denoting the amount of neighbors to return.
     * @param p The query point.
     * @return A {@link BoundedPriorityQueue} containing the k-nearest neighbors of p.
     * This queue will be empty if the tree contains only p.
     * @throws RuntimeException If k&lt;=0.
     * @see KDPoint
     * @see BoundedPriorityQueue
     */
    BoundedPriorityQueue<KDPoint> kNearestNeighbors(int k, KDPoint p);
}
