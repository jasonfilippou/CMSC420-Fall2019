package demos.bags;

import java.util.*;

/**A {@link RandomAccessBag} is a {@link Bag} which, when {@link #shake()}d, essentially just shakes the index sequence
 * into the old linear collection of {@code Item}s. The goal is to prove that, after shaking, and for a relatively large
 * number of {@code Item}s held, looping through the items using the {@link java.util.Iterator} returned by {@link #iterator()}
 * will lead to many cache misses and will thus be slow.
 *
 * @see StaticallyPerturbedBag
 * @see DynamicallyShuffledBag
 * @see Bag
 *
 * @author <a href = "https://github.com/JasonFil">Jason Filippou</a>
 */
public class RandomAccessBag<Item> implements Bag<Item>{

    private Random r;
    private int current;
    private static final long SEED = 47;
    private Item[] storage;
    private static final int DEFAULT_INIT_CAPACITY = 10;
    private Integer[] indexList; // This will hold the indices into the storage array.


    /**
     * Default constructor. Initializes the {@link RandomAccessBag} with a default capacity.
     */
    public RandomAccessBag(){
        this(DEFAULT_INIT_CAPACITY);
    }

    /**
     * A constructor which pre-allocates {@code this} with a provided capacity.
     * @param capacity The capacity to pre-allocate {@code} this with.
     */
    public RandomAccessBag(int capacity){
        storage = (Item[])new Object[capacity];         // Unchecked, yet safe for the most basic Items.
        r = new Random(SEED);
        current = -1;
    }

    /**
     * Adds an {@code Item} to the bag.
     *
     * @param item The {@code Item} to add to the Bag.
     */
    @Override
    public void add(Item item) {
        if(size() == capacity())
            expand();
        storage[++current] = item;
    }

    private void expand(){
        int cap = capacity();
        Item[] newStorage = (Item[]) new Object[2*cap];
        for(int i = 0; i < cap; i++)
            newStorage[i] = storage[i];
        storage = newStorage;
    }

    private int capacity(){
        return storage.length;
    }

    /**
     * Returns {@code true} if there are no elements in the bag.
     *
     * @return {@code true} if and only if the Bag is empty, {@code false} otherwise.
     *
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * <p>"Shakes" the bag, randomly perturbing the order of its elements.</p>
     * <p>The shaking routine for a <b>RandomAccessBag</b> will generate the list of indices into the existing
     * array. randomly permute that index list, and then use the permuted indices to access the existing list.
     * The hope is that this will lead to lots of cache misses. It is also burdened by the generation of original index list
     * as well as the permutation of its elements. Check the Java 8 branch for various optimizations for array handling.</p>
     * @see DynamicallyShuffledBag#shake()
     *
     */
    @Override
    public void shake() {
        indexList = new Integer[size()];
        // This is the stupid pre-Java 8 way. Let's keep this branch stupid and backwards-compatible.
        for(int i = 0; i < indexList.length; i++)
            indexList[i] = i;
        Collections.shuffle(Arrays.asList(indexList), r);
    }

    /**
     * Returns the number of elements in the bag.
     * @return the number of elements in the bag.
     */
    @Override
    public int size() {
        return current + 1;
    }

    /**
     * Returns a {@link Iterator} over elements of type {@code Item}.
     * @return an instance of {@link Iterator} over {@code Item} instances.
     */
    @Override
    public Iterator<Item> iterator() {
        return new Iterator<>() {       // Anonymous inner class.
            private int initSize = size();
            private int itIndex = 0;
            @Override
            public boolean hasNext() {
                return itIndex < current;
            }

            @Override
            public Item next() {
                if(size() != initSize)
                    throw new ConcurrentModificationException("StaticallyPerturbedBag was mutated between calls to iterator()::next().");
                if(indexList != null) // Or, in other words, if the bag has been shaken
                    return storage[indexList[itIndex++]];
                else
                    return storage[itIndex++];
            }
        };
    }

    @Override
    public String toString(){
        return this.getClass().getSimpleName();
    }
}
