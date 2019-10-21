package demos.hashing;

/**
 * Prints the default hashCode() of an {@link Object}.
 * @see Object#hashCode()
 */
public class DefaultHashCode {
    public static void main(String[] args){
        Object o = new Object();
        System.out.println(o.hashCode());
    }
}
