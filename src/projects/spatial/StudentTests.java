package projects.spatial;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import projects.spatial.kdpoint.KDPoint;
import projects.spatial.trees.KDTree;
import projects.spatial.trees.PRQuadTree;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.Collection;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * <p>A testing framework for {@link projects.spatial.trees.KDTree} and {@link projects.spatial.trees.PRQuadTree}</p>.
 * You should extend it with your own tests.
 *
 * @author  --- YOUR NAME HERE! ---
 *
 */

public class StudentTests {


    /* Private fields */

    private PRQuadTree prQuadTree;
    private KDTree kdTree;
    private int MAX_DIM = 10;
    private static final long SEED=47;
    private double EPSILON = Math.pow(10, -8);
    private int SCALE=10;
    private Random r;
    private static final int MAX_ITER = 200;



    /* Private utilities */

    private int getRandomSign(){
        return 2 * r.nextInt(2) - 1;
    }

    private double[] getRandomDoubleCoords(int num){
        double[] coords = new double[num];
        for(int i = 0; i < num; i++)
            coords[i] = getRandomSign() * SCALE * r.nextDouble();
        return coords;
    }

    private KDPoint getRandomPoint(int dim){
        return new KDPoint(getRandomDoubleCoords(dim));
    }

    private boolean checkRangeQuery(KDTree tree, KDPoint origin, BigDecimal range, KDPoint... candidates){
        Collection<KDPoint> rangeQueryResults = tree.range(origin, range);
        List<KDPoint> candidateList = Arrays.asList(candidates);
        return rangeQueryResults.containsAll(candidateList); // Order not important in range queries: only containment.
    }

    private BigDecimal rootOfBigDecimal(BigDecimal bd){
        return new BigDecimal(Math.sqrt(bd.doubleValue()));
    }


    /* Setup and teardown methods; those are run before and after every jUnit test. */


    @Before
    public void setUp(){
        r = new Random(SEED);
        prQuadTree = new PRQuadTree(r.nextInt(), r.nextInt());
    }

    @After
    public void tearDown(){
        r = null;
        kdTree = null;
        prQuadTree = null;
        System.gc();
    }


    /* BPQ Tests.... */



    /* KD-Tree Tests.... */


    @Test
    public void testKDTreeIsEmpty(){
        kdTree = new KDTree(10);
        assertTrue("A freshly created KD-Tree should be empty!", kdTree.isEmpty());
    }

    @Test
    public void testKDTreeFewInsertions(){
        kdTree = new KDTree(2);
        kdTree.insert(new KDPoint(10, 30));
        kdTree.insert(new KDPoint(12, 18));
        kdTree.insert(new KDPoint(-20, 300));

        assertEquals("The first point inserted should be our root.", new KDPoint(10, 30), kdTree.getRoot());
        assertEquals("The height of this KD-Tree should be 1.", 1, kdTree.height());
        assertEquals("The number of nodes in this tree should be 3.", 3, kdTree.count());
    }

    @Test
    public void testKDTreeSimpleRange(){
        for(int dim = 1; dim <= MAX_DIM; dim++){ // For MAX_DIM-many trees...
            for(int i = 0; i < MAX_ITER; i++){ // For MAX_ITER-many points...
                KDPoint originInDim = new KDPoint(dim);
                KDTree tree = new KDTree(dim);
                KDPoint p = getRandomPoint(dim);
                tree.insert(p);
                assertTrue("Failed a range query for a " + dim + "-D tree which only contained " +
                        p + ", KDPoint #" + i + ".", checkRangeQuery(tree, originInDim, rootOfBigDecimal(p.distanceSquared(originInDim))));
            }
        }
    }


    /* PRQ Tests .... */


    @Test
    public void testEmptyPRQuadTree(){
        assertNotNull("Tree reference should be non-null by setUp() method.", prQuadTree);
        assertTrue("A freshly created PR-QuadTree should be empty!", prQuadTree.isEmpty());
    }


    @Test
    public void testSimpleQuadTree(){
        prQuadTree = new PRQuadTree(4, 2); // Space from (-8, -8) to (8, 8), bucketing parameter = 2.
        prQuadTree.insert(new KDPoint(1, 1));
        prQuadTree.insert(new KDPoint(4, 2)); // Should fit
        assertEquals("After two insertions into a PR-QuadTree with b = 2, the result should be a quadtree consisting of a single black node.",
            0, prQuadTree.height());
        assertEquals("After two insertions into a PR-QuadTree, the count should be 2.", 2, prQuadTree.count());

        // The following deletion should work just fine...

        try {
            prQuadTree.delete(new KDPoint(1, 1));
        } catch(Throwable t){
            fail("Caught a " + t.getClass().getSimpleName() + " with message: " + t.getMessage() + " when attempting to delete a KDPoint that *should*" +
                    " be in the PR-QuadTree.");
        }

        assertFalse("After deleting a point from a PR-QuadTree, we should no longer be finding it in the tree.",
                prQuadTree.search(new KDPoint(1, 1)));
        // The following two insertions should split the root node into a gray node with 2 black node children and 2 white node children.

        prQuadTree.insert(new KDPoint(-5, -6));
        prQuadTree.insert(new KDPoint(0, 0)); // (0, 0) should go to the NE quadrant after splitting.
        assertEquals("After inserting three points into a PR-QuadTree with b = 2, the tree should split into a gray node with 4 children.",
                prQuadTree.height(), 1);
        for(KDPoint p: new KDPoint[]{new KDPoint(0, 0), new KDPoint(4, 2), new KDPoint(-5, -6)})
            assertTrue("After inserting a point into a PR-QuadTree without subsequently deleting it, we should be able to find it.", prQuadTree.search(p));

    }


}

