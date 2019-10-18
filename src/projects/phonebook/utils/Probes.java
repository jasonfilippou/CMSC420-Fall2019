package projects.phonebook.utils;

/**
 * {@code Probes} is a class that contains information about the efficiency of an operation, in
 * terms of probes that the code had to &quot;pay&quot; to finish the operation (successfully or not).
 *
 * @author <a href="mailto:rayguan@terpmail.umd.edu">Tianrui (Terry) Guan</a>
 *
 * @see KVPair
 * @see projects.phonebook.hashes.HashTable
 */
public class Probes {

    private String value;

    private int probes;

    public Probes(String value, int probes) {
        this.value = value;
        this.probes = probes;
    }
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof Probes))
            return false;

        Probes temp = (Probes) o;

        return temp.value.equals(this.value) && temp.probes == this.probes;
    }

    /**
     * Simple accessor for number of probes.
     * @return The number of probes that it took for the operation of interest to complete,
     * successfully or not.
     */
    public int getProbes() {
        return probes;
    }

    /**
     * Simple accessor for mapped value.
     * @return The value mapped to by the key that was used in the operation.
     */
    public String getValue(){
        return value;
    }
}
