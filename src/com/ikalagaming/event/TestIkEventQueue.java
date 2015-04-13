package com.ikalagaming.event;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Queue;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the IkEventQueue
 *
 * @author Ches Burks
 *
 */
class TestIkEventQueue {

	private static class InteractionThread extends Thread {
		private boolean done = false;
		public boolean errorFlag = false;
		private IkEventQueue<Event> threadQueue;

		public InteractionThread(IkEventQueue<Event> queue) {
			this.threadQueue = queue;
		}

		public synchronized boolean isDone() {
			return this.done;
		}

		@Override
		public void run() {
			try {
				int iterations = 200;
				int i;
				for (i = 1; i <= iterations; ++i) {
					// add a random number of events, then remove them
					this.threadQueue.add(new TestEvent(i));
					this.threadQueue.remove();
					Thread.sleep(10);
				}
				this.setDone();
			}
			catch (Exception e) {
				this.setDone();
				this.errorFlag = true;
			}
		}

		private synchronized void setDone() {
			this.done = true;
		}

	}

	private IkEventQueue<Event> instanceQueue;

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
		int val;
		try {
			for (i = 0; i < elements; ++i) {
				val = queue.remove().getValue();
				if (val != i) {
					throw new Exception("value " + val + " does not equal " + i);
				}
			}
		}
		catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Tests that the EventQueue is faster than an ArrayDeque for adding and
	 * removing elements. This should not fail, but if it does the class is
	 * still functional. It simply might need some tweaking for speed.
	 */
	@Test
	public void runtimeComparison() {
		IkEventQueue<TestEvent> ik = new IkEventQueue<>();
		ArrayDeque<TestEvent> adq = new ArrayDeque<>();

		int elements = 1000000;
		try {

			//this.queueAdd(adq, elements);

			//this.queueAdd(ik, elements);

			//this.queueRemove(adq, elements);

			//this.queueRemove(ik, elements);

			final long tDequeStart = System.currentTimeMillis();

			this.queueAdd(adq, elements);
			this.queueRemove(adq, elements);

			final long tDequeEnd = System.currentTimeMillis();

			final long tIkStart = System.currentTimeMillis();
			this.queueAdd(ik, elements);
			this.queueRemove(ik, elements);
			final long tIkEnd = System.currentTimeMillis();

			final double tDeque = (tDequeEnd - tDequeStart);
			final double tIkQueue = (tIkEnd - tIkStart);

			if (tIkQueue > tDeque) {
				double time =
						((tIkQueue - tDeque) / ((tDeque + tIkQueue) / 2)) * 100;
				String message = "Slower than ArrayDeque with ";
				message += Double.toString(time);
				message += "% difference (";
				message += tIkQueue + " compared to deque's " + tDeque;
				Assert.fail(message);
			}
			else {
				double time =
						((tDeque - tIkQueue) / ((tDeque + tIkQueue) / 2));
				System.out.println(time + "% faster than deque");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a new event queue for each test
	 *
	 * @throws Exception if an exception occurs
	 */
	@Before
	public void setUp() throws Exception {
		this.instanceQueue = new IkEventQueue<>();
	}

	/**
	 * Empties and dereferences the event queue after each test.
	 *
	 * @throws Exception if an exception occurs
	 */
	@After
	public void tearDown() throws Exception {
		this.instanceQueue.clear();
		this.instanceQueue = null;
	}

	/**
	 * Adds a single element to the queue and ensures no exceptions occurred.
	 */
	@Test
	public void testAdd() {
		try {
			this.instanceQueue.add(new TestEvent(0));
		}
		catch (IllegalStateException e1) {
			Assert.fail("Illegal state exception");
		}
		catch (ClassCastException e2) {
			Assert.fail("Class cast exception");
		}
		catch (NullPointerException e3) {
			Assert.fail("Null pointer exception");
		}
		catch (IllegalArgumentException e4) {
			Assert.fail("Illegal argument exception");
		}
		catch (Exception e) {
			Assert.fail("Exception occurred");
		}

	}

	/**
	 * Creates a set of events and adds them all to the queue. An empty
	 * collection should return false and a non-empty collection should add all
	 * of its elements correctly.
	 */
	@Test
	public void testAddAll() {

		// test empty array
		ArrayList<TestEvent> emptyArray = new ArrayList<>();
		boolean emptyRet = this.instanceQueue.addAll(emptyArray);
		Assert.assertEquals(false, emptyRet);

		// test non-empty array
		ArrayList<TestEvent> eventArray = new ArrayList<>();
		for (int i = 0; i < 20; ++i) {
			eventArray.add(new TestEvent(i));
		}

		boolean ret = this.instanceQueue.addAll(eventArray);
		Assert.assertEquals(true, ret);

		while (!this.instanceQueue.isEmpty()) {
			TestEvent e = (TestEvent) this.instanceQueue.poll();
			if (!eventArray.contains(e)) {
				Assert.fail("Invalid object");
			}
			eventArray.remove(e);
		}
		if (!eventArray.isEmpty()) {
			Assert.fail("Items lost");
		}

	}

	/**
	 * Adds some elements to the queue and clears it. Checks to ensure the queue
	 * is now empty
	 */
	@Test
	public void testClear() {
		int size = 5;
		for (int i = 1; i <= size; ++i) {
			this.instanceQueue.add(new TestEvent(i));
		}
		this.instanceQueue.clear();
		Assert.assertEquals(0, this.instanceQueue.size(), 0);
	}

	/**
	 * Creates a few threads to modify the queue simultaneously to test that it
	 * works correctly.
	 */
	@Test
	public void testConcurrency() {
		InteractionThread thread1 = new InteractionThread(this.instanceQueue);
		InteractionThread thread2 = new InteractionThread(this.instanceQueue);
		InteractionThread thread3 = new InteractionThread(this.instanceQueue);

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
			Assert.fail("Errors from concurrent access");
		}

	}

	@Test
	public void testContains() {
		Assert.fail("Not yet implemented"); // TODO
	}

	/**
	 * Adds a single element to the queue, then removes it and ensures no
	 * exceptions occurred.
	 */
	@Test
	public void testElement() {
		int value = 4;
		this.instanceQueue.add(new TestEvent(value));
		Event e = null;
		try {
			e = this.instanceQueue.element();
		}
		catch (NoSuchElementException ex) {
			Assert.fail("No such element");
		}

		Assert.assertEquals(value, ((TestEvent) e).getValue(), 0);
		Assert.assertEquals(1, this.instanceQueue.size(), 0);
	}
	
	/**
	 * Ensures the queue starts empty, adds an item, checks its not empty,
	 * empties it and then checks that it is empty again.
	 */
	@Test
	public void testIsEmpty() {
		Assert.assertTrue(this.instanceQueue.isEmpty());
		this.instanceQueue.add(new TestEvent(3));
		Assert.assertFalse(this.instanceQueue.isEmpty());
		this.instanceQueue.clear();
		Assert.assertTrue(this.instanceQueue.isEmpty());
	}

	@Test
	public void testIterator() {
		Assert.fail("Not yet implemented"); // TODO
	}

	/**
	 * Adds an element to the queue and ensures it did not throw an exception.
	 */
	@Test
	public void testOffer() {
		try {
			Assert.assertTrue(this.instanceQueue.offer(new TestEvent(0)));
		}
		catch (ClassCastException e2) {
			Assert.fail("Class cast exception");
		}
		catch (NullPointerException e3) {
			Assert.fail("Null pointer exception");
		}
		catch (IllegalArgumentException e4) {
			Assert.fail("Illegal argument exception");
		}
		catch (Exception e) {
			Assert.fail("Exception occurred");
		}
	}

	/**
	 * Creates an element and ensures the peak method returns correctly and does
	 * not remove an item.
	 */
	@Test
	public void testPeek() {
		int value = 4;
		this.instanceQueue.add(new TestEvent(value));
		Event e = this.instanceQueue.peek();
		Assert.assertNotNull(e);
		Assert.assertEquals(value, ((TestEvent) e).getValue(), 0);
		Assert.assertEquals(1, this.instanceQueue.size(), 0);
	}

	/**
	 * Adds a single element to the queue, then removes it and ensures it is not
	 * null. Checks to see if polling an empty queue returns null.
	 */
	@Test
	public void testPoll() {
		int value = 4;
		this.instanceQueue.add(new TestEvent(value));
		Event e = this.instanceQueue.poll();
		Assert.assertNotNull(e);
		Assert.assertEquals(value, ((TestEvent) e).getValue(), 0);
		Assert.assertEquals(0, this.instanceQueue.size(), 0);
		Assert.assertNull(this.instanceQueue.poll());
	}

	/**
	 * Adds a single element to the queue, then removes it and ensures no
	 * exceptions occurred.
	 */
	@Test
	public void testRemove() {
		int value = 4;
		this.instanceQueue.add(new TestEvent(value));
		Event e = null;
		try {
			e = this.instanceQueue.remove();
		}
		catch (NoSuchElementException ex) {
			Assert.fail("No such element");
		}

		Assert.assertNotNull(e);

		Assert.assertEquals(value, ((TestEvent) e).getValue(), 0);
		Assert.assertEquals(0, this.instanceQueue.size(), 0);
		boolean exceptionOccurred = false;
		try {
			e = this.instanceQueue.remove();
		}
		catch (NoSuchElementException ex) {
			exceptionOccurred = true;
		}
		if (!exceptionOccurred) {
			Assert.fail("No exception thrown");
		}
	}

	@Test
	public void testRemoveAll() {
		Assert.fail("Not yet implemented"); // TODO
	}

	@Test
	public void testRemoveObject() {
		Assert.fail("Not yet implemented"); // TODO
	}

	/**
	 * Tests if the queue starts out empty. Adds a few elements, checking the
	 * size each time. Removes the same number of elements that were added,
	 * checking the size each time. Then ensures the queue is empty after all
	 * the elements have been removed.
	 */
	@Test
	public void testSize() {
		Assert.assertEquals(0, this.instanceQueue.size(), 0);
		int size = 4;
		for (int i = 1; i <= size; ++i) {
			this.instanceQueue.add(new TestEvent(i));
			Assert.assertEquals(i, this.instanceQueue.size(), 0);
		}
		for (int i = size - 1; i >= 0; --i) {
			this.instanceQueue.remove();
			Assert.assertEquals(i, this.instanceQueue.size(), 0);
		}
		Assert.assertEquals(0, this.instanceQueue.size(), 0);
	}

	@Test
	public void testToArray() {
		Assert.fail("Not yet implemented"); // TODO
	}

	@Test
	public void testToArrayEArray() {
		Assert.fail("Not yet implemented"); // TODO
	}

}
