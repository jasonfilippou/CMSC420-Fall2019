package demos.bags;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

/**
 * <p>A {@link StaticallyPerturbedBag} is a bag where shaking perturbs the elements in a pre-defined way. For this example, the way
 * will be (index + offset) MOD length. The class uses a static array for storing of {@code Item}s. </p>
 * @see Bag
 * @see DynamicallyShuffledBag
 * @see RandomAccessBag
 * @author  <a href = "https://github.com/JasonFil">Jason Filippou</a>
 */
public class StaticallyPerturbedBag<Item> implements Bag<Item>{


    private int current;
    private Item[] storage;
    private static int DEFAULT_CAPACITY = 10;
    private static int OFFSET=3; // Modular

    public StaticallyPerturbedBag(){
        // The following downcasting is generally unsafe, but ok for our example.
        storage = (Item[])new Object[DEFAULT_CAPACITY];
        current =-1;
    }

    /**
     * Constructor with provided initial capacity.
     * @param initCapacity The capacity that we want to initialize {@code this} with.
     */
    public StaticallyPerturbedBag(int initCapacity){
        storage = (Item[])new Object[initCapacity];     // Same point with above.
        current = -1;
    }

    // Let's not spend time on equals(), extended hashcode(), copy constructors...

    /** Adds an {@code Item} to the bag.
     *@param item The {@code Item} to add to the Bag.
     */
    @Override
    public void add(Item item) {
        if(size() == capacity())
            expand();
        storage[++current] = item;
    }

    private void expand() {
        int currCap = capacity();
        Item[] newArr = (Item[])new Object[2*currCap]; // Double it
        System.arraycopy(storage, 0, newArr, 0, currCap);
        storage = newArr; // current already points where we want it to
    }

    private int capacity(){
        return storage.length;
    }

    /**Returns {@code true} if there are no elements in the bag.
     * @return {@code true} if and only if the Bag is empty, False otherwise.
     *
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }


    /**
     * <p>"Shakes" the bag, randomly perturbing the order of its elements.</p>
     * <p>This particular bag will take any element and move it from position i
     * to position (i+OFFSET)%Length. This shouldn't cause many cache misses for the new array, except for those times
     * where we need to wrap around the last cell in the new array.</p>
     */

    @Override
    public void shake() {
        int sz = size();
        Item[] newStorage = (Item[])(new Object[sz]);
        for(int i = 0; i < sz; i++)
            newStorage[(i+OFFSET)%sz] = storage[i];
        storage = newStorage;
        // System.gc();
    }

    /**
     * Returns the number of elements in the bag.
     * @return the number of elements in the bag.
     */
    @Override
    public int size() {
        return current+1;
    }


    @Override
    public Iterator<Item> iterator() {
        return new Iterator<>() {
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
