package demos.matrices.java;

import java.util.Random;
import static demos.matrices.java.Dimension.*;

/**<p>MajorOrderTest is a simple app that tests the efficiency
 * of performing operations across rows and columns in Java.</p>
 * @author Jason
 */
public class MajorOrderTest {

    private static final int N = 100000;
    private static final int WAIT = 1; // In seconds


    private static final long SEED = 47;
    private static final Random RNG = new Random(SEED); // Supply long seed if you seek re-producibility of experiments


    /**
     * <p>main() is the routine that executes the experiment.</p>
     * @param args Arguments provided by the shell on the command line. This application ignores all command-line arguments.
     */
    public static void main(String[] args){

        double[][] M = randomMatrix(N); // The time this takes is not counted in our experiments, of course
        sumThroughDimension(M, ROWS); // This is
        sumThroughDimension(M, COLUMNS); // And this is.
        System.out.println("Good - bye!");
    }

    /* *********** Some helper functions to make main() cleaner: ***************** */

    private static void  sumThroughDimension(double[][] matrix, Dimension d){
        if(matrix == null || matrix.length == 0)
            throw new IllegalArgumentException("method MajorOrderTest.sumThroughDimension: invalid matrix given");
        double sum = 0.0, begin = System.currentTimeMillis();
        for(int i = 0; i < matrix.length; i++)
            for(int j = 0; j < matrix[i].length; j++) // Safe access
                sum += d.equals(ROWS) ?  matrix[i][j] : matrix[j][i];
        long end = System.currentTimeMillis();
        System.out.println("Summed along dimension " + d + " in " + (end - begin) + " ms.");
        System.out.println("Sum computed: " + sum);
    }

    private static double[][] randomMatrix(int dim){
        double[][] matrix = allocateSquareMatrix(dim);

        // Implicitly obeying row-major order
        for(int i = 0; i < dim; i++)
            for(int j = 0; j < dim; j++)
                matrix[i][j] = RNG.nextDouble();
        return matrix;
    }

    private static double[][] allocateSquareMatrix(int dim){
        double[][] squareMatrix = new double[dim][];
        for(int i = 0; i < dim; i++)
            squareMatrix[i] = new double[dim];
        return squareMatrix;
    }
}
