package ecgBase;

public class ArrUtil {
	
	public static Double [] trim( Double inArray[], int inNewSize ) {
		Double[] tArr = new Double[inNewSize];
		for (int i=0; i<inNewSize; i++) {
			tArr[i]=inArray[i];
		}
		return tArr;
	}
	
	public static Double[] amplitude( Double[] inReal, Double[] inImag ) {
		
		int size = inReal.length;
		Double [] amp = new Double[size];
		for (int i=0; i<size; i++) {
				amp[i] = Math.sqrt( inReal[i]*inReal[i] + inImag[i]*inImag[i]);
			}
		return amp;
		
	}
	
	public static Boolean equal( Double[] inArray1, Double[] inArray2, Double tolerance ) {
		
		if (inArray1.length != inArray2.length ) {
			return false;
		}
		
		for (int i=0; i<inArray1.length; i++) {
			if ( Math.abs( inArray1[i] - inArray2[i]) > tolerance ) {
				return false;
			}
			
		}
		return true;
	}

}
