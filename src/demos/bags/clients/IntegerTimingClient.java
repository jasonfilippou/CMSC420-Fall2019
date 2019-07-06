package demos.bags.clients;

import demos.bags.*;

import java.util.Random;
import java.util.stream.IntStream;

/** <p>IntegerTimingClient tests all implementations of {@link Bag} in the task of retrieving all
 * of their Integer contents.</p>
 * @author jason
 */
public class IntegerTimingClient {

    private static Random r = new Random();
    private static final int NUM_INTS = 100000;
    private static int[] ints = IntStream.rangeClosed(1, NUM_INTS).toArray();

    /**
     * <p>Runner method.</p>
     * @param args  cmd args
     */
    public static void main(String[] args){
        Bag[] bags = {
                new StaticallyPerturbedBag<Integer>(),
                new DynamicallyShuffledBag<Integer>(),
                new RandomAccessBag<Integer>()
        };

        for(Bag b: bags){
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
