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
	
	@Test
	public void testEqual() {
		
		// Inputs and parameters
		Double [] arr1 = {3.0, -3.0, -3.0, 0.0, 3.0};
		Double [] arr2 = {2.9, -3.1, -3.0, 0.1, 3.0};
		
		// Target and parameters
		Double tolerance = 0.101;
		
		// Validation
		assertTrue( ArrUtil.equal(arr1, arr2, tolerance) );
		tolerance = 0.01;
		assertFalse( ArrUtil.equal(arr1, arr2, tolerance) );
		
	}
	
	@Test
	public void testAdd() {
		
		// Inputs and parameters
		Double [] arr1 = {3.0, -3.0, -3.0, 0.0, 3.0};
		Double [] arr2 = {2.9, -3.1, -3.0, 0.1, 3.0};
		
		// Target and parameters
		Double tolerance = 1.0E-6;
		Double [] target = {5.9, -6.1, -6.0, 0.1, 6.0};
		
		Double [] sum = ArrUtil.add(arr1, arr2);
		
		// Validation
		assertTrue( ArrUtil.equal(target, sum, tolerance) );
		
	}
	
	@Test
	public void constant() {
		
		// Inputs and parameters
		Double constant = 2.99;
		
		// Target and parameters
		Double tolerance = 1.0E-6;
		Double [] target = {2.99, 2.99, 2.99};
		
		Double [] constArr = ArrUtil.constant(constant, 3);
		
		// Validation
		assertTrue( ArrUtil.equal(target, constArr, tolerance) );
		
	}
	
	@Test
	public void sequence() {
		
		// Inputs and parameters
		Double interval = 0.1;
		Integer size = 5;
		
		// Target and parameters
		Double tolerance = 1.0E-6;
		Double [] target = {0.0, 0.1, 0.2, 0.3, 0.4};
		
		Double [] result = ArrUtil.sequence(interval, size);
		
		// Validation
		assertTrue( ArrUtil.equal(target, result, tolerance) );
		
	}
	
	

}
