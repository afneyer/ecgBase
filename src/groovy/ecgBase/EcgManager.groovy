package ecgBase

import groovy.util.slurpersupport.GPathResult

class EcgManager {

	def EcgData ecgDat;
	
	EcgManager(long id) {
		ecgDat = EcgData.get(id)
	}
	
	Object evaluatedAndLearn() {
			
			println "Entering Xml-Evaluation"
			
			def byte[] fileData = ecgDat.fileData
			def xmlDataStr = new String(fileData)
			
			def GPathResult result = new XmlSlurper().parseText(xmlDataStr)
			def GPathResult test = result;
			
			println test
			
			def sampleData = 0
			
			println "Done Xml-Evaluations"
	}

}
