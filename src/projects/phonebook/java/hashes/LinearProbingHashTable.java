package projects.phonebook.java.hashes;
import projects.phonebook.java.utils.KVPair;
import projects.phonebook.java.utils.PrimeGenerator;
import projects.phonebook.java.utils.Probes;

/**
 * <p>{@link LinearProbingHashTable} is an Openly Addressed {@link HashTable} implemented with <b>Linear Probing</b> as its
 * collision resolution strategy: every key collision is resolved by moving one address over. It is
 * the most famous collision resolution strategy, praised for its simplicity, theoretical properties
 * and cache locality. It <b>does</b>, however, suffer from the &quot; clustering &quot; problem:
 * collision resolutions tend to cluster collision chains locally, making it hard for new keys to be
 * inserted without collisions. {@link QuadraticProbingHashTable} is a {@link HashTable} that
 * tries to avoid this problem, albeit sacrificing cache locality.</p>
 *
 * @author YOUR NAME HERE!
 *
 * @see HashTable
 * @see SeparateChainingHashTable
 * @see QuadraticProbingHashTable
 * @see CollisionResolver
 */
public class LinearProbingHashTable implements HashTable{

    /* *******************************************************************/
    /* ***** PRIVATE FIELDS / METHODS PROVIDED TO YOU: DO NOT EDIT! ******/
    /* ****************************************************** ***********/

    private static final RuntimeException UNIMPL_METHOD = new RuntimeException("Implement this method!");
    private KVPair[] table;
    private PrimeGenerator primeGenerator;
    private int count = 0;

    private int hash(String key){
        return (key.hashCode() & 0x7fffffff) % table.length;
    }

    /*  YOU SHOULD ALSO IMPLEMENT THE FOLLOWING 2 METHODS ACCORDING TO THE SPECS
     * PROVIDED IN THE PROJECT WRITEUP, BUT KEEP THEM PRIVATE!  */

    private void enlarge(){
        KVPair[] oldTable = table;
        table = new KVPair[primeGenerator.getNextPrime()];
        reinsertAll(oldTable);
    }

    private void reinsertAll(KVPair[] oldTable){
        for(int i = 0; i < oldTable.length; i++){
            if(oldTable[i] != null){
                putHelper(oldTable[i].getKey(), oldTable[i].getValue());
            }
        }

    }


    /* ******************/
    /*  PUBLIC METHODS: */
    /* ******************/


    /**
     *  Default constructor. Initializes the internal storage with a size equal to the default of {@link PrimeGenerator}.
     */
    public LinearProbingHashTable(){
        primeGenerator = new PrimeGenerator();
        table = new KVPair[primeGenerator.getCurrPrime()];
        count = 0;
    }

    /**
     * Inserts the pair &lt;key, value&gt; into this. The container should <b>not</b> allow for null
     * keys and values, and we <b>will</b> test if you are throwing a {@link IllegalArgumentException} from your code
     * if this method is given null arguments! It is important that we establish that no null entries
     * can exist in our database because the semantics of {@link #get(String)} and {@link #remove(String)} are that they
     * return null if, and only if, their key parameter is null. This method is expected to run in <em>amortized
     * constant time</em>.
     *
     * Instances of {@link LinearProbingHashTable} will follow the writeup's guidelines about how to internally resize
     * the hash table when the capacity exceeds 50&#37;
     * @param key The record's key.
     * @param value The record's value.
     * @throws IllegalArgumentException if either argument is null.
     */
    @Override
    public Probes put(String key, String value) {
        if(key == null || value == null) {
            throw new IllegalArgumentException("Values given: key = " + key + ", value = " + value);
        }
        if(size() >= ((float)capacity() / 2)){
            enlarge();
        }
        Probes temp = putHelper(key, value);
        count++;
        return temp;
    }

    private Probes putHelper(String key, String value){
        int probe = hash(key);
        int probeCount = 1;
        while(table[probe] != null) {
            if(table[probe].getKey().equals(key)) {
                return new Probes(null, probeCount);
            }
            probe = (probe + 1) % table.length;
            probeCount++;
        }
        table[probe] = new KVPair(key, value);
        return new Probes(value, probeCount);
    }

    @Override
    public Probes get(String key) {
        int probe = hash(key);
        int probeCount = 1;
        while(table[probe] != null) {
            if(table[probe].getKey().equals(key)) {
                return new Probes(table[probe].getValue(), probeCount);
            }
            probe = (probe + 1) % table.length;
            probeCount++;
        }
        return new Probes(null, probeCount);
    }


    /**
     * <b>Return</b> and <b>remove</b> the value associated with key in the {@link HashTable}. If key does not exist in the database
     * or if key = null, this method returns null. This method is expected to run in <em>amortized constant time</em>.
     *
     * @param key The key to search for.
     * @return The associated value if key is non-null <b>and</b> exists in our database, null
     * otherwise.
     */
    @Override
    public Probes remove(String key) {
        int probe = hash(key);
        int probeCount = 1;
        String flag = null;

        while(table[probe] != null){
            if(table[probe].getKey().equals(key)){ // Found it! Nullify and then re-insert all others
                flag = table[probe].getValue();
                table[probe] = null;
                count--;
                reinsertAllInCluster((probe + 1) % table.length);
                break; // No need to continue outer loop.
            }
            probe = (probe + 1) % table.length; // Keep going down collision chain to find key
            probeCount++;
        }

        return new Probes(flag, probeCount);
    }

    private void reinsertAllInCluster(int probe){
        while(table[probe] != null){
            KVPair toReinsert = table[probe];
            table[probe] = null;
            putHelper(toReinsert.getKey(), toReinsert.getValue());
            probe = (probe + 1) % table.length;
        }
    }

    @Override
    public boolean containsKey(String key) {
        int probe = hash(key);
        while (table[probe] != null) {
            if (table[probe].getKey().equals(key)) {
                return true;
            }
            probe = (probe + 1) % table.length;
        }
        return false;
    }

    @Override
    public boolean containsValue(String value) {

        for(int i = 0; i < table.length; i++){
            if(table[i] != null && table[i].getValue().equals(value)) {
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
        return table.length;
    }
}
