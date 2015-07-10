package ecgBase;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArrUtilTest {

	@Test
	public void testTrim() {
		
		// also tests the ArrUtil.equal routine
		
		// Inputs and parameters
		Double [] test01 = {1.0, 1.1, 1.2, 1.3, 1.4};
		Integer cutOff = 3;
		
		// Target and parameters
		Double [] target01 = {1.0, 1.1, 1.2};
		Double [] target02 = {1.1, 1.1, 1.2};
		Double [] target03 = {1.0, 1.1};
		Double [] target04 = {1.0, 1.1, 1.2, 1.3};
		Double tolerance = 1.0E-6;
		
		// Test run
		Double [] result = ArrUtil.trim( test01, cutOff);
		
		// Validation
		assertTrue( ArrUtil.equal(result, target01, tolerance) );
		assertFalse( ArrUtil.equal(result, target02, tolerance) );
		assertFalse( ArrUtil.equal(result, target03, tolerance) );
		assertFalse( ArrUtil.equal(result, target04, tolerance) );
		
	}

	@Test
	public void testAmplitude() {
		
		// Inputs and parameters
		Double [] real = {3.0, -3.0, -3.0, 0.0, 4.0};
		Double [] imag = {4.0,  4.0, -4.0, 3.0, 0.0};
		
		// Target and parameters
		Double [] target = {5.0, 5.0, 5.0, 3.0, 4.0};
		Double tolerance = 1.0E-6;
		
		// Test run
		Double [] result = ArrUtil.amplitude(real, imag);
		
		// Validation
		assertTrue( ArrUtil.equal(result, target, tolerance) );
		
	}

}
