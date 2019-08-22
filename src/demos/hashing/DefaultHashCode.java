package demos.hashing;

/**
 * <p>Prints the default {@link #hashCode()} of an {@link Object}.</p>
 * @see #hashCode()
 *
 * @author <a href="https://github.com/JasonFil">Jason Filippou</a>
 */
public class DefaultHashCode {
    public static void main(String[] args){
        System.out.println(new Object().hashCode());
    }
}
