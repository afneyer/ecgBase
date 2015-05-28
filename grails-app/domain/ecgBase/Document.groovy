package ecgBase

class Document {

	String fileName
	String fullPath
	Date uploadDate = new Date()
	byte[] fileData

	static constraints = {
		fileName(blank:false,nullable:false)
		fullPath(blank:false,nullable:false)
		fileData maxSize: 1024 * 1024 * 10 // 2MB
	}

	static mapping = {
		columns {
			orgFile type:'longblob'
		}
	}

}
