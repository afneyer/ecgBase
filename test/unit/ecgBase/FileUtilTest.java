package ecgBase;

import static org.junit.Assert.*;

import org.junit.Test;

public class FileUtilTest {
	
	AppLog applog = AppLog.getLogService();

	@Test
	public void testIsBinary() {
		
		byte[] testByte = new byte[1];
		byte b = 0x08;
		testByte[0] = b;
	
		assertTrue(FileUtil.isBinary(testByte).booleanValue());
		
		String testStr = new String("Testing Bytes");
		testByte = testStr.getBytes();
		
		assertFalse(FileUtil.isBinary(testByte).booleanValue());

	}

}
