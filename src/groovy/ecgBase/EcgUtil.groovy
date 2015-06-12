package ecgBase

import java.text.SimpleDateFormat;

class EcgUtil {

	public EcgUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static createEcgGraphArray ( startTime, timeIncrement, valueScale, valueString ) {
		
		def Object[][] ecgArray
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss.sss")
		def Date start = formatter.parse(startTime)
		
		def Double timeInc = timeIncrement
		def Double scale = valueScale
		
		def ecgValues = valueString.split(' ')
		
		def Date time = start
		def Double value = 0.0
		for(int i = 0; i < ecgValues.length; i++) {
			def String strValue = ecgValues[i]
			println 'value = ' + strValue
			value = new Double(ecgValues[i])
			value = value*scale
			ecgArray[i][0] = time
			ecgArray[i][1] = value
			time.setSeconds(time.getSeconds() + timeIncrement)
		}
		
		return ecgArray
	}
}
			
			
	