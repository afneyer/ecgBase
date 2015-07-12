package ecgBase;

import static org.junit.Assert.*;

import java.io.IOException;

import com.xeiam.xchart.*;
import com.xeiam.xchart.BitmapEncoder.BitmapFormat;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

public class FftTest {
	
	MyLog applog = MyLog.getLogService();
	Double tolerance = 1.0E-6;

	@Test
	public void testTransform01() {
		
		Integer numPointsPerPeriod = 100;
		Integer numPeriods = 10;
		Integer seqLength = numPointsPerPeriod*numPeriods;
		Double interval = 1.0 / numPointsPerPeriod;
		Double pi2 = 2*Math.PI;
		
		Double [] seq = genSinusSequence(numPointsPerPeriod, numPeriods);
		Double [] imag = ArrUtil.constant(0.0,seqLength);
		Double [] xDat = ArrUtil.sequence( interval, seqLength );
		
		MyLog.logChart("Transform01SinCurve","X","Y", "sin(x)", xDat, seq);
		
		Double[] amp = Fft.transform(seq, imag);
		Double[] freq = ArrUtil.trim(xDat, amp.length );
		
		applog.log("real",seq);
		applog.log("imag",imag);
	
		
		MyLog.logChart("fftTransfrom","X","Y", "FFT Transform", freq, amp);
		
		// Validate the result
		int freqSize = Math.floorDiv(seqLength,2) - 1;
		assertEquals(freqSize, amp.length);
		// frequency range
		assertEquals(xDat[0],new Double(0.0), tolerance);
		assertEquals(xDat[freqSize-1],new Double(interval*(freqSize-1)),tolerance);
		
		// Validate real array
		Double [] targetReal = ArrUtil.constant(0.0, seqLength);
		int index = numPeriods;
		Double value = 1.0 * seqLength / 2.0;
		targetReal[index] = value;
		targetReal[seqLength-index] = -value;
		
		applog.log("actualReal",seq,"targetReal",targetReal);
		assertTrue(ArrUtil.equal(seq, targetReal, tolerance));
		
		// Validate imaginary array
		Double [] targetImag = new Double[seqLength];
		targetImag[index] = -value;
		targetImag[seqLength-index] = -value;
		
		assertTrue(ArrUtil.equal(seq, targetReal, tolerance));
		
		// Validate amplitude array
		// TODO : validate amplitude array
		
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
	public void testTransformAndChart() {
		
		
		
		Integer numDataPointsPerPeriod = 100;
		Integer numPeriods = 10;
		Integer seqLength = numDataPointsPerPeriod*numPeriods;
		Double cutOffFreq = 10.0;
		Double pi = Math.PI;
		
		Double [] seq = new Double[seqLength];
		Double [] imag = new Double[seqLength];
		Double [] xDat = new Double[seqLength];
		
		// fill sequence with a sin-curve of frequency 1.0
		for (int i=0; i<seqLength; i++) {
			seq[i] = Math.sin( 2*pi / numDataPointsPerPeriod * i);
			seq[i] += Math.sin( 2*pi / numDataPointsPerPeriod * cutOffFreq * i); 
			xDat[i] = new Double(i);
			imag[i] = 0.0;
		}
		
		MyLog.logChart("originalCurve","X","Y", "sin(x) + sin(10x)", xDat, seq);
		
		Double[] amp = Fft.transform(seq, imag);
		Double[] freq = ArrUtil.trim(xDat, amp.length );
		applog.log("amp", amp);		
		
		MyLog.logChart("fftTransfrom","X","Y", "FFT Transform", freq, amp);
		
		// cut off the values
		
		Integer cutOffIndex = new Double(seqLength / cutOffFreq).intValue();
		applog.log("cutOffIndex = " + cutOffIndex );
		
		for (int i=cutOffIndex; i< ( seqLength+1 - cutOffIndex.intValue() ) ; i++ ) {
			seq[i] = 0.0;
			imag[i] = 0.0;
		}
		
		amp = ArrUtil.amplitude(seq, imag);
		applog.log("cutOffAmplitude",amp);
		// int freqSize = Math.floorDiv(seqLength,2) - 1;
		// applog.log("freqSize= " + freqSize);
		// freq = ArrUtil.trim(xDat, freqSize );
		// amp = ArrUtil.trim(amp, freqSize);
		MyLog.logChart("fftTransformCutOff","X","Y", "FFT Transform Cut Off", xDat, amp);
		
		Fft.transform(imag, seq);
		
		MyLog.logChart("filteredSignal","X","Y", "Filtered Signal", xDat, seq);
		
		
	}

}
