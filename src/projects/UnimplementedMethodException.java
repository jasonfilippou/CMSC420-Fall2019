package projects;

/**
 * A simple {@link RuntimeException} subtype which is used by skeleton code to notify students that a certain method
 * needs to be implemented. <b>DO NOT EDIT!</b>
 *
 * @author  <a href="https://github.com/JasonFil">Jason Filippou</a>
 * @see RuntimeException
 */
public class UnimplementedMethodException extends RuntimeException {

    /**
     * Arg-free constructor stores the standard {@link String} &quot;Implement this method!&quot;.
     * @see RuntimeException#RuntimeException(String)
     */
    public UnimplementedMethodException() {
        super("Implement this method!");
    }
}
