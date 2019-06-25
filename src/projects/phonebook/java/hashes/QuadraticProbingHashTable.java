package projects.phonebook.java.hashes;

import projects.phonebook.java.utils.KVPair;
import projects.phonebook.java.utils.PrimeGenerator;
import projects.phonebook.java.utils.Probes;

/**
 * <p>{@link QuadraticProbingHashTable} is an Openly Addressed {@link HashTable} which uses <b>Quadratic
 * Probing</b> as its collision resolution strategy. Quadratic Probing differs from <b>Linear</b> Probing
 * in that collisions are resolved by taking &quot; jumps &quot; on the hash table, the length of which
 * determined by an increasing polynomial factor. For example, during a key insertion which generates
 * several collisions, the first collision will be resolved by moving 1^2 + 1 = 2 positions over from
 * the originally hashed address (like Linear Probing), the second one will be resolved by moving
 * 2^2 + 2= 6 positions over from our hashed address, the third one by moving 3^2 + 3 = 12 positions over, etc.
 * </p>
 *
 * <p>By using this collision resolution technique, {@link QuadraticProbingHashTable} aims to get rid of the
 * &quot;key clustering &quot; problem that {@link LinearProbingHashTable} suffers from. Leaving more
 * space in between memory probes allows other keys to be inserted without many collisions. The tradeoff
 * is that, in doing so, {@link QuadraticProbingHashTable} sacrifices <em>cache locality</em>.</p>
 *
 * @author YOUR NAME HERE!
 *
 * @see HashTable
 * @see SeparateChainingHashTable
 * @see LinearProbingHashTable
 * @see CollisionResolver
 */
public class QuadraticProbingHashTable implements HashTable{

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
                count--; // Because the call to put() will artificially increase the table's count.
                put(oldTable[i].getKey(), oldTable[i].getValue());
            }
        }
    }


    /* ******************/
    /*  PUBLIC METHODS: */
    /* ******************/

    /**
     *  Default constructor. Initializes the internal storage with a size equal to the default of {@link PrimeGenerator}.
     */
    public QuadraticProbingHashTable(){
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
     * Instances of {@link QuadraticProbingHashTable} will follow the writeup's guidelines about how to internally resize
     * the hash table when the capacity exceeds 50&#37;
     * @param key The record's key.
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
        int originalProbe = hash(key), currentProbe = originalProbe;
        int probeCount = 1;

        while(table[currentProbe] != null) {
            if(table[currentProbe].getKey().equals(key)) {
                return new Probes(null, probeCount); // won't affect count, which is good.
            }
            currentProbe = (originalProbe + probeCount + (int)(Math.pow((probeCount++), 2))) % table.length;
        }
        table[currentProbe] = new KVPair(key, value);
        return new Probes(value, probeCount);
    }


    @Override
    public Probes get(String key) {
        int originalProbe = hash(key), currentProbe = originalProbe;
        int probeCount = 1;

        while(table[currentProbe] != null) {
            if(table[currentProbe].getKey().equals(key)) {
                return new Probes(table[currentProbe].getValue(), probeCount);
            }
            currentProbe = (originalProbe + probeCount + (int)(Math.pow((probeCount++), 2))) % table.length;
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
        int originalProbe = hash(key), currentProbe = originalProbe;
        int probeCount = 1;
        String flag = null;

        while(table[currentProbe] != null){
            if(table[currentProbe].getKey().equals(key)){ // Found it! Nullify and then re-insert all others
                flag = table[currentProbe].getValue();
                table[currentProbe] = null;
                count--;
                reinsertAllInCluster(originalProbe , probeCount);
                break; // No need to continue outer loop.
            }
            currentProbe = (originalProbe + probeCount + (int)(Math.pow((probeCount++), 2))) % table.length;
        }
        return new Probes(flag, probeCount);
    }

    private void reinsertAllInCluster(int originalProbe, int probeCount){
        int currentProbe = (originalProbe + probeCount + (int)(Math.pow((probeCount++), 2))) % table.length;
        while(table[currentProbe] != null){
            KVPair toReinsert = table[currentProbe];
            table[currentProbe] = null;
            putHelper(toReinsert.getKey(), toReinsert.getValue());
            currentProbe = (originalProbe + probeCount + (int)(Math.pow((probeCount++), 2))) % table.length;
        }
    }

    @Override
    public boolean containsKey(String key) {
        int probeCount = 1;
        int originalProbe = hash(key), currentProbe = originalProbe;
        while (table[currentProbe] != null) {
            if (table[currentProbe].getKey().equals(key)) {
                return true;
            }
            currentProbe = (originalProbe + probeCount + (int)(Math.pow((probeCount++), 2))) % table.length;
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
        return  count;
    }

    @Override
    public int capacity() {
        return  table.length;
    }
}