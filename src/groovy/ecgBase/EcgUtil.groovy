package ecgBase

import java.sql.Timestamp
import java.text.SimpleDateFormat;

class EcgUtil {

	public EcgUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static createEcgGraphArray ( startTime, timeIncrement, valueScale, valueString ) {
		
		def ecgArray = []
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss.sss")
		def Date start = formatter.parse(startTime)
		
		def Double timeInc = timeIncrement
		def Double scale = valueScale
		
		println 'date = ' + start
		println 'timeInc = ' + timeInc
		println 'scale = ' + valueScale
		// println 'valueString = ' + valueString
		
		def ecgValues = valueString.tokenize()
		
		def Timestamp time = new Timestamp(start.getTime()*1000000)
		def Double relTime = 0;
		def Double value = 0.0
		
		ecgValues.eachWithIndex { strValue, index ->
			// println 'index = '+ index + '     value = ' + strValue
			value = new Double(ecgValues[index])
			value = value*scale
			def row = []
			row.add(relTime)
			row.add(value)
			ecgArray.add(row)
			relTime += timeInc
			time.setNanos( (time.getNanos()+timeInc*1000000).intValue() )
		}
		
		return ecgArray
	}
}
			
			
	