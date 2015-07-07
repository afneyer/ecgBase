package ecgBase;

import static org.junit.Assert.*;

import org.junit.Test;

public class StatTest {

	@Test
	public void testMean() {
		
		Double[] test01 = { 1.0, 1.1, 1.2, 1.1, 1.0 };
		Double mean = Stat.mean(test01);
		
		assertEquals(mean, 1.08, 0.0);
		
	}
	
	@Test
	public void testSqr() {
		Double x = 2.0;
		Double y = Stat.sqr(x);
		assertEquals(y,4.0,0.0);
	}
	
	@Test
	public void testVariance() {
		Double[] test01 = {1.0, 1.0, 1.0, 1.0};
		Double var = Stat.variance(test01);
		assertEquals(var, 0.0, 0.0);
		
		Double[] test02 = {1.0, 1.1, 1.2, 1.1};
		var = Stat.variance(test02);
		assertEquals(var, 0.006667, 0.000001);
	}
	
	@Test
	public void testStdDev() {
		Double[] test = {1.0, 1.1, 1.2, 1.1};
		Double stdDev = Stat.StdDev(test);
		assertEquals(stdDev, 0.08165, 0.000001);
		
	}
	
	@Test
	public void testLargeStdDev() {
		Integer size = 1000000;
		Double[] test = new Double[size];
		for (int i=0; i<size; i++) {
			test[i] = Math.random();
		}
		Double stdDev = Stat.StdDev(test);
	}
	
	@Test
	public void testSqrt() {
		Double x = 4.0;
		Double y = Stat.sqrt(x);
		assertEquals(y,2.0,0.0);
	}

}
