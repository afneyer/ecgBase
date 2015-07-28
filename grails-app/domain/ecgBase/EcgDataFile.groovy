package ecgBase

class EcgDataFile {

	String identifier
	String fileName
	Date uploadDate = new Date()
	byte[] fileData
	EcgDAO ecgDAO = null
	String dataFormat
	
	static formatHL7 = "HL7"
	static formatOpenBCI = "OpenBCI"
	static formatDICOM = "DICOM"
	static formatAEcg = "aEcg"
	static formatBinary = "UnknownBinary"
	static formatText = "UnknownText"
	
	static dataFormats = [ formatHL7, formatOpenBCI, formatDICOM, formatAEcg, formatBinary,formatText]
	static AppLog applog = AppLog.getLogService()
	
	static constraints = {
		identifier(nullable:true)
		fileName(blank:false,nullable:false)
		fileData maxSize: 1024 * 1024 * 10 			// 10 MB Limit
		dataFormat(nullable:true, inList:dataFormats)
	}


	/*
	static mapping = {
		columns { fileData type:'longblob' }
	} */

	static transients = ['ecgDAO']
	
	EcgDataFile() {
	}
	
	EcgDataFile ( File file ) {
		EcgDataFile ecgDat = new EcgDataFile();
		ecgDat.fileName = file.getName()
		ecgDat.fileData = file.getBytes()
		ecgDat.identifier = EcgDataFile.count()
		ecgDat.uploadDate = new Date()
		ecgDat.dataFormat = determineDataFormat(ecgDat.fileData)
		applog.log "Dataformat = " + ecgDat.dataFormat
		ecgDat.save()
	}
	
	EcgDAO initDAO () {
		if (ecgDAO == null) {
			ecgDAO = new EcgDAO(id)
		}
		return ecgDAO
	}
	
	static String determineDataFormat( byte[] inBytes) {
		
		int bSize = inBytes.size()
		if (bSize > 500) bSize=500;
		byte[] beginning = inBytes[0..bSize-1]
		
		String dataFormat = null;
		if ( EcgUtil.isHL7(inBytes) ) {
			return formatHL7
		}
		if ( EcgUtil.isOpenBCI(beginning) ) {
			return formatOpenBCI
	    }
		if ( FileUtil.isBinary(beginning) ) {
			return formatBinary
		} else {
			return formatText
		}	
	} 
	
	EcgFileParser getDataFileParser() {
		
		switch(dataFormat) {
			case formatHL7:  return new HL7FileParser( this )
		}
		
	}
	
	boolean parseDataFile() {
		
		EcgFileParser parser = getDataFileParser()
		
		
	}
	
	String getFileDataStr32() {
		String str = new String(fileData)
		str = str.substring(0,Math.min(31,str.length()))
		return str
	}
		
	String getTimeAbsCode() {
		def String retValue
		if (ecgDAO != null) {
			retValue = ecgDAO.timeAbsCode
		} else {
			retValue = "ECG Data Object is not initialized"
		}
		return retValue
	}
	
}
