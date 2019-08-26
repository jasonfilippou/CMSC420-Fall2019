package demos.bags;

/** A {@link Bag} is an {@link Iterable} which represents a rather simple
 * data structure known as a &quot;bag&quot;. This structure allows for:
 * <ul>
 *  <li>Initialization with a default or a provided capacity. </li>
 *  <li>Querying for emptiness </li>
 *  <li>Adding an element</li>
 *  <li>&quot;Shaking&quot; the bag, which randomly perturbs the elements of the bag.</li>
 * </ul>
 * {@link Bag} instances are allowed to grow arbitrarily large. One cannot remove elements from a bag.
 *
 * @author <a href = "https://github.com/JasonFil">Jason Filippou</a>
 *
 * @see java.lang.Iterable
 * @see StaticallyPerturbedBag
 * @see DynamicallyShuffledBag
 * @see RandomAccessBag
 * @see Bag
 * @see demos.bags.testcases.BagTests
 */
public interface Bag<Item> extends Iterable<Item>{ // So classes implementing it have to expose a fail-fast iterator.


    /** Adds an {@code Item} to the bag.
     * @param item The {@code Item} to add to the Bag.
     */
    void add(Item item);

    /** Returns true if there are no elements in the bag.
     * @return true if and only if the Bag is empty, false otherwise.
     *
     */
    boolean isEmpty();

    /**
     * "Shakes" the bag, randomly perturbing the order of its elements.
     *
     */
    void shake();

    /**
     * Returns the number of elements in the bag.
     * @return the number of elements contained in this bag.
     *
     */
    int size();

    /**
     * {@link Bag} instances should override the default {@link #toString()} so that they can identify themselves in
     * a user - friendly manner.
     */
    @Override
   String toString();

    // TODO: Move implementation of toString() up here and make Bag an abstract class.

}
