package projects.spatial;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import projects.spatial.kdpoint.KDPoint;
import projects.spatial.knnutils.BoundedPriorityQueue;
import projects.spatial.trees.KDTree;
import projects.spatial.trees.PRQuadTree;
import projects.visualization.CompactVizTree;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;
import static projects.spatial.kdpoint.KDPoint.*;

/**
 * <p>A testing framework for {@link projects.spatial.trees.KDTree} and {@link projects.spatial.trees.PRQuadTree}</p>.
 * You should extend it with your own tests.
 *
 * @author  --- YOUR NAME HERE! ---
 *
 */

public class StudentTests {


    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */
    /* *********************************** PRIVATE FIELDS  AND METHODS **************************************** */
    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */

    private PRQuadTree prQuadTree;
    private KDTree kdTree;
    private static final long SEED=47;
    private Random r;
    private static final int MAX_ITER = 200;


    private int getRandomSign(){
        return 2 * r.nextInt(2) - 1;
    }

    private double[] getRandomDoubleCoords(int dim){
        double[] coords = new double[dim];
        int SCALE = 10;
        for(int i = 0; i < dim; i++)
            coords[i] = getRandomSign() * SCALE * r.nextDouble();
        return coords;
    }

    private KDPoint getRandomPoint(int dim){
        return new KDPoint(getRandomDoubleCoords(dim)); // This will trigger KDPoint(double[]...) constructor
    }

    private KDPoint getRandomIntegerCoordPoint(int dim){
        return new KDPoint(getRandomIntegerCoords(dim));    // Will trigger KDPoint(BigDecimal[] ...) constructor
    }


    private BigDecimal[] getRandomIntegerCoords(int dim){
        BigDecimal[] coords = new BigDecimal[dim];
        for(int i = 0; i < coords.length; i++)
            coords[i] = new BigDecimal(r.nextInt(1000)); // Or 10,000, 100,000, whatever you want. Make it a constant if you must.
        return coords; // So this will be an Array of ostensibly BigDecimals, only the actual numbers are ints, drawn from U[0,999]
    }

    private boolean checkRangeQuery(KDTree tree, KDPoint origin, BigDecimal range, KDPoint... candidates){
        Collection<KDPoint> rangeQueryResults = tree.range(origin, range);
        List<KDPoint> candidateList = Arrays.asList(candidates);
        return rangeQueryResults.containsAll(candidateList); // Order not important in range queries: only containment.
    }

    private BigDecimal rootOfBigDecimal(BigDecimal bd){
        return BigDecimal.valueOf(Math.sqrt(bd.doubleValue()));
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


    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */
    /* ***************************************** BPQ Tests ************************************************* */
    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */


    @Test
    public void testBPQBasicEnqueueDequeueFirstAndLast(){
        BoundedPriorityQueue<KDPoint> myQueue = new BoundedPriorityQueue<>(1);
        myQueue.enqueue(ZERO, new BigDecimal(2.3));
        assertEquals("After enqueueing a single KDPoint in a BPQ instance with a capacity of 1, a call to first() did not return " +
                "the point itself.", ZERO, myQueue.first());
        assertEquals("After enqueueing a single KDPoint in a BPQ instance with a capacity of 1, a call to last() did not return " +
                "the point itself.", ZERO, myQueue.last());
        assertEquals("After enqueueing a single KDPoint in a BPQ instance with a capacity of 1, a call to dequeue() did not return " +
                "the point itself.", ZERO, myQueue.dequeue());
    }

    @Test
    public void testBPQComplexEnqueueDequeueFirstAndLast() {
        BoundedPriorityQueue<KDPoint> myQueue = new BoundedPriorityQueue<>(3);
        myQueue.enqueue(ZERO, new BigDecimal(2.3));
        myQueue.enqueue(ONEONE, new BigDecimal(1.1));
        assertEquals("After enqueueing two KDPoints in a BPQ instance with a capacity of 3, a call to first() did not return " +
                "the expected point.", ONEONE, myQueue.first());
        assertEquals("After enqueueing two KDPoints in a BPQ instance with a capacity of 3, a call to last() did not return " +
                "the expected point.", ZERO, myQueue.last());
        assertEquals("After enqueueing two KDPoints in a BPQ instance with a capacity of 3, a call to dequeu() did not return " +
                "the expected point.", ONEONE, myQueue.dequeue());
        myQueue.enqueue(MINUSONEMINUSONE, new BigDecimal(4));
        assertEquals("After enqueueing two KDPoints in a BPQ instance with a capacity of 3, dequeuing one and enqueuing another, " +
                "a call to last() did not return the expected point.", MINUSONEMINUSONE, myQueue.last());

    }

    @Test
    public void testBPQEnqueuePastCapacity(){
        BoundedPriorityQueue<KDPoint> myQueue = new BoundedPriorityQueue<>(5);
        myQueue.enqueue(ZERO, BigDecimal.ONE);
        myQueue.enqueue(ONEONE, new BigDecimal(2));
        myQueue.enqueue(ONEMINUSONE, new BigDecimal(3));
        myQueue.enqueue(MINUSONEONE, new BigDecimal(4));
        myQueue.enqueue(ZEROMINUSONE, new BigDecimal(5));
        myQueue.enqueue(ONEZERO, new BigDecimal(5));   // FIFO should keep this one away
        assertEquals("After enqueuing six elements in a BPQ with initial capacity 5, a call to last() did not return " +
                "the expected element.", ZEROMINUSONE, myQueue.last());
        myQueue.enqueue(ONEZERO, new BigDecimal(0.5));   // The BPQ's sorting should put this first.
        myQueue.enqueue(ZEROONE, new BigDecimal(1.5));  // And this third.
        assertEquals("After enqueuing eight elements in a BPQ with initial capacity 5, we would expect its size to still be" +
                "5.", 5, myQueue.size());
        assertEquals("After enqueuing eight elements in a BPQ with initial capacity 5, a call to dequeue() did not return " +
                "the expected element.", ONEZERO, myQueue.dequeue()); // Two previous last ones must have been thrown out.
        assertEquals("After enqueuing eight elements in a BPQ with initial capacity 5 and one dequeueing, our second call to dequeue() did not return " +
                "the expected element.", ZERO, myQueue.dequeue());
        assertEquals("After enqueuing eight elements in a BPQ with initial capacity 5 and two dequeueings, our third call to dequeue() did not return " +
                "the expected element.", ZEROONE, myQueue.dequeue());
    }


    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */
    /* ***************************************** KD-TREE TESTS ************************************************* */
    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */
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
        int MAX_DIM = 10;
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


    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */
    /* ***************************************** PR-QUADTREE TESTS ******************************************** */
    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */
    @Test
    public void testPRQEmptyPRQuadTree(){
        assertNotNull("Tree reference should be non-null by setUp() method.", prQuadTree);
        assertTrue("A freshly created PR-QuadTree should be empty!", prQuadTree.isEmpty());
    }


    @Test
    public void testPRQSimpleQuadTree(){
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




    @Test
    public void testKNNPRQuadTree(){

        int k = 4;
        int kNN = 3;
        int bucketingParam = 2;
        prQuadTree = new PRQuadTree(k, bucketingParam); // Space from (-8, -8) to (8, 8), bucketing parameter = 2.
        KDPoint[] points = {new KDPoint(1,1),new KDPoint(2,2),new KDPoint(3,3),
                new KDPoint(-2,2),new KDPoint(-1,2),new KDPoint(-2,6),
        };
        for(int i=1;i<points.length;i++)
            prQuadTree.insert(points[i]);




        KDPoint queryPt = new KDPoint(-1,4);


        BoundedPriorityQueue<KDPoint> expectedKnnPoints = new BoundedPriorityQueue<KDPoint>(kNN+1);
        expectedKnnPoints.enqueue(new KDPoint(-1.0, 2.0),new BigDecimal(2));
        expectedKnnPoints.enqueue(new KDPoint(-2.0, 2.0),new BigDecimal(2.236068));
        expectedKnnPoints.enqueue(new KDPoint(-2.0, 6.0),new BigDecimal(2.236068));
//        expectedKnnPoints.enqueue(new KDPoint(-2.0, 7.0),2.236068);



        BoundedPriorityQueue<KDPoint> knnPoints = prQuadTree.kNearestNeighbors(kNN,queryPt);
        assertEquals("Expected KNN result to have "+expectedKnnPoints.size()+" elements but it actually have "+knnPoints.size()+ " elements"
                ,expectedKnnPoints.size(),knnPoints.size());
        KDPoint actualPoint;
        for (int i=0;i<kNN;i++)
        {
            actualPoint = knnPoints.dequeue();
            assertTrue("Expected KNN result should not contain "+actualPoint ,expectedKnnPoints.contains(actualPoint));
        }

    }

    @Test
    public void testNNPRQuadTree(){
        prQuadTree = new PRQuadTree(4, 2); // Space from (-8, -8) to (8, 8), bucketing parameter = 2.
        KDPoint[] points = {new KDPoint(1,1)};

        prQuadTree.insert(points[0]);
        KDPoint nn;

        nn = prQuadTree.nearestNeighbor(points[0]);
        assertNull("nearestNeighbor check; Expected null but actual value is not null. Make sure the code does not include query point in the result",nn);


        nn = prQuadTree.nearestNeighbor(new KDPoint(points[0].coords[0],points[0].coords[1].add(new BigDecimal(0.1))));
        assertEquals("nearestNeighbor check; Expected "+points[0].compactToString()+" but actual "+nn.compactToString(),nn,points[0]);
    }

    @Test
    public void testRangePRQuadTree() {
        prQuadTree = new PRQuadTree(4, 2); // Space from (-8, -8) to (8, 8), bucketing parameter = 2.
        KDPoint[] points = {new KDPoint(1, 1)
        };

        prQuadTree.insert(points[0]);
        double range = 5;
        ArrayList<KDPoint> ptsWithinRange;

        ptsWithinRange = new ArrayList<>(prQuadTree.range(points[0],new BigDecimal(range)));
        assertEquals("Query a Quadtree using the single element, the tree contains, should return 0 elements",
                0,ptsWithinRange.size());

        ptsWithinRange = new ArrayList<>(prQuadTree.range(
                new KDPoint(points[0].coords[0],points[0].coords[1].add(new BigDecimal(0.1))),new BigDecimal(range)));
        assertEquals(1,ptsWithinRange.size());

        assertEquals("nearestNeighbor check; Expected "+points[0].compactToString()+" but actual "+ptsWithinRange.get(0).compactToString(),
                ptsWithinRange.get(0),points[0]);
    }


        @Test
    public void testKDTreeViz(){
        /**
         * This test just gives an example for how to generate a KD-tree visualization using compactVizTree.
         * If successful, an image named compact_kdtree.png should be saved inside your project directory
         * Please make sure to delete these image files before submission
         */
        kdTree = new KDTree(2);
        KDPoint[] points = {new KDPoint(10, 30),
                new KDPoint(12, 18),
                new KDPoint(-20, 300),
                new KDPoint(16, 100),
                new KDPoint(10, 500),
                new KDPoint(18, 500),
        };

        for(int i=1;i<points.length;i++)
            kdTree.insert(points[i]);

        ArrayList<String> kdDescription = kdTree.treeDescription(false);
        CompactVizTree visualizer = new CompactVizTree(120,40,10);
        visualizer.drawBinaryTreeToFile(kdDescription,"compact_kdtree");

    }

    @Test
    public void testPRTreeViz(){
        /**
         * This test just gives an example for how to generate a Quadtree visualization using compactVizTree
         * If successful, an image named compact_quadtree.png should be created
         * Please make sure to delete these image files before submission
         */
        prQuadTree = new PRQuadTree(4, 2); // Space from (-8, -8) to (8, 8), bucketing parameter = 2.

        KDPoint[] points = {new KDPoint(1, 1),
                new KDPoint(4, 2),
                new KDPoint(7, 2),
                new KDPoint(7, 7),
                new KDPoint(2, 7),
                new KDPoint(-1, -7),
                new KDPoint(-1, 7),
                new KDPoint(7, -7),
                new KDPoint(5, 2),
                new KDPoint(1, 2),
                new KDPoint(-2, 2),
                new KDPoint(-2, 1)
        };

        for(int i=1;i<points.length;i++)
            prQuadTree.insert(points[i]);


        ArrayList<String> kdDescription = prQuadTree.treeDescription(false);
//        VizTree visualizer = new VizTree();
        CompactVizTree visualizer = new CompactVizTree(120,120,10);
        visualizer.drawBTreeToFile(kdDescription,4,"compact_quadtree");

    }
}

