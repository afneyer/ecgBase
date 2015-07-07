package ecgBase;

/*
 * Statistics Implements Basic Statistics Functions
 *  
 */

public class Stat {
	
	public static Double mean( Double[] inVector ) {
		
		Double mean = new Double(0.0);
		for (Integer i=0; i<inVector.length; i++) {
			mean += inVector[i];
		}
		return mean/inVector.length;
		
	}
	
	public static Double variance( Double[] inVector ) {
		Double variance = new Double(0.0);
		Double mean = mean(inVector);
		for (Integer i=0; i<inVector.length; i++) {
			variance += sqr(mean - inVector[i]);
		}
		return variance/(inVector.length-1);
	}

	public static Double sqr ( Double inX ) {
		return inX*inX;
	}
	
	public static Double sqrt( Double inX ) {
		return Math.sqrt( inX );
	}

	public static Double StdDev(Double[] inVector) {
		return Stat.sqrt( variance(inVector) );
	}

}
