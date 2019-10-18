package projects.phonebook.hashes;
import projects.phonebook.utils.KVPair;
import projects.phonebook.utils.PrimeGenerator;
import projects.phonebook.utils.Probes;

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
    private boolean softFlag;

    private int hash(String key){
        return (key.hashCode() & 0x7fffffff) % table.length;
    }

    /*  YOU SHOULD ALSO IMPLEMENT THE FOLLOWING 2 METHODS ACCORDING TO THE SPECS
     * PROVIDED IN THE PROJECT WRITEUP, BUT KEEP THEM PRIVATE!  */

    private int getUsedSpace() {
        int count = 0;
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null)
                count++;
        }
        return count;
    }

    private void enlarge(){
        KVPair[] oldTable = table;
        int size = table.length;

        while (size() < ((float)size / 2))
            size = primeGenerator.getPreviousPrime();

        table = new KVPair[primeGenerator.getNextPrime()];
        reinsertAll(oldTable);
    }

    private void reinsertAll(KVPair[] oldTable){
        for(int i = 0; i < oldTable.length; i++){
            if(oldTable[i] != null && !oldTable[i].equals(DELETE)){
                putHelper(oldTable[i].getKey(), oldTable[i].getValue());
            }
        }
    }


    /* ******************/
    /*  PUBLIC METHODS: */
    /* ******************/


    /**
     *  Default constructor with hard deletion. Initializes the internal storage with a size equal to the default of {@link PrimeGenerator}.
     */
    public LinearProbingHashTable(){
        primeGenerator = new PrimeGenerator();
        table = new KVPair[primeGenerator.getCurrPrime()];
        count = 0;
        softFlag = false;
    }

    /**
     *  Constructor for soft deletion option. Initializes the internal storage with a size equal to the default of {@link PrimeGenerator}.
     *  @param soft A boolean indicator of whether we want to use soft deletion or not. {@code true} if and only if
     *                we want soft deletion, {@code false} otherwise.
     */
    public LinearProbingHashTable(boolean soft){
        primeGenerator = new PrimeGenerator();
        table = new KVPair[primeGenerator.getCurrPrime()];
        count = 0;
        softFlag = soft;
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
     * @return The {@link projects.phonebook.utils.Probes} with the value added and the number of probes it makes.
     */
    @Override
    public Probes put(String key, String value) {
        if(key == null || value == null) {
            throw new IllegalArgumentException("Values given: key = " + key + ", value = " + value);
        }
        if(getUsedSpace() >= ((float)capacity() / 2)){
            enlarge();
        }

        Probes temp = get(key);
        if (temp.value != null) {
            return temp;
        }
        temp = putHelper(key, value);
        count++;
        return temp;
    }

    private Probes putHelper(String key, String value){
        int probe = hash(key);
        int probeCount = 1;
        while(table[probe] != null && (!softFlag || !table[probe].equals(DELETE))) {
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
     * @return The {@link projects.phonebook.utils.Probes} with associated value and the number of probe used. If the key is null, return value null
     * and 0 as number of probes; if the key dones't exists in the database, return null and the number of probes used.
     */
    @Override
    public Probes remove(String key) {
        int probe = hash(key);
        int probeCount = 1;
        String flag = null;

        while(table[probe] != null){
            if(table[probe].getKey().equals(key)){ // Found it! Nullify and then re-insert all others
                flag = table[probe].getValue();
                if (softFlag)
                    table[probe] = DELETE;
                else {
                    table[probe] = null;
                    reinsertAllInCluster((probe + 1) % table.length);
                }
                count--;
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
            if (!toReinsert.equals(DELETE))
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

//    @Override
//    public void printTable() {
//        System.out.println("***---***");
//        for (int i = 0; i < table.length; i++) {
//            if (table[i] == null)
//                System.out.println(i + " NULL");
//            else if (table[i].equals(DELETE))
//                System.out.println(i + " TOMBSTONE");
//            else
//                System.out.println(i + " " + table[i].getKey());
//        }
//        System.out.println("***---***");
//    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("***---***\n");
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null)
                ret.append(i + " NULL\n");
            else if (table[i].equals(DELETE))
                ret.append(i + " TOMBSTONE\n");
            else
                ret.append(i + " " + table[i].getKey() + "\n");
        }
        ret.append("***---***");
        return ret.toString();
    }


}
