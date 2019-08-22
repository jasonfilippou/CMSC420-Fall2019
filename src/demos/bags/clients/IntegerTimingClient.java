package demos.bags.clients;

import demos.bags.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

/** <p>{@link IntegerTimingClient} tests all implementations of {@link Bag} in the task of retrieving all
 * of their contents. The type used is {@link Integer}.</p>
 * @author <a href = "https://github.com/JasonFil">Jason Filippou</a>
 *
 * @see demos.bags.testcases.BagTests
 */
public class IntegerTimingClient {

    private static final int NUM_INTS = 100000;
    private static int[] ints = IntStream.rangeClosed(1, NUM_INTS).toArray();

    /**
     * <p>Runner method. Creates three different {@link Bag} instances and adds several {@link Integer}s to each.
     * Then, it loops through all the integers in the {@link Bag}s and reports the time it took to do that.</p>
     * @param args  cmd args
     */
    public static void main(String[] args){

        ArrayList<Bag<Integer>> bags = new ArrayList<>();
        bags.add(new StaticallyPerturbedBag<>());
        bags.add(new DynamicallyShuffledBag<>());
        bags.add(new RandomAccessBag<>());

        for(Bag<Integer> b: bags){
            System.out.println("----------------------------------------------------------------");
            System.out.println("Running Integer experiments for " + b + ".");
            insertAll(ints, b);
            b.shake(); // Not counting that for now.
            long startingMillis = System.currentTimeMillis();
            loopthroughAll(b);
            long endingMillis = System.currentTimeMillis();
            System.out.println("For a " + b + ", looping through all elements took "
                    + (endingMillis - startingMillis) + " ms after shaking.");
            System.out.println("Finished Integer experiments for " + b + ".");
            System.out.println("----------------------------------------------------------------");
        }
    }

    private static void insertAll(int[] ints,  Bag<Integer> b){
        for(Integer i : ints)
            b.add(i);
    }

    private static void loopthroughAll(Bag<Integer> b){
        for(Integer ignored : b)
            ; // We don't really want anything to happen; this is just busy-waiting.

    }
}
