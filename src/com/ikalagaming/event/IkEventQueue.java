package com.ikalagaming.event;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * A modification of the ArrayDeque class that is designed to be even faster.
 *
 * @see ArrayDeque
 *
 * @param <E> the type of elements held in this collection
 */
public class IkEventQueue<E> extends AbstractCollection<E> implements Queue<E>,
		Cloneable, Serializable {
	private class DeqIterator implements Iterator<E> {
		/**
		 * Index of element to be returned by subsequent call to next.
		 */
		private int currentIndex = IkEventQueue.this.head;

		/**
		 * Tail recorded at construction (also in remove), to stop iterator and
		 * also to check for comodification.
		 */
		private int lastSafeIndex = IkEventQueue.this.tail;

		/**
		 * Index of element returned by most recent call to next. Reset to -1 if
		 * element is deleted by a call to remove.
		 */
		private int lastRet = -1;

		public DeqIterator() {}

		@Override
		public boolean hasNext() {
			return this.currentIndex != this.lastSafeIndex;
		}

		@Override
		public E next() {
			if (this.currentIndex == this.lastSafeIndex) {
				throw new NoSuchElementException();
			}
			final E result = IkEventQueue.this.elements[this.currentIndex];
			// This check doesn't catch all possible comodifications,
			// but does catch the ones that corrupt traversal
			if (IkEventQueue.this.tail != this.lastSafeIndex || result == null) {
				throw new ConcurrentModificationException();
			}
			this.lastRet = this.currentIndex;
			this.currentIndex =
					this.currentIndex + 1 & IkEventQueue.this.elements.length
							- 1;
			return result;
		}

		@Override
		public void remove() {
			if (this.lastRet < 0) {
				throw new IllegalStateException();
			}
			if (IkEventQueue.this.delete(this.lastRet)) {
				// if left-shifted, undo increment in next()
				this.currentIndex =
						this.currentIndex - 1
								& IkEventQueue.this.elements.length - 1;
				this.lastSafeIndex = IkEventQueue.this.tail;
			}
			this.lastRet = -1;
		}
	}

	private static final long serialVersionUID = -6955217856676299888L;

	/**
	 * The array in which the elements of the queue are stored. The capacity of
	 * the queue is the length of this array, which is always a power of two.
	 * The array is never allowed to become full, except transiently within an
	 * addX method where it is resized (see doubleCapacity) immediately upon
	 * becoming full, thus avoiding head and tail wrapping around to equal each
	 * other. We also guarantee that all array cells not holding queue elements
	 * are always null.
	 */
	transient E[] elements;

	/**
	 * The index of the element at the head of the queue (which is the element
	 * that would be removed by remove()); or an arbitrary number equal to tail
	 * if the queue is empty.
	 */
	transient int head;

	/**
	 * The index at which the next element would be added to the tail of the
	 * queue.
	 */
	transient int tail;

	/**
	 * The minimum capacity that we'll use for a newly created queue. Must be a
	 * power of 2.
	 */
	private static final int MIN_INITIAL_CAPACITY = 8;

	/**
	 * Constructs an empty array queue with an initial capacity sufficient to
	 * hold 16 elements.
	 */
	@SuppressWarnings("unchecked")
	public IkEventQueue() {
		this.elements = (E[]) new Object[8];
	}

	/**
	 * Inserts the specified element at the end of this queue.
	 *
	 * @param e the element to add
	 * @return <tt>true</tt> (as specified by {@link Collection#add})
	 * @throws NullPointerException if the specified element is null
	 */
	@Override
	public boolean add(final E e) {
		if (e == null) {
			throw new NullPointerException();
		}
		this.elements[this.tail] = e;
		if ((this.tail = this.tail + 1 & this.elements.length - 1) == this.head) {
			this.doubleCapacity();
		}
		return true;
	}

	/**
	 * Allocate empty array to hold the given number of elements.
	 *
	 * @param numElements the number of elements to hold
	 */
	@SuppressWarnings("unchecked")
	public void allocateElements(final int numElements) {
		int initialCapacity = IkEventQueue.MIN_INITIAL_CAPACITY;
		// Find the best power of two to hold elements.
		// Tests "<=" because arrays aren't kept full.
		if (numElements >= initialCapacity) {
			initialCapacity = numElements;
			initialCapacity |= initialCapacity >>> 1;
			initialCapacity |= initialCapacity >>> 2;
			initialCapacity |= initialCapacity >>> 4;
			initialCapacity |= initialCapacity >>> 8;
			initialCapacity |= initialCapacity >>> 16;
			++initialCapacity;

			if (initialCapacity < 0) {
				initialCapacity >>>= 1;// Good luck allocating 2 ^ 30 elements
			}
		}
		this.elements = (E[]) new Object[initialCapacity];
	}

	/**
	 * Removes all of the elements from this queue. The queue will be empty
	 * after this call returns.
	 */
	@Override
	public void clear() {
		final int h = this.head;
		final int t = this.tail;
		if (h != t) { // clear all cells
			this.head = this.tail = 0;
			int i = h;
			final int mask = this.elements.length - 1;
			do {
				this.elements[i] = null;
				i = i + 1 & mask;
			}
			while (i != t);
		}
	}

	/**
	 * Returns a copy of this queue.
	 *
	 * @return a copy of this queue
	 */
	@Override
	public IkEventQueue<E> clone() {
		try {
			@SuppressWarnings("unchecked")
			IkEventQueue<E> result = (IkEventQueue<E>) super.clone();
			result.elements =
					Arrays.copyOf(this.elements, this.elements.length);
			return result;

		}
		catch (final CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}

	/**
	 * Returns <tt>true</tt> if this queue contains the specified element. More
	 * formally, returns <tt>true</tt> if and only if this queue contains at
	 * least one element <tt>e</tt> such that <tt>o.equals(e)</tt>.
	 *
	 * @param o object to be checked for containment in this queue
	 * @return <tt>true</tt> if this queue contains the specified element
	 */
	@Override
	public boolean contains(final Object o) {
		if (o == null) {
			return false;
		}
		final int mask = this.elements.length - 1;
		int i = this.head;
		E x;
		while ((x = this.elements[i]) != null) {
			if (o.equals(x)) {
				return true;
			}
			i = i + 1 & mask;
		}
		return false;
	}

	/**
	 * Removes the element at the specified position in the elements array,
	 * adjusting head and tail as necessary. This can result in motion of
	 * elements backwards or forwards in the array.
	 *
	 * <p>
	 * This method is called delete rather than remove to emphasize that its
	 * semantics differ from those of {@link List#remove(int)}.
	 *
	 * @param i the position of the element to delete
	 *
	 * @return true if elements moved backwards
	 */
	boolean delete(final int i) {
		assert this.elements[this.tail] == null;
		assert this.head == this.tail ? this.elements[this.head] == null
				: this.elements[this.head] != null
						&& this.elements[this.tail - 1 & this.elements.length
								- 1] != null;
		assert this.elements[this.head - 1 & this.elements.length - 1] == null;
		E[] elems = this.elements;
		final int mask = elems.length - 1;
		final int h = this.head;
		final int t = this.tail;
		final int front = i - h & mask;
		final int back = t - i & mask;

		// Invariant: head <= i < tail mod circularity
		if (front >= (t - h & mask)) {
			throw new ConcurrentModificationException();
		}

		// Optimize for least element motion
		if (front < back) {
			if (h <= i) {
				System.arraycopy(elems, h, elems, h + 1, front);
			}
			else { // Wrap around
				System.arraycopy(elems, 0, elems, 1, i);
				elems[0] = elems[mask];
				System.arraycopy(elems, h, elems, h + 1, mask - h);
			}
			elems[h] = null;
			this.head = h + 1 & mask;
			return false;
		}
		if (i < t) { // Copy the null tail as well
			System.arraycopy(elems, i + 1, elems, i, back);
			this.tail = t - 1;
		}
		else { // Wrap around
			System.arraycopy(elems, i + 1, elems, i, mask - i);
			elems[mask] = elems[0];
			System.arraycopy(elems, 1, elems, 0, t);
			this.tail = t - 1 & mask;
		}
		return true;
	}

	/**
	 * Double the capacity of this queue. Call only when full, i.e., when head
	 * and tail have wrapped around to become equal.
	 */
	@SuppressWarnings("unchecked")
	private void doubleCapacity() {
		assert this.head == this.tail;
		final int p = this.head;
		final int n = this.elements.length;
		final int r = n - p; // number of elements to the right of p
		final int newCapacity = n << 1;
		if (newCapacity < 0) {
			throw new IllegalStateException("Queue has exceeded max size");
		}
		Object[] a = new Object[newCapacity];
		System.arraycopy(this.elements, p, a, 0, r);
		System.arraycopy(this.elements, 0, a, r, p);
		this.elements = (E[]) a;
		this.head = 0;
		this.tail = n;
	}

	/**
	 * Retrieves, but does not remove, the head of the queue represented by this
	 * queue. This method differs from {@link #peek peek} only in that it throws
	 * an exception if this queue is empty.
	 *
	 * @return the head of the queue represented by this queue
	 * @throws NoSuchElementException {@inheritDoc}
	 */
	@Override
	public E element() {
		E x = this.elements[this.head];
		if (x == null) {
			throw new NoSuchElementException();
		}
		return x;
	}

	/**
	 * Returns <tt>true</tt> if this queue contains no elements.
	 *
	 * @return <tt>true</tt> if this queue contains no elements
	 */
	@Override
	public boolean isEmpty() {
		return this.head == this.tail;
	}

	/**
	 * Returns an iterator over the elements in this queue. The elements will be
	 * ordered from first (head) to last (tail). This is the same order that
	 * elements would be queueued in (via successive calls to {@link #remove}).
	 *
	 * @return an iterator over the elements in this queue
	 */
	@Override
	public Iterator<E> iterator() {
		return new DeqIterator();
	}

	/**
	 * Inserts the specified element at the end of this queue.
	 *
	 * @param e the element to add
	 * @return <tt>true</tt> (as specified by {@link Queue#offer})
	 * @throws NullPointerException if the specified element is null
	 */
	@Override
	public boolean offer(final E e) {
		if (e == null) {
			throw new NullPointerException();
		}
		this.elements[this.tail] = e;
		if ((this.tail = this.tail + 1 & this.elements.length - 1) == this.head) {
			this.doubleCapacity();
		}
		return true;
	}

	/**
	 * Retrieves, but does not remove, the head of the queue represented by this
	 * queue, or returns <tt>null</tt> if this queue is empty.
	 *
	 *
	 * @return the head of the queue represented by this queue, or <tt>null</tt>
	 *         if this queue is empty
	 */
	@Override
	public E peek() {
		return this.elements[this.head]; // elements[head] is null if queue
	}

	/**
	 * Retrieves and removes the head of the queue represented by this queue (in
	 * other words, the first element of this queue), or returns <tt>null</tt>
	 * if this queue is empty.
	 *
	 * @return the head of the queue represented by this queue, or <tt>null</tt>
	 *         if this queue is empty
	 */
	@Override
	public E poll() {
		final int h = this.head;
		E result = this.elements[h]; // Element is null if queue empty
		if (result == null) {
			return null;
		}
		this.elements[h] = null; // Must null out slot
		this.head = h + 1 & this.elements.length - 1;
		return result;
	}

	/**
	 * Deserialize this queue.
	 *
	 * @param s the object input stream to read into
	 * @throws IOException Any of the usual Input/Output related exceptions.
	 * @throws ClassNotFoundException Class of a serialized object cannot be
	 *             found.
	 */
	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream s) throws IOException,
			ClassNotFoundException {
		s.defaultReadObject();

		// Read in size and allocate array
		final int size = s.readInt();
		this.allocateElements(size);
		this.head = 0;
		this.tail = size;

		// Read in all elements in the proper order.
		for (int i = 0; i < size; ++i) {
			this.elements[i] = (E) s.readObject();
		}
	}

	/**
	 * Retrieves and removes the head of the queue represented by this queue.
	 *
	 * This method differs from {@link #poll poll} only in that it throws an
	 * exception if this queue is empty.
	 *
	 * @return the head of the queue represented by this queue
	 * @throws NoSuchElementException {@inheritDoc}
	 */
	@Override
	public E remove() {
		final int h = this.head;
		E result = this.elements[h]; // Element is null if queue empty
		if (result == null) {
			throw new NoSuchElementException();
		}
		this.elements[h] = null; // Must null out slot
		this.head = h + 1 & this.elements.length - 1;
		return result;
	}

	/**
	 * Removes a single instance of the specified element from this queue. If
	 * the queue does not contain the element, it is unchanged. More formally,
	 * removes the first element <tt>e</tt> such that <tt>o.equals(e)</tt> (if
	 * such an element exists). Returns <tt>true</tt> if this queue contained
	 * the specified element (or equivalently, if this queue changed as a result
	 * of the call).
	 *
	 *
	 * @param o element to be removed from this queue, if present
	 * @return <tt>true</tt> if this queue contained the specified element
	 */
	@Override
	public boolean remove(final Object o) {
		if (o == null) {
			return false;
		}
		final int mask = this.elements.length - 1;
		int i = this.head;
		E x;
		while ((x = this.elements[i]) != null) {
			if (o.equals(x)) {
				this.delete(i);
				return true;
			}
			i = i + 1 & mask;
		}
		return false;
	}

	// *** Object methods ***

	/**
	 * Returns the number of elements in this queue.
	 *
	 * @return the number of elements in this queue
	 */
	@Override
	public int size() {
		return this.tail - this.head & this.elements.length - 1;
	}

	/**
	 * Returns an array containing all of the elements in this queue in proper
	 * sequence (from first to last element); the runtime type of the returned
	 * array is that of the specified array. If the queue fits in the specified
	 * array, it is returned therein. Otherwise, a new array is allocated with
	 * the runtime type of the specified array and the size of this queue.
	 *
	 * <p>
	 * If this queue fits in the specified array with room to spare (i.e., the
	 * array has more elements than this queue), the element in the array
	 * immediately following the end of the queue is set to <tt>null</tt>.
	 *
	 * <p>
	 * Like the {@link #toArray()} method, this method acts as bridge between
	 * array-based and collection-based APIs. Further, this method allows
	 * precise control over the runtime type of the output array, and may, under
	 * certain circumstances, be used to save allocation costs.
	 *
	 * <p>
	 * Suppose <tt>x</tt> is a queue known to contain only strings. The
	 * following code can be used to dump the queue into a newly allocated array
	 * of <tt>String</tt>:
	 *
	 * <pre>
	 * String[] y = x.toArray(new String[0]);
	 * </pre>
	 *
	 * Note that <tt>toArray(new Object[0])</tt> is identical in function to
	 * <tt>toArray()</tt>.
	 *
	 * @param a the array into which the elements of the queue are to be stored,
	 *            if it is big enough; otherwise, a new array of the same
	 *            runtime type is allocated for this purpose
	 * @return an array containing all of the elements in this queue
	 * @throws ArrayStoreException if the runtime type of the specified array is
	 *             not a supertype of the runtime type of every element in this
	 *             queue
	 * @throws NullPointerException if the specified array is null
	 */
	@SuppressWarnings("all")
	@Override
	public <T> T[] toArray(T[] a) {
		final int size = this.size();
		if (a.length < size) {
			a =
					(T[]) java.lang.reflect.Array.newInstance(a.getClass()
							.getComponentType(), size);
		}
		if (this.head < this.tail) {
			System.arraycopy(this.elements, this.head, a, 0, this.size());
		}
		else if (this.head > this.tail) {
			final int headPortionLen = this.elements.length - this.head;
			System.arraycopy(this.elements, this.head, a, 0, headPortionLen);
			System.arraycopy(this.elements, 0, a, headPortionLen, this.tail);
		}

		if (a.length > size) {
			a[size] = null;
		}
		return a;
	}

	/**
	 * Serialize this queue.
	 *
	 * @param s the output stream to write to
	 * @throws IOException if I/O errors occur while writing to the underlying
	 *             OutputStream
	 *
	 * @serialData The current size (<tt>int</tt>) of the queue, followed by all
	 *             of its elements (each an object reference) in first-to-last
	 *             order.
	 */
	private void writeObject(ObjectOutputStream s) throws IOException {
		s.defaultWriteObject();

		// Write out size
		s.writeInt(this.size());

		// Write out elements in order.
		final int mask = this.elements.length - 1;
		for (int i = this.head; i != this.tail; i = i + 1 & mask) {
			s.writeObject(this.elements[i]);
		}
	}
}
