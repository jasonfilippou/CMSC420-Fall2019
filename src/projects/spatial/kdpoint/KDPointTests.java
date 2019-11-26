package projects.spatial.kdpoint;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Random;

import static org.junit.Assert.*;
import static projects.spatial.kdpoint.KDPoint.distanceSquared;

/**
 * <p>{@link KDPointTests} is a unit testing class for {@link KDPoint}. It has next to nothing
 * to do with your project and is just here to provide some confidence about the fact that
 * {@link KDPoint}s should work as advertised. </p>
 *
 * <p>It might be of interest to you to see how the method {@link Assert#assertEquals(double, double, double)}
 * can be used when comparing doubles in Java. It turns out that comparing doubles in Java is not particularly safe
 * because of precision issues. In fact, it is recommended that one uses {@link java.math.BigDecimal} instead,
 * which offers arbitrary long precision.</p>
 *
 * @author <a href = "https://github.com/JasonFil">Jason Filippou</a>
 */
public class KDPointTests {

	private KDPoint origin2D, origin3D;
	private Random r;
	private static final int SEED = 47;
	private static final int MAX_ITER = 100000;
	private static final int MAX_DIM = 1000;

	@Before

	public void setUp()  {
		origin2D = new KDPoint();
		origin3D = new KDPoint(3);
		r = new Random(SEED); // Re-producible results via static seed.
	}

	@After

	public void tearDown()  {
		origin2D = origin3D = null;
		r = null;
	}

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
		KDPoint point = new KDPoint(2.12, -9.23, 0.01, -34);
		assertEquals("The length of KDPoint with 4 dimensions must be 4",4, point.coords.length);
		assertEquals("The first dimension's value should have been 2.12 for point (2.12, -9.23, 0.01, -34)", new BigDecimal(2.12), point.coords[0]);
		assertEquals("The second dimension's value should have been -9.23 for point (2.12, -9.23, 0.01, -34)",new BigDecimal(-9.23), point.coords[1]);
		assertEquals("The third dimension's value should have been 0.01 for point (2.12, -9.23, 0.01, -34)",new BigDecimal(0.01), point.coords[2]);
		assertEquals("The fourth dimension's value should have been -34 for point (2.12, -9.23, 0.01, -34)", new BigDecimal(-34), point.coords[3]);
	}

	@Test
	public void testKDPointKDPoint() {
		assertEquals("KDPoint created from copy constructor should have been equal to the original",new KDPoint(origin2D), origin2D);
		assertEquals("KDPoint created from copy constructor should have been equal to the original",new KDPoint(origin3D), origin3D);
		KDPoint fourDPoint = new KDPoint(-20.45, 6.78, 0.56, -9.76);
		assertEquals("KDPoint created from copy constructor should have been equal to the original", new KDPoint(fourDPoint),fourDPoint);

	}

	@Test
	public void testKDPointDistanceKDPoint() {

		// Trivial zero distances
		String messageTrivialDistance = "The distance between a point and itself must be 0";
		assertEquals(messageTrivialDistance, BigDecimal.ZERO, origin2D.distanceSquared(origin2D));
		assertEquals(messageTrivialDistance, BigDecimal.ZERO, origin3D.distanceSquared(origin3D));
		for(int i = 0; i < MAX_ITER; i++){
			KDPoint p = new KDPoint(- r.nextDouble(),  r.nextDouble());
			assertEquals(messageTrivialDistance,BigDecimal.ZERO, p.distanceSquared(p));

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
		assertEquals(messageTrivialDistance,BigDecimal.ZERO, one.distanceSquared(one));
		assertEquals(messageTrivialDistance,BigDecimal.ZERO, two.distanceSquared(two));
		assertEquals("The squared Euclidean distance between two points is wrong",new BigDecimal(9), one.distanceSquared(two));
		assertEquals("The squared Euclidean distance between two points is wrong",new BigDecimal(9), two.distanceSquared(one));
		KDPoint three = new KDPoint(-3.0);
		assertEquals("The squared Euclidean distance between two points is wrong",new BigDecimal(9), two.distanceSquared(three));
		assertEquals("The squared Euclidean distance between two points is wrong",new BigDecimal(9), three.distanceSquared(two));

		// Classic.
		KDPoint oneOne = new KDPoint(1, 1);
		assertEquals("The squared Euclidean distance between (1,1) and (0,0) should be 2",new BigDecimal(2),
				new KDPoint().distanceSquared(oneOne));
		KDPoint minusOneOne = new KDPoint(-1, 1);
		assertEquals("The squared Euclidean distance between (1,-1) and (0,0) should be 2", new BigDecimal(2),
				new KDPoint().distanceSquared(minusOneOne));
		KDPoint oneMinusOne = new KDPoint(1, -1);
		assertEquals("The squared Euclidean distance between (-1,1) and (0,0) should be 2", new BigDecimal(2), new KDPoint().distanceSquared(oneMinusOne));
		KDPoint minusOneminusOne = new KDPoint(-1, -1);
		assertEquals("The squared Euclidean distance between (-1,-1) and (0,0) should be 2", new BigDecimal(2), new KDPoint().distanceSquared(minusOneminusOne));

		// And a not so trivial one
		KDPoint complexPointOne = new KDPoint(3.5, 2.1, -10.9);
		KDPoint complexPointTwo = new KDPoint(-1.4, 2.8, -0.0007);
		assertEquals("The squared Euclidean distance between two points is wrong", new BigDecimal(143.29474049), complexPointOne.distanceSquared(complexPointTwo)); // Computed with Google's calculator
		assertEquals("The squared Euclidean distance between two points is wrong", new BigDecimal(143.29474049), complexPointTwo.distanceSquared(complexPointOne));
	}

	@Test
	public void testKDPointDistanceStatic() {
		// Some trivial ones
		String messageTrivialDistance = "Static distanceSquared(): The distance between a point and itself must be 0";
		assertEquals(messageTrivialDistance, BigDecimal.ZERO, distanceSquared(origin2D, origin2D)); // Recall that the static method has been statically imported, so this works.
		assertEquals(messageTrivialDistance, BigDecimal.ZERO, distanceSquared(origin3D, origin3D));
		for(int i = 0; i < MAX_ITER; i++){
			KDPoint p = new KDPoint(- r.nextDouble(), r.nextDouble());
			assertEquals(messageTrivialDistance, BigDecimal.ZERO, distanceSquared(p, p));
		}



		// The complex example from the previous test:
		KDPoint complexPointOne = new KDPoint(3.5, 2.1, -10.9);
		KDPoint complexPointTwo = new KDPoint(-1.4, 2.8, -0.0007);
		assertEquals("The squared Euclidean distance (static distanceSquared()) between two points is wrong", new BigDecimal(143.29474049), distanceSquared(complexPointOne, complexPointTwo));
		assertEquals("The squared Euclidean distance (static distanceSquared()) between two points is wrong", new BigDecimal(143.29474049), distanceSquared(complexPointTwo, complexPointOne));

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



}
