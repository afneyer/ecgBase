package ecgBase

import java.io.File;
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
				def ecgDataInstance = uploadSampleFile(file)
			}
	}
	
	static EcgData uploadSampleFile( String inFileName ) {
		
		def filePath = Holders.config.uploadFolder 
		def file = new File(filePath,inFileName)
		def ecgDataInstance = uploadSampleFile(file)		
		return ecgDataInstance
		
	}
	
	static EcgData uploadSampleFile( File inFile ) {

		applog.log "Uploading " + inFile.getName()
		def ecgDataInstance = new EcgData( inFile )
		return ecgDataInstance
	
	}
	
	/*
	 * Returns the xml-type of the byte array, null if it cannot identify it
	 */
	static boolean isHL7( byte[] inBytes ) {

		def byte[] fileData = inBytes
		def xmlDataStr = new String(fileData)

		try {
			
			def result = new XmlSlurper(false,false,false).parseText(xmlDataStr)
			// TODO remove 
			// applog.log( result.text() )

			def xmlns = result.@xmlns
			// TODO remove
			applog.log 'xmlns = ' + xmlns

			if (xmlns == 'urn:hl7-org:v3') return true;
		
		} catch (Exception e) {
			return false
		}


		return false
	}
	
	static boolean isOpenBCI( byte[] inBytes ) {
		 
		String firstLine = '%OpenBCI Raw EEG Data'
		int flength = firstLine.length()
		if (inBytes.size() < flength) return false;
		
		byte[] inFlBytes = inBytes[0..flength-1]
		String inFirstLine = new String( inFlBytes )
		return firstLine == inFirstLine
		
	}
}
			
			
	