package projects.spatial.nodes;

import projects.spatial.kdpoint.KDPoint;
import projects.spatial.trees.PRQuadTree;

import java.util.Arrays;
import java.util.Collection;


/** <p>A {@link PRQuadBlackNode} is a &quot;black&quot; {@link PRQuadNode}. It maintains the following
 * invariants: </p>
 * <ul>
 *  <li>It does <b>not</b> have children.</li>
 *  <li><b>Once created</b>, it will contain at least one {@link KDPoint}. </li>
 * </ul>
 *
 * <p><b>YOU ***** MUST ***** IMPLEMENT THIS CLASS!</b></p>
 *
 * @author --- YOUR NAME HERE! ---
 */
public class PRQuadBlackNode extends PRQuadNode {

    /**
     * The default bucket size for all of our black nodes will be 1, and this is something
     * that the interface also communicates to consumers.
     */
    public static final int DEFAULT_BUCKETSIZE = 1;

    /* Private stuff */


    private KDPoint[] points;

    private int pointsStored;

    /* Public stuff */


    /**
     * Creates a {@link PRQuadBlackNode} with the provided parameters.
     * @param centroid The {@link KDPoint} which will act as the centroid of the quadrant spanned by the current {@link PRQuadBlackNode}.
     * @param k An integer to which 2 is raised to define the side length of the quadrant spanned by the current {@link PRQuadBlackNode}.
     *          See {@link PRQuadTree#PRQuadTree(int, int)} for a full explanation of how k works.
     * @param bucketingParam The bucketing parameter provided to us {@link PRQuadTree}.
     * @see PRQuadTree#PRQuadTree(int, int)
     * @see #PRQuadBlackNode(KDPoint, int, int, KDPoint)
     */
    public PRQuadBlackNode(KDPoint centroid, int k, int bucketingParam){
        super(centroid, k, bucketingParam); // Call to the super class' protected constructor to properly initialize the object!
        assert bucketingParam >= 1: "Bucketing parameter should be at least 1";
        points = new KDPoint[bucketingParam]; // Invariant: bucketingParam >= 1
        pointsStored = 0;
    }

    /**
     * Creates a {@link PRQuadBlackNode} with the provided parameters.
     * @param centroid The centroid of the quadrant spanned by the current {@link PRQuadBlackNode}.
     * @param k The exponent to which 2 is raised in order to define the side of the current quadrant. Refer to {@link PRQuadTree#PRQuadTree(int, int)} for
     *          a thorough explanation of this parameter.
     * @param bucketingParam The bucketing parameter of the {@link PRQuadBlackNode}, passed to us by the {@link PRQuadTree} or {@link PRQuadGrayNode} during
     *                       object construction.
     * @param p The {@link KDPoint} with which we want to initialize this.
     * @see #DEFAULT_BUCKETSIZE
     * @see PRQuadTree#PRQuadTree(int, int)
     * @see #PRQuadBlackNode(KDPoint, int, int)
     */
    public PRQuadBlackNode(KDPoint centroid, int k, int bucketingParam, KDPoint p){
        this(centroid, k, bucketingParam); // Call to the current class' other constructor, which takes care of the base class' initialization itself.
        assert bucketingParam >= 1: "Bucketing parameter should be at least 1";
        points[0] = new KDPoint(p);
        pointsStored = 1;
    }


    /**
     * <p>Inserting a {@link KDPoint} into a {@link PRQuadBlackNode} can have one of two outcomes:</p>
     *
     * <ol>
     *     <li>If, after the insertion, the node's capacity is still <b>SMALLER THAN OR EQUAL TO </b> the bucketing parameter,
     *     we should simply store the {@link KDPoint} internally.</li>
     *
     *     <li>If, after the insertion, the node's capacity <b>SURPASSES</b> the bucketing parameter, we will have to
     *     <b>SPLIT</b> the current {@link PRQuadBlackNode} into a {@link PRQuadGrayNode} which will recursively insert
     *     all the available{@link KDPoint}s. This pprocess will continue until we reach a {@link PRQuadGrayNode}
     *     which successfully separates all the {@link KDPoint}s of the quadrant it represents. Programmatically speaking,
     *     this means that the method will polymorphically call itself, splitting black nodes into gray nodes as long as
     *     is required for there to be a set of 4 quadrants that separate the points between them. This is one of the major
     *     bottlenecks in PR-QuadTrees; the presence of a pair of {@link KDPoint}s with a very small {@link
     *     KDPoint#distanceSquared(KDPoint) distanceSquared} between them can negatively impact search in certain subplanes, because
     *     the subtrees through which those subplanes will be modeled will be &quot;unnecessarily&quot; tall.</li>
     * </ol>
     *
     * @param p A {@link KDPoint} to insert into the subtree rooted at the current node.
     * @param k The side length of the quadrant spanned by the <b>current</b> {@link PRQuadGrayNode}. It will need to be updated
     *           per recursive call to help guide the input {@link KDPoint} to the appropriate subtree.
     * @return The subtree rooted at the current node, potentially adjusted after insertion.
     */
    @Override
    public PRQuadNode insert(KDPoint p, int k) {
        if(pointsStored == bucketingParam) {// Gotta split
            PRQuadGrayNode newSplitter = new PRQuadGrayNode(centroid, k, bucketingParam);
            Arrays.asList(points).forEach(pt->newSplitter.insert(pt, k)); // Re-insert all points into the new gray node
            newSplitter.insert(p, k);
            return newSplitter;
        } else {
            points[pointsStored++] = new KDPoint(p);
            return this;
        }
    }


    /**
     * <p><b>Successfully</b> deleting a {@link KDPoint} from a {@link PRQuadBlackNode} always decrements its capacity by 1. If, after
     * deletion, the capacity is at least 1, then no further changes need to be made to the node. Otherwise, it can
     * be scrapped and turned into a white node.</p>
     *
     * <p>If the provided {@link KDPoint} is <b>not</b> contained by this, no changes should be made to the internal
     * structure of this, which should be returned as is.</p>
     * @param p The {@link KDPoint} to delete from this.
     * @return Either this or null, depending on whether the node underflows.
     */
    @Override
    public PRQuadNode delete(KDPoint p) {
        if(pointsStored == 1 && points[0].equals(p)) {
            return null; // White node
        } else if(pointsStored > 1){
            int ptIndex = indexOfElement(p, points);
            if(ptIndex != -1) {
                points = (KDPoint[]) delAndShift(ptIndex, points); // Safe downcasting
                pointsStored --;
            }
        }
        return this;
    }

    private int indexOfElement(KDPoint o, KDPoint[] arr) {
        assert arr != null : "indexOfElement(): null array provided.";
        for(int i = 0; i < arr.length && arr[i] != null; i++)
            if(arr[i].equals(o))
                return i;
        return -1;
    }

    private KDPoint[] delAndShift(int index, KDPoint[] arr){
        assert arr != null && arr.length >= 1: "Null or zero-length array provided.";
        assert index >=0 && index < arr.length : "delAndShift(): bad index value " + index + " provided.";
        KDPoint[] retArr = new KDPoint[arr.length];
        for(int i = 0; i < index; i++)
            retArr[i] = new KDPoint(arr[i]);
        for(int i = index + 1; i < arr.length; i++)
            retArr[i - 1] = new KDPoint(arr[i]); // Excluding arr[index]
        /*for(int i = 0; i < arr.length - 1; i++) {
            if (i < index)
                retArr[i] = new KDPoint(arr[i]);
            else
                retArr[i] = new KDPoint(arr[i+1]);
        }*/
        return retArr;
    }

    @Override
    public boolean search(KDPoint p){
        for(int i = 0; i < pointsStored; i++)
            if(points[i].equals(p))
                return true;
        return false;
    }

    @Override
    public int height(){
        return 0;
    }

    @Override
    public int count(){
        return pointsStored; // Erase this after you implement the method!
    }

    /** Returns all the {@link KDPoint}s contained by the {@link PRQuadBlackNode}. <b>INVARIANT</b>: the returned
     * {@link Collection}'s size can only be between 1 and bucket-size inclusive.
     *
     * @return A {@link Collection} that contains all the {@link KDPoint}s that are contained by the node. It is
     * guaranteed, by the invariants, that the {@link Collection} will not be empty, and it will also <b>not</b> be
     * a null reference.
     */
    public Collection<KDPoint> getPoints(){
        return Arrays.asList(points);
    }
}
