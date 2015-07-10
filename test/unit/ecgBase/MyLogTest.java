package ecgBase;

import static org.junit.Assert.*;

import org.junit.Test;

public class MyLogTest {

	@Test
	public void testLogStringObjectArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testLogString() {

		MyLog applog = MyLog.getLogService();
		applog.initialize();

		String logString = "Basic Log Test";
		applog.log(logString);

		String[] lineList = applog.readLines();

		// expect
		assertEquals("", lineList[1]);
		assertEquals(logString, lineList[2]);
	}

	@Test
	public void testLogArray() {

		MyLog applog = MyLog.getLogService();
		applog.initialize();

		Double[] arrayToLog = { 1.0, 1.1, 1.2, 1.3 };
		applog.log("ArrayToLog", arrayToLog);

		String[] lineList = applog.readLines();
		String logLine3 = "ArrayToLog[0] = 1.0";
		String logLine5 = "ArrayToLog[2] = 1.2";

		// expect:
		assertEquals(logLine3, lineList[2]);
		assertEquals(logLine5, lineList[4]);
	}

	@Test
	public void testLogChart() {
		fail("Not yet implemented");
	}

}
