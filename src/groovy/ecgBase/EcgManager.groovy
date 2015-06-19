package ecgBase

import groovy.util.slurpersupport.GPathResult

class EcgManager {

	def timeAbsCode = "TIME_ABSOLUTE"
	def leadCodes = [ "MDC_ECG_LEAD_I", "MDC_ECG_LEAD_II", "MDC_ECG_LEAD_III", "MDC_ECG_LEAD_AVR", "MDC_ECG_LEAD_AVL", "MDC_ECG_LEAD_AVF",
                      "MDC_ECG_LEAD_V1", "MDC_ECG_LEAD_V2", "MDC_ECG_LEAD_V3", "MDC_ECG_LEAD_V4", "MDC_ECG_LEAD_V5", "MDC_ECG_LEAD_V6" ]
	
	def leads = []
	
	def EcgData ecgDat;
	
	EcgManager(long id) {
		ecgDat = EcgData.get(id)
	}
	
	Object createLeads() {
	    
		println "---Entering Create Leads ----------------------------------------------------------------"
		leadCodes.eachWithIndex { it, i -> // `it` is the current element, while `i` is the index
			def EcgLead lead = createLead(i,it)
			leads[i] = lead
		}
		println "---Exiting CreateLeads ------------------------------------------------------------------"
	}
	
	Object createLead( leadIndex, leadCode ) {
		
		println "Entering Lead Creations for " + leadCode
		println "----------------------------------------"
		
		def byte[] fileData = ecgDat.fileData
		def xmlDataStr = new String(fileData)
		
		def result = new XmlSlurper().parseText(xmlDataStr)
		
		def seriesId = result.component.series.id.@root
		println 'seriesId = ' + seriesId
		
		def series = result.component.series
		
		def timeElement = series.component.sequenceSet.component.'*'.find{ seq->
			seq.code.@code == timeAbsCode
		}
		
		def timeInc = new Double(timeElement.value.increment.@value.toString())
		println 'timeInc ' + timeInc
		
		def seq = series.component.sequenceSet.component.'*'.find{ sequence->
			sequence.code.@code == leadCode
		}
		println 'Code = ' + seq.code.@code
		
		def Double origin = new Double(seq.value.origin.@value.toString())
		println 'origin ' + origin
		
		def Double scale = new Double(seq.value.scale.@value.toString())
		println 'scale ' + scale
		
		def sequenceData = seq.value.digits.toString()
		println 'Sequence Data = ' + sequenceData[0..80]
		// println 'Type of Sequence Data = ' + sequenceData.getClass()
		
		def leadArray = EcgUtil.createEcgGraphArray('20021122091000.000',timeInc,scale,sequenceData)
		
		// println "leadArray = " + leadArray
		
		def lead = new EcgLead( leadCode )
		lead.setOrigin(origin)
		lead.setScale(scale)
		lead.setTimeValueArray(leadArray)
		
		println "x-axis max value = " + leadArray[leadArray.size()-1]
		println"---------------------"
		println "Done Xml-Evaluations"
		
		return lead
	}
	
	Object createEcgGraphDataArray() {
		
			def leadI = 'MDC_ECG_LEAD_I'
			
			println "Entering Xml-Evaluation"
			println "-----------------------"
			
			def byte[] fileData = ecgDat.fileData
			def xmlDataStr = new String(fileData)
			
			def result = new XmlSlurper().parseText(xmlDataStr)
			// println "GpathResult = "
			// println "GpathResult Type = " + result.getClass()
			// println result.attributes()
			// println result.text()
			
			def headId = result.@xmlns
			println 'headId = ' + headId
			
			def seriesId = result.component.series.id.@root
			println 'seriesId = ' + seriesId
			
			def seq = result.'**'.find { sequence->
				sequence.code.@code == leadI
			}
			println "Seq Type = " + seq.getClass()
			
			// println 'Code System = ' + seq.code.@codeSystem
			
			def sequenceData = seq.value.digits.toString()
			// println 'Sequence Data = ' + sequenceData
			println 'Type of Sequence Data = ' + sequenceData.getClass()
			
			
			
			def ecgData = EcgUtil.createEcgGraphArray('20021122091000.000',new Double(0.002),new Double(2.5),sequenceData)
			
			
			println 'Returned graphArray' + ecgData[0..80]
			
			println"---------------------"
			println "Done Xml-Evaluations"
			
			return ecgData
	}
	
	String getGraphOptions() { 
		
		def ecgHorizontalMajorInterval = 0.2
		def ecgHorizontalMinorInterval = 0.04
		def ecgVerticalMajorInterval = 500.0 // in microVolt
		def ecgVerticalMinorInterval = 100.0 // in microVolt
		
		def pixelsPerMinorGridline = 5
		def pixelsPerMajorGridline = 25
		
		def horizontalChartBorder = 100
		def verticalChartBorder = 100
		
		def EcgLead lead = leads[0]
		def dataArray = lead.getTimeValueArray()
		
		def minTime = dataArray[0][0]
		println 'minTime = ' + minTime
		def maxTime = dataArray[dataArray.size()-1][0]
		println 'maxTime = ' + maxTime
		
        def maxVerticalValue = 2000.00 // max extend in microVolt
		
		def hMinValue = minTime
		def hMaxValue = maxTime-minTime
		
		def numHorizontalGridIntervals = (hMaxValue)/ecgHorizontalMajorInterval
		// round the number of Gridlines to a whole number and re-adjust the hMaxValue
		numHorizontalGridIntervals = Math.round(numHorizontalGridIntervals)
		hMaxValue = numHorizontalGridIntervals * ecgHorizontalMajorInterval
		
		def numHorizontalGridLines = numHorizontalGridIntervals + 1
		println 'numHorizontalGridLines = ' + numHorizontalGridLines
		
		def numHorizontalMinGridLines = 5
		def horizontalChartExtend = numHorizontalGridIntervals * numHorizontalMinGridLines * pixelsPerMinorGridline
		def horizontalChartWidth = horizontalChartExtend + 2*horizontalChartBorder
		
		
		def vMinValue = -maxVerticalValue
		def vMaxValue = maxVerticalValue
		def vExtend = 2*maxVerticalValue
		
		def numVerticalGridIntervals = (vMaxValue)/ecgVerticalMajorInterval
		// round the number of Gridlines to a whole number and re-adjust the hMaxValue
		numVerticalGridIntervals = Math.round(numVerticalGridIntervals)
		vMaxValue = numVerticalGridIntervals * ecgVerticalMajorInterval
		vMinValue = -vMaxValue
		
		def numVerticalGridLines = 2.0*numVerticalGridIntervals + 1
		
		println 'numVerticalGridLines = ' + numVerticalGridLines
		
		def numVerticalMinGridLines = 5
		def verticalChartExtend = 2.0 * numVerticalGridIntervals  * numVerticalMinGridLines * pixelsPerMinorGridline
		def verticalChartWidth = verticalChartExtend + 2*verticalChartBorder		
		
		def graphOptionsStr = """{
			hAxis : {
				title : 'Time',
		        gridlines : {
					count : $numHorizontalGridLines
				},
				minorGridlines : {
					count : $numHorizontalMinGridLines
                },
				minValue : 0,
                maxValue : $hMaxValue
			},
			vAxis : {
				title : 'Ecg Voltage',
		        gridlines : {
					count : $numVerticalGridLines
				},
				minorGridlines : {
					count : $numVerticalMinGridLines
                },
				minValue : $vMinValue,
				maxValue : $vMaxValue,
				
			},
            width : $horizontalChartWidth,
            height : $verticalChartWidth,
			
			chartArea : { 
                left: $horizontalChartBorder,
                top: $verticalChartBorder,
                width: $horizontalChartExtend,
				height: $verticalChartExtend
			}
		}"""
		
		println "Options String"
		println graphOptionsStr
		
		return graphOptionsStr
	}
	
	String getGraphDataString ( String inCode ) {
		
		def leadIndex = leadCodes.findIndexOf { it == inCode }
		def graphDataStr = leads[leadIndex].timeValueArray.toString()
		
		return graphDataStr
		
	}
	
	String getGraphDataString () {
		
		def dataArray = []
		
		// iterate over all time values
		def EcgLead lead = leads[0]
		def sizeOfLeadArray = lead.timeValueArray.size()
		
		for (def i = 0; i < sizeOfLeadArray; i++) {
			def row = []		
			row.add( lead.timeValueArray[i][0] )
			
			for (def j = 0; j < leads.size(); j++) {
				lead = leads[j]
				row.add( lead.timeValueArray[i][1] )
			}
			dataArray.add(row)
		}
		
		return dataArray.toString()
	}
		
	
	String getGraphColumnString() {
		
		def graphColumnArray = []
		def dataType = /'number'/
		
		def graphColumnElement = []
		graphColumnElement.add(dataType)
		graphColumnElement.add(EcgUtil.quote('Time'))
		graphColumnArray.add(graphColumnElement)
		
		
		leadCodes.eachWithIndex { it, i -> // `it` is the current element, while `i` is the index
			graphColumnElement = []
			graphColumnElement.add(dataType)
			graphColumnElement.add(EcgUtil.quote(it))
			graphColumnArray.add(graphColumnElement)
		}

		return graphColumnArray.toString()
		
	}
	
	String getGraphColumnString( String inLeadCode ) {
		
		def graphColumnArray = []
		def dataType = /'number'/
		
		def graphColumnElement = []
		graphColumnElement.add(dataType)
		graphColumnElement.add(EcgUtil.quote('Time'))
		graphColumnArray.add(graphColumnElement)
		

		graphColumnElement = []
		graphColumnElement.add(dataType)
		graphColumnElement.add(EcgUtil.quote(inLeadCode))
		graphColumnArray.add(graphColumnElement)
		
		return graphColumnArray.toString()
		
	}
	
	
	    

}
