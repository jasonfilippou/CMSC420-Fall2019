package demos.bags;

import java.util.*;

/**A <b>RandomAccessBag</b> is a {@link Bag} which, when {@link #shake()}d, essentially just shakes the index sequence
 * into the old linear collection of <b>Item</b>s. The goal is to prove that, after shaking, and for a relatively large
 * number of <b>Item</b>s held, looping through the items using the {@link java.util.Iterator} returned by {@link #iterator()}
 * will lead to many cache misses and will thus be slow.
 * @version 1.0
 * @see StaticallyPerturbedBag
 * @see DynamicallyShuffledBag
 * @author jason
 */
public class RandomAccessBag<Item> implements Bag<Item>{

    private Random r;
    private int current;
    private Item[] storage;
    private static int DEFAULT_INIT_CAPACITY = 10;
    private Integer[] indexList; // This will hold the indices into the storage array.


    public RandomAccessBag(){
        this(DEFAULT_INIT_CAPACITY);
    }

    public RandomAccessBag(int capacity){
        storage = (Item[])new Object[capacity];
        r = new Random();
        current = -1;
    }

    public RandomAccessBag(long seed){
        this(DEFAULT_INIT_CAPACITY, seed);
    }

    public RandomAccessBag(int capacity, long seed){
        storage = (Item[])new Object[capacity];
        r = new Random(seed);
        current = -1;
    }
    /**
     * Adds an <b>Item</b> to the bag.
     *
     * @param i The <b>Item</b> to add to the Bag.
     * @since 1.0
     */
    @Override
    public void add(Item i) {
        if(size() == capacity())
            expand();
        storage[++current] = i;
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
     * Returns true if there are no elements in the bag.
     *
     * @return True if and only if the Bag is empty, False otherwise.
     * @since 1.0
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
     * @since 1.0
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
     *
     * @since 1.0
     */
    @Override
    public int size() {
        return current + 1;
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private int initSize = size();
            private int itIndex = -1;
            @Override
            public boolean hasNext() {
                return itIndex < current;
            }

            @Override
            public Item next() {
                if(size() != initSize)
                    throw new ConcurrentModificationException("StaticallyPerturbedBag was mutated between calls to iterator().next().");
                if(indexList != null) // Or, in other words, if the bag has been shaken
                    return storage[indexList[++itIndex]];
                else
                    return storage[++itIndex];
            }
        };
    }

    @Override
    public String toString(){
        return this.getClass().getSimpleName();
    }
}
