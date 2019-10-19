package projects.phonebook.hashes;

import projects.phonebook.utils.KVPair;
import projects.phonebook.utils.PrimeGenerator;

/**
 * <p>{@code OpenAddressingHashTable} is an {@code abstract} class that models <b>openly addressed hash tables</b>, i.e
 * hash tables which store the key-value pairs within the table itself instead of using
 * the table to store pointers to separate buckets. A {@code OpenAddressingHashTable} instance
 * needs to provide <i>amortized constant time</i> for searches, insertions and deletions&quot; soft &quot;
 * or &quot; hard &quot;). It also needs to resize the table when it's getting full or empty, otherwise
 * keys will not be able to be inserted or we will be wasting a lot of memory respectively. This is
 * in contradiction to {@link SeparateChainingHashTable} instances, where resizing is a <i>suggested</i>
 * operation meant to improve efficiency.</p>
 *
 * <p>Essentially, this class allows us to re-use some fields and methods that are common across <b>all</b>
 * of your openly addressed hash tables. For example, <b>all</b> openly addressed {@link HashTable}
 * instances need to contain a one-dimensional array over {@link KVPair} instances, and they also
 * need ab accepted constant for the tombstone and a store for a {@code boolean} variable that
 * determines what kind of deletion we are doing (&quot; soft &quot; or &quot; hard &quot;).</p>
 *
 * <p> DO NOT EDIT THE <b>**** EXISTING ****** </b> FUNCTIONALITY OF THIS CLASS! If there is a method
 * or field that you want <b>all</b> of your openly addressed hash tables to see, you should
 * add it to this class as a {@code protected} field.</p>
 *
 * @author <a href="github.com/JasonFil">Jason Filippou</a>
 *
 * @see HashTable
 * @see KVPair
 * @see SeparateChainingHashTable
 * @see LinearProbingHashTable
 * @see OrderedLinearProbingHashTable
 * @see QuadraticProbingHashTable
 */
public abstract class OpenAddressingHashTable implements HashTable{

    /* *************************************************************** */
    /* *** DO NOT EDIT THE FOLLOWING PROTECTED FIELDS AND METHODS! *** */
    /* *************************************************************** */

    /**
     * A {@code final} variable which will be used in soft deletion across all our
     * openly addressed hash tables.
     */
    protected KVPair TOMBSTONE = new KVPair("", "");

    /**
     * A 1D array over {@link KVPair} instances which will hold our Key-Value pairs.
     */
    protected KVPair[] table;

    /**
     * A {@link PrimeGenerator} instance which will be used for resizings of the table.
     * We must always keep the size of the Hash Table <b>prime</b>, and this instance will
     * help us with that.
     *
     * @see PrimeGenerator
     */
    protected PrimeGenerator primeGenerator;

    /**
     * Initialized to zero, this variable should hold the number of key-value pairs stored in {@code this.}
     */
    protected int count;

    /**
     * A store of the user's preference towards deletion type. {@code true} means soft deletion,
     * {@code false} means hard.
     */
    protected boolean softFlag;

    /**
     * A hash function that uses the default hash code for {@link String} types, but masks the top
     * bit to avoid negative hashes.
     * @param key The {@link String} key to find the hash code of.
     * @return The hash code of the parameter {@link String} as produced by {@link String#hashCode()},
     * but with the top bit masked.
     * @see String#hashCode()
     */
    protected int hash(String key) {
        return (key.hashCode() & 0x7fffffff) % table.length;
    }

    /**
     * A {@code public } {@link Object#toString()} overriding we provide for you. Allows you to
     * print the table to stdout to help you visualize it. Useful for debugging.
     */
    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("***---***\n");
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null)
                ret.append(i).append(" NULL\n");    // Chained append() is better than constructor with String concatenation.
            else if (table[i].equals(TOMBSTONE))
                ret.append(i).append(" TOMBSTONE\n");
            else
                ret.append(i).append(" ").append(table[i].getKey()).append("\n");
        }
        ret.append("***---***");
        return ret.toString();
    }

    /* *************************************************************** */
    /* *** ADD ANY ADDITIONAL PROTECTED FIELDS OR METHODS HERE: ****** */
    /* *************************************************************** */

}
