package ecgBase

import grails.util.Holders;

import java.sql.Timestamp
import java.text.SimpleDateFormat;

class EcgUtil {
	
	def static applog = AppLog.getLogService()

	public EcgUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static createEcgGraphArray ( String startTime, Double timeIncrement, Double valueScale, String valueString ) {
		
		def ecgArray = []
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss.sss")
		def Date start = formatter.parse(startTime)
		
		def Double timeInc = timeIncrement
		def Double scale = valueScale
		
		applog.log 'date = ' + start
		applog.log 'timeInc = ' + timeInc
		applog.log 'scale = ' + valueScale
		applog.log 'valueString = ' + valueString[0..200]
		
		def ecgValues = valueString.tokenize()
		
		def Timestamp time = new Timestamp(start.getTime()*1000000)
		def Double relTime = 0;
		def Double value = 0.0
		
		ecgValues.eachWithIndex { strValue, index ->
			// applog.log 'index = '+ index + '     value = ' + strValue
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
	
	public static String quote(String inString) {
		String sq = /'/
		return sq + inString + sq
	}
	
	static void uploadSampleFiles() {
	
		def filePath = Holders.config.uploadFolder
	
			new File(filePath).eachFile() { file ->
				applog.log "Uploading " + file.getName()
				def ecgDataInstance = new EcgData()
				ecgDataInstance.fileName = file.getName()
				ecgDataInstance.fileData = file.getBytes()
				ecgDataInstance.identifier = EcgData.count()
				ecgDataInstance.uploadDate = new Date()
				ecgDataInstance.save()
			}
	}
}
			
			
	