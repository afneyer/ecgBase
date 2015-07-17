package ecgBase;

import static org.junit.Assert.*;

import org.junit.Test;

public class MyLogTest {

	@Test
	public void testLogString() {

		AppLog applog = AppLog.getLogService();
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

		AppLog applog = AppLog.getLogService();
		applog.initialize();

		Double[] arrayToLog = { 1.0, 1.1, 1.2, 1.3 };
		applog.log("ArrayToLog",arrayToLog);

		String[] lineList = applog.readLines();
		String logLine3 = "ArrayToLog[0] = 1.0";
		String logLine5 = "ArrayToLog[2] = 1.2";

		// expect:
		assertEquals(logLine3, lineList[2]);
		assertEquals(logLine5, lineList[4]);
	}
	
	@Test
	public void testLogTwoArrays() {

		AppLog applog = AppLog.getLogService();
		applog.initialize();

		Double[] array1ToLog = { 1.0, 1.1, 1.2, 1.3 };
		Double[] array2ToLog = { 1.0001, 1.1001, 1.2001, 1.3001 };
		applog.log("Array1ToLog",array1ToLog, "Array2ToLog", array2ToLog);

		String[] lineList = applog.readLines();
		String logLine3 = "Array1ToLog[0] = 1.0  |  Array2ToLog[0] = 1.0001";
		String logLine5 = "Array1ToLog[2] = 1.2  |  Array2ToLog[2] = 1.2001";

		// expect:
		assertEquals(logLine3, lineList[2]);
		assertEquals(logLine5, lineList[4]);
	}

	@Test
	public void testLogChart() {
		
		// create a basic sine curve
		Double [] sinx = FftTest.genSinusSequence(100, 5);
		Double [] x = ArrUtil.sequence(0.01, 500);
		
		AppLog.logChart("SinCurveTest", "X", "Y", "sin(x)", x, sinx);
		
	}

}
