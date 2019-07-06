package demos.bags;

import java.util.*;

/**
 * A DynamicallyShuffledBag is a {@link Bag} which, very much like a {@link RandomAccessBag}, shakes its contents
 * completely (pseudo-) randomly. However, it does so by storing the elements in their new order, instead of
 * indexing into the old container with a permuted index set.
 * @see RandomAccessBag
 * @see StaticallyPerturbedBag
 * @author jason
 */
public class DynamicallyShuffledBag<Item> implements Bag<Item>{

    private Random r;
    private static int DEFAULT_INIT_CAPACITY = 10;
    private Item[] storage;
    private int current = -1;
    /**
     * This constructor is used to initialize the bag with full pseudorandom capabilities for shaking
     * and the default starting capacity.
     * @since 1.0
     */
    public DynamicallyShuffledBag() {
        this(DEFAULT_INIT_CAPACITY);
    }


    /**
     * This constructor creates a DynamicallyShuffledBag with a provided initial capacity as well
     * as a client-provided seed. Clients might be interested in reproducing experiments.
     * @param capacity The initial capacity of the Bag.
     * @param seed A caller-provided seed for the random number generator.
     * @since 1.0
     */
    public DynamicallyShuffledBag(int capacity, long seed){
        storage = (Item[])(new Object[capacity]);
        r = new Random(seed);
    }

    /**
     * This constructor creates a DynamicallyShuffledBag with a provided seed for experiment
     * reproducibility.
     * @see #DynamicallyShuffledBag(int, long)
     * @param seed A caller-provided seed for the random number generator.
     * @since 1.0
     */
    public DynamicallyShuffledBag(long seed){
        this(DEFAULT_INIT_CAPACITY, seed);
    }

    /**
     * This constructor creates a DynamicallyShuffledBag with the provided default capacity. The bag shakes
     * fully (pseudo-)randomly.
     * @param capacity  The initial capacity for the Bag.
     * @see #DynamicallyShuffledBag(int)
     * @since 1.0
     */
    public DynamicallyShuffledBag(int capacity){
        storage = (Item[])(new Object[capacity]);
        r = new Random();
    }
    /**
     * Adds an <b>Item</b> to the bag.
     *
     * //@param i The <b>Item</b> to add to the Bag.
     * @since 1.0
     */
    @Override
    public void add(Item i) {
        if (size() == capacity())
            expand();
        storage[++current] = i;

    }

    private void expand(){
        int currCap = capacity();
        Item[] newStorage = (Item[])new Object[2*currCap];
        for(int i = 0; i < currCap; i++)
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
     * <p>"Shakes" the bag, randomly perturbing the order of its elements. </p>
     * <p>This {@link Bag} randomly permutes the elements of the existing bag and exposes a classic linear
     * indexing over the new permuted elements. So it "eats" the cost of permuting the entire collection of elements,
     * but accessing them later should not lead to cache misses. </p>
     * @see RandomAccessBag#shake()
     * @since 1.0
     */
    @Override
    public void shake() {
        Item[] items = Arrays.copyOfRange(storage, 0, current + 1);
        Collections.shuffle(Arrays.asList(items), r);
        System.arraycopy(items, 0, storage, 0, items.length);
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
            private int index = -1;
            private int initSize = size();
            @Override
            public boolean hasNext() {
                return index < current;
            }

            @Override
            public Item next() {
                if(size() != initSize)
                    throw new ConcurrentModificationException("StaticallyPerturbedBag was mutated between calls to iterator().next().");
                return storage[++index];
            }
        };
    }

    @Override
    public String toString(){
        return this.getClass().getSimpleName();
    }
}
