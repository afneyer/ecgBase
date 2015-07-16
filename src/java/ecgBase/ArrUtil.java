package ecgBase;

import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;

import jline.internal.Log;

public class ArrUtil {

	public static Double[] trim(Double inArray[], int inNewSize) {
		Double[] tArr = new Double[inNewSize];
		for (int i = 0; i < inNewSize; i++) {
			tArr[i] = inArray[i];
		}
		return tArr;
	}
	
	public static Integer[] trim(Integer inArray[], int inNewSize) {
		Integer[] tArr = new Integer[inNewSize];
		for (int i = 0; i < inNewSize; i++) {
			tArr[i] = inArray[i];
		}
		return tArr;
	}

	public static Double[] amplitude(Double[] inReal, Double[] inImag) {

		int size = inReal.length;
		Double[] amp = new Double[size];
		for (int i = 0; i < size; i++) {
			amp[i] = Math.sqrt(inReal[i] * inReal[i] + inImag[i] * inImag[i]);
		}
		return amp;

	}

	public static Boolean equal(Double[] inArray1, Double[] inArray2,
			Double tolerance) {

		if (inArray1.length != inArray2.length) {
			return false;
		}

		for (int i = 0; i < inArray1.length; i++) {
			if (Math.abs(inArray1[i] - inArray2[i]) > tolerance) {
				return false;
			}

		}
		return true;
	}
	
	public static Boolean equal(Integer[] inArray1, Integer[] inArray2) {

		if (inArray1.length != inArray2.length) {
			return false;
		}

		for (int i = 0; i < inArray1.length; i++) {
			if (inArray1[i] != inArray2[i]) {
				return false;
			}

		}
		return true;
	}

	public static Double[] add(Double[] inArr1, Double[] inArr2) {

		int size = inArr1.length;
		Double[] sum = new Double[size];
		for (int i = 0; i < size; i++) {
			sum[i] = inArr1[i] + inArr2[i];
		}
		return sum;

	}

	public static Double[] constant(Double inConstant, Integer inSize) {

		Double[] constArr = new Double[inSize];
		for (int i = 0; i < inSize; i++) {
			constArr[i] = inConstant;
		}
		return constArr;

	}

	public static Double[] sequence(Double inInterval, Integer inSeqLength) {
		
		Double[] seq = new Double[inSeqLength];
		for (int i = 0; i < inSeqLength; i++) {
			seq[i] = i * inInterval;
		}
		return seq;
		
	}
	
	public static Double[] slope( Double[] inArray ) {
		
		int arrSize = inArray.length;
		Double[] slope = new Double[arrSize];
		// set the slope at 0 to the same value as the slope at 1 to avoid a discontinuity
		slope[0] = inArray[1]-inArray[0];	
		for (int i = 0; i < arrSize-1; i++) {
			slope[i+1] = inArray[i+1]-inArray[i];
		}
		
		return slope;
	}
	
	public static Double[] absSlope( Double[] inArray ) {
		
		int arrSize = inArray.length;
		Double[] slope = new Double[arrSize];
		// set the slope at 0 to the same value as the slope at 1 to avoid a discontinuity
		slope[0] = Math.abs(inArray[1]-inArray[0]);	
		for (int i = 0; i < arrSize-1; i++) {
			slope[i+1] = Math.abs(inArray[i+1]-inArray[i]);
		}
		
		return slope;
	}
	
	public static Double rangeAverage( Double[] inArray, Integer inFromIndex, Integer inToIndex ) {
		
		Double average = 0.0;
		Integer size = inArray.length;
		
		if (inFromIndex < 0 || inFromIndex > size) {
			Log.error("ArrUtil.rangeAverage: fromIndex = " + inFromIndex + " is not within array bounds [0.." + inArray.length + "]");
			return null;
		}
		if (inToIndex < 0 || inToIndex > size) {
			Log.error("ArrUtil.rangeAverage: toIndex = " + inToIndex + " is not within array bounds [0.." + inArray.length + "]");
			return null;
		}
		
		if (inFromIndex > inToIndex) {
			Log.error("ArrUtil.rangeAverage: fromIndex = " + inFromIndex + " is larger than toIndex = " + inToIndex);
			return null;
		}
		
		for (int i = inFromIndex; i <=inToIndex; i++) {
			average += inArray[i];
		}
				
	    return average/(inToIndex - inFromIndex + 1);	
	}

	public static double[][] toPrimitive(Double[][] inYData) {
		
		int rows = inYData.length;
		int cols = inYData[0].length;
		double[][] returnVal = new double[rows][cols];
		
		for (int i=0; i<rows; i++) {
			returnVal[i] = ArrayUtils.toPrimitive(inYData[i]);
		}

		return returnVal;
	}
	
	public static Double[] smooth( Double[] inArray ) {
		Double[] sm = new Double[inArray.length ]; 
		
		// TODO write
		
		return sm;
		
	}

	public static Double[] getCopy(Double[] inArray) {
		
		int arrSize = inArray.length;
		Double[] cp = new Double[arrSize];
		for (int i = 0; i < arrSize; i++) {
			cp[i] = inArray[i];
		}
		return cp;
	}

	public static Double[] scale(Double[] inArray, Double scaleFactor) {
		
		int arrSize = inArray.length;
		Double[] sc = new Double[arrSize];
		for (int i = 0; i < arrSize; i++) {
			sc[i] = inArray[i]*scaleFactor;
		}
		return sc;
		
	}
	/*
	 * returns an array of peaks that are above or below a certain threshold
	 * 
	 * the threshold is given in % of the number of values that exceed the threshold if positive 
	 * or are below the threshold if negative 
	 */
	public static Integer[] peaks(Double[] inArray, Double inThreshold ) {
		
		Double scale;
		if (inThreshold >= 0.0) {
			scale = 1.0;
		} else {
			scale = -1.0;
		}
		Double [] locArray = ArrUtil.scale(inArray,scale);
		
		int arrSize = locArray.length;
		Integer[] peaks = new Integer[arrSize];
		
		// sort the array
		Double[] sortedArr = null;
		if (inThreshold >= 0.0) {
			sortedArr = ArrUtil.getCopy(locArray);
		} else {
			sortedArr = ArrUtil.scale(locArray, -1.0);
		}
		Arrays.sort(sortedArr);
		
		int index = (int) (Math.abs(inThreshold) * arrSize);
		Double cutoff = sortedArr[index];
		
		// determine the peaks above the cutoff
		int peakCount = 0;
		Double peakValue = 0.0;
		int peakIndex = 0;
		boolean aboveCutoff = false;
		for (int i = 0; i < arrSize; i++) {

			if (locArray[i] < cutoff) {
				// here we are falling below the cut-off and we record the peak
				if (aboveCutoff == true) {
					peaks[peakCount] = peakIndex;
					peakCount++;
					peakValue = 0.0; 
					aboveCutoff = false;
				}
			} else { 
				aboveCutoff = true;
				if ( locArray[i] >= peakValue ) {
					peakValue = locArray[i];
					peakIndex = i;
				}
			}
		}
		if (aboveCutoff == true) {
			peaks[peakCount]= peakIndex;
			peakCount++;
		}

		peaks = ArrUtil.trim(peaks, peakCount);
		return peaks;
	}

}
