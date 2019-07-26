package projects.spatial.nodes;

import projects.spatial.kdpoint.KDPoint;
import projects.spatial.knnutils.BoundedPriorityQueue;
import projects.spatial.knnutils.NNData;

import java.math.BigDecimal;
import java.util.Collection;

import static projects.spatial.kdpoint.KDPoint.distanceSquared;

/**
 * <p>{@link KDTreeNode} is an abstraction over nodes of a KD-Tree. It is used extensively by
 * {@link projects.spatial.trees.KDTree} to implement its functionality.</p>
 *
 * <p><b>YOU ***** MUST ***** IMPLEMENT THIS CLASS!</b></p>
 *
 * @author  ---- YOUR NAME HERE! -----
 *
 * @see projects.spatial.trees.KDTree
 */
public class KDTreeNode {

    private static RuntimeException UNIMPL_METHOD = new RuntimeException("Implement this method!");

    /* *************************************************************************
     ************** PLACE YOUR PRIVATE METHODS AND FIELDS HERE: ****************
     ***************************************************************************/

    private KDPoint p;
    private int height;
    private KDTreeNode left, right;
    private static final BigDecimal INFTY = new BigDecimal(-1); // Encoding infinity with a negative number is safer than Double.MAX_VALUE for our purposes.

    private BigDecimal rootOfBigDecimal(BigDecimal bd){
        return new BigDecimal(Math.sqrt(bd.doubleValue()));
    }

    private boolean coordLargerOrEqual(KDPoint p1, KDPoint p2, int dim){
        return p1.coords[dim].compareTo(p2.coords[dim]) >= 0;
    }

    private KDPoint findMinOfDim(int soughtDim, int currDim, int dims){
        int nextDim = (currDim + 1) % dims;
        if(soughtDim == currDim)
            if(left != null)
                return left.findMinOfDim(soughtDim, nextDim,  dims);
            else
                return new KDPoint(p);
        else{
            KDPoint leftVal = (left != null) ? left.findMinOfDim(soughtDim, nextDim, dims) : p,
                    rightVal = (right != null) ? right.findMinOfDim(soughtDim, nextDim, dims) : p;
            return min(leftVal, p, rightVal, soughtDim);
        }

    }

    private int max(int a, int b){
        return a >= b ? a : b;
    }

    // We follow a clockwise approach when we have equality
    // cases: L is preferred over C is preferred over R whenever any pair
    // of those nodes is equal w.r.t the coordinate "dim".
    private KDPoint min(KDPoint left, KDPoint curr, KDPoint right, int dim){
        if(coordLargerOrEqual(right, curr, dim)) // R >= C
            if(coordLargerOrEqual(curr, left, dim)) // C >= L
                return left; // R >= C >= L
            else // L >= C
                return curr; // R >= C && L >= C, and we only care about min
        else // C >= R
            if(coordLargerOrEqual(right, left, dim)) // R >= L
                return left; // C >= R >= L
            else // L >= R
                return right;  // C >= R && L >= R, , and we only care about min
    }

    /* Methods useful for pruning out large areas of the tree during range
     * queries. The first one checks to see if the anchor point is too far
     * away from the currently considered point in the increasing direction,
     * for instance, +x, +y, +z etc.
     */
    private boolean anchorTooFarAwayFromAbove(KDPoint anchor, KDPoint p, BigDecimal range, int coord){
        return (anchor.coords[coord].subtract(range).compareTo(p.coords[coord]) > 0);
    }

    /* While the second one does the converse thing. */
    private boolean anchorTooFarAwayFromBelow(KDPoint anchor, KDPoint p, BigDecimal range, int coord){
        return (anchor.coords[coord].add(range).compareTo(p.coords[coord]) <  0);
    }

    /* ***************************************************************************** */
    /* ******************* PUBLIC (INTERFACE) METHODS ****************************** */
    /* ***************************************************************************** */


    /**
     * 1-arg constructor. Stores the provided {@link KDPoint} inside the freshly created node.
     * @param p The {@link KDPoint} to store inside this. Just a reminder: {@link KDPoint}s are
     *          <b>mutable!!!</b>.
     */
    public KDTreeNode(KDPoint p){
        this.p = new KDPoint(p);
        height = 0;
    }

    /**
     * <p>Inserts the provided {@link KDPoint} in the tree rooted at this. To select which subtree to recurse to,
     * the KD-Tree acts as a Binary Search Tree on currDim; it will examine the value of the provided {@link KDPoint}
     * at currDim and determine whether it is larger than or equal to the contained {@link KDPoint}'s relevant dimension
     * value. If so, we recurse right, like a regular BST, otherwise left.</p>
     * @param currDim The current dimension to consider
     * @param dims The total number of dimensions that the space considers.
     * @param pIn The {@link KDPoint} to insert into the node.
     * @see #delete(KDPoint, int, int)
     */
    public  void insert(KDPoint pIn, int currDim, int dims){
        int nextDim = (currDim + 1) % dims;
        if(coordLargerOrEqual(pIn, p, currDim)) // Go right
            if(right == null)
                right = new KDTreeNode(pIn);
            else
                right.insert(pIn, nextDim, dims);
        else // Go left
            if(left == null)
                left = new KDTreeNode(pIn);
            else
                left.insert(pIn, nextDim, dims);
        height = max(height(left), height(right)) + 1;
    }

    /**
     * <p>Deletes the provided {@link KDPoint} from the tree rooted at this. To select which subtree to recurse to,
     * the KD-Tree acts as a Binary Search Tree on currDim; it will examine the value of the provided {@link KDPoint}
     * at currDim and determine whether it is larger than or equal to the contained {@link KDPoint}'s relevant dimension
     * value. If so, we recurse right, like a regular BST, otherwise left. There exist two special cases of deletion,
     * depending on whether we are deleting a {@link KDPoint} from a node who either:</p>
     *
     * <ul>
     *      <li>Has a NON-null subtree as a right child.</li>
     *      <li>Has a NULL subtree as a right child.</li>
     * </ul>
     *
     * <p>You should consult the class slides, your notes, and the textbook about what you need to do in those two
     * special cases.</p>
     * @param currDim The current dimension to consider.
     * @param dims The total number of dimensions that the space considers.
     * @param pIn The {@link KDPoint} to insert into the node.
     * @see #insert(KDPoint, int, int)
     * @return A reference to this after the deletion takes place.
     */
    public KDTreeNode delete(KDPoint pIn, int currDim, int dims){
        int nextDim = (currDim + 1) % dims;
        if(p.equals(pIn)){
            if(right != null) {
                p = right.findMinOfDim(currDim, nextDim, dims);
                right = right.delete(p, nextDim, dims);
            } else if (left != null){
                p = left.findMinOfDim(currDim, nextDim, dims);
                right = left.delete(p, nextDim, dims); // Special case delete.
                left = null;
            } else { // Leaf nodes
                return null;
            }
        } else if(coordLargerOrEqual(pIn, p, currDim) && right != null)
            right = right.delete(pIn, nextDim, dims);
        else if(left != null)
            left = left.delete(pIn, nextDim, dims);
        height = max(height(left), height(right)) + 1;
        return this;
    }

    /**
     * Searches the subtree rooted at the current node for the provided {@link KDPoint}.
     * @param pIn The {@link KDPoint} to search for.
     * @param currDim The current dimension considered.
     * @param dims The total number of dimensions considered.
     * @return true iff pIn was found in the subtree rooted at this, false otherwise.
     */
    public  boolean search(KDPoint pIn, int currDim, int dims){
        int nextDim = (currDim + 1) % dims;
        if(pIn.equals(p))
            return true;
        else
        if(coordLargerOrEqual(pIn, p, currDim) && right != null)
            return right.search(pIn, nextDim, dims);
        else if(left != null)
            return left.search(pIn, nextDim, dims);
        return false;
    }

    /**
     * <p>Executes a range query in the given {@link KDTreeNode}. Given an &quot;anchor&quot; {@link KDPoint},
     * all {@link KDPoint}s that have a {@link KDPoint#distanceSquared(KDPoint) distanceSquared} of <b>at most</b> range
     * <b>INCLUSIVE</b> from the anchor point <b>except</b> for the anchor itself should be inserted into the {@link Collection}
     * that is passed.</p>
     *
     * <p>Remember: range queries behave <em>greedily</em> as we go down (approaching the anchor as &quot;fast&quot;
     * as our currDim allows and <em>prune subtrees</em> that we <b>don't</b> have to visit as we backtrack. Consult
     * all of our resources if you need a reminder of how these should work.</p>
     *
     * <p>Finally, note that the range parameter is a Euclidean Distance, not the square of a Euclidean
     * Distance! </p>
     * @param anchor The centroid of the hypersphere that the range query implicitly creates.
     * @param results A {@link Collection} that accumulates all the {@link }
     * @param currDim The current dimension examined by the {@link KDTreeNode}.
     * @param dims The total number of dimensions of our {@link KDPoint}s.
     * @param range The <b>INCLUSIVE</b> range from the &quot;anchor&quot; {@link KDPoint}, within which all the
     *              {@link KDPoint}s that satisfy our query will fall. The distanceSquared metric used} is defined by
     *              {@link KDPoint#distanceSquared(KDPoint)}.
     */
    public void range(KDPoint anchor, Collection<KDPoint> results,
                      BigDecimal range, int currDim , int dims){
        int nextDim = (currDim + 1) % dims;
        // Search pruning step: Calculate distanceSquared between current and anchor
        // point. If you find it to be above range, compare the current coordinate
        // between the current point and the anchor point, and recurse to the appropriate
        // subtree such that you find responses faster.
        BigDecimal dist = rootOfBigDecimal(distanceSquared(anchor, p));
        if(dist.compareTo(range) >= 0){ // Either outside range or exactly within range.
            if(dist.equals(range)) // If exactly on rim of edge, add point.
                results.add(new KDPoint(p));
            // We structured this condition in a double nested if condition
            // because the following logic applies to both cases, i.e point
            // in rim or point outside rim.
            if(!anchorTooFarAwayFromAbove(p, anchor, range, currDim) && right != null)
                right.range(anchor, results,  range, nextDim, dims);
            if(!anchorTooFarAwayFromBelow(p, anchor, range, currDim) && left != null)
                left.range(anchor, results, range, nextDim, dims);
        } else { // Within range entirely.
            if(!anchor.equals(p)) // Recall that we don't want to report the anchor point itself.
                results.add(new KDPoint(p));
            // In this case, we are within the range in our entirety,
            // so we need to recurse to both chidren, provided they exist.
            if(left != null)
                left.range(anchor, results, range, nextDim, dims);
            if(right != null)
                right.range(anchor, results, range, nextDim, dims);
        }
    }


    /**
     * <p>Executes a nearest neighbor query, which returns the nearest neighbor, in terms of
     * {@link KDPoint#distanceSquared(KDPoint)}, from the &quot;anchor&quot; point.</p>
     *
     * <p>Recall that, in the descending phase, a NN query behaves <em>greedily</em>, approaching our
     * &quot;anchor&quot; point as fast as currDim allows. While doing so, it implicitly
     * <b>bounds</b> the acceptable solutions under the current <b>best solution</b>, which is passed as
     * an argument. This approach is known in Computer Science as &quot;branch-and-bound&quot; and it helps us solve an
     * otherwise exponential complexity problem (nearest neighbors) efficiently. Remember that when we want to determine
     * if we need to recurse to a different subtree, it is <b>necessary</b> to compare the distanceSquared reported by
     * {@link KDPoint#distanceSquared(KDPoint)} and coordinate differences! Those are comparable with each other because they
     * are the same data type ({@link Double}).</p>
     *
     * @return An object of type {@link NNData}, which exposes the pair (distance_of_NN_from_anchor, NN),
     * where NN is the nearest {@link KDPoint} to the anchor {@link KDPoint} that we found.
     *
     * @param anchor The &quot;ancor&quot; {@link KDPoint}of the nearest neighbor query.
     * @param currDim The current dimension considered.
     * @param dims The total number of dimensions considered.
     * @param n An object of type {@link NNData}, which will define a nearest neighbor as a pair (distance_of_NN_from_anchor, NN),
     *      * where NN is the nearest neighbor found.
     *
     * @see NNData
     * @see #kNearestNeighbors(int, KDPoint, BoundedPriorityQueue, int, int)
     */
    public  NNData<KDPoint> nearestNeighbor(KDPoint anchor, int currDim,
                                            NNData<KDPoint> n, int dims){

        int nextDim = (currDim +1)%dims;
        if(((distanceSquared(p, anchor).compareTo(n.bestDist) < 0 || n.bestDist.equals(INFTY) )&& !p.equals(anchor))){
            n.bestDist = distanceSquared(p, anchor);
            n.bestGuess = new KDPoint(p); // TODO: Could make a method called update() for NNData...
        }
        KDTreeNode picked = null, notPicked = null;
        if(p.coords[currDim].compareTo(anchor.coords[currDim]) <= 0 && right != null){
            picked = right;
            notPicked = left;
        }
        else if(left != null){
            picked = left;
            notPicked = right;
        }

        // Recurse to the subtree likeliest to give you a better answer first.

        if(picked != null)
            n = picked.nearestNeighbor(anchor, nextDim, n, dims);

        /* It is possible that you have to recurse to the other subtree, if
         * the hypersphere constructed by the anchor and radius intersects
         * the splitting plane defined by p and currDim. We check for this here.
         */

        if(( (p.coords[currDim].subtract(anchor.coords[currDim] ) ).abs().compareTo(rootOfBigDecimal(n.bestDist)) <= 0 )
                        && notPicked != null)
            n = notPicked.nearestNeighbor(anchor, nextDim, n, dims);

        return n;
    }

    /**
     * <p>Executes a nearest neighbor query, which returns the nearest neighbor, in terms of
     * {@link KDPoint#distanceSquared(KDPoint)}, from the &quot;anchor&quot; point.</p>
     *
     * <p>Recall that, in the descending phase, a NN query behaves <em>greedily</em>, approaching our
     * &quot;anchor&quot; point as fast as currDim allows. While doing so, it implicitly
     * <b>bounds</b> the acceptable solutions under the current <b>worst solution</b>, which is maintained as the
     * last element of the provided {@link BoundedPriorityQueue}. This is another instance of &quot;branch-and-bound&quot;
     * Remember that when we want to determine if we need to recurse to a different subtree, it is <b>necessary</b>
     * to compare the distanceSquared reported by* {@link KDPoint#distanceSquared(KDPoint)} and coordinate differences!
     * Those are comparable with each other because they are the same data type ({@link Double}).</p>
     *
     * <p>The main difference of the implementation of this method and the implementation of
     * {@link #nearestNeighbor(KDPoint, int, NNData, int)} is the necessity of using the class
     * {@link BoundedPriorityQueue} effectively. Consult your various resources
     * to understand how you should be using this class.</p>
     *
     * @param k The total number of neighbors to retrieve. It is better if this quantity is an odd number, to
     *          avoid ties in Binary Classification tasks.
     * @param anchor The &quot;anchor&quot; {@link KDPoint} of the nearest neighbor query.
     * @param currDim The current dimension considered.
     * @param dims The total number of dimensions considered.
     * @param queue A {@link BoundedPriorityQueue} that will maintain at most k nearest neighbors of
     *              the anchor point at all times, sorted by distanceSquared to the point.
     *
     * @see BoundedPriorityQueue
     */
    public  void kNearestNeighbors(int k, KDPoint anchor, BoundedPriorityQueue<KDPoint> queue, int currDim, int dims){

        // Invariant: queue != null

        int nextDim = (currDim +1)%dims;
        if(!p.equals(anchor))
            queue.enqueue(new KDPoint(p), distanceSquared(anchor, p)); // May change the queue, may not, depending on the distanceSquared.
        KDTreeNode picked = null, notPicked = null;
        if(p.coords[currDim].compareTo(anchor.coords[currDim]) <= 0){
            picked = right;
            notPicked = left;
        }
        else {
            picked = left;
            notPicked = right;
        }
        // Recurse to the subtree likeliest to give you a better answer first, if it exists.
        if(picked != null)
            picked.kNearestNeighbors(k, anchor, queue, nextDim, dims);

        /* It is possible that you have to recurse to the other subtree. You have to do this if:
         *	(a) We have fewer than k nearest neighbors stored in our queue OR
         *	(b) We have k nearest neighbors stored and the candidate circle defined by the anchor point
         *	and the MAXIMUM priority neighbor crosses the splitting plane.
         *
         * Naturally, in order to recurse to the other subtree, the other subtree has to exist.
         * We check for this also.
         */
        BigDecimal candidateCircleRadius = queue.isEmpty() ? BigDecimal.ZERO : distanceSquared(anchor, queue.last());
        if((queue.size() < k ||
                p.coords[currDim].subtract(anchor.coords[currDim] ).abs().compareTo(rootOfBigDecimal(candidateCircleRadius)) <= 0)
                        && notPicked != null)
            notPicked.kNearestNeighbors(k, anchor, queue, nextDim, dims);
    }

    /**
     * Returns the height of the subtree rooted at the current node. Recall our definition of height for binary trees:
     * <ol>
     *     <li>A null tree has a height of 0.</li>
     *     <li>A non-null tree has a height equal to max(height(left_subtree), height(right_subtree))</li>
     * </ol>
     * @return the height of the subtree rooted at the current node.
     */
    public int height(){
        return height;
    }

    private int height(KDTreeNode n){
        return (n == null) ? - 1 : n.height;
    }

    /**
     * A simple getter for the {@link KDPoint} held by the current node. Remember: {@link KDPoint}s ARE
     * MUTABLE, SO WE NEED TO DO DEEP COPIES!!!
     * @return The {@link KDPoint} held inside this.
     */
    public KDPoint getPoint(){
        return new KDPoint(p);
    }
}