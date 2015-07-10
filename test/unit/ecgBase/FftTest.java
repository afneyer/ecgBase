package ecgBase;

import static org.junit.Assert.*;

import java.io.IOException;

import com.xeiam.xchart.*;
import com.xeiam.xchart.BitmapEncoder.BitmapFormat;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

public class FftTest {
	
	MyLog applog = MyLog.getLogService();

	@Test
	public void testTransform() {
		
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
		
		amp = Fft.amplitude(seq, imag);
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
