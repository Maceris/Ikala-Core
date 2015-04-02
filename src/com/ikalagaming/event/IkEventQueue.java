
package com.ikalagaming.event;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * A Doubly Linked List implementation that is designed to be a very fast queue
 * to be used in the event system. Null entries are allowed, but not suggested.
 * 
 * @author Ches Burks
 *
 * @param <T> The type to use in the queue
 */
public class IkEventQueue<T> implements Queue<T> {
	private transient EventNode<T> header;
	private transient EventNode<T> trailer;
	private transient int size;

	/**
	 * Constructs an empty event queue
	 */
	public IkEventQueue() {
		header = new EventNode<T>(null, null, null);
		trailer = new EventNode<T>(null, null, null);
		header.next = trailer;
		header.prev = header;
		trailer.prev = header;
		trailer.next = trailer;
		size = 0;
	}

	@Override
	public synchronized T peek() {
		if (size == 0) {
			return null;
		}
		return header.next.contents;
	}

	public synchronized boolean isEmpty() {
		return size == 0;
	}

	@Override
	public boolean addAll(Collection<? extends T> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public synchronized void clear() {
		EventNode<T> t = header.next;
		EventNode<T> next;
		// Remove all references to the items
		while (t != trailer) {
			next = t.next;
			t.next = t.prev = null;
			t.contents = null;
			t = next;
		}
		header.next = trailer;
		header.prev = header;
		trailer.prev = header;
		trailer.next = trailer;
		size = 0;
	}

	@Override
	public boolean contains(Object o) {
		return indexOf(o) != -1;
	}

	/**
	 * Returns the index of the first occurrence of the specified element in
	 * this list, or -1 if this list does not contain the element. More
	 * formally, returns the lowest index <code>i</code> such that
	 * <code>(o==null ? e==null : o.equals(e))</code>, or -1 if there is no such
	 * index.
	 * 
	 * @param o element to search for
	 * @return the index of the first occurrence of the specified element in
	 *         this list, or -1 if this list does not contain the element
	 */
	public int indexOf(Object o) {
		int index = 0;
		if (o == null) {
			for (EventNode<T> t = header.next; t != trailer; t = t.next) {
				if (t.contents == null)
					return index;
				++index;
			}
		}
		else {
			for (EventNode<T> t = header.next; t != trailer; t = t.next) {
				if (o.equals(t.contents))
					return index;
				++index;
			}
		}
		return -1;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object arg : c) {
			if (!contains(arg)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public synchronized int size() {
		return size;
	}

	@Override
	public Object[] toArray() {
		Object[] result = new Object[size];
		int index = 0;
		for (EventNode<T> t = header.next; t != trailer; t = t.next) {
			result[index] = t.contents;
			++index;
		}
		return result;
	}

	/**
	 * Returns an array containing all of the elements in this list in proper
	 * sequence (from first to last element); the runtime type of the returned
	 * array is that of the specified array. If the list fits in the specified
	 * array, it is returned therein. Otherwise, a new array is allocated with
	 * the runtime type of the specified array and the size of this list. If the
	 * list fits in the specified array with room to spare (i.e., the array has
	 * more elements than the list), the element in the array immediately
	 * following the end of the collection is set to null. (This is useful in
	 * determining the length of the list only if the caller knows that the list
	 * does not contain any null elements.)
	 * 
	 * 
	 * @param a the array into which the elements of the list are to be stored,
	 *            if it is big enough; otherwise, a new array of the same
	 *            runtime type is allocated for this purpose.
	 * 
	 * @return an array containing the elements of the list
	 * 
	 * @throws ArrayStoreException if the runtime type of the specified array is
	 *             not a supertype of the runtime type of every element in this
	 *             list
	 * @throws NullPointerException if the specified array is null
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <E> E[] toArray(E[] a) {
		if (a.length < size) {
			a = (E[]) Array.newInstance(a.getClass().getComponentType(), size);
		}
		int index = 0;
		for (EventNode<T> t = header.next; t != trailer; t = t.next) {
			a[index] = (E) t.contents;
			++index;
		}
		if (a.length > size) {
			a[size] = null;
		}
		return a;
	}

	@Override
	public synchronized boolean add(T t) {
		EventNode<T> insert = new EventNode<T>(t, trailer, trailer.prev);
		// add right after header
		insert.prev.next = insert;
		insert.next.prev = insert;
		++size;
		return true;
	}

	@Override
	public synchronized T element() {
		if (size == 0) {
			throw new NoSuchElementException();
		}
		return header.next.contents;
	}

	@Override
	public synchronized boolean offer(T t) {
		EventNode<T> insert = new EventNode<T>(t, trailer, trailer.prev);
		// add right after header
		insert.prev.next = insert;
		insert.next.prev = insert;
		++size;
		return true;
	}

	@Override
	public synchronized T poll() {
		if (size == 0) {
			return null;
		}
		EventNode<T> toRemove = header.next;
		T other = header.next.contents;
		toRemove.prev.next = toRemove.next;
		toRemove.next.prev = toRemove.prev;
		toRemove.next = toRemove.prev = null;
		toRemove.contents = null;
		--size;
		return other;
	}

	@Override
	public synchronized T remove() {// e = header.next
		if (size == 0) {
			throw new NoSuchElementException();
		}
		EventNode<T> toRemove = header.next;
		T other = header.next.contents;
		toRemove.prev.next = toRemove.next;
		toRemove.next.prev = toRemove.prev;
		toRemove.next = toRemove.prev = null;
		toRemove.contents = null;
		--size;
		return other;
	}

	private static class EventNode<T> {
		EventNode<T> prev;
		EventNode<T> next;
		T contents;

		public EventNode(T t, EventNode<T> next, EventNode<T> previous) {
			this.prev = previous;
			this.next = next;
			this.contents = t;
		}
	}

}
