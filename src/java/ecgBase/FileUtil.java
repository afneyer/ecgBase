package ecgBase;

public class FileUtil {
	
	public static Boolean isBinary(byte[] inByte) {
		
	    int testSize = 1024;
	    if (inByte.length < testSize) testSize = inByte.length;

	    int other = 0;
	    int ascii = 0;

	    for(int i = 0; i < testSize; i++) {
	        byte b = inByte[i];
	        if( b < 0x09 ) return true;

	        if( b == 0x09 || b == 0x0A || b == 0x0C || b == 0x0D ) ascii++;
	        else if( b >= 0x20  &&  b <= 0x7E ) ascii++;
	        else other++;
	    }

	    if( other == 0 ) return false;

	    return 100 * other / testSize > 95;
		
	}

}
