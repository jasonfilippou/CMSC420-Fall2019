package projects.phonebook.java.hashes;

import projects.phonebook.java.utils.*;

/**<p>{@link SeparateChainingHashTable} is a {@link HashTable} that implements <b>Separate Chaining</b>
 * as its collision resolution strategy, i.e the collision chains are implemented as actual
 * Linked Lists. These Linked Lists are <b>not assumed ordered</b>. It is the easiest and most &quot; natural &quot; way to
 * implement a hash table and is useful for estimating hash function quality. In practice, it would
 * <b>not</b> be the best way to implement a hash table, because of the wasted space for the heads of the lists.
 * Open Addressing methods, like those implemented in {@link LinearProbingHashTable} and {@link QuadraticProbingHashTable}
 * are more desirable in practice, since they use the original space of the table for the collision chains themselves.</p>
 *
 * @author YOUR NAME HERE!
 * @see HashTable
 * @see SeparateChainingHashTable
 * @see LinearProbingHashTable
 * @see CollisionResolver
 */
public class SeparateChainingHashTable implements HashTable{


    /* *******************************************************************/
    /* ***** PRIVATE FIELDS / METHODS PROVIDED TO YOU: DO NOT EDIT! ******/
    /* ****************************************************** ***********/

    private static final RuntimeException UNIMPL_METHOD = new RuntimeException("Implement this method!");
    private KVPairList[] table;
    private int count;
    private PrimeGenerator primeGenerator;

    private int hash(String key){
        return (key.hashCode() & 0x7fffffff) % table.length;
    }

    /* ******************/
    /*  PUBLIC METHODS: */
    /* ******************/
    /**
     *  Default constructor. Initializes the internal storage with a size equal to the default of {@link PrimeGenerator}.
     */
    public SeparateChainingHashTable(){
        primeGenerator = new PrimeGenerator();
        table = new KVPairList[primeGenerator.getCurrPrime()];
        for(int i = 0; i < table.length; i++){
            table[i] = new KVPairList();
        }
        count = 0;
    }

    @Override
    public Probes put(String key, String value) {
        table[hash(key)].addFront(key, value); // For efficient insertions, just drop at front. We have unordered lists.
        count++;
        return new Probes(value, 1);
    }

    @Override
    public Probes get(String key) {
        return table[hash(key)].getValue(key);
    }

    @Override
    public Probes remove(String key) {
        KVPairList targetList = table[hash(key)];
        Probes temp = targetList.removeByKey(key);
        if (temp.value != null)
            count--;
        return temp;
    }

    @Override
    public boolean containsKey(String key) {
        return table[hash(key)].containsKey(key);
    }

    @Override
    public boolean containsValue(String value) {
        for(int i = 0; i < table.length; i++){ // hash function can't help us here!
            if(table[i] != null && table[i].containsValue(value)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public int capacity() {
        return table.length; // Or the value of the current prime.
    }
    /**
     * Enlarges this hash table. At the very minimum, this method should increase the <b>capacity</b> of the hash table and ensure
     * that the new size is prime. The class {@link PrimeGenerator} implements the enlargement heuristic that
     * we have talked about in class and can be used as a black box if you wish.
     * @see PrimeGenerator#getNextPrime()
     */
    public void enlarge() {
        int newSize = primeGenerator.getNextPrime();
        KVPairList[] newTable = new KVPairList[newSize], oldTable = table;
        for(int i = 0; i < newTable.length; i++)
            newTable[i] = new KVPairList();
        table = newTable;
        reinsertAll(oldTable);
    }

    /**
     * Shrinks this hash table. At the very minimum, this method should decrease the size of the hash table and ensure
     * that the new size is prime. The class {@link PrimeGenerator} implements the shrinking heuristic that
     * we have talked about in class and can be used as a black box if you wish.
     *
     * @see PrimeGenerator#getPreviousPrime()
     */
    public void shrink(){
        int newSize = primeGenerator.getPreviousPrime();
        KVPairList[] newTable = new KVPairList[newSize], oldTable = table;
        for(int i = 0; i < newTable.length; i++)
            newTable[i] = new KVPairList();
        table = newTable;
        reinsertAll(oldTable);
    }


    private void reinsertAll(KVPairList[] oldTable){
        for(KVPairList kvpairlist : oldTable) {
            for (KVPair kvpair : kvpairlist) {
                count--; // To counter-act the fact that put() will increase it.
                put(kvpair.getKey(), kvpair.getValue());
            }
        }
    }
}
