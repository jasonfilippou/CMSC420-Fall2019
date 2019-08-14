package demos.hashing;

/**
 * <p>Prints the default hashCode() of an {@link Object}.</p>
 * @see #hashCode()
 */
public class DefaultHashCode {
    public static void main(String[] args){
        Object o = new Object();
        System.out.println(o.hashCode());
    }
}
