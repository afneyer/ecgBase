package ecgBase

import java.util.Date;

class EkgData {
	
	String identifier
	String name
	String filename
	
	
	Date uploadDate = new Date()
	
	static constraints = {
		filename(blank:false,nullable:false)
	}
	
}
