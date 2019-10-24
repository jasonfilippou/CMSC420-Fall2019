package projects.phonebook.hashes;

import projects.phonebook.utils.Probes;

/**
 * <p>{@link HashTable} is an abstraction over hash tables which store {@link String} keys and map to 
 * {@link String} values. Implementing classes should offer <em>amortized constant</em> insertion, search and 
 * deletion. The method names that you have to implement are the namesakes of {@link java.util.Hashtable}
 * (<b>not</b> {@link java.util.HashMap}!). </p>
 *
 *  <p><b>**** DO NOT EDIT THIS INTERFACE'S DECLARATION! ****** </b></p>
 * *
 * @author <a href="https://github.com/JasonFil">Jason Filippou</a>
 *
 * @see OpenAddressingHashTable
 * @see SeparateChainingHashTable
 * @see LinearProbingHashTable
 * @see OrderedLinearProbingHashTable
 * @see QuadraticProbingHashTable
 */
public interface HashTable {

    /**
     * Inserts the pair &lt;key, value&gt; into this. The container should <b>not</b> allow for {@code null}
     * keys and values, and we <b>will</b> test if you are throwing a {@link IllegalArgumentException} from your code
     * if this method is given {@code null} arguments! It is important that we establish that no {@code null} entries
     * can exist in our database because the semantics of {@link #get(String)} and {@link #remove(String)} are that they
     * return {@code null} if, and only if, their key parameter is {@code null}. This method is expected to run in <em>amortized
     * constant time</em>.
     * @param key The record's key.
     * @param value The record's value.
     * @throws IllegalArgumentException if either argument is {@code null}.
     * @return The {@link projects.phonebook.utils.Probes} with the value added and the number of probes it makes.
     */
    Probes put(String key, String value);

    /**
     * Get the value associated with key in the {@link HashTable}. This method is expected to run in <em>amortized constant time</em>.
     * @param key The key to search for.
     * @return The {@link projects.phonebook.utils.Probes} with associated value and the number of probe used. If the key is {@code null}, return value {@code null}
     * and 0 as number of probes; if the key dones't exists in the database, return {@code null} and the number of probes used.
     */
    Probes get(String key);

    /**
     * <b>Return</b> and <b>remove</b> the value associated with key in the {@link HashTable}. This method is expected to run in <em>amortized constant time</em>.
     * @param key The key to search for.
     * @return The {@link projects.phonebook.utils.Probes} with associated value and the number of probe used. If the key is {@code null}, return value {@code null}
     * and 0 as number of probes; if the key dones't exists in the database, return {@code null} and the number of probes used.
     */
    Probes remove(String key);

    /**
     * Queries the {@link HashTable} about the existence of the key key in its internal storage. This method is expected to run in <em>amortized constant time</em>.
     * @param key The key to search for.
     * @return {@code true} if key is the key of some record in our hash table, {@code false} otherwise.
     */
    boolean containsKey(String key);

    /**
     * Queries the {@link HashTable} about the existence of the value value in its internal storage. This method is expected to run in <em>linear time</em> (i.e
     * containsValue() is expected to be an <b>inefficient</b> operation. This is to be expected, since this {@link HashTable} hashes \
     * keys, not values.
     * @param value The value to search for.
     * @return {@code true} if key is the key of some record in our hash table, {@code false} otherwise.
     */
    boolean containsValue(String value);

    /**
     * Returns the number of records in this {@link HashTable}. Please note that this is <b>not</b> the same as returning the hash table's <b>capacity</b>
     * in Open Addressing collision resolution schemes (like Linear Probing)!
     * @return The number of records stored in this.
     */
    int size();

    /**
     * Returns the <b>capacity</b> of this {@link HashTable}. In Separate Chaining, this is the total number of cells from which the lists begin. In Open Addressing
     * methods, this method returns the size of the underlying array. As a consequence, in {@link SeparateChainingHashTable}, it's possible for {@link #size()} to
     * return a value larger than this method. On the other hand, for Open Addressing methods like {@link LinearProbingHashTable} and {@link QuadraticProbingHashTable},
     * the value returned by this method is an <b>upper bound</b> on the value returned by {@link #size()}.
     * @return the number of cells in the table.
     */
    int capacity();
}
