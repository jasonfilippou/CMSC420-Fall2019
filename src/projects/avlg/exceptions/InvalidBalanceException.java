package projects.avlg.exceptions;

import projects.avlg.AVLGTree;

/**
 *<p> {@link InvalidBalanceException} is an {@link Exception} that is thrown by the constructor
 * of {@link AVLGTree}
 *
 * <p>You should <b>not</b> edit this class! It is given to you as a resource for your project.</p>

 * @author <a href="https://github.com/jasonfil">Jason Filippou</a>
 *
 * @see EmptyTreeException
 * @see AVLGTree
*/
public class InvalidBalanceException extends Exception {
    public InvalidBalanceException(String msg){
        super(msg);
    }
}
