package projects.spatial.trees;

import projects.spatial.kdpoint.KDPoint;
import projects.spatial.knnutils.BoundedPriorityQueue;
import projects.spatial.knnutils.NNData;
import projects.spatial.nodes.KDTreeNode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * <p>{@link KDTree} implements <em>K</em>-D Trees, where  <em>K</em> is a positive integer
 * that defines the dimensionality of the space. By default, <em>k=2</em>.</p>
 *
 * <p>A <em>K</em>-D tree supports standard insertion, deletion and search routines, and additionally allows for
 * <b>range</b> and <b>nearest-neighbor</b> queries.</p>
 *
 * <p>KD-Trees alternate dimensions with every increasing level. At any given level,
 * a KD-Tree acts as a Binary Search Tree over the relevant dimension. Refer to the course
 * slides and the textbook for exact algorithms, with code samples, of insertion, deletion and range / kNN
 * queries.</p>
 *
 * <p><b>YOU SHOULD ***NOT*** EDIT THIS CLASS!</b> If you do, you risk <b>not passing our tests!</b> The
 * entire functionality will be implemented in {@link KDTreeNode}.</p>
 *
 * @author  <a href ="https://github.com/JasonFil">Jason Filippou</a>
 *
 * @see SpatialDictionary
 * @see SpatialQuerySolver
 * @see KDTreeNode
 * @see PRQuadTree
 */
public class KDTree implements SpatialDictionary, SpatialQuerySolver {

	/* *********************************************************************/
	/* *********** SOME STATIC CONSTANTS WE AGREE TO SHARE WITH CLIENTS ***/
	/* *********************************************************************/
	/**
	 * We define the default dimensionality for a KD-Tree to be 2. An application might want to use it,
	 * so we provide it as part of the contract.
	 */
	public static final int DEFAULT_DIMS = 2;

	/**
	 * Encoding infinity with a negative number is safer than {@link Double#MAX_VALUE} for our purposes,
	 * and allows for faster comparisons as well. An application may use it as given.
	 */
	public static final BigDecimal INFTY = new BigDecimal(-1);

	/* ************************************************************************** */
	/* ************************* PRIVATE FIELDS ********************************* */
	/* *********  JAVADOC STILL GENERATABLE, FOR EDUCATIONAL PURPOSES ************* */
	/* *********  GENERATION OF JAVADOC FOR PROTECTED & PRIVATE MEMBERS ************* */
	/* *********  CAN BE TOGGLED THROUGH FLAGS TO THE SHELL PROGRAM ************* */
	/* ***************************	  javadoc   ******************************** */
	/* *********  OR THROUGH THE VISUAL INTERFACE OF ANY GIVEN IDE.************* */
	/* ************************************************************************** */

	/**
	 * The root of the <em>k</em>-d tree.
	 * @see KDTreeNode
	 */
	private KDTreeNode root;

	/**
	 * The dimensionality of the space considered.
	 * @see KDTreeNode
	 */
	private int dims;

	/**
	 * The total number of {@link KDPoint}s held by the container.
	 */
	private int count;


	/* *********************************************************************************************** */
	/* *************************** PUBLIC METHOD IMPLEMENTATION ************************************* */
	/* ********************* ACTUALLY DEFERS ALL THE WORK TO CLASS KDTreeNode, AFTER **************** */
	/* ***************************** SOME ELEMENTARY SANITY CHECKS. ********************************** */
	/* *********************************************************************************************** */
	/**
	 * Default constructor constructs this with <em>k=2</em>.
	 * @see #DEFAULT_DIMS
	 */
	public KDTree(){
		this(DEFAULT_DIMS);
	}

	/**
	 * This constructor requires that the user provide the value for <em>k</em>.
	 * @param k The dimensionality of this.
	 * @throws RuntimeException if k&lt;=0.
	 */
	public KDTree(int k){
		if(k <= 0)
			throw new RuntimeException("The value of k provided, " + k + ", is invalid: Please provide a positive integer.");
		dims = k;
		root = null;
		count = 0;
	}

	@Override
	public void insert(KDPoint p){
		if(root == null)
			root = new KDTreeNode(p);
		else
			root.insert(p, 0, dims);
		count++;
	}

	@Override
	public void delete(KDPoint p){
		if (root != null) {
			if(search(p)) {
				root = root.delete(p, 0, dims);
				count--; // Guaranteed successful deletion.
			}
		}
	}

	@Override
	public boolean search(KDPoint p){
		return (root != null) && root.search(p, 0, dims);
	}


	@Override
	public Collection<KDPoint> range(KDPoint p, BigDecimal range){
		LinkedList<KDPoint> pts = new LinkedList<>();
		if(root == null)
			return pts; // empty
		else
			root.range(p, pts, range, 0, dims);
		return pts;
	}

	@Override
	public KDPoint nearestNeighbor(KDPoint p){
		NNData<KDPoint> n = new NNData<KDPoint>(null, INFTY);
		if(root != null)
			n = root.nearestNeighbor(p, 0, n, dims);
		return n.bestGuess;
	}

	@Override
	public BoundedPriorityQueue<KDPoint> kNearestNeighbors(int k, KDPoint p){
		if(k <= 0)
			throw new RuntimeException("The value of k provided, " + k + ", is invalid: Please provide a positive integer.");
		BoundedPriorityQueue<KDPoint> queue = new BoundedPriorityQueue<KDPoint>(k);
		if(root != null)
			root.kNearestNeighbors(k, p, queue, 0, dims);
		return queue; // Might be empty; that's not a problem.
	}
	@Override
	public int height(){
		return root == null ? -1 : root.height();
	}

	@Override
	public boolean isEmpty(){
		return height() == -1;
	}

	@Override
	public int count(){
		return count;
	}

	/**
	 * Returns the {@link KDPoint} located at the <b>root</b>of the KDTree.
	 * Only non-interface method! Added primarily for debugging purposes.
	 * @return The {@link KDPoint} located at the root of the tree, or null
	 * if the tree is empty.
	 */
	public KDPoint getRoot(){
		return root == null ? null : new KDPoint(root.getPoint());
	}

	/**
	 * A simple tree description generator for VizTree/CompactVizTree. It returns a string representation for the KD-Tree.
	 * This tree representation follows jimblackler style(http://jimblackler.net/treefun/index.html).
	 * To identify child-index (left/right or NW,NE,SW,SE), I use "*" as special character to indicate null leaves.
	 * DO NOT EDIT!
	 * @param verbose whether to print the tree description to stdout or not
	 * @return An {@link ArrayList} that gives a string-fied representation of the KD-Tree.
	 */
	public ArrayList<String> treeDescription(boolean verbose)
	{
		ArrayList<String> tree = new ArrayList<String>();
		treeDescription(root,"",tree,verbose);
		return tree;
	}
	/**
	 * Private <b>recursive</b> help for treeDescription. DO NOT EDIT!
	 * @param root the current subtree root
	 * @param space tracks parent-child relationship
	 * @param tree Arraylist containing the tree description
	 * @param verbose whether to print the tree description to stdout or not
	 */
	private void treeDescription(KDTreeNode root,String space,ArrayList<String> tree,boolean verbose)
	{
		if(root== null || root.getPoint() == null)
		{

			tree.add(space+"*");
			return;
		}
		if (verbose)
			System.out.println(space+root.getPoint().compactToString());

		tree.add(space+root.getPoint().compactToString());
		treeDescription(root.getLeft(), space+" ",tree,verbose);
		treeDescription(root.getRight(), space+" ",tree,verbose);
	}
}
