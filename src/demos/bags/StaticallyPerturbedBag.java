package demos.bags;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

/**
 * <p>A StaticallyPerturbedBag is a bag where shaking perturbs the elements in a pre-defined way. For this example, the way
 * will be (index + offset) MOD length.</p>
 * <p> The class uses a static array for storing of <b>Item</b>s. </p>
 * @see DynamicallyShuffledBag
 * @see RandomAccessBag
 * @author jason
 */
public class StaticallyPerturbedBag<Item> implements Bag{


    private int current;
    private Item[] storage;
    private static int DEFAULT_CAPACITY = 10;
    private static int OFFSET=3; // Modular

    public StaticallyPerturbedBag(){
        // The following downcasting is unsafe, but ok for our example.
        storage = (Item[])new Object[DEFAULT_CAPACITY];
        current =-1;
    }

    public StaticallyPerturbedBag(int initCapacity){
        storage = (Item[])new Object[initCapacity];
        current = -1;
    }

    // Let's not spend time on equals(), extended hashcode(), copy constructors...

    /** Adds an <b>Item</b> to the bag.
     *@param o The <b>Item</b> to add to the Bag.
     *@since 1.0
     */
    @Override
    public void add(Object o) {
        if(size() == capacity())
            expand();
        storage[++current] = (Item)o;
    }

    private void expand() {
        int currCap = capacity();
        Item[] newArr = (Item[])new Object[2*currCap]; // Double it
        for(int i = 0; i < currCap; i ++)
            newArr[i] = storage[i];
        storage = newArr; // current already points where we want it to
    }

    private int capacity(){
        return storage.length;
    }

    /**Returns true if there are no elements in the bag.
     * @return True if and only if the Bag is empty, False otherwise.
     * @since 1.0
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
     * @since 1.0
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
     *
     * @since 1.0
     */
    @Override
    public int size() {
        return current+1;
    }


    @Override
    public Iterator iterator() {
        return new Iterator() {
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
