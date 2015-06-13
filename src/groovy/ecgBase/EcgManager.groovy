package ecgBase

import groovy.util.slurpersupport.GPathResult

class EcgManager {

	def EcgData ecgDat;
	
	EcgManager(long id) {
		ecgDat = EcgData.get(id)
	}
	
	Object evaluatedAndLearn() {
		
			def leadI = 'MDC_ECG_LEAD_I'
			
			println "Entering Xml-Evaluation"
			println "-----------------------"
			
			def byte[] fileData = ecgDat.fileData
			def xmlDataStr = new String(fileData)
			
			def GPathResult result = new XmlSlurper().parseText(xmlDataStr)
			
			def seq = result.'**'.find { sequence->
				sequence.code.@code == leadI
			}
			
			println 'Code System = ' + seq.code.@codeSystem
			
			def sequenceData = seq.value.digits.toString()
			println 'Sequence Data = ' + sequenceData
			println 'Type of Sequence Data = ' + sequenceData.getClass()
			
			def ecgData = EcgUtil.createEcgGraphArray('20021122091000.000',0.002,2.5,sequenceData)
			
			
			println ecgData
			
			println"---------------------"
			println "Done Xml-Evaluations"
			
			return ecgData
	}
	
	String getGraphOptions() { 
		
		def numGridLines = 20
		def numMinGridLines = 5
		
		def graphOptionsStr = """{
			hAxis : {
				title : 'Time',
		        gridlines : {
					count : $numGridLines
				},
				minorGridlines : {
					count : $numMinGridLines
                },
			},
			vAxis : {
				title : 'Ecg Voltage'
			}
		}"""
		
		return graphOptionsStr
	}

}
