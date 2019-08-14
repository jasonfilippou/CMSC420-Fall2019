package demos.bags;

/** A {@link Bag} is an {@link Iterable} which represents a rather simple
 * data structure known as a &quot;bag&quot;. This structure allows for:
 * <ul>
 *  <li>Initialization with a default or a provided capacity. </li>
 *  <li>Querying for emptiness </li>
 *  <li>Adding an element</li>
 *  <li>&quot;Shaking&quot; the bag, which randomly perturbs the elements of the bag. This method will be crucial for our algorithmic
 *  analysis.</li>
 * </ul>
 * Bags are allowed to grow arbitrarily. One cannot remove elements from a bag.
 *
 * @author <a href = "https://github.com/JasonFil">Jason Filippou</a>
 *
 * @see java.lang.Iterable
 * @see StaticallyPerturbedBag
 * @see DynamicallyShuffledBag
 * @see RandomAccessBag
 */
public interface Bag<Item> extends Iterable<Item>{ // So classes implementing it have to expose a fail-safe iterator.


    /** Adds an <b>Item</b> to the bag.
     *@param i The <b>Item</b> to add to the Bag.
     *
     */
    void add(Item i);

    /**Returns true if there are no elements in the bag.
     * @return True if and only if the Bag is empty, False otherwise.
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
     * <b>Bag</b>s are to override the default toString() so that they can identify themselves.
     *
     */
    @Override
   String toString();

    // TODO: Move implementation of toString() up here and make Bag an abstract class.

}
