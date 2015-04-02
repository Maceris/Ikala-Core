
package gui.console;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ikalagaming.event.EventManager;
import com.ikalagaming.gui.console.Console;

public class ConsoleTest {

	static EventManager manager;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		manager = new EventManager();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		manager.shutdown();
	}

	@Before
	public void setUp() throws Exception {}

	@After
	public void tearDown() throws Exception {}

	@Test
	public void testConsole() {
		Console console = new Console(manager);
		console.onLoad();
		console.enable();
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testAppendMessage() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testDisable() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testEnable() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetHeight() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetMaxLineCount() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetName() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetVersion() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetWidth() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetWindowTitle() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testIsEnabled() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testOnDisable() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testOnEnable() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testOnLoad() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testOnLogEvent() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testOnLogErrorEvent() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testOnReportUnknownCommand() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testOnUnload() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testReload() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testSetHeight() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testSetMaxLineCount() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testSetWidth() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testSetWindowTitle() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetListeners() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetPackageState() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testSetPackageState() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testLostOwnership() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testWindowAdapter() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testWindowOpened() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testWindowClosing() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testWindowClosed() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testWindowIconified() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testWindowDeiconified() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testWindowActivated() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testWindowDeactivated() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testWindowStateChanged() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testWindowGainedFocus() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testWindowLostFocus() {
		fail("Not yet implemented"); // TODO
	}

}
