package homeworks.hw1;

/**
 *  {@link InvalidCapacityException} instances are thrown from {@link MyStack#MyStack(int, StackSize)} when the user
 *  provides a non-positive capacity for the {@link MyStack} instance.
 *
 * @see MyStack
 * @see Exception
 */
public class InvalidCapacityException extends RuntimeException {
    /**
     * Simple constructor which stores the provided message.
     * @param msg A descriptive message.
     * @see RuntimeException#RuntimeException(String)
     */
    public InvalidCapacityException(String msg){
        super(msg);
    }
}
