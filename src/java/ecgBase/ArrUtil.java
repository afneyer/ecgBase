package ecgBase;

import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang.ArrayUtils;

import jline.internal.Log;

public class ArrUtil {
	
	public static AppLog applog = AppLog.getLogService();

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

	public static Boolean equal(Double[][] inArray1, Double[][] inArray2,
			Double tolerance) {

		if (inArray1.length != inArray2.length) {
			return false;
		}

		if (inArray1[0].length != inArray2[0].length) {
			return false;
		}

		for (int i = 0; i < inArray1.length; i++) {
			for (int j = 0; j < inArray1[0].length; j++) {
				if (Math.abs(inArray1[i][j] - inArray2[i][j]) > tolerance) {
					return false;
				}
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

	public static Boolean equal(Integer[][] inArray1, Integer[][] inArray2) {

		if (inArray1.length != inArray2.length) {
			return false;
		}

		if (inArray1[0].length != inArray2[0].length) {
			return false;
		}

		for (int i = 0; i < inArray1.length; i++) {
			for (int j = 0; j < inArray1[0].length; j++) {
				if (inArray1[i][j] != inArray2[i][j]) {
					return false;
				}
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
	
	public static Integer[] constant(Integer inConstant, Integer inSize) {

		Integer[] constArr = new Integer[inSize];
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

	public static Double[] slope(Double[] inArray) {

		int arrSize = inArray.length;
		Double[] slope = new Double[arrSize];
		// set the slope at 0 to the same value as the slope at 1 to avoid a
		// discontinuity
		slope[0] = inArray[1] - inArray[0];
		for (int i = 0; i < arrSize - 1; i++) {
			slope[i + 1] = inArray[i + 1] - inArray[i];
		}

		return slope;
	}

	public static Double[] absSlope(Double[] inArray) {

		int arrSize = inArray.length;
		Double[] slope = new Double[arrSize];
		// set the slope at 0 to the same value as the slope at 1 to avoid a
		// discontinuity
		slope[0] = Math.abs(inArray[1] - inArray[0]);
		for (int i = 0; i < arrSize - 1; i++) {
			slope[i + 1] = Math.abs(inArray[i + 1] - inArray[i]);
		}

		return slope;
	}

	public static Double rangeAverage(Double[] inArray, Integer inFromIndex,
			Integer inToIndex) {

		Double average = 0.0;
		Integer size = inArray.length;

		if (inFromIndex < 0 || inFromIndex > size) {
			Log.error("ArrUtil.rangeAverage: fromIndex = " + inFromIndex
					+ " is not within array bounds [0.." + inArray.length + "]");
			return null;
		}
		if (inToIndex < 0 || inToIndex > size) {
			Log.error("ArrUtil.rangeAverage: toIndex = " + inToIndex
					+ " is not within array bounds [0.." + inArray.length + "]");
			return null;
		}

		if (inFromIndex > inToIndex) {
			Log.error("ArrUtil.rangeAverage: fromIndex = " + inFromIndex
					+ " is larger than toIndex = " + inToIndex);
			return null;
		}

		for (int i = inFromIndex; i <= inToIndex; i++) {
			average += inArray[i];
		}

		return average / (inToIndex - inFromIndex + 1);
	}

	public static double[][] toPrimitive(Double[][] inYData) {

		int rows = inYData.length;
		int cols = inYData[0].length;
		double[][] returnVal = new double[rows][cols];

		for (int i = 0; i < rows; i++) {
			returnVal[i] = ArrayUtils.toPrimitive(inYData[i]);
		}

		return returnVal;
	}

	public static Double[] smooth(Double[] inArray) {
		Double[] sm = new Double[inArray.length];

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
			sc[i] = inArray[i] * scaleFactor;
		}
		return sc;

	}

	public static Double max(Double[] inArray) {
		return (Double) Collections.max(Arrays.asList(inArray));
	}

	public static Double min(Double[] inArray) {
		return (Double) Collections.min(Arrays.asList(inArray));
	}

	/*
	 * returns an array of peaks that are above or below a certain threshold
	 * 
	 * the threshold is given in % of the number of values that exceed the
	 * threshold if positive or are below the threshold if negative
	 */
	public static Integer[] peaks(Double[] inArray, Double inThreshold) {
		return peakIntervals(inArray, inThreshold)[2];
	}

	/*
	 * 
	 */
	public static Integer[][] peakIntervals(Double[] inArray, Double inThreshold) {

		Double scale;
		if (inThreshold >= 0.0) {
			scale = 1.0;
		} else {
			scale = -1.0;
		}
		Double[] locArray = ArrUtil.scale(inArray, scale);

		int arrSize = locArray.length;
		Integer[] peaks = new Integer[arrSize];
		Integer[] start = new Integer[arrSize];
		Integer[] end = new Integer[arrSize];

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
					end[peakCount] = i - 1;
					peaks[peakCount] = peakIndex;
					peakCount++;
					peakValue = 0.0;
					aboveCutoff = false;
				}
			} else {
				if (!aboveCutoff) {
					start[peakCount] = i;
				}
				aboveCutoff = true;
				if (locArray[i] >= peakValue) {
					peakValue = locArray[i];
					peakIndex = i;
				}
			}
		}
		if (aboveCutoff == true) {
			end[peakCount] = arrSize - 1;
			peaks[peakCount] = peakIndex;
			peakCount++;
		}

		start = ArrUtil.trim(start, peakCount);
		peaks = ArrUtil.trim(peaks, peakCount);
		end = ArrUtil.trim(end, peakCount);
		Integer[][] peakIntervals = new Integer[3][peakCount];
		peakIntervals[0] = start;
		peakIntervals[1] = peaks;
		peakIntervals[2] = end;

		return peakIntervals;
	}

	/*
	 * the size of the inOutCenter array determines the number of clusters
	 */
	public static Integer[] kMeans(Double[] inArray, Integer k,
			Double[] inOutCenters) {

		int arrSize = inArray.length;
		int numCenters = 0;

		// TODO : check arraySize
		Double[] initial = new Double[arrSize];

		// determine tolerance
		Double max = ArrUtil.max(inArray);
		Double min = ArrUtil.min(inArray);
		Double delta = (max - min) / (k - 1);
		Double tolerance = 1.0E-6 * delta;

		// determine initial centers
		if (inOutCenters == null) {
			inOutCenters = new Double[k];
			Double kVal = min;
			for (int i = 0; i < k; i++) {
				inOutCenters[i] = kVal;
				kVal += delta;
			}
		} else {
			if (inOutCenters.length != k) {
				Log.error("Function:  public static Integer[] kMeans(Double[] inArray, Integer k, Double[] inOutCenters called with inconsistent arguments\n"
						+ "   k = "
						+ k
						+ "\n"
						+ "   size(inOutCenters) = "
						+ inOutCenters.length + "\n");
				return null;
			}
		}
		numCenters = k;

		Double[] centerOld = null;
		Double[] centerNew = ArrUtil.getCopy(inOutCenters);

		Integer[] clusters = null;
		Integer[] clusterCount = null;

		int iter = 0;
		// actual k-means algorithm
		do {
            
			iter++;
			centerOld = centerNew;
			clusters = new Integer[arrSize];

			// distribute the values over the k clusters
			for (int i = 0; i < arrSize; i++) {

				// compute the minimum distance to all centers
				Double minDist = Double.MAX_VALUE;
				int index = -1;
				for (int j = 0; j < numCenters; j++) {
					double dist = Math.abs(inArray[i] - centerOld[j]);
					if (dist < minDist) {
						minDist = dist;
						index = j;
					}
				}
				// assign the index to the new cluster
				clusters[i] = index;
			}

			// compute the means of the new clusters
			clusterCount = ArrUtil.constant(0, numCenters);
			centerNew = ArrUtil.constant(0.0, numCenters);
			for (int i = 0; i < arrSize; i++) {
				centerNew[clusters[i]] += inArray[i];
				clusterCount[clusters[i]]++;
			}

			for (int j = 0; j < numCenters; j++) {
				if (clusterCount[j] != 0) {
					centerNew[j] = centerNew[j] / clusterCount[j];
				} else {
					centerNew[j] = 0.0;
				}
			}
			
			applog.log("iteration " + iter);
			applog.log("array",inArray);
			applog.log("centerOld",centerOld);
			applog.log("centerNew",centerNew);
			applog.log("clusters",clusters);

		} while (!ArrUtil.equal(centerNew, centerOld, tolerance));

		inOutCenters = centerNew;

		return clusters;
	}

}
