package projects.phonebook.hashes;

/**
 * <p>{@link CollisionResolver} is an enum which provides named constants for
 * four of the most widely used collision resolution techniques in hash tables: </p>
 * <ol>
 *     <li><i>Separate Chaining</i>, a simple collision resolver which allocates a linked list for every cell of the hash table.
 *          All keys hashed to the same cell are put in the back of a linked list which containsKVPair all same-hash keys. Enlarging this hash table
 *          leads to better search performance in the face of many insertions, but leads to additional <b>contiguous</b> storage cost.</li>
 *     <li><i>Linear Probing</i>, the most famous Open Addressing method, where collisions are resolved by having keys move on to the next available cell in the
 *     table. This method is susceptible to the <b>clustering phenomenon</b> talked about in class and in Sedgewick &amp; Wayne, chapter 3.4, but displays <b>excellent
 *     cache locality.</b></li>
 *     <li><i>Ordered Linear Probing</i>, a modification of Linear Probing which keeps the collision chains <b>sorted</b>,
 *     making searches destined to fail, fail <b>faster!</b></li>
 *     <li><i>Quadratic Probing</i>, a  modification of Linear Probing where collisions are resolved by having the key make quadratically - increased &quot; jumps &quot;
 *     until it finds an empty cell. See writeup for more details.</li>
 * </ol>
 *
 * <p><b>**** DO NOT EDIT THIS ENUM! ****** </b></p>
 *
 * @author <a href="https://github.com/JasonFil">Jason Filippou</a>
 *
 * @see HashTable
 * @see SeparateChainingHashTable
 * @see LinearProbingHashTable
 * @see QuadraticProbingHashTable
 */
public enum CollisionResolver {
    SEPARATE_CHAINING,
    LINEAR_PROBING,
    ORDERED_LINEAR_PROBING,
    QUADRATIC_PROBING
}
