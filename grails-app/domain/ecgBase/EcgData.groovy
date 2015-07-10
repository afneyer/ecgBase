package ecgBase

class EcgData {

	String identifier
	String fileName
	Date uploadDate = new Date()
	byte[] fileData
	EcgManager ecgDAO = null
	
	static MyLog applog = MyLog.getLogService()
	
	static constraints = {
		identifier(nullable:true)
		fileName(blank:false,nullable:false)
		fileData maxSize: 1024 * 1024 * 10 			// 10 MB Limit
	}

	/*
	static mapping = {
		columns { fileData type:'longblob' }
	} */

	static transients = ['ecgDAO']
	
	EcgManager initDAO () {
		if (ecgDAO == null) {
			ecgDAO = new EcgManager(id)
		}
		return ecgDAO
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
