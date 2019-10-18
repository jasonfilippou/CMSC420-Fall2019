package projects.pqueue.lists;

import projects.pqueue.exceptions.InvalidCapacityException;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

/** <p>StaticArrayList is an implementation of a {@link List} with an array. It does not
 * make any practical sense, because it would be much more efficient to use a {@link LinkedList}
 * in the general case, but it is possible theoretically. The only method of the {@link List} interface
 * that is executed more efficiently in {@link StaticArrayList} than in {@link LinkedList} is  {@link #delete(int)}
 * which runs in <i>O(1)</i>. Of course, other methods such as {@link #pushFront(Object)} are horribly inneficient.</p>
 *
 *<p>You should <b>not</b> edit this class! It is given to you as a resource for your project.</p>
 *
 * @author  <a href="https://github.com/JasonFil">Jason Filippou</a>
 *
 * @param <T> the type held by the ArrayLinearList.
 *
 * @see LinkedList
 * @see DoublyLinkedList
 */

@SuppressWarnings("unchecked")
public class StaticArrayList<T> implements List<T> {

	private T[] data;
	private final int INIT_CAPACITY = 50;
	private int lastIndex; // Holds the index of the last element in the array.
	protected boolean modificationFlag; 

	/* Shift all elements of the list to the right, expanding the 
	 * array if required. */
	private void shiftRight(){
		if(data[data.length - 1] != null)
			expandCapacity();
		for(int i = lastIndex; i > -1; i--)
			data[i+1] = data[i];
		lastIndex++;
	}

	/* Expands the capacity of the array by twice its current capacity. */
	private void expandCapacity(){
		int newCapacity = 2 * data.length;
		T[] newStore = (T[])(new Object[newCapacity]);
		for(int i = 0; i < data.length; i++)
			newStore[i] = data[i];
		// {@code null}ify the rest of the elements
		for(int i = data.length; i < newCapacity; i++)
			newStore[i] = null;
		data = newStore;
	}


	/**
	 * Constructor. Initializes the list with a default capacity.
	 */
	public StaticArrayList(){
		data = (T[])(new Object[INIT_CAPACITY]); // References are {@code null}ified when declaring new arrays.
		lastIndex = -1;
		modificationFlag = false;
	}

	/**
	 * Constructor. Initializes the list with the provided capacity.
	 * @param capacity The capacity provided by the user.
	 * @throws InvalidCapacityException If the capacity provided is negative.
	 */
	public StaticArrayList(int capacity) throws InvalidCapacityException{
		if(capacity < 0)
			throw new InvalidCapacityException("Invalid capacity provided!");
		data = (T[])(new Object[capacity]);
		lastIndex = -1;
		modificationFlag = false;
	}

	/**
	 * Copy constructor. Makes a deep copy of the parameter.
	 * Note that the argument is of type {@link List}; we can make a copy of any kind of list to our internal representation.
	 * @param list The {@link List} to make a copy of.
	 */
	public StaticArrayList(List<T> list){
		if(list == null)
			return;
		data = (T[])(new Object[list.size()]);
		lastIndex = -1;
		for(T element : list)// Recall that because LinearList<T> is Iterable, we can use for-each loops.
			pushBack(element);
		modificationFlag = false;
	}

	/**
	 * Returns a String representation of the current object.
	 * @return a String representation of the current object.
	 */
	@Override
	public String toString(){
		String retVal = "[";
		for(int i = 0; i <= lastIndex; i++){
			retVal += data[i];
			if(i < lastIndex)
				retVal += ", ";
		}
		retVal += "]";
		return retVal;
	}

	/**
	 * Standard equals() method. Two LinearLists are equal if they contain
	 * the exact same elements at the same positions. Note that this equals()
	 * method allows for comparing an ArrayLinearList with an ArrayListLinearList,
	 * and potentially allowing them to be equal because they maintain the aforementioned
	 * contract! We are interested in ADT comparison, not 1-1 memory allocation comparison.
	 * 
	 * @param other The Object reference to compare to
	 * @return {@code true} if the two objects are considered "equal", with equality defined above.
	 */
	@Override 
	public boolean equals(Object other){
		if(other == null)
			return false;
		if(!(other instanceof List<?>))
			return false;
		List<T> otherList= (List<T>)other;
		if(otherList.size() != size())
			return false;
		Iterator<T> ito = otherList.iterator(), itc = this.iterator();
		while(ito.hasNext())
			if(!ito.next().equals(itc.next()))
				return false;
		return true;
	}

	/* Methods overridden from LinearList interface. */

	@Override
	public void pushFront(T element) {
		shiftRight();
		data[0] = element;
		modificationFlag = true;
	}

	@Override
	public void pushBack(T element) {
		if(data[data.length - 1] != null)
			expandCapacity();
		data[++lastIndex] = element;
		modificationFlag = true;
	}

	@Override
	public T getFirst() throws EmptyListException {
		T retVal = null;
		try {
			retVal = get(0);
		} catch(IllegalListAccessException ile){}
		return retVal;
	}

	@Override
	public T getLast() throws EmptyListException {
		T retVal = null;
		try {
			retVal = get(size() -1);
		} catch(IllegalListAccessException ile){}
		return retVal;
	}

	@Override
	public T get(int index) throws EmptyListException,
	IllegalListAccessException {
		if(isEmpty())
			throw new EmptyListException("get(" + index + "): list is empty!");
		if(index < 0 || index >= size())
			throw new IllegalListAccessException("get(): Index " + index + " is out of bounds");
		return data[index];
	}

	@Override
	public boolean contains(T element) {
		for(int i = 0; i <= lastIndex; i++)
			if(data[i].equals(element)) // We are facilitated by the fact that T objects are Comarable
				return true;
		return false;
	}

	@Override
	public boolean delete(T element) {
		for(int i = 0; i <= lastIndex; i++){
			if(data[i].equals(element)){
				data[i] = null; // Drop it, allow for garbage collection if no other references to it exist.
				for(int j = i + 1; j <= lastIndex; j++) // Shift all elements to the right one position to the left.
					data[j - 1] = data[j];
				data[lastIndex--] = null;
				modificationFlag = true;
				return true;
			}
		}
		return false;
	}

	/* delete(int) is very fastly implemented in such a list. */
	@Override
	public void delete(int index) throws IllegalListAccessException{
		if(index < 0 || index > lastIndex)
			throw new IllegalListAccessException("delete(int): Index " + index + " refers to a position outside the list.");
		data[index] = null;
		for(int j = index + 1; j <= lastIndex; j++) // Shift all elements to the right one position to the left.
			data[j - 1] = data[j];
		data[lastIndex--] = null;
		modificationFlag = true;
	}

	/* deleteAll(T) will be similar to delete(T), the main difference being that we
	 * won't exit the method after the getFirst removal.
	 */
	@Override
	public boolean deleteAll(T element) {
		// I'll just call delete() as many times as needed...
		boolean contained = false;
		while(contains(element)){
			contained = true;
			delete(element);
		}
		if(contained == true)
			modificationFlag = true;
		return contained;
	}

	@Override
	public int size() {
		return lastIndex + 1;
	}

	@Override
	public void clear() {
		for(int i = 0; i <= lastIndex; i++)
			data[i] = null;
		lastIndex = -1;
		modificationFlag = true;
	}

	@Override
	public boolean isEmpty() {
		return lastIndex == -1;
	}

	/* Because we implement the Iterable<T> interface, we need to override the iterator()
	 * method. This will be done as an inner class defined in the body of this method.
	 */
	@Override
	public Iterator<T> iterator() {
		return new StaticArrayListIterator();
	}

	class StaticArrayListIterator implements Iterator<T>{

		private int currentIndex = 0;
		private int previous = 0;

		public StaticArrayListIterator(){
			modificationFlag = false;
		}

		@Override
		public boolean hasNext(){
			return currentIndex < data.length && data[currentIndex] != null;
		}

		@Override
		public T next() throws ConcurrentModificationException{
			if(modificationFlag)
				throw new ConcurrentModificationException("next(): Attempted to traverse a list after removal.");
			previous = currentIndex;
			return  data[currentIndex++];
		}

		@Override
		public void remove() throws IllegalStateException{
			if(currentIndex == 0)
				throw new IllegalStateException("Need at least one call to next() before attempting removal.");
			data[previous] = null; // Already incremented by "next"/
			for(int j = previous + 1; j < data.length && data[j] != null; j++)
				data[j - 1] = data[j];
			currentIndex--;
			previous--;
			lastIndex--; // The enclosing class' data member.
		} 

	}



}
