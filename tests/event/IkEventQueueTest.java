
package event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ikalagaming.event.Event;
import com.ikalagaming.event.IkEventQueue;

/**
 * Unit tests for the IkEventQueue
 * 
 * @author Ches Burks
 *
 */
public class IkEventQueueTest {

	IkEventQueue<Event> queue;

	/**
	 * Creates a new event queue for each test
	 * 
	 * @throws Exception if an exception occurs
	 */
	@Before
	public void setUp() throws Exception {
		queue = new IkEventQueue<Event>();
	}

	/**
	 * Empties and dereferences the event queue after each test.
	 * 
	 * @throws Exception if an exception occurs
	 */
	@After
	public void tearDown() throws Exception {
		queue.clear();
		queue = null;
	}

	@Test
	public void testIkEventQueue() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Creates an element and ensures the peak method returns correctly and does
	 * not remove an item.
	 */
	@Test
	public void testPeek() {
		int value = 4;
		queue.add(new TestEvent(value));
		Event e = queue.peek();
		assertNotNull(e);
		assertEquals(value, ((TestEvent) e).getValue(), 0);
		assertEquals(1, queue.size(), 0);
	}

	/**
	 * Ensures the queue starts empty, adds an item, checks its not empty,
	 * empties it and then checks that it is empty again.
	 */
	@Test
	public void testIsEmpty() {
		assertTrue(queue.isEmpty());
		queue.add(new TestEvent(3));
		assertFalse(queue.isEmpty());
		queue.clear();
		assertTrue(queue.isEmpty());
	}

	@Test
	public void testAddAll() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Adds some elements to the queue and clears it. Checks to ensure the queue
	 * is now empty
	 */
	@Test
	public void testClear() {
		int size = 5;
		for (int i = 1; i <= size; ++i) {
			queue.add(new TestEvent(i));
		}
		queue.clear();
		assertEquals(0, queue.size(), 0);
	}

	@Test
	public void testContains() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testIndexOf() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testContainsAll() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testIterator() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testRemoveObject() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testRemoveAll() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testRetainAll() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Tests if the queue starts out empty. Adds a few elements, checking the
	 * size each time. Removes the same number of elements that were added,
	 * checking the size each time. Then ensures the queue is empty after all
	 * the elements have been removed.
	 */
	@Test
	public void testSize() {
		assertEquals(0, queue.size(), 0);
		int size = 4;
		for (int i = 1; i <= size; ++i) {
			queue.add(new TestEvent(i));
			assertEquals(i, queue.size(), 0);
		}
		for (int i = size - 1; i >= 0; --i) {
			queue.remove();
			assertEquals(i, queue.size(), 0);
		}
		assertEquals(0, queue.size(), 0);
	}

	@Test
	public void testToArray() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testToArrayEArray() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Adds a single element to the queue and ensures no exceptions occurred.
	 */
	@Test
	public void testAdd() {
		try {
			queue.add(new TestEvent(0));
		}
		catch (IllegalStateException e1) {
			fail("Illegal state exception");
		}
		catch (ClassCastException e2) {
			fail("Class cast exception");
		}
		catch (NullPointerException e3) {
			fail("Null pointer exception");
		}
		catch (IllegalArgumentException e4) {
			fail("Illegal argument exception");
		}
		catch (Exception e) {
			fail("Exception occurred");
		}

	}

	/**
	 * Adds a single element to the queue, then removes it and ensures no
	 * exceptions occurred.
	 */
	@Test
	public void testElement() {
		int value = 4;
		queue.add(new TestEvent(value));
		Event e = null;
		try {
			e = queue.element();
		}
		catch (NoSuchElementException ex) {
			fail("No such element");
		}

		assertEquals(value, ((TestEvent) e).getValue(), 0);
		assertEquals(1, queue.size(), 0);
	}

	/**
	 * Adds an element to the queue and ensures it did not throw an exception.
	 */
	@Test
	public void testOffer() {
		try {
			assertTrue(queue.offer(new TestEvent(0)));
		}
		catch (ClassCastException e2) {
			fail("Class cast exception");
		}
		catch (NullPointerException e3) {
			fail("Null pointer exception");
		}
		catch (IllegalArgumentException e4) {
			fail("Illegal argument exception");
		}
		catch (Exception e) {
			fail("Exception occurred");
		}
	}

	/**
	 * Adds a single element to the queue, then removes it and ensures it is not
	 * null. Checks to see if polling an empty queue returns null.
	 */
	@Test
	public void testPoll() {
		int value = 4;
		queue.add(new TestEvent(value));
		Event e = queue.poll();
		assertNotNull(e);
		assertEquals(value, ((TestEvent) e).getValue(), 0);
		assertEquals(0, queue.size(), 0);
		assertNull(queue.poll());
	}

	/**
	 * Adds a single element to the queue, then removes it and ensures no
	 * exceptions occurred.
	 */
	@Test
	public void testRemove() {
		int value = 4;
		queue.add(new TestEvent(value));
		Event e = null;
		try {
			e = queue.remove();
		}
		catch (NoSuchElementException ex) {
			fail("No such element");
		}

		assertEquals(value, ((TestEvent) e).getValue(), 0);
		assertEquals(0, queue.size(), 0);
		boolean exceptionOccurred = false;
		try {
			e = queue.remove();
		}
		catch (NoSuchElementException ex) {
			exceptionOccurred = true;
		}
		if (!exceptionOccurred) {
			fail("No exception thrown");
		}
	}

	/**
	 * Creates a few threads to modify the queue simultaneously to test that it
	 * works correctly.
	 */
	@Test
	public void testConcurrency() {
		InteractionThread thread1 = new InteractionThread(queue);
		InteractionThread thread2 = new InteractionThread(queue);
		InteractionThread thread3 = new InteractionThread(queue);

		thread1.start();
		thread2.start();
		thread3.start();

		long start = System.currentTimeMillis();
		long timeout = 10000;// 10 seconds
		int i = 0;

		boolean d1 = false;
		boolean d2 = false;
		boolean d3 = false;

		boolean e1 = false;
		boolean e2 = false;
		boolean e3 = false;

		while (!d1 || !d2 || !d3) {

			if (i >= 10000) {
				i = 0;
				if (System.currentTimeMillis() - start >= timeout) {
					start = System.currentTimeMillis();
					if (d1 && d2 && d3) {
						System.out.println("it should not be true here ");
						break;
					}
					String errorMsg = "Infinite loop. Threads: ";
					errorMsg += "1";
					if (d1) {
						errorMsg += "(done)";
					}
					System.out.println(thread1 + " " + thread2 + " " + thread3);

					errorMsg += " 2";
					if (d2) {
						errorMsg += "(done)";
					}

					errorMsg += " 3";
					if (d3) {
						errorMsg += "(done)";
					}

					if (e1 || e2 || e3) {
						String er = "errors at ";
						if (e1) {
							er += "1";
						}
						if (e2) {
							er += " 2";
						}
						if (e3) {
							er += " 3";
						}
						System.out.println(er);
					}
					System.out.println(errorMsg);

					// fail(errorMsg);
				}
			}
			++i;
			// for when they join and loose the variable contents.
			d1 = d1 || thread1.isDone();
			d2 = d2 || thread2.isDone();
			d3 = d3 || thread3.isDone();
			// if they are ever true, they stay true.
			e1 = e1 || thread1.errorFlag;
			e2 = e2 || thread2.errorFlag;
			e3 = e3 || thread3.errorFlag;
		}
		if (e1 || e2 || e3) {
			fail("Errors from concurrent access");
		}

	}

	/**
	 * Tests that the EventQueue is faster than a LinkedList for adding and
	 * removing elements. This should not fail, but if it does the class is
	 * still functional. It simply might need some tweaking for speed.
	 */
	@Test
	public void runtimeComparison() {
		IkEventQueue<TestEvent> ik = new IkEventQueue<TestEvent>();
		LinkedList<TestEvent> ll = new LinkedList<TestEvent>();

		int elements = 1000000;
		try {
			synchronized (ll) {
				queueAdd(ll, elements);
			}
			queueAdd(ik, elements);
			synchronized (ll) {
				queueRemove(ll, elements);
			}
			queueRemove(ik, elements);

			long t2start = System.currentTimeMillis();
			synchronized (ll) {
				queueAdd(ll, elements);
				queueRemove(ll, elements);
			}
			long t2end = System.currentTimeMillis();

			long t1start = System.currentTimeMillis();
			queueAdd(ik, elements);
			queueRemove(ik, elements);
			long t1end = System.currentTimeMillis();

			if ((t1end - t1start) > (t2end - t2start)) {
				fail("Slower than LinkedList for adding " + elements
						+ " elements");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void queueAdd(Queue<TestEvent> queue, int elements)
			throws Exception {
		int i;

		try {
			for (i = 0; i < elements; ++i) {
				queue.add(new TestEvent(i));
			}
		}
		catch (Exception e) {
			throw e;
		}
	}

	private void queueRemove(Queue<TestEvent> queue, int elements)
			throws Exception {
		int i;

		try {
			for (i = 0; i < elements; ++i) {
				if (queue.remove().getValue() != i) {
					throw new Exception("value does not equal " + i);
				}
			}
		}
		catch (Exception e) {
			throw e;
		}
	}

	private static class InteractionThread extends Thread {
		private boolean done = false;
		public boolean errorFlag = false;
		private IkEventQueue<Event> queue;

		public InteractionThread(IkEventQueue<Event> queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			try {
				int iterations = 200;
				int i;
				for (i = 1; i <= iterations; ++i) {
					// add a random number of events, then remove them
					queue.add(new TestEvent(i));
					queue.remove();
					sleep(10);
				}
				this.setDone();
			}
			catch (Exception e) {
				this.setDone();
				this.errorFlag = true;
			}
		}

		public synchronized boolean isDone() {
			return this.done;
		}

		private synchronized void setDone() {
			this.done = true;
		}

	}

}
