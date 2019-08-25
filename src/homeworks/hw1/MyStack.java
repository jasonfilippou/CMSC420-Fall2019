package homeworks.hw1;

import java.util.*;

/**
 * {@link MyStack} is a simple class that implements a stack abstraction. To help debug the class and demonstrate
 * how fail-fast {@link java.util.Iterator}s should work, this class is made {@link Iterable}.
 *
 * {@link MyStack} instances are created with a maximum capacity which is provided at construction time. If pushes
 * are made that would exceed this capacity, an instance of {@link StackOverflowError} is thrown.
 *
 * @see StackOverflowError
 * @author <a href="github.com/JasonFil">Jason Filippou</a> and ***** YOUR NAME HERE! ****
 */
public class MyStack<T> implements Iterable<T> {

    // The constructor decides which implementation of java.util.List
    // will be used by the current MyStack instance.
    private List<T> data;
    private int maxCapacity;

    /**
     * Creates an instance of {@link MyStack} with a provided maximum capacity.
     * @param maxCapacity The maximum number of elements we allow in {@code this}.
     * @param expectedSize A {@link StackSize} instance that describes the user expectation
     *                     about the stack.
     * @throws InvalidCapacityException if {@code maxCapacity} &lt; 0
     */
    public MyStack(int maxCapacity, StackSize expectedSize) throws InvalidCapacityException {
        if(maxCapacity <= 0)
            throw new InvalidCapacityException("Invalid capacity provided: " + maxCapacity + ".");
        this.maxCapacity = maxCapacity;
        switch(expectedSize){
            case SMALL:
                data = new ArrayList<>(maxCapacity);        // Note the constructor argument; why do we provide it?
                break;
            case LARGE:
                data = new LinkedList<>();
                break;
            default:
                throw new RuntimeException("Invalid expected size parameter " + expectedSize + "."); // Why does this not need to be checked?
        }
    }

    /**
     * Adds an element to the top of the stack. For the underlying implementation, we use the "back" of the list
     * as the "top" of the stack.
     * @param element The datum to push into {@code this}.
     * @throws StackOverflowError If we attempt to push into a stack that is at capacity.
     */
    public void push(T element) throws StackOverflowError {
        if (data.size() == maxCapacity)
            throw new StackOverflowError("Overblew maximum stack capacity of: " + maxCapacity + ".");
        data.add(element);
    }

    /**
     * Return <b>and remove</b> the top element of the stack.
     * @return The top element of the stack, simultaneously removing it.
     */
    public T pop(){
        if(data.isEmpty())
            throw new EmptyStackException(); // Why do we not need to check this Exception?
        final int LASTELEMENTIDX = data.size() - 1;
        T retVal = data.get(LASTELEMENTIDX);
        data.remove(LASTELEMENTIDX);    // This can throw; why do we not need to wrap it in a try-block or check the Exception?
        return retVal;
    }

    /**
     * Return the top element of the stack <b>without</b> removing it..
     * @return The top element of the stack, <b>without</b> removing it.
     * @throws EmptyStackException if {@code this} is empty.
     */
    public T peek() throws EmptyStackException {
        if(data.isEmpty())
            throw new EmptyStackException(); // Same question as above.
        return data.get(data.size() - 1);   //  Same question as above.
    }

    /**
     * Queries {@code this} for emptiness.
     * @return {@code true} iff {@code this} is empty, {@code false} otherwise.
     */
    public boolean isEmpty(){
        return data.isEmpty();
    }

    /**
     * Returns the number of elements pushed, but not popped, in {@code this}.
     * @return the number of elements stored in {@code this}.
     */
    public int size(){
        return data.size();
    }
    /**
     * Returns a smart pointer over elements of type {@code T}. You should think about how
     * you can implement this {@link Iterator} to:
     *
     *  <ol>
     *
     *      <li>Access the elements in so-called <i>proper order</i>, which is fancy speak for &quot; the order
     *      that the data structure implies by its semantics &quot;. For a stack, proper order means LIFO, since
     *      when imagining a stack, we want the most recently pushed element to be accessed first, the second
     *      most recent one accessed second, etc. For a  priority queue, proper order would mean from smaller to
     *      greater priority, with tie-breakers broken by insertion order (FIFO). </li>
     *
     *      <li>Make it <b>fail-fast</b>. After you do this, you will probably also want to implement a
     *      <b>fail-safe</b> {@link Iterator} so that you can answer your homework effectively. Can you see for
     *      yourself how complex the behavior and implementation of such a {@link Iterator} can be?</li>
     *
     *  </ol>
     *
     * @return An {@link Iterator} instance.
     * @see ConcurrentModificationException
     * @see IllegalStateException
     * @see NoSuchElementException
     */
    public Iterator<T> iterator() {
        return new MyStackIterator();
    }

    private class MyStackIterator implements Iterator<T>{

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return false;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public T next() {
            return null;
        }

        /**
         * Removes from the underlying collection the last element returned
         * by this iterator (optional operation).  This method can be called
         * only once per call to {@link #next}.
         * <p>
         * The behavior of an iterator is unspecified if the underlying collection
         * is modified while the iteration is in progress in any way other than by
         * calling this method, unless an overriding class has specified a
         * concurrent modification policy.
         * <p>
         * The behavior of an iterator is unspecified if this method is called
         * after a call to the {@link #forEachRemaining forEachRemaining} method.
         *
         * @throws UnsupportedOperationException if the {@code remove}
         *                                       operation is not supported by this iterator
         * @throws IllegalStateException         if the {@code next} method has not
         *                                       yet been called, or the {@code remove} method has already
         *                                       been called after the last call to the {@code next}
         *                                       method
         * @implSpec The default implementation throws an instance of
         * {@link UnsupportedOperationException} and performs no other action.
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException("This Iterator does not support remove().");
        }
    }
}
