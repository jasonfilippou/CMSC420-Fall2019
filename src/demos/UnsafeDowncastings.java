package demos;

import java.util.LinkedList;

/**
 * <p>An example of unsafe downcasting in Java. {@link LinkedList}s are {@link Iterable}s,
 * while {@link Object}s are <b>not</b>. Therefore, both the constructor casting as well
 * as the casting of {@link #insertIntoList(String, int)} throw instances of
 * {@link ClassCastException} at runtime.
 *
 * @author <a href="github.com/JasonFil">Jason Filippou</a>
 */
public class UnsafeDowncastings {
    private Object[] arrayOfLists; // Cannot create an array of LinkedList<String>s, since that type is a generic.

    public UnsafeDowncastings(int sz){
        if(sz < 1 )
            throw new RuntimeException("Bad size value: " + sz + ".");
        arrayOfLists = (LinkedList<String>[])new Object[sz]; // Will throw a ClassCastException
    }

    public void insertIntoList(String element, int pos){
        if(pos < 0 || pos >= arrayOfLists.length )
            throw new RuntimeException("Bad position value: " + pos + ".");
        ((LinkedList<String>)arrayOfLists[pos]).add(element); // Would throw a ClassCastException, if the constructor didn't throw it first!
    }

    public static void main(String[] args){
        UnsafeDowncastings obj = new UnsafeDowncastings(5);
        obj.insertIntoList("Peter", 3);
    }
}
