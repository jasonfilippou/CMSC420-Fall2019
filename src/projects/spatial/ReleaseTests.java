
package projects.spatial;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import projects.spatial.kdpoint.KDPoint;
import projects.spatial.knnutils.BoundedPriorityQueue;
import projects.spatial.knnutils.KNNComparator;
import projects.spatial.trees.KDTree;
import projects.spatial.trees.PRQuadTree;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;
import static projects.spatial.kdpoint.KDPoint.distanceSquared;


/**
 * <p>A testing framework for {@link projects.spatial.trees.KDTree} and {@link projects.spatial.trees.PRQuadTree}</p>.
 *
 * @author <a href="https://github.com/JasonFil">Jason Filippou</a>
 *
 */

public class ReleaseTests {


    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */
    /* ***************************************** PRIVATE FIELDS  ********************************************* */
    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */


    private KDPoint origin2D, origin3D;
    private BoundedPriorityQueue<String> people;
    private static final int MAX_PRIORITY = 1000;
    private static final int DEFAULT_ITER = 500;
    private static final int DEFAULT_CAPACITY = 10;
    private static final int MAX_ITER = 200;
    private KDTree twoDTree, threeDTree, fourDTree, mount2DTree, jason2DTree, jason3DTree;
    private static final int MAX_DIM = 30;
    private Random r;
    private static final int SEED = 47;
    private static final int DEFAULT_BUCKETSIZE = 2;
    private static final int DEFAULT_K = 5;
    private PRQuadTree quadTree;
    private static final int MAX_POINTS = 1000;
    private final double EPSILON = Math.pow(10, -8);



    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */
    /* ***************************************** PRIVATE METHODS  ********************************************* */
    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */


    private static String getThrowableClassName(Throwable t){
        assert t!= null : "No null references should be passed to getSimpleName()";
        return t.getClass().getSimpleName();
    }

    private KDPoint getRandomKDPoint(){
        assert r!= null : "The random number generator needs to have been initialized before a call to getRandomKDPoint()";
        return new KDPoint(r.nextDouble(), r.nextDouble());
    }


   /**
     * Return {+1, -1} uniformly
     * @return Either 1 or -1 from a uniform distribution.
     */
    private int getRandomSign(){
        return 2 * r.nextInt(2) - 1;
    }

    /**
     * Return a very small positive or negative {@link BigDecimal}.
     * @return A signed {@link BigDecimal} instance with a tiny absolute value.
     */
    private BigDecimal getSignedEps(){
        return new BigDecimal(getRandomSign() * EPSILON);
    }

    /**
     * Return a {@link BigDecimal} that is a slight perturbation of the argument
     * @param num A &quot;seed&quot; {@link BigDecimal} instance.
     * @return A {@link BigDecimal} instance whose value is very close to the argument's, but guaranteed different.
     * @see #plusMinusEps(double)
     */
    private BigDecimal plusMinusEps(BigDecimal num){
        return getSignedEps().add(num);
    }

    /**
     * Return a {@link BigDecimal} that is a slight perturbation of the argument
     * @param num A floating-point number.
     * @return A {@link BigDecimal} instance whose value is very close to the argument's, but guaranteed different.
     * @see #plusMinusEps(BigDecimal)
     */
    private BigDecimal plusMinusEps(double num){
        return new BigDecimal(getSignedEps().doubleValue() + num);
    }

    /**
     * Check whether a range query on a {@link KDTree} is satisfied.
     * @param tree A populated {@link KDTree}.
     * @param origin The {@link KDPoint} used as the center of the range query.
     * @param range The maximum {@link KDPoint#distanceSquared(KDPoint, KDPoint)} that any {@link KDPoint} can have
     * from origin in order to satisfy our query.
     * @param candidates A varargs array containing the ground truth answers to our range query.
     * @return true iff all the {@link KDPoint}s in candidates are at a {link {@link KDPoint#distanceSquared(KDPoint, KDPoint)}
     * of at most range from origin.
     */

    private boolean checkRangeQuery(KDTree tree, KDPoint origin, BigDecimal range, KDPoint... candidates){
        Collection<KDPoint> rangeQueryResults = tree.range(origin, range);
        List<KDPoint> candidateList = Arrays.asList(candidates);
        return rangeQueryResults.containsAll(candidateList); // Order not important in range queries: only containment.
    }


    /**
     * Check whether a K-Nearest Neighbors query is satisfied by a {@link KDTree}.
     * @param tree A populated {@link KDTree}.
     * @param origin The {@link KDPoint} to calculate the nearest neighbors of.
     * @param k The number of nearest neighbors to compute.
     * @param neighbors A {@link List} containing the ground truth response to the query.
     * @return true iff the {@link KDPoint}s in candidates are the k nearest
     * points to origin, as dictated by {@link KDPoint#distanceSquared(KDPoint)}.
     */

    private boolean checkKNNQuery(KDTree tree, KDPoint origin, int k, List<KDPoint> neighbors){
        assert neighbors.size() == k : "Mismatch between requested neighbors and number of expected neighbors.";
        BoundedPriorityQueue<KDPoint> results = tree.kNearestNeighbors(k, origin);
        Iterator<KDPoint> resultsIt = results.iterator();
        for(KDPoint n : neighbors)
            if(!resultsIt.hasNext() || !resultsIt.next().equals(n))
                return false;
        return true;
    }


/**
     * Check whether a K-Nearest Neighbors query is satisfied by a {@link KDTree}. Different
     * from {@link #checkKNNQuery(KDTree, KDPoint, int, List)} in the type of last argument.
     * @param tree A populated {@link KDTree}.
     * @param origin The {@link KDPoint} to calculate the nearest neighbors of.
     * @param k The number of nearest neighbors to compute.
     * @param neighbors A varargs array containing the ground truth response to the query.
     * @return true iff the {@link KDPoint}s in candidates are the k nearest
     * points to origin, as dictated by {@link KDPoint#distanceSquared(KDPoint)}.
     */

    private boolean checkKNNQuery(KDTree tree, KDPoint origin, int k, KDPoint... neighbors){
        assert neighbors.length == k : "Mismatch between requested neighbors and number of expected neighbors.";
        BoundedPriorityQueue<KDPoint> results = tree.kNearestNeighbors(k, origin);
        Iterator<KDPoint> resultsIt = results.iterator();
        for(KDPoint n : neighbors)
            if(!resultsIt.hasNext() || !resultsIt.next().equals(n))
                return false;
        return true;
    }

    private BigDecimal[] getRandomCoords(int num){
        BigDecimal[] coords = new BigDecimal[num];
        for(int i = 0; i < num; i++)
            coords[i] = new BigDecimal(getRandomSign() * r.nextDouble());
        return coords;
    }

    private KDPoint getRandomPoint(int dim){
        return new KDPoint(getRandomCoords(dim));
    }

    private BigDecimal rootOfBigDecimal(BigDecimal bd){
        return new BigDecimal(Math.sqrt(bd.doubleValue()));
    }

    /* Prepares David Mount's 2D-tree, taken from his 420 notes, page 76.
     * IMPORTANT NOTE: DAVID MOUNT'S MADE A MISTAKE ON THAT TREE OF HIS.
     * Namely, the leaf nodes (60, 10) should be its parent's left child.
     * To make the example work, I changed that particular nodes to (75, 10).
     *
     */

    private void prepMountTree(){
        KDPoint[] points = {
                new KDPoint(35, 60), new KDPoint(20, 45),
                new KDPoint(10, 35), new KDPoint(20, 20),
                new KDPoint(60, 80), new KDPoint(80, 40),
                new KDPoint(50, 30), new KDPoint(70, 20),
                new KDPoint(75, 10),new KDPoint(90, 60)
        };
        for(KDPoint p: points)
            mount2DTree.insert(p);


/*The tree should now have the following structure:
         *
         *
         *								   (35,60)
         * 									/    \
         * 								   /      \
         * 								  /        \
         * 							  (20,45)     (60,80)
         * 								/		    /
         * 							   /      	   /
         * 						   (10,35)	    (80,40)
         * 							   \		  /  \
         * 								\		 /    \
         * 							  (20,20) (50,30) (90,60)
         * 									   /
         * 									  /
         * 								   (70,20)
         * 									  \
         * 									   \
         * 									 (75,10)
         */

    }


    /* Prepares a custom 2D-tree. This tree will be lop-sided, to create
     * a deletion special case which we can check via getRoot().
     */

    private void prepJason2DTree(){
        KDPoint[] points = {
                new KDPoint(2, 3), new KDPoint(-1, 6),
                new KDPoint(0, 5), new KDPoint(-1, 0),
                new KDPoint(-10, -3), new KDPoint(-4,-3),
                new KDPoint(-6,-6), new KDPoint(-4,-1),
                new KDPoint(1,3)
        };
        for(KDPoint p: points)
            jason2DTree.insert(p);


/* This is what the tree should look like:
         *
         * 					(2, 3)
         * 					/
         * 				   /
         * 				(-1,6)
         * 				 /
         * 		   		/
         * 			 (0,5)
         * 			  /  \
         * 		     /    \
         * 	   	   (-1,0) (1,3)
         * 		   /
         * 		  /
         * 	  (-10,-3)
         * 		  \
         * 		   \
         * 		  (-4,-3)
         * 			/  \
         * 		   /    \
         * 		(-6,-6) (-4,-1)
         */

    }


/* Prepares a custom 3D-tree. */

    private void prepJason3DTree(){
        KDPoint[] points = {
                new KDPoint(2,3,-5), new KDPoint(-1, 2, 3), new KDPoint(-1,-9,2),
                new KDPoint(-4,1,0), new KDPoint(-3,-8,-3), new KDPoint(-3,-10,1),
                new KDPoint(-3,-8,0), new KDPoint(-6,4,8), new KDPoint(-5,6,8),
                new KDPoint(6,0,-1), new KDPoint(8,-2,6), new KDPoint(8,-2,7),
                new KDPoint(8,-1,9), new KDPoint(4,1,-2)
        };
        for(KDPoint p: points)
            jason3DTree.insert(p);


/*
         *	This is what the tree should look like:
         *
         *							  (2,3,-5)
         *						      / 	  \
         *						     /	  	   \
         *						    /		    \
         *			            (-1,2,3)        (6,0,-1)
         *						  /	   \           /   \
         *					     /		\         /     \
         *                  (-1,-9,2) (-6,4,8) (8,-2,6) (4,1,-2)
         *                    /			  \		   \
         *                   /			   \		\
         * 				 (-4,1,0)		(-5,6,8)   (8,-2,7)
         * 					\						  \
         * 					 \						   \
         * 					(-3,-8,-3)				  (8,-1,9)
         * 				     /		\
         * 					/		 \
         * 				(-3,-10,1)	(-3,-8,0)
         *
         * Measure 233 from (8,-2,7)...
         *
         * Nodes within:
         * 		[2, 3, -5]
         *		[-1, 2, 3]
         *		[6, 0, -1]
         *		[-1, -9, 2]
         *		[-6, 4, 8]
         *		[8, -2, 6]
         *		[4, 1, -2]
         *		[-4, 1, 0]
         *		[8, -2, 7] <---- The point itself...
         *		[8, -1, 9]
         *		[-3, -10, 1]
         *		[-3, -8, 0]
         *
         */

    }


/* ******************************************************************************************************** */


/* ******************************************************************************************************** */


/* ***************************************** SETUP / TEAR DOWN ********************************************* */


/* ******************************************************************************************************** */


/* ******************************************************************************************************** */



/**
     * A setup method that runs before every one of our jUnit testing methods.
     */

    @Before
    public void setUp(){
        origin2D = new KDPoint();
        origin3D = new KDPoint(3);
        twoDTree = new KDTree(2); // Using the default constructor should also be fine.
        threeDTree = new KDTree(3);
        fourDTree = new KDTree(4);
        mount2DTree = new KDTree(2);
        jason2DTree = new KDTree(2);
        jason3DTree = new KDTree(3);
        quadTree = new PRQuadTree(DEFAULT_K, DEFAULT_BUCKETSIZE);
        r = new Random(SEED);
        try {
            people = new BoundedPriorityQueue<>(DEFAULT_CAPACITY);
        }catch(Throwable t){
            fail("During setUp(), we caught a " + t.getClass() +
                    " with message: " + t.getMessage() + " when creating a BPQ with the default capacity of:" +
                    DEFAULT_CAPACITY + ".");
        }
    }



/**
     * A cleanup method that runs after every one of our jUnit testing methods.
     */

    @After
    public void tearDown(){
        people = null;
        origin2D = origin3D = null;
        twoDTree = threeDTree = fourDTree = mount2DTree = jason2DTree = jason3DTree = null;
        r = null;
        quadTree = null;
        System.gc();
    }



/* ******************************************************************************************************** */
/* ******************************************************************************************************** */
/* ***************************************** KDPoint Tests ************************************************* */
/* ******************************************************************************************************** */
/* ******************************************************************************************************** */

    @Test
    public void testKDPointInt() {
        KDPoint hopefullyOrigin2D = new KDPoint(2),
                hopefullyOrigin3D = new KDPoint(3);
        assertEquals(hopefullyOrigin2D, origin2D);
        assertEquals(hopefullyOrigin3D, origin3D);

        // Is the appropriate exception properly thrown?
        for(int i = 0; i < MAX_ITER; i++){
            int currDim = -r.nextInt(1000); // Negative integers or zero.
            RuntimeException re = null;
            try {
                new KDPoint(currDim);
            } catch(RuntimeException rexc){
                re = rexc;
            } catch(Throwable t){
                fail("Should've caught a RuntimeException when creating a KDPoint of dimensionality " +currDim + " . Instead we caught a "
                        + t.getClass() + " with message: " + t.getMessage() + ".");
            }
            assertNotNull("Should've caught a RuntimeException when creating a KDPoint of dimensionality " +currDim + "." , re);
        }
    }

    @Test
    public void testKDPointDoubleArray() {
        KDPoint point = new KDPoint(2.12, -9.23, 0.01, -34); // TODO: Make sure the call of the double... constructor doesn't cause issues here.
        assertEquals(4, point.coords.length);
        assertEquals(new BigDecimal(2.12), point.coords[0]);
        assertEquals(new BigDecimal(-9.23), point.coords[1]);
        assertEquals(new BigDecimal(0.01), point.coords[2]);
        assertEquals(new BigDecimal(-34), point.coords[3]);
    }

    @Test
    public void testKDPointKDPoint() {
        assertEquals(new KDPoint(origin2D), origin2D);
        assertEquals(new KDPoint(origin3D), origin3D);
        KDPoint fourDPoint = new KDPoint(-20.45, 6.78, 0.56, -9.76);
        assertEquals(new KDPoint(fourDPoint),fourDPoint);
    }

    @Test
    public void testKDPointDistanceKDPoint() {

        // Trivial zero distances
        assertEquals(BigDecimal.ZERO, origin2D.distanceSquared(origin2D));
        assertEquals(BigDecimal.ZERO, origin3D.distanceSquared(origin3D));
        for(int i = 0; i < MAX_ITER; i++){
            KDPoint p = new KDPoint(- r.nextDouble(),  r.nextDouble());
            assertEquals(BigDecimal.ZERO, p.distanceSquared(p));
        }

        // Let's also check if some exceptions are properly thrown.
        RuntimeException re = null; // We will use this reference in the next few examples.
        try {
            origin2D.distanceSquared(origin3D);
        } catch(RuntimeException rexc){
            re = rexc;
        } catch(Throwable t){
            fail("Should've caught a RuntimeException when computing the distanceSquared between two KDPoints of different dimensionalities. Instead, " +
                    "we caught a " + t.getClass() + " with message: " + t.getMessage() + ".");
        }
        assertNotNull("Should've caught a RuntimeException when computing the distanceSquared between two KDPoints of different dimensionalities.", re);

        try {
            origin3D.distanceSquared(origin2D);
        } catch(RuntimeException rexc){
            re = rexc;
        } catch(Throwable t){
            fail("Should've caught a RuntimeException when computing the distanceSquared between two KDPoints of different dimensionalities. Instead, " +
                    "we caught a " + t.getClass() + " with message: " + t.getMessage() + ".");
        }
        assertNotNull("Should've caught a RuntimeException when computing the distanceSquared between two KDPoints of different dimensionalities.", re);

        for(int i = 0; i < MAX_ITER; i++){
            int currDim = r.nextInt(MAX_DIM);
            re = null;
            try {
                new KDPoint(currDim).distanceSquared(new KDPoint(currDim + 1));
            } catch(RuntimeException rexc){
                re = rexc;
            }
            catch(Throwable t){
                fail("Should've caught a RuntimeException when computing the distanceSquared between two KDPoints of dimensionalities + " +
                        currDim + " and " + currDim + 1 + ". Instead, " +
                        "we caught a " + t.getClass() + " with message: " + t.getMessage() + ".");
            }
            assertNotNull("Should've caught a RuntimeException when computing the distanceSquared between two KDPoints of dimensionalities + " +
                    currDim + " and " + currDim + 1 + ".", re);
        }

        // Simple stuff first, 1-D points!
        KDPoint one = new KDPoint(3.0), two = new KDPoint(0.0);
        assertEquals(BigDecimal.ZERO, one.distanceSquared(one));
        assertEquals(BigDecimal.ZERO, two.distanceSquared(two));
        assertEquals(new BigDecimal(9), one.distanceSquared(two));
        assertEquals(new BigDecimal(9), two.distanceSquared(one));
        KDPoint three = new KDPoint(-3.0);
        assertEquals(new BigDecimal(9), two.distanceSquared(three));
        assertEquals(new BigDecimal(9), three.distanceSquared(two));

        // Classic.
        KDPoint oneOne = new KDPoint(1, 1);
        assertEquals(new BigDecimal(2), new KDPoint().distanceSquared(oneOne));
        KDPoint minusOneOne = new KDPoint(1, -1);
        assertEquals(new BigDecimal(2), new KDPoint().distanceSquared(minusOneOne));
        KDPoint oneMinusOne = new KDPoint(1, -1);
        assertEquals(new BigDecimal(2), new KDPoint().distanceSquared(oneMinusOne));
        KDPoint minusOneminusOne = new KDPoint(-1, -1);
        assertEquals(new BigDecimal(2), new KDPoint().distanceSquared(minusOneminusOne));

        // And a not so trivial one
        KDPoint complexPointOne = new KDPoint(3.5, 2.1, -10.9);
        KDPoint complexPointTwo = new KDPoint(-1.4, 2.8, -0.0007);
        assertEquals(new BigDecimal(143.29474049), complexPointOne.distanceSquared(complexPointTwo)); // Computed with Google's calculator
        assertEquals(new BigDecimal(143.29474049), complexPointTwo.distanceSquared(complexPointOne));
    }

    @Test
    public void testKDPointDistanceStatic() {
        // Some trivial ones
        assertEquals(BigDecimal.ZERO, distanceSquared(origin2D, origin2D)); // Recall that the static method has been statically imported, so this works.
        assertEquals(BigDecimal.ZERO, distanceSquared(origin3D, origin3D));
        for(int i = 0; i < MAX_ITER; i++){
            KDPoint p = new KDPoint(- r.nextDouble(), r.nextDouble());
            assertEquals(BigDecimal.ZERO, distanceSquared(p, p));
        }

        // The complex example from the previous test:
        KDPoint complexPointOne = new KDPoint(3.5, 2.1, -10.9);
        KDPoint complexPointTwo = new KDPoint(-1.4, 2.8, -0.0007);
        assertEquals(new BigDecimal(143.29474049), distanceSquared(complexPointOne, complexPointTwo));
        assertEquals(new BigDecimal(143.29474049), distanceSquared(complexPointTwo, complexPointOne));

        // And, finally, proper exceptions thrown when comparing objects of different
        // dimensionalities:
        for(int i = 0; i < MAX_ITER; i++){
            int currDim = r.nextInt(MAX_DIM);
            RuntimeException re = null;
            try {
                distanceSquared(new KDPoint(currDim), new KDPoint(currDim + 1));
            } catch(RuntimeException rexc){
                re = rexc;
            }
            catch(Throwable t){
                fail("Should've caught a RuntimeException when computing the distanceSquared between two KDPoints of dimensionalities + " +
                        currDim + " and " + currDim + 1 + ". Instead, " +
                        "we caught a " + t.getClass() + " with message: " + t.getMessage() + ".");
            }
            assertNotNull("Should've caught a RuntimeException when computing the distanceSquared between two KDPoints of dimensionalities + " +
                    currDim + " and " + currDim + 1 + ".", re);
        }
    }

    @Test
    public void testKDPointToString(){

        // (1) 1D KDPoints
        for(int i = 0; i < MAX_ITER; i++){
            BigDecimal randNum = new BigDecimal(r.nextDouble());
            KDPoint p = new KDPoint(randNum);
            assertEquals("We failed to generate a proper String-ified representation for "
                            + "the 1D point  #" + i, "A KDPoint with coordinates: ("+(randNum)+")",
                    p.toString());
            p = new KDPoint(randNum.negate());
            assertEquals("We failed to generate a proper String-ified representation for "
                            + "the 1D point  #" + i, "A KDPoint with coordinates: ("+ (randNum.negate())+")",
                    p.toString());
        }


        // (2) 2D KDPoints
        for(int i = 0; i < MAX_ITER; i++){
            BigDecimal[] randNums = {new BigDecimal(r.nextDouble()), new BigDecimal(r.nextDouble())};
            KDPoint p = new KDPoint(randNums);
            assertEquals("We failed to generate a proper String-ified representation for "
                    + "the 1D point  #" + i, "A KDPoint with coordinates: ("+randNums[0]+", "
                    + randNums[1] + ")",p.toString());
            BigDecimal[] minusRandNums = {randNums[0].negate(), randNums[1].negate()};
            p = new KDPoint(minusRandNums);
            assertEquals("We failed to generate a proper String-ified representation for "
                    + "the 1D point  #" + i, "A KDPoint with coordinates: ("+minusRandNums[0]+", "
                    + minusRandNums[1] + ")",p.toString());
        }

        // Could add tests for more dimensions, but it's not like we will be using toString()
        // for anything other than debugging information...
    }


    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */
    /* ***************************************** BPQ Tests ************************************************* */
    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */



    @Test
    public void testBoundedPriorityQueue() {

        // First, test that for a bunch of invalid priorities,
        // an exception is thrown.
        for(int i = 0; i < DEFAULT_ITER; i++){
            int randNegInt = -r.nextInt(MAX_PRIORITY); // 0, -1, ..., -MAX_PRIORITY + 1
            RuntimeException re = null;
            try {
                new BoundedPriorityQueue<>(randNegInt);
            } catch(RuntimeException rexc){
                re = rexc;
            } catch(Throwable t){
                fail("When creating BPQ # " + i + " with priority " + randNegInt
                        + ", we should've caught a RuntimeException. Instead, we caught a "
                        + t.getClass() + " with message: " + t.getMessage() + ".");
            }
            assertNotNull("When creating BPQ # " + i + " with priority " + randNegInt
                    + ", we should've caught a RuntimeException.", re);
        }

        // Then, check that for a bunch of valid priorities, everything goes well.
        for(int i = 0; i < DEFAULT_ITER; i++){
            int randInt = r.nextInt(MAX_PRIORITY); // 0, 1, ..., MAX_PRIORITY - 1
            try {
                new BoundedPriorityQueue<>(randInt);
            }
            catch(Throwable t){
                fail("When creating BPQ # " + i + " with priority " + randInt +
                        ", we caught a " + t.getClass() + " with message: " +
                        t.getMessage() + ".");
            }
        }
    }

    @Test
    public void testEnqueue() {

        // (1) Enqueue exactly DEFAULT_CAPACITY many elements. Assure not empty
        // after each enqueueing.
        assertTrue("BPQ should be empty", people.isEmpty());
        for(int i = 0; i < DEFAULT_CAPACITY; i++){
            BigDecimal priority = new BigDecimal(r.nextInt(MAX_PRIORITY));
            try {
                people.enqueue("Person " + i, priority);
            } catch(Throwable t){
                fail("After inserting person #" + i + " with priority " + priority + " in our BPQ, we caught a " +
                        t.getClass() + " with error message: " + t.getMessage() + ".");
            }
            assertFalse("After enqueuing the person #" + i + " with priority " + priority +
                    ", the BPQ was determined to be empty.", people.isEmpty());
        }


        // (2) Enqueue another one, check that size() is the same since this is a BPQ.
        BigDecimal priority = new BigDecimal(r.nextInt(MAX_PRIORITY));
        try {
            people.enqueue("Person " + DEFAULT_CAPACITY, priority);
        }catch(Throwable t){
            fail("Caught a " + t.getClass() + " with message: " + t.getMessage() +
                    " when inserting element \"Person " + DEFAULT_CAPACITY + "\"" +
                    " with priority " + priority + ", which is one beyond the queue's capacity.");
        }
        assertEquals("Inserting an element beyond a BPQ's capacity should not expand the queue, "
                + "which is what happened when inserting element \"Person " + DEFAULT_CAPACITY + "\"" +
                " with priority " +	priority + ".",	DEFAULT_CAPACITY, people.size());

        // (3) Keep on doing that DEFAULT_ITER - many times, always making sure the size
        // remains the same.
        for(int i = 0; i < DEFAULT_ITER; i++){
            priority = new BigDecimal(r.nextInt(MAX_PRIORITY));
            try {
                people.enqueue("Person " + (DEFAULT_CAPACITY + i), priority);
            } catch(Throwable t){
                fail("After inserting person #" + (DEFAULT_CAPACITY + i) +
                        " with priority " + priority + " in our BPQ, we caught a "
                        + t.getClass() + " with error message: " + t.getMessage() + ".");
            }
            assertEquals("Inserting elements beyond a BPQ's capacity should not expand the queue, "
                    + "which is what happened when inserting element \"Person " +
                    (DEFAULT_CAPACITY + i) + "\"" + " with priority " +
                    priority + ".",	people.size(), DEFAULT_CAPACITY);
        }
    }

    @Test
    public void testDequeue() {

        // (1) An easy test.
        people.enqueue("Jim", new BigDecimal(r.nextInt(MAX_PRIORITY)));
        assertEquals("Jim", people.dequeue());
        assertTrue("BPQ should be empty after dequeueing its only element.", people.isEmpty());

        // (2) Make sure that two elements with equal priorities
        // get processed in a FIFO manner.
        people.enqueue("Jim", new BigDecimal(11.3));
        people.enqueue("Jill", new BigDecimal(11.3));
        people.enqueue("Mary",new BigDecimal(9.1)); // precedes the other two!
        assertEquals("It appears that this BPQ does not provide for proper ordering.",
                "Mary", people.dequeue());
        assertEquals("Elements with the same priority should be treated in a FIFO manner",
                "Jim", people.dequeue());
        assertEquals("Elements with the same priority should be treated in a FIFO manner",
                "Jill", people.dequeue());
        assertNull("Dequeueing an empty queue should return null to the caller.",
                people.first());

        assertTrue("BPQ should be empty after dequeueing all of its elements.", people.isEmpty());

        // (3) A rather contrived test.
        Double[] priorities = new Double[DEFAULT_CAPACITY];
        for(int i = 0; i < priorities.length; i++){
            double priority =  r.nextDouble();
            priorities[i] = priority;
            people.enqueue("Person " + priority, new BigDecimal(priority));
        }

        Arrays.sort(priorities);
        int size = DEFAULT_CAPACITY;
        for(Double pr : priorities){
            assertEquals("It appears that this BPQ does not provide for proper ordering.",
                    "Person " + pr, people.dequeue());
            assertEquals("Dequeuing a BPQ should result in a size decrease by 1.", --size, people.size());
        }
        assertTrue("BPQ should be empty after dequeueing all of its elements.", people.isEmpty());

    }

    @Test
    public void testFirst() {

        // Similar tests to testDequeue(), with the obvious caveat
        // that just peeking into the BPQ should not alter its size...

        // (1) An easy test.
        people.enqueue("Jim", new BigDecimal(r.nextInt(MAX_PRIORITY)));
        assertEquals("Jim", people.first());
        assertFalse("A singleton BPQ should not be empty after peeking its top element.",
                people.isEmpty());
        people = new BoundedPriorityQueue<>(DEFAULT_CAPACITY);
        // (2) Make sure that two elements with equal priorities
        // get processed in a FIFO manner.
        people.enqueue("Jim", new BigDecimal(11.3));
        people.enqueue("Jill", new BigDecimal(11.3));
        assertEquals("Elements with the same priority should be treated in a FIFO manner",
                "Jim", people.first());
        people.dequeue();
        assertEquals("Elements with the same priority should be treated in a FIFO manner",
                "Jill", people.dequeue()); // Attention, this is dequeue()!
        // So, now, peeking should be null.
        assertNull("Peeking into an empty queue should provide us with null.", people.first());

        // (3) A rather contrived test.
        Double[] priorities = new Double[DEFAULT_CAPACITY];
        for(int i = 0; i < priorities.length; i++){
            double priority =  r.nextDouble();
            priorities[i] = priority;
            people.enqueue("Person " + priority, new BigDecimal(priority));
        }
        Arrays.sort(priorities);
        assertEquals("It appears that this BPQ does not provide for proper ordering.",
                "Person " + priorities[0], people.first());

        // (4) Finally, test semantics. Make sure that insertions after the queue is full
        // obey the priorities.
        people.enqueue("New Min Person", new BigDecimal(priorities[0] - 1.0));
        assertEquals("Inserting a minimum priority element after the queue is full should not alter its size.",
                DEFAULT_CAPACITY, people.size());
        assertEquals("Inserting a minimum priority element after the queue is full should bubble it up front.",
                "New Min Person", people.first());
        people.enqueue("Dude who should be second", new BigDecimal(priorities[0] - 0.5));
        assertEquals("Inserting a minimum priority element after the queue is full should not alter its size.",
                DEFAULT_CAPACITY, people.size());
        assertNotEquals("Encountered an error with the prioritization of elements inserted after the queue is full.",
                "Dude who should be second", people.first());
        people.dequeue();
        assertEquals("Inserting a minimum priority element after the queue is full should bubble it up front.",
                "Dude who should be second", people.first());
    }

    @Test
    public void testLast(){

        // Similar to testFirst().
        assertNull("Querying an empty BPQ for its last element should return null.", people.last());

        // (1) An easy test.
        people.enqueue("Jim", new BigDecimal(r.nextInt(MAX_PRIORITY)));
        assertEquals("Jim", people.last());
        assertFalse("A singleton BPQ should not be empty after peeking its last element.",
                people.isEmpty());
        people = new BoundedPriorityQueue<>(DEFAULT_CAPACITY);
        // (2) Make sure that two elements with equal priorities
        // get processed in a FIFO manner.
        people.enqueue("Jim", new BigDecimal(11.3));
        people.enqueue("Jill", new BigDecimal(11.3));
        assertEquals("Elements with the same priority should be treated in a FIFO manner",
                "Jill", people.last());
        people.dequeue(); people.dequeue();
        // So, now, peeking should be null.
        assertNull("Querying an empty BPQ for its last element should return null.", people.last());

        // (3) A rather contrived test.
        Double[] priorities = new Double[DEFAULT_CAPACITY];
        for(int i = 0; i < priorities.length; i++){
            double priority =  r.nextDouble();
            priorities[i] = priority;
            people.enqueue("Person " + priority, new BigDecimal(priority));
        }
        Arrays.sort(priorities);
        assertEquals("It appears that this BPQ does not provide for proper ordering.",
                "Person " + priorities[priorities.length - 1], people.last());

        // (4) And test semantics.
        people.enqueue("New Max Person", new BigDecimal(priorities[priorities.length - 1] + 1.0));
        assertNotEquals("Prioritization of enqueuing a past-max-priority element past capacity problematic.",
                "New Max Person", people.last());
        people.enqueue("Barely New Max Person", new BigDecimal(priorities[priorities.length - 1]));
        assertNotEquals("Prioritization of enqueuing a past-max-priority element past capacity problematic.",
                "Barely New Max Person", people.last());

        // After we insert an element in the queue (that does not have the max priority),
        // we should pop out the last element of the queue.
        people.enqueue("New Min Person", new BigDecimal(priorities[1]));
        assertNotEquals("Should be ejecting the maximum priority element when inserting a "
                + "non-maximum priority element in a BPQ that's already full.", "New Max Person", people.last());
        assertEquals("Did not detect the expected new maximum priority element after inserting a " +
                        "non-maximum priority element in a BPQ that's already full.",
                "Person " + priorities[priorities.length - 2], people.last());
    }

    @Test
    public void testIterator(){

        // Insert some peeps.
        Double[] priorities = new Double[DEFAULT_CAPACITY];
        for(int i = 0; i < DEFAULT_CAPACITY; i++)
            priorities[i] =  r.nextDouble();
        for(Double pr: priorities)
            people.enqueue("Person " + pr, new BigDecimal(pr));
        Arrays.sort(priorities);

        // (1) Check proper use of Iterator.next()
        Iterator<String> personIt = people.iterator();
        assertTrue("For a freshly created Iterator<> instance returned by a non-empty BPQ," +
                "\"hasNext()\" returned false.", personIt.hasNext());
        for(int i = 0; i < priorities.length; i++)
            assertEquals("Failed in our fetching of person #" + i + " from our Iterator.",
                    "Person " + priorities[i], personIt.next());
        assertFalse("After looping through a non-empty BPQ using an Iterator<> instance, \"hasNext()\" " +
                "should be returning false.", personIt.hasNext());

        // (2) Since BoundedPriorityQueues are now Iterables, the
        // for-each loop should operate primo, right?
        int i = 0;
        for(String p : people)
            assertEquals("Failed in our fetching of person #" + i + " in our for-each loop.",
                    "Person " + priorities[i++], p);

        // (3) Check that appropriate exceptions are thrown.

        //ConcurrentModificationException
        for(Double pr: priorities)
            people.enqueue("Person " + pr, new BigDecimal(pr));
        Iterator<String> it = people.iterator();

        it.next();
        people.dequeue();
        ConcurrentModificationException cme = null;
        try {
            it.next();
        }catch(ConcurrentModificationException cmeThrown){
            cme = cmeThrown;
        } catch(Throwable t){
            fail("Caught a " + t.getClass() + " with message: " + t.getMessage() +
                    " instead of a ConcurrentModificationException when calling \"next()\" on an Iterator"
                    + " over a BPQ that was modified in between creation of the Iterator and the call to \"next()\".");
        }
        assertNotNull("We should have caught a ConcurrentModificationException when calling \"next()\" on an Iterator"
                + " over a BPQ that was modified in between creation of the Iterator and the call to \"next()\".", cme);
    }


    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */
    /* ***************************************** KD-Tree Tests ************************************************ */
    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */


    /**
     * Test method for {@link KDTree#KDTree()}.
     */
    @Test
    public void testKDTree() {
        for(int i = 0; i < MAX_ITER; i++){
            try {
                new KDTree();
            } catch(Throwable t){
                fail("Caught a " + t.getClass() + " with message: " + t.getMessage() +
                        " when constructing a KD-Tree with the default constructor in loop " + i + ".");
            }
        }
    }


    /**
     * Test method for {@link KDTree#KDTree(int)}.
     */

    @Test
    public void testKDTreeInt() {

    /* First, track any exceptions thrown for valid values of
         * the dimensionality parameter.
         */

        for(int i = 0; i < MAX_ITER; i++){
            int randDim = r.nextInt(MAX_DIM) + 1; // (Candidate set: {1, 2, ..., MAX_DIM}
            try {
                new KDTree(randDim);
            } catch(Throwable t){
                fail("Caught a " + t.getClass() + " with message: " + t.getMessage() +
                        " when constructing a KD-Tree of dimensionality " + randDim +
                        " in loop " + i + ".");
            }
        }


    /* Second, make sure that a good set of invalid values of the
         * dimensionality parameter throws the expected exception.
         */


        for(int i = 0; i < MAX_ITER; i++){
            int randDim = r.nextInt(MAX_DIM); // (Candidate set: {0, 1, ..., MAX_DIM - 1}
            RuntimeException re = null;
            try {
                new KDTree(-randDim); // 0 is also not acceptable by constructor.
            } catch(RuntimeException rexc) {
                re = rexc;
            } catch(Throwable t){
                // Not good
                fail("While expecting a RuntimeException, we instead caught a " +
                        t.getClass() + " with message: " + t.getMessage() +
                        " when constructing a KD-Tree of dimensionality " + (-randDim) +
                        " in loop " + i + ".");
            }
            assertNotNull("When constructing a KD-Tree of dimensionality " + (-randDim) + "in loop " + i + ", we " +
                    "were expecting a RuntimeException to be thrown.", re);
        }
    }



    /**
     * Test method for {@link KDTree#insert(KDPoint)}.
     */

    @Test
    public void testKDTreeInsert() {


    /* (1) Create the trees using the private methods
         * and look for exceptions.
         */

        try {
            prepMountTree();
        } catch(Throwable t){
            fail("Caught a " + t.getClass() + " with message: "
                    + t.getMessage() + " when creating our first 2D tree.");
        }

        try {
            prepJason2DTree();
        } catch(Throwable t){
            fail("Caught a " + t.getClass() + " with message: "
                    + t.getMessage() + " when creating our second 2D tree.");
        }

        try {
            prepJason3DTree();
        } catch(Throwable t){
            fail("Caught a " + t.getClass() + " with message: "
                    + t.getMessage() + " when creating our 3D tree.");
        }


        /*
         * (2) Stress tests for arbitrary dimension trees.
         */


        for(int dim = 1; dim < MAX_DIM + 1; dim++){ // For all possible dimensions that we consider...
            KDTree tempTree = new KDTree(dim);
            for(int i = 0; i < MAX_ITER; i++){ // Bunch of points
                KDPoint tempPt = getRandomPoint(dim);
                try {
                    tempTree.insert(tempPt);
                } catch(Throwable t){
                    fail("For a " + dim + "-D tree, we attempted to insert " + tempPt +
                            ", which was point #" + i + " in the sequence, and received a "
                            + t.getClass() +" with message: " + t.getMessage() + ".");
                }
            }

        }
    }


    /**
     * Test method for {@link KDTree#delete(KDPoint)}.
     */

    @Test
    public void testKDTreeDelete() {


    /* (1) Simple tests on Mount tree: deletions of both inner nodes and leaves, as
         * 	well as deletions of elements that are not in the tree.
         */

        prepMountTree();
        int origMountTreeHeight = mount2DTree.height();
        // Mount's tree contains no negative coordinate points; let's make sure that no
        // matter how many such points we generate, deleting them has no impact on the tree
        // whatsoever.
        for(int i = 0; i < MAX_ITER; i++){
            double x = -r.nextDouble(), y = -r.nextDouble();
            mount2DTree.delete(new KDPoint(x, y));
            assertEquals("Deleting elements not in the tree should have no impact on the " +
                    "tree structure whatsoever.", origMountTreeHeight, mount2DTree.height());
        }

        // The following deletion, however, should reduce the tree's height by 1.
        mount2DTree.delete(new KDPoint(75, 10));
        assertEquals("Are you adjusting the height correctly after leaf deletions?", origMountTreeHeight - 1, mount2DTree.height());

        // While this one should also change the root of the tree (obviously).
        mount2DTree.delete(mount2DTree.getRoot());
        assertEquals("Are you deleting inner nodes (or even the root) correctly?",
                new KDPoint(50, 30), mount2DTree.getRoot());

        // It appears that Mount's tree also gives rise to a special case
        // of deletion: let's check by performing a deletion and then a search()
        // which'll fail unless the client code takes care of both appropriately.

        mount2DTree.delete(new KDPoint(20, 45));
        assertTrue("After deletion of a nodes without a right child, a nodes that formerly existed " +
                " in its left subtree could no longer be found in the tree.",mount2DTree.search(new KDPoint(20, 20)));


/* (2) Tests on custom 2D tree: This tree gives rise to a deletion special case
         * the consistency of which can be checked via getRoot().
         */


        prepJason2DTree();
        int origJason2DTreeHeight = jason2DTree.height();
        // Delete the root, check expected root and expected height.
        try {
            jason2DTree.delete(jason2DTree.getRoot());
        }catch(Throwable t){
            fail("Caught a " + t.getClass() + " with message " + t.getMessage() +
                    " when deleting a root with no right subtree in a 2D tree.");
        }
        assertEquals("Check your deletion special cases for the root nodes.",
                new KDPoint(-10,-3), jason2DTree.getRoot());
        assertEquals("Just deleted the root, but this particular deletion should not have affected " +
                " the tree height.", origJason2DTreeHeight, jason2DTree.height());


/* (3) A similar test, only for a 3D tree this time. We go ahead and
         * delete the root and its entire right subtree, checking for exceptions
         * along the way. Then we are left with essentially the same tree as in (2),
         * only in three dimensions.
         */

        prepJason3DTree();
        KDPoint[] pointsToInitDelete = {
                new KDPoint(2,3,-5), new KDPoint(6,0,-1),
                new KDPoint(8,-2,6), new KDPoint(8,-2,7),
                new KDPoint(8,-1,9)
        };
        for(KDPoint p: pointsToInitDelete){
            try {
                jason3DTree.delete(p);
            } catch(Throwable t){
                fail("Caught a " + t.getClass() + " with message: " + t.getMessage() +
                        " when deleting " + p + " from a 3D tree.");
            }
        }

        // Now, deleting the root should reflect the expected changes.
        int origJason3DTreeHeight = jason3DTree.height();
        try {
            jason3DTree.delete(jason3DTree.getRoot());
        }catch(Throwable t){
            fail("Caught a " + t.getClass() + " with message " + t.getMessage() +
                    " when deleting a root with no right subtree in a 3D tree.");
        }
        assertEquals("Incorrect root detected after a special case deletions!",
                new KDPoint(-6,4,8), jason3DTree.getRoot());
        assertEquals("Incorrect tree height detected after a special case deletion which should " +
                "not change the tree's height.", origJason3DTreeHeight, jason3DTree.height());


        /* (4) Stress test for deletion on arbitrary dimension trees.
         *	We create MAX_DIM-many KD-trees, and for each tree we insert
         *	MAX_ITER-many KDPoints of the appropriate dimensionality, while
         *	also keeping a store for these points. We then shuffle this store,
         *	and sequentially delete all the points in the store from the actual
         *	tree.
         */


        for(int dim = 1; dim < MAX_DIM + 1; dim++){ // For MAX_DIM-many trees...
            KDTree tree = new KDTree(dim);
            LinkedList<KDPoint> pts = new LinkedList<>();
            for(int i = 0; i < MAX_ITER; i++){ // For MAX_ITER-many points...
                KDPoint randPoint = getRandomPoint(dim);
                pts.add(randPoint); // Add a new point based on random coordinates
                tree.insert(randPoint);
            }
            // Now delete all the points and check for exceptions while you delete
            // them as well as tree consistency after they're all gone.
            Collections.shuffle(pts, r);
            for(KDPoint p : pts){
                try {
                    tree.delete(p);
                }catch(Throwable t){
                    fail("Caught a " + t.getClass() + " with message " + t.getMessage() +
                            " when deleting " + p + " from a " + dim + "-D tree.");
                }
            }
            assertTrue("After deleting all the elements inserted in a " + dim +
                    "-D tree, the tree was deemed to not be empty.", tree.isEmpty());
        }

    }


/**
     * Test method for {@link KDTree#search(KDPoint)}.
     */

    @Test
    public void testKDTreeSearch() {
        // Really the only thing we can do at this point is a stress test
        // for arbitrary-dimensional KD-trees.
        for(int dim = 1; dim < MAX_DIM + 1; dim++){ // For MAX_DIM-many trees...
            KDTree tree = new KDTree(dim);
            LinkedList<KDPoint> pts = new LinkedList<>();
            for(int i = 0; i < MAX_ITER; i++){ // For MAX_ITER-many points...
                KDPoint randPoint = getRandomPoint(dim);
                pts.add(randPoint); // Add a new point based on random coordinates
                tree.insert(randPoint);
            }
            // Now search all the points and check for exceptions while you look
            // them all up. Make sure that nothing changes in the tree after every single search.
            Collections.shuffle(pts, r);
            int treeHeight = tree.height();
            KDPoint rootPt = tree.getRoot();
            for(KDPoint p : pts){
                try {
                    assertTrue("Looking up " + p + " which is known to be in the tree failed.",
                            tree.search(p));
                    assertEquals("Looking up a KDPoint in the tree shouldn't alter its height in any way.",
                            treeHeight, tree.height());
                    assertEquals("Looking up a KDPoint in the tree shouldn't alter its root in any way.",
                            rootPt, tree.getRoot());
                }
                catch(AssertionError ae){ throw(ae); } // Generated by possible failures of assertions; separate from other Throwables so that
                                            // you don't report multiple errors in the catchblocks.
                catch(Throwable t){
                    fail("Caught a " + t.getClass() + " with message " + t.getMessage() +
                            " when deleting " + p + " from a " + dim + "-D tree.");
                }
            }
        }
    }


    /**
     * Test method for {@link KDTree#getRoot()}.
     */

    @Test
    public void testKDTreeGetRoot(){
        // Since we've covered the cases for emptiness in other
        // methods, we will just make a really simple test here.
        // getRoot() will be used in other tests, such as deletions, to
        // ensure that the proper root is maintained anyway.
        for(int dim = 1; dim <= MAX_DIM; dim++){
            KDTree tempTree = new KDTree(dim);
            KDPoint p = getRandomPoint(dim);
            tempTree.insert(p);
            assertEquals("For a " + dim + "-D tree where we inserted only " + p +
                    ", we did not find it in the root of the tree.", p, tempTree.getRoot());
        }
    }


/**
     * Test method for {@link KDTree#range(KDPoint, BigDecimal)}.
     */

    @Test
    public void testKDTreeRange() {
        // (1) Start with very simple tests that reveal fundamental errors.
        // (a) Insert one point in an arbitrary dimensional KD-tree,
        // make sure that you can find it if you draw a circle from
        // the origin.
        for(int dim = 1; dim <= MAX_DIM; dim++){ // For MAX_DIM-many trees...
            for(int i = 0; i < MAX_ITER; i++){ // For MAX_ITER-many points...
                KDPoint originInDim = new KDPoint(dim);
                KDTree tree = new KDTree(dim);
                KDPoint p = getRandomPoint(dim);
                tree.insert(p);
                assertTrue("Failed a range query for a " + dim + "-D tree which only contained " +
                        p + ", KDPoint #" + i + ".", checkRangeQuery(tree, originInDim, rootOfBigDecimal(p.distanceSquared(originInDim)), p));
            }
        }

        // (b) Make a 1-D KD-tree (simple BST). Test Range queries on that one.
        KDPoint[] points = {
                new KDPoint(15.0), new KDPoint(-20.0),
                new KDPoint(-10.0), new KDPoint(30.0),
                new KDPoint(20.0), new KDPoint(30.0)
        };
        KDTree degenerate = new KDTree(1);
        for(KDPoint p: points)
            degenerate.insert(p);

        // A range of 1225 for the left child of the root should cover the root
        // and the last remaining child of the left subtree (points[2]).
        KDPoint root = points[0], leftChildOfRoot = points[1];
        assertTrue("1-D Tree (Simple BST): Failed a range query for the left child of the root.",
                checkRangeQuery(degenerate, leftChildOfRoot, new BigDecimal(1225), root, points[2]));

        // Repeat the same logic on the root for a different range. This time, only the root's
        // right subtree should be contained in the result of the query.
        assertTrue("1-D Tree (Simple BST): Failed a range query for the root.",
                checkRangeQuery(degenerate, root, new BigDecimal(225), points[3], points[4], points[5]));

        // (2) Test my own 2D Tree.
        prepJason2DTree();

        // (a) For every single point in the tree, check to see if the range
        // query starting from the origin satisfies its squared 2-norm.
        KDPoint[] ptsInTree  = {
                new KDPoint(2, 3), new KDPoint(-1, 6),
                new KDPoint(0, 5), new KDPoint(-1, 0),
                new KDPoint(-10, -3), new KDPoint(-4,-3),
                new KDPoint(-6,-6), new KDPoint(-4,-1),
                new KDPoint(1,3)
        };

        KDPoint origin = new KDPoint(2);
        Collection<KDPoint> rQueryRes;
        for(KDPoint p: ptsInTree){
            rQueryRes = jason2DTree.range(origin, rootOfBigDecimal(distanceSquared(p, origin)));
            assertTrue("2-D Tree: We checked whether " + p +
                            " satisfied a range query from the origin and the check failed.",
                    rQueryRes.contains(p));
        }
        // (b) Observe the tree and make some simple, static tests.
        // I've build the spatial decomposition on a piece of paper so I know
        // the following queries should work.
        assertTrue("2-D Tree: Range query failure at 1st quadrant for a KDPoint in the tree.",
                checkRangeQuery(jason2DTree, ptsInTree[8], new BigDecimal(100),
                        ptsInTree[1], ptsInTree[2], ptsInTree[0]));
        assertTrue("2-D Tree: Range query failure at 3rd quadrant for a KDPoint in the tree.",
                checkRangeQuery(jason2DTree, ptsInTree[6], new BigDecimal(5184), ptsInTree[4],
                        ptsInTree[7], ptsInTree[5], ptsInTree[3]));

        // Let's also make some tests where the query point is not a part of the tree.
        // I will make this simple, perturbing the previous query points by EPSILON
        // and making sure the previous origins are now contained in the answer.
        KDPoint newOrigin1 = new KDPoint(plusMinusEps(ptsInTree[8].coords[0]), ptsInTree[8].coords[1]),
                newOrigin2 = new KDPoint(ptsInTree[6].coords[0], plusMinusEps(ptsInTree[6].coords[1]));
        assertTrue("2-D Tree: Range query failure at 1st quadrant for a KDPoint *not* in the tree.",
                checkRangeQuery(jason2DTree, newOrigin1, new BigDecimal(100),
                        ptsInTree[1], ptsInTree[2], ptsInTree[0], ptsInTree[8]));
        assertTrue("2-D Tree: Range query failure at 3rd quadrant for a KDPoint *not* in the tree.",
                checkRangeQuery(jason2DTree, newOrigin2, new BigDecimal(5184),
                        ptsInTree[4], ptsInTree[7], ptsInTree[5], ptsInTree[3], ptsInTree[6]));

        // (3) Test my own 3D Tree. Similar to (2), but in 3 dimensions.
        // I have precomputed this with Python.
        prepJason3DTree();
        assertTrue("We failed our \"point-in-tree\"range query for the 3D Tree.",
                checkRangeQuery(jason3DTree, new KDPoint(8,-2,7), new BigDecimal(233),
                        new KDPoint(2,3,-5), new KDPoint(-1,2,3), new KDPoint(6,0,-1),
                        new KDPoint(-1,-9,2), new KDPoint(-6,4,8), new KDPoint(8,-2,6),
                        new KDPoint(4,1,-2), new KDPoint(-4,1,0), new KDPoint(8,-1,9),
                        new KDPoint(-3,-10,1), new KDPoint(-3,-8,0)));
        assertTrue("We failed our \"point-not-in-tree\"range query for the 3D Tree.",
                checkRangeQuery(jason3DTree, new KDPoint(plusMinusEps(8),plusMinusEps(-2),
                                plusMinusEps(7)), new BigDecimal(233), new KDPoint(8, -2, 7), // Include previous anchor point
                        new KDPoint(2,3,-5), new KDPoint(-1,2,3), new KDPoint(6,0,-1),
                        new KDPoint(-1,-9,2), new KDPoint(-6,4,8), new KDPoint(8,-2,6),
                        new KDPoint(4,1,-2), new KDPoint(-4,1,0), new KDPoint(8,-1,9),
                        new KDPoint(-3,-10,1), new KDPoint(-3,-8,0)));


        // (4) Stress test. Arbitrary dimensions. Mechanically construct KDPoints,
        // Store their pairwise squared euclidean distances,
        // make sure that the range query from every point to another is satisfied.

        for(int dim = 1; dim <= MAX_DIM; dim++){
            KDTree tree = new KDTree(dim);
            HashMap<KDPoint[], BigDecimal> ptPairs = new HashMap<>();
            for(int i = 0; i < MAX_ITER; i++){
                KDPoint p1 = getRandomPoint(dim), p2 = getRandomPoint(dim);
                ptPairs.put(new KDPoint[]{p1, p2}, distanceSquared(p1, p2));
                tree.insert(p1);
                tree.insert(p2);
            }
            for(KDPoint[] pair : ptPairs.keySet())
                assertTrue("In a " + dim + "-D tree, we failed a range query for: "
                                + pair[0] + " and " + pair[1] + ".",
                        checkRangeQuery(tree, pair[0], rootOfBigDecimal(distanceSquared(pair[0], pair[1])), pair[1]));
        }
    }



/**
     * Test method for {@link KDTree#nearestNeighbor(KDPoint)}.
     */

    @Test
    public void testKDTreeNearestNeighbor() {

        // Test for both points in the tree as well as points not in the tree.

        // (1) 1-D KD-tree (simple BST). Insert 10 points in a BST with an incremental
        // gap between them, check nearest neighbor for every one. Repeat this MAX_ITER-many times.

        for(int i = 0; i <MAX_ITER; i++){
            LinkedList<KDPoint> randPts = new LinkedList<>();
            KDTree tree = new KDTree(1);
            for(int j = 0; j < 10; j++){
                KDPoint randPt = getRandomPoint(1);
                randPts.add(randPt);
                tree.insert(randPt);
            }
            // Now, sort the points based on the distanceSquared to the first point.
            //Collections.sort(randPts, new KNNComparator<>(randPts.get(0)));
            randPts.sort(new KNNComparator<>(randPts.get(0)));
            // The KDPoint generated from KDTree.nearestNeighbor(KDPoint) should then
            // be equal to randPts.get(1), the closest point after the sorting that's not
            // the point itself.
            assertEquals("1-D Tree (Classic BST): Failed on a nearest neighbor query for iteration #"
                            + i + " with anchor point " + randPts.get(0) + ".", randPts.get(1),
                    tree.nearestNeighbor(randPts.get(0)));

        }

        // (2) Dave Mount's tree
        // Points in tree mapped to their nearest neighbor.
        prepMountTree();
        assertEquals("First NN query for Mount tree failed.",
                new KDPoint(75, 10), mount2DTree.nearestNeighbor(new KDPoint(70, 20)));
        assertEquals("Second NN query for Mount tree failed.",
                new KDPoint(35,60), mount2DTree.nearestNeighbor(new KDPoint(60,80)));

        // Slightly perturbed previous anchor points now should map to
        // the previous anchor points in terms of nearest neighbor.
        KDPoint p1 = new KDPoint(plusMinusEps(75), plusMinusEps(10)),
                p2 = new KDPoint(plusMinusEps(35), plusMinusEps(60));
        assertEquals("Third NN query for Mount tree failed.", new KDPoint(75.0, 10.0), mount2DTree.nearestNeighbor(p1));
        assertEquals("Fourth NN query for Mount tree failed.", new KDPoint(35.0, 60.0), mount2DTree.nearestNeighbor(p2));

        // (3) My 2D Tree
        prepJason2DTree();
        //Same deal. Two nearest neighbor queries for points in the tree,
        // and two for points not in the tree.
        assertEquals("First NN query for custom 2D tree failed.", new KDPoint(2, 3),
                jason2DTree.nearestNeighbor(new KDPoint(1, 3)));
        assertEquals("Second NN query for custom 2D tree failed.", new KDPoint(-4, -1),
                jason2DTree.nearestNeighbor(new KDPoint(-4, -3)));

        p1 = new KDPoint(plusMinusEps(1), plusMinusEps(3));
        p2 = new KDPoint(); // 2D Origin
        assertEquals("Third NN query for custom 2D tree failed.", new KDPoint(1,3),
                jason2DTree.nearestNeighbor(p1));
        assertEquals("Fourth NN query for custom 2D tree failed.", new KDPoint(-1,0),
                jason2DTree.nearestNeighbor(p2));

        // (4) My 3D Tree
        prepJason3DTree();
        // Same deal.
        assertEquals("First NN query for custom 3D tree failed.", new KDPoint(8,-2,6),
                jason3DTree.nearestNeighbor(new KDPoint(8,-2,7)));
        assertEquals("Second NN query for custom 3D tree failed.", new KDPoint(-1,2,3),
                jason3DTree.nearestNeighbor(new KDPoint(-4,1,0)));
        p1 = new KDPoint(plusMinusEps(8), plusMinusEps(-2 ), plusMinusEps(7));
        p2 = new KDPoint(plusMinusEps(-4 ), plusMinusEps(1), plusMinusEps(0) );
        assertEquals("Third NN query for custom 3D tree failed.", new KDPoint(8,-2,7),
                jason3DTree.nearestNeighbor(p1));
        assertEquals("Fourth NN query for custom 3D tree failed.", new KDPoint(-4,1,0),
                jason3DTree.nearestNeighbor(p2));
    }

    /**
     * Test method for {@link KDTree#kNearestNeighbors(int, KDPoint)}.
     */

    @Test
    public void testKDTreeKNearestNeighbors() {
        // Straightforward modification of testNearestNeighbor().

        // (1) 1-D Tree (Simple BST).
        for(int i = 0; i <MAX_ITER; i++){
            ArrayList<KDPoint> randPts = new ArrayList<>();
            KDTree tree = new KDTree(1);
            for(int j = 0; j < 10; j++){
                KDPoint randPt = getRandomPoint(1);
                randPts.add(randPt);
                tree.insert(randPt);
            }

            // Now, sort the points based on the distanceSquared to the first point.
            randPts.sort(new KNNComparator<>(randPts.get(0)));

            // And check whether all K-NN queries for K=2,3,...9 are satisfied.
            for(int k = 2; k <=9; k++)
                assertTrue("1-D Tree (Classic BST): In iteration " + i + ", we failed our " + k + "-NN query for " +
                        randPts.get(0) + ".", checkKNNQuery(tree, randPts.get(0), k, randPts.subList(1, k+1)));
        }

        // (2) Dave Mount's tree, point-in-tree and not-in-tree.
        prepMountTree();
        assertTrue("Failed 2-NN query for point-in-Mount tree.", // Point in tree
                checkKNNQuery(mount2DTree, new KDPoint(20,45), 2, new KDPoint(10,35), new KDPoint(35,60)));
        assertTrue("Failed 3-NN query for point-in-Mount tree.", // Point not in tree
                checkKNNQuery(mount2DTree, new KDPoint(20,45 + EPSILON), 3,
                        new KDPoint(20, 45), new KDPoint(10,35), new KDPoint(35,60)));

        // (3) My 2D Tree, point-in-tree and point-not-in-tree.
        prepJason2DTree();
        assertTrue("Failed 3-NN query for point-in-Jason's 2D tree.",checkKNNQuery(jason2DTree,
                new KDPoint(-4,-3), 3,	new KDPoint(-4,-1), new KDPoint(-6,-6), new KDPoint(-1,0)));
        assertTrue("Failed 4-NN query for point-not-in-Jason's 2D tree.",checkKNNQuery(jason2DTree, new KDPoint(-4 - EPSILON, -3), 4,
                new KDPoint(-4, -3), new KDPoint(-4,-1), new KDPoint(-6,-6), new KDPoint(-1,0)));

        // (4) My 3D Tree. same deal.
        prepJason3DTree();
        assertTrue("Failed 4-NN query for point-in-Jason's 3D tree.", checkKNNQuery(jason3DTree,
                new KDPoint(-3,-8,-3), 4, new KDPoint(-3,-8,0), new KDPoint(-3,-10,1), new KDPoint(-1,-9,2), new KDPoint(-4, 1, 0)));
        assertTrue("Failed 5-NN query for point-not-in-Jason's 3D tree.", checkKNNQuery(jason3DTree,
                new KDPoint(-3,-8,-3 - EPSILON), 5, new KDPoint(-3,-8,-3), new KDPoint(-3,-8,0),
                new KDPoint(-3,-10,1), new KDPoint(-1,-9,2), new KDPoint(-4, 1, 0)));

        // (5) Stress test for all dimensions, with intermediate neighbor sorting
        // based on custom comparator.
        for(int dim = 1; dim <= MAX_DIM; dim++){
            KDTree tree = new KDTree(dim);
            LinkedList<KDPoint> ptCollection = new LinkedList<>();
            for(int i = 0; i < MAX_ITER; i++){
                KDPoint randPt = getRandomPoint(dim);
                ptCollection.add(randPt);
                tree.insert(randPt);
            }
            // We sort all the KDPoints based on their distances to the first one.
            ptCollection.sort(new KNNComparator<>(ptCollection.get(0)));

            // And then we check if all possible k-nn queries, for k = 1 to 11, are satisfied.
            // This can be improved, so that the upper bound of k is also MAX_ITER!
            for(int k = 1; k < 12; k++)
                assertTrue("Stress test for " + dim + "-D tree: Failed a " + k +"-NN query " +
                                "when anchoring around " + ptCollection.get(0) + ".",
                        checkKNNQuery(tree, ptCollection.get(0), k, ptCollection.subList(1, k+1)));
        }

    }



        /**
     * Test method for {@link KDTree#height()}.
     */

    @Test
    public void testKDTreeHeight() {


        /* Tests for height equal to -1 for empty trees have already
         * occurred in testIsEmpty(), so we won't do that here.
         * Instead:
         */

        /*  **** (1) Check if a stub tree has a height of 0.**********
         ****** Do that with a bunch of valid dimensionality trees.
         */

        for(int i = 0; i < MAX_ITER; i++) {
            int randDim = r.nextInt(MAX_DIM) + 1; // [1, ..., MAX_DIM]
            KDTree tempTree = new KDTree(randDim);
            tempTree.insert(new KDPoint(randDim));
            assertEquals("A KD-tree of dimensionality " + randDim + " with one element "
                    + " inside it was found to have a height of : " + tempTree.height() +
                    " instead of 0", 0, tempTree.height());
        }


        /* **** (2) Three short trees. We will perform many random ********************
         ***** tests, by discriminating on the x axis by the smallest amounts possible
         ***** to make it so that we build appropriately shaped trees.****************/


        for(int i = 0; i < MAX_ITER; i++){

            // (a) Right-heavy.
            double xVal = r.nextDouble();
            twoDTree.insert(new KDPoint(xVal, r.nextDouble()));
            twoDTree.insert(new KDPoint(xVal + EPSILON, r.nextDouble())); // Barely larger on x!
            assertEquals("On iteration " + i + ", we created what should have been a right-heavy 2D-"
                    + "tree with height 1, but the height was: " + twoDTree.height() + ".",	1, twoDTree.height());

            // (b) Left-heavy.
            threeDTree.insert(new KDPoint(xVal, r.nextDouble(), r.nextDouble()));
            threeDTree.insert(new KDPoint(xVal - EPSILON, r.nextDouble(), r.nextDouble())); // Barely smaller on x!
            assertEquals("On iteration " + i + ", we created what should have been a left-heavy 3D-"
                    + "tree with height 1, but the height was: " + threeDTree.height() + ".", 1, threeDTree.height());

            // (c) Balanced.
            KDPoint first = new KDPoint(xVal, r.nextDouble(), r.nextDouble(), r.nextDouble());
            fourDTree.insert(first);
            KDPoint second = new KDPoint(xVal - 1000*EPSILON, r.nextDouble(), r.nextDouble(), r.nextDouble());
            assertNotEquals("Something's not right here", first.coords[0], second.coords[0]);
            fourDTree.insert(second);
            fourDTree.insert(new KDPoint(xVal + EPSILON, r.nextDouble(), r.nextDouble(), r.nextDouble()));
            assertEquals("On iteration " + i + ", we created what should have been a balanced 4D-"
                    + "tree with height 1, but the height was: " + fourDTree.height() + ".", 1, fourDTree.height());

            // (d) Right-heavy, but with a height of 2.
            fourDTree = new KDTree(4); // In the absence of a clear() method...
            double yVal = r.nextDouble();
            fourDTree.insert(new KDPoint(xVal, r.nextDouble(), r.nextDouble(), r.nextDouble()));
            fourDTree.insert(new KDPoint(xVal - EPSILON, r.nextDouble(), r.nextDouble(), r.nextDouble()));
            fourDTree.insert(new KDPoint(xVal + EPSILON, yVal, r.nextDouble(), r.nextDouble()));
            fourDTree.insert(new KDPoint(xVal + EPSILON, yVal + EPSILON, r.nextDouble(), r.nextDouble())); // Guarantees right-heaviness
            assertEquals("On iteration " + i + ", we created what should have been a right-heavy 4D-"
                    + "tree with height 2, but the height was: " + fourDTree.height() + ".", 2, fourDTree.height());

            // (e) Left-heavy, but with a height of 2.
            fourDTree = new KDTree(4);
            fourDTree.insert(new KDPoint(xVal, r.nextDouble(), r.nextDouble(), r.nextDouble()));
            fourDTree.insert(new KDPoint(xVal - EPSILON, yVal, r.nextDouble(), r.nextDouble()));
            fourDTree.insert(new KDPoint(xVal + EPSILON, r.nextDouble(), r.nextDouble(), r.nextDouble()));
            fourDTree.insert(new KDPoint(xVal - EPSILON, yVal - EPSILON, r.nextDouble(), r.nextDouble())); // Guarantees left-heaviness
            assertEquals("On iteration " + i + ", we created what should have been a left-heavy 4D-"
                    + "tree with height 2, but the height was: " + fourDTree.height() + ".", 2, fourDTree.height());

            // (f) Balanced, but with a height of 2.
            double yVal2 = yVal + 3*EPSILON;
            fourDTree = new KDTree(4);
            fourDTree.insert(new KDPoint(xVal, r.nextDouble(), r.nextDouble(), r.nextDouble()));
            fourDTree.insert(new KDPoint(xVal - EPSILON, yVal, r.nextDouble(), r.nextDouble()));
            fourDTree.insert(new KDPoint(xVal + EPSILON, yVal2, r.nextDouble(), r.nextDouble()));
            fourDTree.insert(new KDPoint(xVal - EPSILON, yVal - EPSILON, r.nextDouble(), r.nextDouble()));
            fourDTree.insert(new KDPoint(xVal - EPSILON, yVal + EPSILON, r.nextDouble(), r.nextDouble()));
            fourDTree.insert(new KDPoint(xVal + EPSILON, yVal2-EPSILON, r.nextDouble(), r.nextDouble()));
            fourDTree.insert(new KDPoint(xVal + EPSILON, yVal2+EPSILON, r.nextDouble(), r.nextDouble()));
            assertEquals("On iteration " + i + ", we created what should have been a balanced 4D-"
                    + "tree with height 2, but the height was: " + fourDTree.height() + ".", 2, fourDTree.height());

            // Reset trees.
            try {
                tearDown();
                setUp();
            }catch(Throwable thr){
                fail("Caught an instance of " + thr.getClass() + " with message: " + thr.getMessage() + ".");
            }
        } // for i = 0... MAX_ITER - 1


        // Mount's tree's and Jason's trees' height checks:

        prepMountTree();
        prepJason2DTree();
        prepJason3DTree();
        assertEquals("Mismatch between expected and actual tree height for a 2D-tree.", 5, mount2DTree.height());
        assertEquals("Mismatch between expected and actual tree height for a 2D-tree.", 6, jason2DTree.height());
        assertEquals("Mismatch between expected and actual tree height for a 3D-tree.", 5, jason3DTree.height());
    }


    /**
     * Test method for {@link  KDTree#isEmpty()}.
     */

    @Test
    public void testKDTreeIsEmpty() {
        // Check if all our freshly built trees are empty.
        assertTrue("Freshly created 2D tree found to not be empty.", twoDTree.isEmpty());
        assertTrue("Freshly created 3D tree found to not be empty.", threeDTree.isEmpty());
        assertTrue("Freshly created 4D tree found to not be empty.", fourDTree.isEmpty());

        // What about getRoot()? Is it behaving as expected?
        assertNull("Root of an empty 2D tree was not null.", twoDTree.getRoot());
        assertNull("Root of an empty 3D tree was not null.", threeDTree.getRoot());
        assertNull("Root of an empty 4D tree was not null.", fourDTree.getRoot());

        // Check if all our freshly build trees also have a
        // height of -1. This is making sure that the definition
        // of isEmpty(), as mentioned in the docs, is satisfied.
        assertEquals("Freshly created 2D tree found to not have a height of -1.",
                -1, twoDTree.height());
        assertEquals("Freshly created 3D tree found to not have a height of -1.",
                -1,	threeDTree.height());
        assertEquals("Freshly created 4D tree to not have a height of -1.",
                -1, fourDTree.height());

        // For a bunch of trees of valid dimensionalities, make sure the
        // invariants for isEmpty() are satisfied.
        for(int i = 0; i < MAX_ITER; i++){
            int randDim = r.nextInt(MAX_DIM) + 1; // [1,..., MAX_DIM]
            KDTree tempTree = new KDTree(randDim);
            assertTrue("Freshly created KDTree of dimensionality " +
                    randDim + " was found to not be empty.", tempTree.isEmpty());
            assertEquals("Freshly created KDTree of dimensionality " +
                    randDim + " was found to not have a height of -1.", -1, tempTree.height());
            assertNull("Root of freshly created KDTree of dimensionality " +
                    randDim + " was found to not be null.", tempTree.getRoot());
        }

        // (4) Finally, make sure that trees with elements in them are not
        // considered empty. Try this with our three pre-existing trees.
        for(int i = 0; i < MAX_ITER; i++){
            twoDTree.insert(new KDPoint(r.nextInt(), r.nextInt()));
            assertFalse("2D tree with one KDPoint in it was found to be empty.", twoDTree.isEmpty());
            twoDTree.insert(new KDPoint(r.nextInt(), r.nextInt()));
            assertFalse("2D tree with two KDPoints in it was found to be empty.", twoDTree.isEmpty());

            threeDTree.insert(new KDPoint(r.nextInt(), r.nextInt(), r.nextInt()));
            assertFalse("3D tree with one KDPoint in it was found to be empty.", threeDTree.isEmpty());
            threeDTree.insert(new KDPoint(r.nextInt(), r.nextInt(), r.nextInt()));
            assertFalse("3D tree with two KDPoints in it was found to be empty.", threeDTree.isEmpty());

            fourDTree.insert(new KDPoint(r.nextInt(), r.nextInt(), r.nextInt(), r.nextInt()));
            assertFalse("4D tree with one KDPoint in it was found to be empty.", fourDTree.isEmpty());
            fourDTree.insert(new KDPoint(r.nextInt(), r.nextInt(), r.nextInt(), r.nextInt()));
            assertFalse("4D tree with two KDPoints in it was found to be empty.", fourDTree.isEmpty());

        }

    }


/**
     * Test an empty PR-QuadTree.
     */

    @Test
    public void testPRQEmptyPRQuadTree(){
        // TODO: These are pretty useless for students since they just check the provided implementation of the PR-QuadTree.
        // TODO: Erase them once you are certain that the provided implementation of PR-QuadTree makes sense.
        assertNotNull("Tree reference should be non-null by setUp() method.", quadTree);
        assertTrue("A freshly created PR-QuadTree should be empty!", quadTree.isEmpty());
        assertEquals("An empty quadTree's count should be 0.", 0, quadTree.count());
        assertEquals("An empty quadTree's height should be -1.",  -1 , quadTree.height() );
        assertFalse("No KDPoints should be able to be found in an empty PR-QuadTree.", quadTree.search(getRandomKDPoint()));
    }


    @Test
    public void testPRQSingleBlackNode(){
        assertNotNull("Tree reference should be non-null by setUp() method.", quadTree);
        KDPoint rand = getRandomKDPoint();
        try {
            quadTree.insert(rand);
        }catch(Throwable t){
            fail("Caught a " + getThrowableClassName(t) + "with message:" + t.getMessage() + " when inserting " + rand
                    + "into an initially empty PRQuadTree.");
        }
        assertFalse("A PR-QuadTree with a single KDPoint stored should not be empty!", quadTree.isEmpty());
        assertEquals("A PR-QuadTree with a single KDPoint stored should have a count of 1.", 1, quadTree.count());
        assertEquals("A PR-QuadTree with a single KDPoint stored should have a height of 0.",  0 , quadTree.height() );
        assertTrue("After inserting " + rand + " into an initially empty PR-QuadTree, we should be able to find it in the quadTree.", quadTree.search(rand));

        KDPoint rand2 = getRandomKDPoint();
        quadTree.insert(rand2); // Default bucket size is 2, so this should still give out a stub quadTree.
        assertFalse("A PR-QuadTree with a bucket size of 2 and two KDPoints stored should not be empty!", quadTree.isEmpty());
        assertEquals("A PR-QuadTree with a bucket size of 2 and two KDPoints stored should have a count of 2.", 2, quadTree.count());
        assertEquals("A PR-QuadTree with a bucket size of 2 and two KDPoints stored should have a height of 0.",  0 , quadTree.height() );
        assertTrue("After inserting " + rand + " and " + rand2 + " into an initially empty PR-QuadTree with a bucket size of 2, we should be able to find *BOTH* in the quadTree.", quadTree.search(rand) && quadTree.search(rand2));
    }

    @Test
    public void testPRQDuplicateInsertions(){
        assertNotNull("Tree reference should be non-null by setUp() method.", quadTree);
        KDPoint rand = getRandomKDPoint();
        quadTree.insert(rand);
        try {
            quadTree.insert(rand);
        } catch(Throwable t){
            fail("While inserting " + rand + " *TWICE* into an initially empty PR-QuadTree, we encountered a " + getThrowableClassName(t) + ".");
        }
        assertEquals("When inserting duplicates into a PR-QuadTree, the duplicate element should not be inserted, " +
                "and the quadTree's KDPoint count should be unaffected.", 1, quadTree.count());
        assertEquals("When inserting duplicates into a PR-QuadTree, the duplicate element should not be inserted, " +
                "and the quadTree's height should be unaffected.", 0, quadTree.height());
    }


/* A private method useful in the next few tests. */


/* Invariant: varargs parameter "points" does NOT contain duplicates! */

    private boolean checkPRQuadTree(PRQuadTree tree, int expectedHeight, KDPoint... points){
        if(tree == null || tree.height() != expectedHeight || tree.count() != points.length)
            return false;
        for(KDPoint p : points)
            if(!tree.search(p))
                return false;
        return true;
    }

    @Test
    public void testPRQInsertionsWithoutSplits(){
        assertNotNull("Tree reference should be non-null by setUp() method.", quadTree);
        KDPoint[] points = { new KDPoint(-1, 1), new KDPoint(1, 1),
                new KDPoint(-1, -1), new KDPoint(1, -1),
                new KDPoint(-2, 2), new KDPoint(2, 2),
                new KDPoint(-2, -2), new KDPoint(2, -2) };
        Arrays.asList(points).forEach(quadTree::insert);
        // After the above for-each loop, we should have a gray node with 4 black node children.
        assertTrue("Check the behavior of a PRQuadTree of bucket size 2 with a gray root and 4 black children.",
                checkPRQuadTree(quadTree, 1, points));
    }

    @Test
    public void testPRQOneRecursiveSplitNW(){
        assertNotNull("Tree reference should be non-null by setUp() method.", quadTree);
        KDPoint[] points = { new KDPoint(-14, 14), new KDPoint(-9, 10), new KDPoint(-7, 10)};
        for(KDPoint p : points)
            quadTree.insert(p);
        assertTrue("Check your quadTree's height, count, and contained KDPoints when one recursive split of the NW Quadrant is required.",
                checkPRQuadTree(quadTree, 2, points));
    }

    @Test
    public void testPRQOneRecursiveSplitNE(){
        assertNotNull("Tree reference should be non-null by setUp() method.", quadTree);
        KDPoint[] points = { new KDPoint(2, 3), new KDPoint(12, 10), new KDPoint(12, 3)};
        for(KDPoint p : points)
            quadTree.insert(p);
        assertTrue("Check your quadTree's height, count, and contained KDPoints when one recursive split of the NE Quadrant is required.",
                checkPRQuadTree(quadTree, 2, points));
    }

    @Test
    public void testPRQOneRecursiveSplitSW(){
        assertNotNull("Tree reference should be non-null by setUp() method.", quadTree);
        KDPoint[] points = { new KDPoint(-2, -3), new KDPoint(-12, -10), new KDPoint(-1, -2) };
        for(KDPoint p : points)
            quadTree.insert(p);
        assertTrue("Check your quadTree's height, count, and contained KDPoints when one recursive split of the SW Quadrant is required.",
                checkPRQuadTree(quadTree, 2, points));
    }

    @Test
    public void testPRQOneRecursiveSplitSE(){
        assertNotNull("Tree reference should be non-null by setUp() method.", quadTree);
        KDPoint[] points = { new KDPoint(2, -3), new KDPoint(12, -10), new KDPoint(1, -2) };
        for(KDPoint p : points)
            quadTree.insert(p);
        assertTrue("Check your quadTree's height, count, and contained KDPoints when one recursive split of the SE Quadrant is required.",
                checkPRQuadTree(quadTree, 2, points));
    }

    @Test
    public void testPRQDoubleRecursiveSplitNW(){
        assertNotNull("Tree reference should be non-null by setUp() method.", quadTree);
        KDPoint[] points = { new KDPoint(-15, 15), new KDPoint(-14, 14), new KDPoint(-11, 11)};
        for(KDPoint p : points)
            quadTree.insert(p);
        assertTrue("Check your quadTree's height, count, and contained KDPoints when a double recursive split of the NW Quadrant is required.",
                checkPRQuadTree(quadTree, 3, points));
    }

    @Test
    public void testPRQDoubleRecursiveSplitNE(){
        assertNotNull("Tree reference should be non-null by setUp() method.", quadTree);
        KDPoint[] points = { new KDPoint(15, 15), new KDPoint(14, 14), new KDPoint(11, 11)};
        for(KDPoint p : points)
            quadTree.insert(p);
        assertTrue("Check your quadTree's height, count, and contained KDPoints when a double recursive split of the NE Quadrant is required.",
                checkPRQuadTree(quadTree, 3, points));
    }

    @Test
    public void testPRQDoubleRecursiveSplitSW(){
        assertNotNull("Tree reference should be non-null by setUp() method.", quadTree);
        KDPoint[] points = { new KDPoint(-15, -15), new KDPoint(-14, -14), new KDPoint(-11, -11)};
        for(KDPoint p : points)
            quadTree.insert(p);
        assertTrue("Check your quadTree's height, count, and contained KDPoints when a double recursive split of the SW Quadrant is required.",
                checkPRQuadTree(quadTree, 3, points));
    }

    @Test
    public void testPRQDoubleRecursiveSplitSE(){
        assertNotNull("Tree reference should be non-null by setUp() method.", quadTree);
        KDPoint[] points = { new KDPoint(15, -15), new KDPoint(14, -14), new KDPoint(11, -11)};
        for(KDPoint p : points)
            quadTree.insert(p);
        assertTrue("Check your quadTree's height, count, and contained KDPoints when a double recursive split of the SW Quadrant is required.",
                checkPRQuadTree(quadTree, 3, points));
    }

    @Test
    public void testPRQPointsOnQuadrantBorders() {
        assertNotNull("Tree reference should be non-null by setUp() method.", quadTree);
        KDPoint[] points = {new KDPoint(10, 10), new KDPoint(6, 5),
                new KDPoint(-2, -3), new KDPoint(1, 1), new KDPoint(0, 3)}; // Ensure (0, 3) gets to the appropriate quadrant
        for (KDPoint p : points)
            quadTree.insert(p);
        assertTrue("Make sure that any point with an 'x' value that puts it on a VERTICAL quadrant separator is assigned to the RIGHT quadrant",
                checkPRQuadTree(quadTree, 3, points));
        quadTree.insert(new KDPoint(2, 0));
        KDPoint[] allPts = new KDPoint[points.length + 1];
        System.arraycopy(points, 0, allPts, 0, points.length); // Does deep copy of elements, unlike clone()!
        allPts[allPts.length - 1] = new KDPoint(2, 0);
        assertTrue("Make sure that any point with an 'x' value that puts it on a HORIZONTAL quadrant separator is assigned to the TOP quadrant",
                checkPRQuadTree(quadTree, 4, allPts));
    }


    @Test
    public void testPRQDeletionsWithoutNodeMerges(){

        assertNotNull("Tree reference should be non-null by setUp() method.", quadTree);
        KDPoint[] origInsertedPts = { new KDPoint(-1, 1), new KDPoint(1, 1),
                new KDPoint(-1, -1), new KDPoint(1, -1),
                new KDPoint(-2, 2), new KDPoint(2, 2),
                new KDPoint(-2, -2), new KDPoint(2, -2) };
        Arrays.asList(origInsertedPts).forEach(quadTree::insert);

        quadTree.delete(new KDPoint(2, -2));

        KDPoint[] remainingPts = new KDPoint[]{
                new KDPoint(-1, 1), new KDPoint(1, 1),
                new KDPoint(-1, -1), new KDPoint(1, -1),
                new KDPoint(-2, 2), new KDPoint(2, 2),
                new KDPoint(-2, -2)
        };
        assertTrue("Make sure that you handle deletions that should NOT trigger node merges correctly.",
                checkPRQuadTree(quadTree, 1, remainingPts));

        KDPoint[] KDPointsToDel = {  new KDPoint(-2, 2), new KDPoint(2, 2),
                new KDPoint(-2, -2)};

        Arrays.asList(KDPointsToDel).forEach(quadTree::delete);

        for(KDPoint p : KDPointsToDel)
            assertFalse("We deleted " + p + " from a PR-QuadTree with bucketing parameter 2, but searching for it still succeeded. " +
                    "Remember: NO DUPLICATES ALLOWED in a PR-QuadTree!", quadTree.search(p));
    }


/* ************************************************************************************************************** */


/* ************************************************************************************************************** */


/* ***************************************** PR-QuadTree Tests ************************************************** */


/* ************************************************************************************************************** */


/* ************************************************************************************************************** */


    @Test
    public void testPRQDeletionsWithNodeMerges(){
        assertNotNull("Tree reference should be non-null by setUp() method.", quadTree);
        KDPoint[] points = { new KDPoint(2, 3), new KDPoint(12, 10), new KDPoint(12, 3)};
        for(KDPoint p : points)
            quadTree.insert(p);
        KDPoint toDel = new KDPoint(12, 3);
        quadTree.delete(toDel);
        KDPoint[] shouldStillBeInThere = {new KDPoint(2, 3), new KDPoint(12, 10) };
        assertTrue("Make sure that a node merge at a SINGLE level results in correct height, count and makes search() " +
                        "work as expected for the remaining elements.",
                checkPRQuadTree(quadTree, 0, shouldStillBeInThere));
        assertFalse("We deleted " + toDel + " from a PR-QuadTree with bucketing parameter 2, but searching for it still succeeded. " +
                "Remember: NO DUPLICATES ALLOWED in a PR-QuadTree!", quadTree.search(toDel));
    }

    @Test
    public void testPRQFewInsertionsAndDeletions(){

        assertNotNull("Tree reference should be non-null by setUp() method.", quadTree);


/* We will construct a scenario with one deletion that should affect the quadTree's height (Gray Node collapse) and
         * one that should not affect it (Gray Node sustained).
         */


        KDPoint[] kdPoints = {
                new KDPoint(-4, 14), new KDPoint(-2, 6), new KDPoint(-6, 2),
                new KDPoint(-15, 7), new KDPoint(-11, 7), new KDPoint(-11, 2),
                new KDPoint(-14, -14)
        };

        Arrays.asList(kdPoints).forEach(quadTree::insert);

        assertTrue("Make sure that your insertions work as expected, with appropriate splits along the way.",
                checkPRQuadTree(quadTree, 3, kdPoints));
        try {
            quadTree.delete(new KDPoint(-11, 7));
        } catch(Throwable t){
            fail("Caught a " + t.getClass().getSimpleName() + " with message: " + t.getMessage() +
                    " while deleting " + new KDPoint(-11, 7) + " which was on its own" +
                    " in a black node. The quadTree's bucketing parameter is: " + quadTree.getBucketSize() + ".");
        }

        KDPoint[] kdPointsAfterFirstDel = {
                new KDPoint(-4, 14), new KDPoint(-2, 6), new KDPoint(-6, 2),
                new KDPoint(-15, 7), new KDPoint(-11, 2), new KDPoint(-14, -14)
        };

        assertTrue("Make sure that a gray node with ONLY white or black children, where the children COLLECTIVELY have fewer than or equal " +
                "to \"bucket_param\" KDPoints is collapsed.",   checkPRQuadTree(quadTree, 2, kdPointsAfterFirstDel));


        try {
            quadTree.delete(new KDPoint(-2, 6));
        } catch(Throwable t){
            fail("Caught a " + t.getClass().getSimpleName() + " with message: " + t.getMessage()
                    + " while deleting " + new KDPoint(-2, 6) + " which was coupled" +
                    "with another KDPoint in a black node. The quadTree's bucketing parameter is: "
                    + quadTree.getBucketSize() + ".");
        }

        KDPoint[] kdPointsAfterSecondDel = {
                new KDPoint(-4, 14), new KDPoint(-6, 2),
                new KDPoint(-15, 7), new KDPoint(-11, 2), new KDPoint(-14, -14)
        };

        assertTrue("Make sure that a gray node with ONLY white or black children, where the children COLLECTIVELY have MORE " +
                "than \"bucket_param\" KDPoints is NOT collapsed.",   checkPRQuadTree(quadTree, 2, kdPointsAfterSecondDel));
    }


    @Test
    public void testPRQManyInsertionsAndDeletions(){
        assertNotNull("Tree reference should be non-null by setUp() method.", quadTree);
        double spaceLength = Math.pow(2, DEFAULT_K);
        List<KDPoint> list = new LinkedList<>();
        for(int i = 0; i < MAX_POINTS; i++)
            list.add( new KDPoint(spaceLength * r.nextDouble() - spaceLength / 2,
                    (spaceLength * r.nextDouble() - spaceLength / 2 )));
        Set<KDPoint> kdPoints = new HashSet<>(list); // Erase duplicates based on equals().

        // Insert them all, check for Throwables.
        for(KDPoint pt: kdPoints){
            try {
                quadTree.insert(pt);
            } catch(Throwable t){
                fail("Caught a " + t.getClass().getSimpleName() + " with message: " + t.getMessage() +
                        " while inserting " + pt + " in a PR-QuadTree with bucketing parameter " + quadTree.getBucketSize() + ".");
            }
        }

        // Ensure searches for all are successful an produce an empty PR-QuadTree..

        for(KDPoint pt: kdPoints)
            assertTrue("Failed the search for " + pt + " in a PR-QuadTree with bucketing parameter " +
                    quadTree.getBucketSize() + " where the KDPoint was successfully inserted.", quadTree.search(pt));


        // Delete all of them, check for Throwables.

        for(KDPoint pt: kdPoints){
            try {
                quadTree.delete(pt);
            } catch(Throwable t){
                fail("Caught a " + t.getClass().getSimpleName() + " with message: " + t.getMessage() +
                        " while deleting " + pt + " from a PR-QuadTree with bucketing parameter " + quadTree.getBucketSize() + ".");
            }
        }


        // Ensure that after deletion of all elements, none of the previous elements can be found.

        for(KDPoint pt: kdPoints)
            assertFalse("Succeeded the search for " + pt + " in a PR-QuadTree with bucketing parameter " +
                    quadTree.getBucketSize() + " where the KDPoint has been deleted.", quadTree.search(pt));

        assertEquals("After deleting all of the KDPoints in a PR-QuadTree, it's height should be -1.",
                -1, quadTree.height());
        assertEquals("After deleting all of the KDPoints in a PR-QuadTree, it's count should be 0.",
                0, quadTree.count());

        assertTrue("After deleting all of the KDPoints in a PR-QuadTree, it should be considered empty",
                quadTree.isEmpty());
    }



}
