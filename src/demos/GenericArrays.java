package demos;

import java.util.LinkedList;

/**
 * <p>In Java, one cannot create raw arrays of generics. It is necessary to create {@link Object} arrays and perform careful
 * castings in our various methods. {@link GenericArrays} demonstrates how to do this. This class is a simple wrapper over
 * an array of {@link LinkedList} instances which contain {@link String}s. You can see that ugly castings are necessary
 * to avoid errors at compile or even run-time.</p>
 *
 * @author <a href="github.com/JasonFil">Jason Filippou</a>
 *
 * @see ClassCastException
 */
public class GenericArrays {

    private Object[] arrayOfLists;

    /**
     * Constructor initializes {@code this} with the provided size. Observe the upcasting which happens
     * in the code. Upcasting is ALWAYS ok in Java and any object-oriented programming language.
     * @param sz the initial size of the underlying array.
     */
    public GenericArrays(int sz){
        if(sz < 1 )
            throw new RuntimeException("Bad size value: " + sz + ".");
        arrayOfLists = new Object[sz];
        for(int i = 0; i < sz; i++)
            arrayOfLists[i] = new LinkedList<String>();    // Upcasting is ALWAYS ok.
    }

    /**
     * Inserts a {@link String} into the provided position of {@code this}
     * @param pos The position of the array to insert {@code into}.
     * @param element The {@link String} to insert into {@code this.}
     */
    public void insertIntoList(int pos, String element){
        if(pos < 0 || pos >= arrayOfLists.length )
            throw new RuntimeException("Bad position value: " + pos + ".");
        /*
         * Necessary downcasting, since there is no Object::add method! Downcastings in OOP
         * are dangerous, since the user really needs to know what they are doing. To demonstrate
         * how fickle code downcastings can create, go to the constructor and initialize
         * every element of the array as Objects instead of LinkedList<String>s. Run the code again.
         * What happens?
         */
        ((LinkedList<String>)arrayOfLists[pos]).add(element);
    }


    /**
     * Insert a collection of {@link String}s into the provided position of {@code this}
     * @param pos The position of the array to insert {@code elements} into.
     * @param elements The {@link String}s to insert into {@code this}.
     */
    public void insertIntoList(int pos, String... elements){
        if(pos < 0 || pos >= arrayOfLists.length )
            throw new RuntimeException("Bad position value: " + pos + ".");
        if(elements == null || elements.length == 0)
            throw new RuntimeException("No Strings detected to add into collection.");
        for(String s:elements)
            insertIntoList(pos, s);
    }

    /**
     * Get all the {@link String}s stored at position {@code pos}.
     * @param pos The position of the array to retrieve the list of {@link String}s from.
     * @return A {@link LinkedList} containing all the {@link String}s in position {@code pos}.
     * If there are no {@link String}s stored at that position, the {@link LinkedList} returned is empty.
     */
    public LinkedList<String> getStrings(int pos){
        if(pos < 0 || pos >= arrayOfLists.length )
            throw new RuntimeException("Bad position value: " + pos + ".");
        return (LinkedList<String>)arrayOfLists[pos]; // Downcasting again necessary...
    }

    public static void main(String[] args){
        GenericArrays container = new GenericArrays(5);
        container.insertIntoList(3, "Anvitha");
        container.insertIntoList(1, "Jason");
        container.insertIntoList(3, "Ravi");  // into 3
        System.out.println(container.getStrings(3));   // Expecting [Anvitha, Ravi].
    }
}
