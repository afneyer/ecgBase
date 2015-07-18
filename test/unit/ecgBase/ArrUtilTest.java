package ecgBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ArrUtilTest {

	public static AppLog applog = AppLog.getLogService();

	@Test
	public void testTrim() {

		// also tests the ArrUtil.equal routine

		// Inputs and parameters
		Double[] test01 = { 1.0, 1.1, 1.2, 1.3, 1.4 };
		Integer cutOff = 3;

		// Target and parameters
		Double[] target01 = { 1.0, 1.1, 1.2 };
		Double[] target02 = { 1.1, 1.1, 1.2 };
		Double[] target03 = { 1.0, 1.1 };
		Double[] target04 = { 1.0, 1.1, 1.2, 1.3 };
		Double tolerance = 1.0E-6;

		// Test run
		Double[] result = ArrUtil.trim(test01, cutOff);

		// Validation
		assertTrue(ArrUtil.equal(result, target01, tolerance));
		assertFalse(ArrUtil.equal(result, target02, tolerance));
		assertFalse(ArrUtil.equal(result, target03, tolerance));
		assertFalse(ArrUtil.equal(result, target04, tolerance));

	}

	@Test
	public void testTrimInteger() {

		// also tests the ArrUtil.equal routine

		// Inputs and parameters
		Integer[] test01 = { 1, 2, 3, 4, 5 };
		Integer cutOff = 3;

		// Target and parameters
		Integer[] target01 = { 1, 2, 3 };
		Integer[] target02 = { 1, 1, 2 };
		Integer[] target03 = { 1, 1 };
		Integer[] target04 = { 0, 1, 2, 3 };

		// Test run
		Integer[] result = ArrUtil.trim(test01, cutOff);

		// Validation
		assertTrue(ArrUtil.equal(result, target01));
		assertFalse(ArrUtil.equal(result, target02));
		assertFalse(ArrUtil.equal(result, target03));
		assertFalse(ArrUtil.equal(result, target04));

	}

	@Test
	public void testAmplitude() {

		// Inputs and parameters
		Double[] real = { 3.0, -3.0, -3.0, 0.0, 4.0 };
		Double[] imag = { 4.0, 4.0, -4.0, 3.0, 0.0 };

		// Target and parameters
		Double[] target = { 5.0, 5.0, 5.0, 3.0, 4.0 };
		Double tolerance = 1.0E-6;

		// Test run
		Double[] result = ArrUtil.amplitude(real, imag);

		// Validation
		assertTrue(ArrUtil.equal(result, target, tolerance));

	}

	@Test
	public void testEqual() {

		// Inputs and parameters
		Double[] arr1 = { 3.0, -3.0, -3.0, 0.0, 3.0 };
		Double[] arr2 = { 2.9, -3.1, -3.0, 0.1, 3.0 };

		// Target and parameters
		Double tolerance = 0.101;

		// Validation
		assertTrue(ArrUtil.equal(arr1, arr2, tolerance));
		tolerance = 0.01;
		assertFalse(ArrUtil.equal(arr1, arr2, tolerance));

	}
	
	@Test
	public void testEqualTwoDim() {

		// Inputs and parameters
		Double[][] arr1 = { {3.0,-3.0} , {-3.0, 0.0}, {3.0,2.0} };
		Double[][] arr2 = { {2.99, -3.01}, {-3.0, 0.0}, {3.0,2.0} };
		Double[][] arr3 = { {3.0}, {-3.0}, {3.0} };
		Double[][] arr4 = { {3.0,-3.0}, {-3.0, 0.0} };
		Double[][] arr5 = { {3.0} };

		// Target and parameters
		Double tolerance = 0.02;

		// Validation
		assertTrue(ArrUtil.equal(arr1, arr2, tolerance));
		assertFalse(ArrUtil.equal(arr1, arr3, tolerance));
		assertFalse(ArrUtil.equal(arr1, arr4, tolerance));
		assertFalse(ArrUtil.equal(arr1, arr5, tolerance));

	}

	@Test
	public void testAdd() {

		// Inputs and parameters
		Double[] arr1 = { 3.0, -3.0, -3.0, 0.0, 3.0 };
		Double[] arr2 = { 2.9, -3.1, -3.0, 0.1, 3.0 };

		// Target and parameters
		Double tolerance = 1.0E-6;
		Double[] target = { 5.9, -6.1, -6.0, 0.1, 6.0 };

		Double[] sum = ArrUtil.add(arr1, arr2);

		// Validation
		assertTrue(ArrUtil.equal(target, sum, tolerance));

	}

	@Test
	public void constant() {

		// Inputs and parameters
		Double constant = 2.99;

		// Target and parameters
		Double tolerance = 1.0E-6;
		Double[] target = { 2.99, 2.99, 2.99 };

		Double[] constArr = ArrUtil.constant(constant, 3);

		// Validation
		assertTrue(ArrUtil.equal(target, constArr, tolerance));

	}

	@Test
	public void sequence() {

		// Inputs and parameters
		Double interval = 0.1;
		Integer size = 5;

		// Target and parameters
		Double tolerance = 1.0E-6;
		Double[] target = { 0.0, 0.1, 0.2, 0.3, 0.4 };

		Double[] result = ArrUtil.sequence(interval, size);

		// Validation
		assertTrue(ArrUtil.equal(target, result, tolerance));

	}

	@Test
	public void slope() {

		// Inputs and parameters
		Double[] seq = { 1.1, 1.0, 0.95, 0.93, 0.92 };

		// Target and parameters
		Double tolerance = 1.0E-6;
		Double[] target = { -0.1, -0.1, -0.05, -0.02, -0.01 };

		Double[] result = ArrUtil.slope(seq);

		// Validation
		assertTrue(ArrUtil.equal(target, result, tolerance));

	}

	@Test
	public void absSlope() {

		// Inputs and parameters
		Double[] seq = { 1.1, 1.0, 0.95, 0.93, 0.92 };

		// Target and parameters
		Double tolerance = 1.0E-6;
		Double[] target = { 0.1, 0.1, 0.05, 0.02, 0.01 };

		Double[] result = ArrUtil.absSlope(seq);

		// Validation
		assertTrue(ArrUtil.equal(target, result, tolerance));

	}

	@Test
	public void rangeAverage() {

		// Inputs and parameters
		Double[] seq = { 1.1, 1.0, 0.95, 0.93, 0.92 };

		// Target and parameters
		Double tolerance = 1.0E-6;

		assertEquals(0.96, ArrUtil.rangeAverage(seq, 1, 3), tolerance);

		assertNull(ArrUtil.rangeAverage(seq, 6, 10));
		assertNull(ArrUtil.rangeAverage(seq, 3, 6));
		assertNull(ArrUtil.rangeAverage(seq, 3, 1));

	}

	@Test
	public void testToPrimitive() {
		// Input
		Double[][] inArray = { { 1.1, 1.2 }, { 2.1, 2.2 }, { 3.1, 3.2 } };

		double[][] outArray = ArrUtil.toPrimitive(inArray);

		int inRows = inArray.length;
		int outRows = outArray.length;
		assertEquals(inRows, outRows);
		int inCols = inArray[0].length;
		int outCols = outArray[0].length;
		assertEquals(inCols, outCols);

		double tolerance = 1.0E-6;

		for (int i = 0; i < inRows; i++) {
			for (int j = 0; j < inCols; j++) {
				assertEquals(inArray[i][j].doubleValue(), outArray[i][j],
						tolerance);
			}
		}
	}

	@Test
	public void testCopy() {

		// Inputs and parameters
		Double[] seq = { 1.1, 1.0, 0.95, 0.93, 0.92 };

		// Target and parameters
		Double tolerance = 1.0E-6;

		Double[] result = ArrUtil.getCopy(seq);

		// Validation
		assertTrue(ArrUtil.equal(seq, result, tolerance));

	}

	@Test
	public void testScale() {

		// Inputs and parameters
		Double[] seq = { 1.1, 1.0, 0.95, 0.93, 0.92 };

		// Target and parameters
		Double tolerance = 1.0E-6;
		Double[] target = { 2.2, 2.0, 1.9, 1.86, 1.84 };

		Double[] result = ArrUtil.scale(seq, 2.0);

		// Validation
		assertTrue(ArrUtil.equal(target, result, tolerance));

	}

	@Test
	public void testPeaks() {

		// Inputs and parameters
		Double[] seq = { 0.0, 0.2, 0.4, 0.5, 0.4, 0.2, 0.0, -0.2, -0.4, -0.5,
				-0.4, -0.2, 0.0, 0.2, 0.3, 0.35, 0.3, 0.2, 0.0, -0.2, -0.3,
				-0.35, -0.3, -0.2, 0.0 };

		// Target and parameters
		// single peak
		Double cutOff = 0.95;
		Integer[] target = { 3 };
		Integer[] result = ArrUtil.peaks(seq, cutOff);
		assertTrue(ArrUtil.equal(target, result));

		// multiple peak
		cutOff = 0.7;
		target = new Integer[] { 3, 15 };
		result = ArrUtil.peaks(seq, cutOff);
		assertTrue(ArrUtil.equal(target, result));

		// multiple peak, the last value is a peak
		cutOff = 0.5;
		target = new Integer[] { 3, 15, 24 };
		result = ArrUtil.peaks(seq, cutOff);
		assertTrue(ArrUtil.equal(target, result));

		// single negative peak
		cutOff = -0.95;
		target = new Integer[] { 9 };
		result = ArrUtil.peaks(seq, cutOff);
		assertTrue(ArrUtil.equal(target, result));

		// multiple negative peak
		cutOff = -0.7;
		target = new Integer[] { 9, 21 };
		result = ArrUtil.peaks(seq, cutOff);
		assertTrue(ArrUtil.equal(target, result));

		// multiple peak running staying above the cutoff until the end of the
		// sequence
		cutOff = -0.4;
		// target05 = new Integer[]{ 100,200 };
		target = new Integer[] { 0, 9, 21 };
		result = ArrUtil.peaks(seq, cutOff);
		assertTrue(ArrUtil.equal(target, result));

	}
	
	@Test
	public void testPeakIntervals() {

		// Inputs and parameters
		Double[] seq = { 0.0, 0.2, 0.4, 0.5, 0.4, 0.2, 0.0, -0.2, -0.4, -0.5,
				-0.4, -0.2, 0.0, 0.2, 0.3, 0.35, 0.3, 0.2, 0.0, -0.2, -0.3,
				-0.35, -0.3, -0.2, 0.0 };
		
		Double[] x = ArrUtil.sequence(1.0, 25);
		applog.logChart("PeakIntervalTestData", "x", "y", "y(x)", x, seq);

		// Target and parameters
		// single peak
		Double cutOff = 0.95;
		Integer[][] target = {{2},{3},{4}};
		Integer[][] result = ArrUtil.peakIntervals(seq, cutOff);
		for (int i=0; i<result[0].length; i++) {
			applog.log("PeakInterval [" + i + "] = " + result[0][i] + "|" + result[1][i] + "|" + result[2][i]  );
		}
		assertTrue(ArrUtil.equal(target, result));
		
		// multiple peak
		cutOff = 0.7;
		target = new Integer[][] {{1,13},{3,15},{5,17}};
		result = ArrUtil.peakIntervals(seq, cutOff);
		for (int i=0; i<result[0].length; i++) {
			applog.log("PeakInterval [" + i + "] = " + result[0][i] + "|" + result[1][i] + "|" + result[2][i]  );
		}
		assertTrue(ArrUtil.equal(target, result));
		

		// multiple peak, the last value is a peak
		cutOff = 0.5;
		target = new Integer[][] {{0,12,24},{ 3,15,24 },{6,18,24}};
		// target = new Integer[] { 3, 15, 24 };
		result = ArrUtil.peakIntervals(seq, cutOff);
		for (int i=0; i<result[0].length; i++) {
			applog.log("PeakInterval [" + i + "] = " + result[0][i] + "|" + result[1][i] + "|" + result[2][i]  );
		}
		assertTrue(ArrUtil.equal(target, result));

		// single negative peak
		cutOff = -0.95;
		target = new Integer[][] {{8},{9},{10}};
		// target = new Integer[] { 9 };
		result = ArrUtil.peakIntervals(seq, cutOff);
		for (int i=0; i<result[0].length; i++) {
			applog.log("PeakInterval [" + i + "] = " + result[0][i] + "|" + result[1][i] + "|" + result[2][i]  );
		}
		assertTrue(ArrUtil.equal(target, result));

		// multiple negative peak
		cutOff = -0.7;
		target = new Integer[][] {{7,19},{9,21},{11,23}};
		// target = new Integer[] { 9, 21 };
		result = ArrUtil.peakIntervals(seq, cutOff);
		for (int i=0; i<result[0].length; i++) {
			applog.log("PeakInterval [" + i + "] = " + result[0][i] + "|" + result[1][i] + "|" + result[2][i]  );
		}
		assertTrue(ArrUtil.equal(target, result));

		// multiple peak running staying above the cutoff until the end of the
		// sequence
		cutOff = -0.4;
		target = new Integer[][] {{0,6,18},{0,9,21},{0,12,24}};
		// target = new Integer[] { 0, 9, 21 };
		result = ArrUtil.peakIntervals(seq, cutOff);
		for (int i=0; i<result[0].length; i++) {
			applog.log("PeakInterval [" + i + "] = " + result[0][i] + "|" + result[1][i] + "|" + result[2][i]  );
		}
		assertTrue(ArrUtil.equal(target, result));

	}

}
