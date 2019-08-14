package projects.pqueue.heaps; // ******* <---  DO NOT ERASE THIS LINE!!!! *******

/* *****************************************************************************************
 * THE FOLLOWING IMPORT IS NECESSARY FOR THE ITERATOR() METHOD'S SIGNATURE. FOR THIS
 * REASON, YOU SHOULD NOT ERASE IT! YOUR CODE WILL BE UNCOMPILABLE IF YOU DO!
 * ********************************************************************************** */

import java.util.Iterator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * <p>{@link ArrayMinHeap} is a {@link MinHeap} implemented using an internal array. Since heaps are <b>complete</b>
 * binary trees, using contiguous storage to store them is an excellent idea, since with such storage we avoid
 * wasting bytes per null pointer in a linked implementation.</p>
 *
 * <p>You <b>must</b> edit this class! To receive <b>any</b> credit for the unit tests related to this class,
 * your implementation <b>must</b> be a <b>contiguous storage</b> implementation based on a linear {@link java.util.Collection}
 * or a raw Java array.</p>
 *
 * @author -- YOUR NAME HERE ---
 *
 * @see MinHeap
 * @see LinkedMinHeap
 */

public class ArrayMinHeap<T extends Comparable<T>> implements MinHeap<T> {

	private static final int INIT_CAPACITY = 10;
	private Object[] data;
	private int size;
    private boolean modified;

	/**
	 * Default constructor initializes the data structure with the default
	 * capacity.
	 */
	public ArrayMinHeap(){
		data = new Object[INIT_CAPACITY];
		size = 0;
	}

	/**
	 *  Second, non-default constructor.
	 *  @param rootElement the element to create the root with.
	 */
	public ArrayMinHeap(T rootElement){
		data = new Object[INIT_CAPACITY];
		data[0] = rootElement;
		size = 1;
	}

	/**
	 * Copy constructor initializes the current MinHeap as a carbon
	 * copy of the parameter.
	 *
	 * @param other The MinHeap object to base construction of the current object on.
	 */
	public ArrayMinHeap(MinHeap<T> other){
		if(other == null){
			data = new Object[INIT_CAPACITY];
			size = 0;
		} else {
			data = new Object[other.size()];
			for (T el : other) insert(el);
		}
	}

	@Override
	public void insert(T element) {
		// To insert an element in the minheap, we insert it as the size element,
		// and then we keep swapping it with its parent until the minheap
		// identity is maintained.
		if(size == data.length)
			expandCapacity();
		data[size++] = element;

		// current = 2*parent + 1 or current = 2*parent + 2
		int current = size - 1, parent = (current - 1) / 2;

		// While you need to switch, switch
		while(((T) data[parent]).compareTo((T) data[current]) > 0){
			Object temp = data[current];
			data[current] = data[parent];
			data[parent] = temp;
			current = parent;
			parent = (current - 1) / 2;
		}
		modified = true;
	}

	/**
	 * Expands the capacity of the ArrayMinHeap. This method is typically
	 * called by insert(T2 element) when we're trying to insert an element in an already
	 * full heap.
	 * {@link #insert(Comparable)}
	 */
	private void expandCapacity(){
		Object[] newData = new Object[2*data.length];
		System.arraycopy(data, 0, newData, 0, data.length);
		data = newData;
	}

	@Override
	public T deleteMin() throws EmptyHeapException {
		// To delete the minimum element, we delete the root,
		// and then swap the smallest child of the root with the root
		// and keep on doing that until the heap identity is maintained.
		if(size == 0)
			throw new EmptyHeapException("deleteMin(): Heap is empty!");
		T retVal = (T) data[0];
		data[0] = data[--size];
		data[size] = null;
		int current = 0, child = getChildToSwap(0);
		// While you have to switch, switch.
		while(child != -1 ){
			Object temp = data[current];
			data[current] = data[child];
			data[child] = temp;
			current = child;
			child = getChildToSwap(current);
		}
		modified = true;
		return retVal;
	}

	/**
	 * Returns the index of the minimum child of the given parent.
	 * @param parent the index of the parent
	 * @return the index of the smaller child.
	 */
	private int getChildToSwap(int parent){
		int lChild = 2 * parent + 1, rChild = 2 * parent + 2;
		if(lChild >= size)
			return -1;

		if(rChild >= size)
			return ((T) data[parent]).compareTo((T) data[lChild]) <= 0 ? -1 : lChild;

		if(((T) data[parent]).compareTo((T) data[lChild]) <= 0 && ((T) data[parent]).compareTo((T) data[rChild]) <= 0)
			return -1;

		return ((T) data[lChild]).compareTo((T) data[rChild]) < 0 ? lChild : rChild;
	}

	@Override
	public T getMin() throws EmptyHeapException {
		if(data[0] == null)
			throw new EmptyHeapException("getMin(): heap is empty!");
		return (T) data[0];
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Standard equals() method.
	 * @return true if the current object and the parameter object
	 * are equal, with the code providing the equality contract.
	 */
	@Override
	public boolean equals(Object other){
		if(other == null || !(other instanceof MinHeap))
			return false;
		Iterator itThis = iterator();
		Iterator itOther = ((MinHeap) other).iterator();
		while(itThis.hasNext())
			if(!itThis.next().equals(itOther.next()))
				return false;
		return true;
	}


	@Override
	public Iterator<T> iterator() {
		return new ArrayMinHeapIterator();
	}

	/**
	 * An implementation of a fail-fast max-getFirst Iterator for MinHeaps.
	 * @author Jason Filippou (jasonfil@cs.umd.edu)
	 */
	class ArrayMinHeapIterator implements Iterator<T>{

		private ArrayMinHeap<T> tempHeap;
		private T curr;

		ArrayMinHeapIterator(){
			tempHeap = new ArrayMinHeap<>();
			tempHeap.data = new Object[size];
			System.arraycopy(data, 0, tempHeap.data, 0, size);
			tempHeap.size = size;
			modified = false;
		}

		@Override
		public boolean hasNext() {
			return tempHeap.size != 0;
		}

		@Override
		public T next() throws ConcurrentModificationException, NoSuchElementException{
			if(modified)
				throw new ConcurrentModificationException("next(): attempted to traverse the heap through an Iterator after extraneous modifications.");
			try {
				curr = tempHeap.deleteMin();
				return curr;
			} catch(EmptyHeapException e){
				throw new NoSuchElementException("next(): heap is empty.");
			}
		}

		/**
		 * delete() is an unsupported operation. It does not make sense to provide
		 * a MaxHeap with the ability to delete an arbitrary element.
		 * @throws UnsupportedOperationException always.
		 */
		@Override
		public void remove() throws UnsupportedOperationException, IllegalStateException {
			if(curr == null)
				throw new IllegalStateException("Need at least one call to next() prior to removal.");
			if(curr != data[0])
				throw new UnsupportedOperationException("Removal of arbitrary elements is not supported for MinHeaps.");
			try {
				deleteMin();
				curr = null;
				modified = false;
			} catch (EmptyHeapException ignored) {
				throw new Error("this is not possible");
			}
		}
	}
}
