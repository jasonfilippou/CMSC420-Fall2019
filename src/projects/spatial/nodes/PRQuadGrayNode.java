package projects.spatial.nodes;

import projects.spatial.kdpoint.KDPoint;
import projects.spatial.trees.PRQuadTree;

import java.math.BigDecimal;
import java.util.Collection;

/** <p>A {@link PRQuadGrayNode} is a gray (&quot;mixed&quot;) {@link PRQuadNode}. It
 * maintains the following invariants: </p>
 * <ul>
 *      <li>Its children pointer buffer is non-null and has a length of 4.</li>
 *      <li>If there is at least one black node child, the total number of {@link KDPoint}s stored
 *      by <b>all</b> of the children is greater than the bucketing parameter (because if it is equal to it
 *      or smaller, we can prune the node.</li>
 * </ul>
 *
 * <p><b>YOU ***** MUST ***** IMPLEMENT THIS CLASS!</b></p>
 *
 *  @author --- YOUR NAME HERE! ---
 */
public class PRQuadGrayNode extends PRQuadNode{

    private PRQuadNode[] children;

    private int height;

    private static final int NW = 0, NE= 1, SW = 2, SE = 3;


    /* ***************************************************************************** */
    /* ******************* PUBLIC (INTERFACE) METHODS ****************************** */
    /* ***************************************************************************** */


    /**
     * Creates a {@link PRQuadGrayNode}  with the provided {@link KDPoint} as a centroid;
     * @param centroid A {@link KDPoint} that will act as the centroid of the space spanned by the current
     *                 node.
     * @param k The See {@link PRQuadTree#PRQuadTree(int, int)} for more information on how this parameter works.
     * @param bucketingParam The bucketing parameter fed to this by {@link PRQuadTree}.
     * @see PRQuadTree#PRQuadTree(int, int)
     */
    public PRQuadGrayNode(KDPoint centroid, int k, int bucketingParam){
        super(centroid, k, bucketingParam); // Call to the super class' protected constructor to properly initialize the object!
        children = new PRQuadNode[4];
        height = 0; // Initially. Whenever a Gray Node is brought into existence, it is followed by calls to insert(), which adjust its height.
    }


    /**
     * <p>Insertion into a {@link PRQuadGrayNode} consists of navigating to the appropriate child
     * and recursively inserting elements into it. If the child is a white node, memory should be allocated for a
     * {@link PRQuadBlackNode} which will contain the provided {@link KDPoint} If it's a {@link PRQuadBlackNode},
     * refer to {@link PRQuadBlackNode#insert(KDPoint, int)} for details on how the insertion is performed. If it's a {@link PRQuadGrayNode},
     * the current method would be called recursively. Polymorphism will allow for the appropriate insert to be called
     * based on the child object's runtime object.</p>
     * @param p A {@link KDPoint} to insert into the subtree rooted at the current {@link PRQuadGrayNode}.
     * @param k The side length of the quadrant spanned by the <b>current</b> {@link PRQuadGrayNode}. It will need to be updated
     *          per recursive call to help guide the input {@link KDPoint}  to the appropriate subtree.
     * @return The subtree rooted at the current node, potentially adjusted after insertion.
     * @see PRQuadBlackNode#insert(KDPoint, int)
     */
    @Override
    public PRQuadNode insert(KDPoint p, int k) {
        int targetQuadrant = findTargetQuadrant(p, centroid);
        KDPoint targetQuadrantCentroid = computeTargetCentroid(targetQuadrant, centroid, k);
        if(children[targetQuadrant] == null)
            children[targetQuadrant] = new PRQuadBlackNode(targetQuadrantCentroid, k-1, bucketingParam, p);
        else
            children[targetQuadrant] = children[targetQuadrant].insert(p, k-1);
        height = findMaxHeight(children) + 1;
        return this;
    }

    private int findTargetQuadrant(KDPoint pIn, KDPoint centroid){
        assert pIn.coords.length == centroid.coords.length && centroid.coords.length ==  2 :
                "findTargetQuadrant(): Fed at least one KDPoint whose dimensionality is not 2.";
        if(pIn.coords[0].compareTo(centroid.coords[0]) < 0 && pIn.coords[1].compareTo(centroid.coords[1]) >= 0)
            return NW;
        else if(pIn.coords[0].compareTo(centroid.coords[0]) >= 0 && pIn.coords[1].compareTo(centroid.coords[1]) >= 0)
            return NE;
        else if(pIn.coords[0].compareTo(centroid.coords[0]) < 0 && pIn.coords[1].compareTo(centroid.coords[1]) < 0)
            return SW;
        else
            return SE;
    }

    private KDPoint computeTargetCentroid(int targetQuadrant, KDPoint currentCentroid, int currentK){
        switch(targetQuadrant){
            case NW:
                return northWestCentroid(currentCentroid, currentK);
            case NE:
                return northEastCentroid(currentCentroid, currentK);
            case SW:
                return southWestCentroid(currentCentroid, currentK);
            case SE:
                return southEastCentroid(currentCentroid, currentK);
            default:
                throw new IllegalArgumentException("computeTargetCentroid(): Illegal targetQuadrant parameter=" + targetQuadrant + " provided.");
        }
    }

    private KDPoint northWestCentroid(KDPoint currentCentroid, int currentK){
        BigDecimal x = currentCentroid.coords[0], y = currentCentroid.coords[1]; // those are effectively ints for centroids
        BigDecimal newSideLength = new BigDecimal(Math.pow(2, currentK - 2)); // -2 instead of -1 because the offset is a quarter of the previous space!
        return new KDPoint(x.subtract(newSideLength), y.add(newSideLength));
    }

    private KDPoint northEastCentroid(KDPoint currentCentroid, int currentK){
        BigDecimal x = currentCentroid.coords[0], y = currentCentroid.coords[1]; // those are effectively ints for centroids
        BigDecimal newSideLength = new BigDecimal(Math.pow(2, currentK - 2)); // See above
        return new KDPoint(x.add(newSideLength), y.add(newSideLength));
    }


    private KDPoint southWestCentroid(KDPoint currentCentroid, int currentK){
        BigDecimal x = currentCentroid.coords[0], y = currentCentroid.coords[1]; // those are effectively ints for centroids
        BigDecimal newSideLength = new BigDecimal(Math.pow(2, currentK - 2));
        return new KDPoint(x.subtract(newSideLength), y.add(newSideLength));
    }


    private KDPoint southEastCentroid(KDPoint currentCentroid, int currentK){
        BigDecimal x = currentCentroid.coords[0], y = currentCentroid.coords[1]; // those are effectively ints for centroids
        BigDecimal newSideLength = new BigDecimal(Math.pow(2, currentK - 2));
        return new KDPoint(x.add(newSideLength), y.subtract(newSideLength));
    }

    /**
     * <p>Deleting a {@link KDPoint} from a {@link PRQuadGrayNode} consists of recursing to the appropriate
     * {@link PRQuadBlackNode} child to find the provided {@link KDPoint}. If no such child exists, the search has
     * <b>necessarily failed</b>; <b>no changes should then be made to the subtree rooted at the current node!</b></p>
     *
     * <p>Polymorphism will allow for the recursive call to be made into the appropriate delete method.
     * Importantly, after the recursive deletion call, it needs to be determined if the current {@link PRQuadGrayNode}
     * needs to be collapsed into a {@link PRQuadBlackNode}. This can only happen if it has no gray children, and one of the
     * following two conditions are satisfied:</p>
     *
     * <ol>
     *     <li>The deletion left it with a single black child. Then, there is no reason to further subdivide the quadrant,
     *     and we can replace this with a {@link PRQuadBlackNode} that contains the {@link KDPoint}s that the single
     *     black child contains.</li>
     *     <li>After the deletion, the <b>total</b> number of {@link KDPoint}s contained by <b>all</b> the black children
     *     is <b>equal to or smaller than</b> the bucketing parameter. We can then similarly replace this with a
     *     {@link PRQuadBlackNode} over the {@link KDPoint}s contained by the black children.</li>
     *  </ol>
     *
     * @param p A {@link KDPoint} to delete from the tree rooted at the current node.
     * @return The subtree rooted at the current node, potentially adjusted after deletion.
     */
    @Override
    public PRQuadNode delete(KDPoint p) {
        int targetQuadrant = findTargetQuadrant(p, centroid);
        if(children[targetQuadrant] != null){
            children[targetQuadrant] = children[targetQuadrant].delete(p);
            if(mustCollapse()) {
                PRQuadBlackNode newBlackNode = new PRQuadBlackNode(centroid, k, bucketingParam);
                for (int i = 0; i < 4; i++)
                    if (children[i] != null) // black nodes
                        ((PRQuadBlackNode) children[i]).getPoints().forEach(pt ->
                            { if(pt!= null) newBlackNode.insert(pt, k); }); // Safe down-casting by semantics
                return newBlackNode;
            }
        }
        // If execution reaches this line, it means that either the point was not in the tree,
        // or it was deleted with no collapsing of the current gray node. In either case, we
        // update the height and return our current object.
        height = findMaxHeight(children) + 1;
        return this;
    }

    private boolean mustCollapse(){
        assert children != null && children.length == 4 : "mustCollapse(): the current gray node should have 4 children pointers.";

        // Constraint #1: All children should be non-gray nodes
        for(int i = 0; i < children.length; i++)
            if(height(children[i]) > 0) // gray node child; should not collapse parent
                return false;

        // Constraint #2: The sum of points of all black nodes should be at most equal to bucketing_param
        int pointsStoredInChildren = 0;
        for(int i = 0; i < children.length; i++){
            if(children[i] != null) // black node
                pointsStoredInChildren += sumNonNull(((PRQuadBlackNode)children[i]).getPoints());
        }

        return (pointsStoredInChildren <= bucketingParam);
    }

    private int sumNonNull(Collection<KDPoint> coll){
        int cnt = 0;
        Object[] objectArr = coll.toArray();
        for(int i = 0; i < coll.size(); i++)
            if(objectArr[i] != null)
                cnt++;
        return cnt;
    }

    @Override
    public boolean search(KDPoint p){
        int targetQuadrant = findTargetQuadrant(p, centroid);
        if(children[targetQuadrant] != null) // At least one of the chilren is guaranteed to be non-null
            return children[targetQuadrant].search(p);
        else
            return false;
    }

    @Override
    public int height(){
        return height;
    }

    private int height(PRQuadNode n){
        return n == null ? - 1 : n.height();
    }

    private int findMaxHeight(PRQuadNode[] nodes){
        assert nodes != null && nodes.length == 4 : "finMaxHeight(): We were provided with an invalid nodes array.";
        int maxHeight = height(nodes[0]);
        for(int i = 1; i < 4; i++)
            if(height(nodes[i]) > maxHeight)
                maxHeight = height(nodes[i]);
        return maxHeight;
    }

    @Override
    public int count(){
        int totalCount = 0;
        for(int i = 0; i < children.length; i++)
            if(children[i] != null)
                totalCount += children[i].count();
        return totalCount;
    }
}

