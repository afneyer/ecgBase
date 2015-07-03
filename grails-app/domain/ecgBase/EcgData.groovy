package ecgBase




class EcgData {

	String identifier
	String fileName
	Date uploadDate = new Date()
	byte[] fileData

	String getFileDataStr32() {
		String str = new String(fileData)
		str = str.substring(0,Math.min(31,str.length()))
		return str
	}
	
	static constraints = {
		identifier(nullable:true)
		fileName(blank:false,nullable:false)
		fileData maxSize: 1024 * 1024 * 10 			// 10 MB Limit
	}

	/*
	static mapping = {
		columns { fileData type:'longblob' }
	} */

	static transients = ['fileDataStr32']

	
}
