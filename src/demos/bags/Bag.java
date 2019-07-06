package demos.bags;

/** A <b>Bag&lt;Item&gt;</b> is an <b>Iterable&lt;Item&gt;</b> which represents a rather useless data structure known as a bag.
 * A bag allows for:
 * <ul>
 *  <li>Initialization with a default or a provided capacity. </li>
 *  <li>Querying for emptiness </li>
 *  <li>Adding an element</li>
 *  <li>"Shaking" the bag, which randomly perturbs the elements of the bag. This method will be crucial for our algorithmic
 *  analysis.</li>
 * </ul>
 * Bags are allowed to grow arbitrarily. One cannot remove elements from a bag.
 * @author jason
 * @version 1.0
 * @see java.lang.Iterable
 */
public interface Bag<Item> extends Iterable<Item>{ // So classes implementing it have to expose a fail-safe iterator.


    /** Adds an <b>Item</b> to the bag.
     *@param i The <b>Item</b> to add to the Bag.
     *@since 1.0
     */
    void add(Item i);

    /**Returns true if there are no elements in the bag.
     * @return True if and only if the Bag is empty, False otherwise.
     * @since 1.0
     */
    boolean isEmpty();

    /**
     * "Shakes" the bag, randomly perturbing the order of its elements.
     * @since 1.0
     */
    void shake();

    /**
     * Returns the number of elements in the bag.
     * @return the number of elements contained in this bag.
     * @since 1.0
     */
    int size();

    /**
     * <b>Bag</b>s are to override the default toString() so that they can identify themselves.
     * @since 1.1
     */
    @Override
   String toString();

}
