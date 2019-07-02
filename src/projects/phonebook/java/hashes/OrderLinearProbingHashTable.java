package projects.phonebook.java.hashes;
import projects.phonebook.java.utils.KVPair;
import projects.phonebook.java.utils.PrimeGenerator;
import projects.phonebook.java.utils.Probes;


public class OrderLinearProbingHashTable implements HashTable{

    private KVPair[] table;
    private PrimeGenerator primeGenerator;
    private int count = 0;

    private int hash(String key){
        return (key.hashCode() & 0x7fffffff) % table.length;
    }

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


    public OrderLinearProbingHashTable(){
        primeGenerator = new PrimeGenerator();
        table = new KVPair[primeGenerator.getCurrPrime()];
        count = 0;
    }

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
        KVPair cur = new KVPair(key, value);
        while(table[probe] != null) {
            if(table[probe].getKey().equals(cur)) {
                return new Probes(null, probeCount);
            }
            if(table[probe].getKey().compareTo(cur.getKey()) > 0) {
                KVPair temp = table[probe];
                table[probe] = cur;
                cur = temp;
            }
            probe = (probe + 1) % table.length;
            probeCount++;
        }
        table[probe] = cur;
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
            if(table[probe].getKey().compareTo(key) > 0) {
                return new Probes(null, probeCount);
            }
            probe = (probe + 1) % table.length;
            probeCount++;
        }
        return new Probes(null, probeCount);
    }

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
            if(table[probe].getKey().compareTo(key) > 0) {
                break;
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
            if(table[probe].getKey().compareTo(key) > 0) {
                return false;
            }
            probe = (probe + 1) % table.length;
        }
        return false;    }

    @Override
    public boolean containsValue(String value) {
        for(int i = 0; i < table.length; i++){
            if(table[i] != null && table[i].getValue().equals(value)) {
                return true;
            }
        }
        return false;    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public int capacity() {
        return table.length;
    }
}
