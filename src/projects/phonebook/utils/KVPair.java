package projects.phonebook.utils;

/**
 * <p>{@link KVPair} is a simple abstraction of a  &lt;{@link String}, {@link String}&lt; pair.</p>
 *
 * <p><b>Do ***NOT*** edit this class!</b></p>
 *
 * @author <a href = "mailto:jason.filippou@gmail.com">jason.filippou@gmail.com</a>
 *
 * @see KVPairList
 */
public class KVPair {

    private String key;
    private String value;

    /**
     * Simple constructor.
     * @param key The key {@link String} of the entry.
     * @param value The value {@link String} of the entry.
     */
    public KVPair(String key, String value){
        this.key = key;
        this.value = value;
    }

    /**
     * Simple accessor.
     * @return The key associated with the entry.
     */
    public String getKey() {
        return key;
    }

    /**
     * Simple accessor.
     * @return The value associated with the entry.
     */
    public String getValue() {
        return value;
    }

    /**
     * Simple mutator.
     * @param key The new key to provide to this entry. Useful in UPDATE queries.
     */
    public void setKey(String key){
        this.key = key;
    }

    /**
     * Simple mutator.
     * @param value The new value to provide to this entry. Useful in UPDATE queries.
     */
    public void setValue(String value){
        this.value = value;
    }

    @Override
    public boolean equals(Object other){
        if(other == null)
            return false;
        KVPair otherCasted = null;
        try {
            otherCasted = (KVPair)other;
        } catch(ClassCastException ignored){
            return  false;
        }
        return otherCasted.key.equals(key) && otherCasted.value.equals(value);
    }
}
