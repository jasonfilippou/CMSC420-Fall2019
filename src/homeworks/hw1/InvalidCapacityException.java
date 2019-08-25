package homeworks.hw1;

/**
 *  {@link InvalidCapacityException} instances are thrown from {@link MyStack#MyStack(int)} when the user
 *  provides a non-positive capacity for the {@link MyStack} instance.
 *
 * @see MyStack
 * @see Exception
 */
public class InvalidCapacityException extends Exception {
    public InvalidCapacityException(String msg){
        super(msg);
    }
}
