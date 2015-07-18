package ecgBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FftTest {
	
	AppLog applog = AppLog.getLogService();
	Double tolerance = 1.0E-6;

	@Test
	public void testTransform01() {
		
		Integer numPointsPerPeriod = 100;
		Integer numPeriods = 10;
		Integer seqLength = numPointsPerPeriod*numPeriods;
		Double interval = 1.0 / numPointsPerPeriod;
		Double [] seq = genSinusSequence(numPointsPerPeriod, numPeriods);
		Double [] imag = ArrUtil.constant(0.0,seqLength);
		Double [] xDat = ArrUtil.sequence( interval, seqLength );
		
		applog.logChart("TestTransform01SinCurve","X","Y", "sin(x)", xDat, seq);
		
		Double[] amp = Fft.transform(seq, imag);
		Double[] freq = ArrUtil.trim(xDat, amp.length );
		
		applog.log("real",seq);
		applog.log("imag",imag);
	
		
		applog.logChart("TestTransform01FftTransfrom","X","Y", "FFT Transform", freq, amp);
		
		// Validate the result
		int freqSize = Math.floorDiv(seqLength,2) - 1;
		assertEquals(freqSize, amp.length);
		// frequency range
		assertEquals(xDat[0],new Double(0.0), tolerance);
		assertEquals(xDat[freqSize-1],new Double(interval*(freqSize-1)),tolerance);
		
		// Validate real array
		Double [] targetReal = ArrUtil.constant(0.0, seqLength);
		
		applog.log("actualReal",seq,"targetReal",targetReal);
		assertTrue(ArrUtil.equal(seq, targetReal, tolerance));
		
		// Validate imaginary array
		int index = numPeriods;
		Double value = 1.0 * seqLength / 2.0;
		Double [] targetImag = ArrUtil.constant(0.0, seqLength);
		targetImag[index] = -value;
		targetImag[seqLength-index] = -value;
		
		assertTrue(ArrUtil.equal(seq, targetReal, tolerance));
		
		// Validate amplitude array
		Double [] targetAmp = ArrUtil.constant(0.0, freqSize);
		Double ampValue = value;
		targetAmp[index] = ampValue;
		assertTrue(ArrUtil.equal(amp, targetAmp, tolerance));
		
	}
	
	/*
	 * Creates a basic sine sequence for testing
	 */
	public static Double[] genSinusSequence( Integer inNumPointsPerPeriod, Integer inNumPeriods ) {
		int size = inNumPointsPerPeriod*inNumPeriods;
		Double [] sin = new Double[size];
		double pi2 = 2.0 * Math.PI;
		for (int i=0; i<size; i++) {
			sin[i] = Math.sin( pi2 / inNumPointsPerPeriod * i);
			
		}
		return sin;
	}
	
	@Test
	public void testfftFilter() {
		
		Integer numDataPointsPerPeriod = 100;
		Integer numPeriods = 10;
		Integer seqLength = numDataPointsPerPeriod*numPeriods;
		Double cutOffFreq = 10.0;
		Double pi = Math.PI;
		
		Double [] seq = new Double[seqLength];
		Double [] xDat = new Double[seqLength];
		
		// fill sequence with a sin-curve of frequency 1.0
		for (int i=0; i<seqLength; i++) {
			seq[i] = Math.sin( 2*pi / numDataPointsPerPeriod * i);
			seq[i] += Math.sin( 2*pi / numDataPointsPerPeriod * cutOffFreq * i); 
			xDat[i] = new Double(i);
		}
		
		applog.logChart("b1TestOriginalCurve","X","Y", "sin(x) + sin(10x)", xDat, seq);
		
		Double[] filtered = Fft.fftFilter(seq,50.0);
		
		applog.logChart("b1TestfilteredSignal","X","Y", "Filtered Signal", xDat, filtered);
		
		// verify filtered sequence
		Double tolerance = 1.0E-100;
		Double[] targetSequence = genSinusSequence(numDataPointsPerPeriod,numPeriods);
		
		ArrUtil.equal(targetSequence, filtered, tolerance);
		
	}

}
