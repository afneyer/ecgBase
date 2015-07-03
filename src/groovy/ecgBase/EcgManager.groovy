package ecgBase

import ecgBase.AppLog;
import grails.util.Holders;
import groovy.util.slurpersupport.GPathResult

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.support.GenericGroovyApplicationContext

class EcgManager {

	def timeAbsCode = "TIME_ABSOLUTE"
	def leadCodes = [ "MDC_ECG_LEAD_I", "MDC_ECG_LEAD_II", "MDC_ECG_LEAD_III", "MDC_ECG_LEAD_AVR", "MDC_ECG_LEAD_AVL", "MDC_ECG_LEAD_AVF",
                      "MDC_ECG_LEAD_V1", "MDC_ECG_LEAD_V2", "MDC_ECG_LEAD_V3", "MDC_ECG_LEAD_V4", "MDC_ECG_LEAD_V5", "MDC_ECG_LEAD_V6" ]
	
	def leads = []
	
	def EcgData ecgDat
	
	def AppLog appLog = AppLog.getLogService()
	
	EcgManager(long id) {
		ecgDat = EcgData.get(id)
	}
	
	Object createLeads() {
	    
		appLog.log "---Entering Create Leads ----------------------------------------------------------------"
		leadCodes.eachWithIndex { it, i -> // `it` is the current element, while `i` is the index
			def EcgLead lead = createLead(i,it)
			leads[i] = lead
		}
		
		createSquareLead()
		
		appLog.log "---Exiting CreateLeads ------------------------------------------------------------------"
	}
	
	Object createLead( leadIndex, leadCode ) {
		
		appLog.log "Entering Lead Creations for " + leadCode
		appLog.log "----------------------------------------"
		
		def byte[] fileData = ecgDat.fileData
		def xmlDataStr = new String(fileData)
		
		def result = new XmlSlurper().parseText(xmlDataStr)
		
		def seriesId = result.component.series.id.@root
		appLog.log 'seriesId = ' + seriesId
		
		def series = result.component.series
		
		def timeElement = series.component.sequenceSet.component.'*'.find{ seq->
			seq.code.@code == timeAbsCode
		}
		
		def timeInc = new Double(timeElement.value.increment.@value.toString())
		appLog.log 'timeInc ' + timeInc
		
		def seq = series.component.sequenceSet.component.'*'.find{ sequence->
			sequence.code.@code == leadCode
		}
		appLog.log 'Code = ' + seq.code.@code
		
		def Double origin = new Double(seq.value.origin.@value.toString())
		appLog.log 'origin ' + origin
		
		def Double scale = new Double(seq.value.scale.@value.toString())
		appLog.log 'scale ' + scale
		
		def sequenceData = seq.value.digits.toString()
		appLog.log 'Sequence Data = ' + sequenceData[0..80]
		// appLog.log 'Type of Sequence Data = ' + sequenceData.getClass()
		
		def leadArray = EcgUtil.createEcgGraphArray('20021122091000.000',timeInc,scale,sequenceData)
		
		// appLog.log "leadArray = " + leadArray
		
		def lead = new EcgLead( leadCode )
		lead.setOrigin(origin)
		lead.setScale(scale)
		lead.setTimeValueArray(leadArray)
		
		appLog.log "x-axis max value = " + leadArray[leadArray.size()-1]
		appLog.log"---------------------"
		appLog.log "Done Xml-Evaluations"
		
		return lead
	}
	
	void createSquareLead() {
		
		def numLeads = leads.size()
		def EcgLead newLead = new EcgLead("Square Lead")
		
		def EcgLead firstLead = leads[0]
		def leadArraySize = firstLead.timeValueArray.size()
		
		def EcgLead lead
		
		def leadArray = []
		
		for (def i = 0; i < leadArraySize; i++) {
			
			def row = []
			
			def squareVal = 0.0
					
			row.add( firstLead.timeValueArray[i][0] )
			
			for (def j = 0; j < leads.size(); j++) {
				lead = leads[j]
				squareVal += lead.timeValueArray[i][1] * lead.timeValueArray[i][1]
			}
				
			squareVal = Math.sqrt(squareVal)
			row.add(squareVal)
			
			leadArray.add(row)
			// appLog.log "row element = " + row	
			
		}
		
	    newLead.setOrigin(0.0)
	    newLead.setScale( firstLead.getScale() )
	    newLead.setTimeValueArray(leadArray)
			
	    leads[leads.size()] = newLead
		appLog.log "new lead = " + newLead.code
	}
	
	Object createEcgGraphDataArray() {
		
			def leadI = 'MDC_ECG_LEAD_I'
			
			appLog.log "Entering Xml-Evaluation"
			appLog.log "-----------------------"
			
			def byte[] fileData = ecgDat.fileData
			def xmlDataStr = new String(fileData)
			
			def result = new XmlSlurper().parseText(xmlDataStr)
			// appLog.log "GpathResult = "
			// appLog.log "GpathResult Type = " + result.getClass()
			// appLog.log result.attributes()
			// appLog.log result.text()
			
			def headId = result.@xmlns
			appLog.log 'headId = ' + headId
			
			def seriesId = result.component.series.id.@root
			appLog.log 'seriesId = ' + seriesId
			
			def seq = result.'**'.find { sequence->
				sequence.code.@code == leadI
			}
			appLog.log "Seq Type = " + seq.getClass()
			
			// appLog.log 'Code System = ' + seq.code.@codeSystem
			
			def sequenceData = seq.value.digits.toString()
			// appLog.log 'Sequence Data = ' + sequenceData
			appLog.log 'Type of Sequence Data = ' + sequenceData.getClass()
			
			
			
			def ecgData = EcgUtil.createEcgGraphArray('20021122091000.000',new Double(0.002),new Double(2.5),sequenceData)
			
			
			appLog.log 'Returned graphArray' + ecgData[0..80]
			
			appLog.log"---------------------"
			appLog.log "Done Xml-Evaluations"
			
			return ecgData
	}
	
	String getGraphOptions() { 
		
		def ecgHorizontalMajorInterval = 0.2
		def ecgHorizontalMinorInterval = 0.04
		def ecgVerticalMajorInterval = 500.0 // in microVolt
		def ecgVerticalMinorInterval = 100.0 // in microVolt
		
		def pixelsPerMinorGridline = 5
		def pixelsPerMajorGridline = 25
		
		def horizontalChartLeftBorder = 50
		def horizontalChartRightBorder = 200
		def verticalChartTopBorder = 100
		def verticalChartBottomBorder = 100
		
		def EcgLead lead = leads[0]
		def dataArray = lead.getTimeValueArray()
		
		def minTime = dataArray[0][0]
		appLog.log 'minTime = ' + minTime
		def maxTime = dataArray[dataArray.size()-1][0]
		appLog.log 'maxTime = ' + maxTime
		
        def maxVerticalValue = 1500.00 // max extend in microVolt
		
		def hMinValue = minTime
		def hMaxValue = maxTime-minTime
		
		def numHorizontalGridIntervals = (hMaxValue)/ecgHorizontalMajorInterval
		// round the number of Gridlines to a whole number and re-adjust the hMaxValue
		numHorizontalGridIntervals = Math.round(numHorizontalGridIntervals)
		hMaxValue = numHorizontalGridIntervals * ecgHorizontalMajorInterval
		
		def numHorizontalGridLines = numHorizontalGridIntervals + 1
		appLog.log 'numHorizontalGridLines = ' + numHorizontalGridLines
		
		def numHorizontalMinGridLines = 5
		def horizontalChartExtend = numHorizontalGridIntervals * numHorizontalMinGridLines * pixelsPerMinorGridline
		def horizontalChartWidth = horizontalChartExtend + horizontalChartLeftBorder + horizontalChartRightBorder
		
		def numLeads = leads.size()
		def vExtend = numLeads*maxVerticalValue*2
		def vMinValue = -maxVerticalValue
		def vMaxValue = vMinValue + vExtend
		
		
		def numVerticalGridIntervals = (vExtend)/ecgVerticalMajorInterval
		// round the number of Gridlines to a whole number and re-adjust the hMaxValue
		numVerticalGridIntervals = Math.round(numVerticalGridIntervals)
		vExtend = numVerticalGridIntervals * ecgVerticalMajorInterval
		vMinValue = - vExtend / (numLeads*2)
		vMaxValue = vMinValue + vExtend
		
		
		def numVerticalGridLines = numVerticalGridIntervals + 1
		
		appLog.log 'numVerticalGridLines = ' + numVerticalGridLines
		
		def numVerticalMinGridLines = 5
		
		def verticalChartExtend = numVerticalGridIntervals  * numVerticalMinGridLines * pixelsPerMinorGridline
		def verticalChartHeight = verticalChartExtend + verticalChartTopBorder + verticalChartBottomBorder	
		
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
				textPosition : 'none',
			},
            width : $horizontalChartWidth,
            height : $verticalChartHeight,
			
			chartArea : { 
                left: $horizontalChartLeftBorder,
                top: $verticalChartTopBorder,
                width: $horizontalChartExtend,
				height: $verticalChartExtend
			}
		}"""
		
		appLog.log "Options String"
		appLog.log graphOptionsStr
		
		return graphOptionsStr
	}
	
	Double getGraphOffset() {
		return new Double(3000.00)
	}
	
	String getGraphDataString ( String inCode ) {
		
		def leadIndex = leadCodes.findIndexOf { it == inCode }
		def graphDataStr = leads[leadIndex].timeValueArray.toString()
		
		return graphDataStr
		
	}
	
	String getFftGraphDataString( String inCode ) {
		
		def leadIndex = leadCodes.findIndexOf { it == inCode }
		def EcgLead curLead = leads[leadIndex]
		def timeValArray = curLead.timeValueArray 
		def arrSize = timeValArray.size()
		
		// extract value array
		def Double[] valArray = new Double[arrSize]
		def Double[] imagArray = new Double[arrSize]
		for (def i=0; i<timeValArray.size(); i++) {
			valArray[i] = timeValArray[i][1]
			imagArray[i] = 0.0
		}
		
		Fft.transform(valArray, imagArray)
		
		// def deltaf = 2 * Math.PI / ( curLead.getNumSamples() * curLead.getSampleInterval() )
		def deltaf = 1.0 / ( curLead.getNumSamples() * curLead.getSampleInterval() )
		
		appLog.log "numSamples = " + curLead.getNumSamples()
		appLog.log "sampleIntervale = " + curLead.getSampleInterval()
		appLog.log "deltaf = " + deltaf
		
		def freq = deltaf
		def freqGraphSize = timeValArray.size() / 2 - 1
		def freqArray = []
		
		appLog.log "TimeValueArray for FFT"
		for (def i=0; i<freqGraphSize; i++) {
			
			def squareValue = new Double( Math.sqrt( valArray[i]**2 + imagArray[i]**2 ) )
		    def row = [freq,squareValue]
			freqArray.add(row)
			
			freq += deltaf
			appLog.log " Index i=" + i + " [ " + freq + " , " + squareValue + " ]"
		}
	
		def graphDataStr = freqArray.toString()
		
		return graphDataStr
		
	}
	
	String getFftGraphOptions() {
		
		def pixelsPerMinorGridline = 5
		def pixelsPerMajorGridline = 25
		
		def horizontalChartLeftBorder = 200
		def horizontalChartRightBorder = 200
		def verticalChartTopBorder = 100
		def verticalChartBottomBorder = 100
		
		
		def hMinValue = 0.0
		def hMaxValue = 60.0
		
		def numHorizontalGridLines = 12
		appLog.log 'numHorizontalGridLines = ' + numHorizontalGridLines
		
		def numHorizontalMinGridLines = 10
		def horizontalChartExtend = 1000
		def horizontalChartWidth = horizontalChartExtend + horizontalChartLeftBorder + horizontalChartRightBorder
		
		def numVerticalMinGridLines = 10
		
		def verticalChartExtend = 500
		def verticalChartHeight = verticalChartExtend + verticalChartTopBorder + verticalChartBottomBorder
		
		def graphOptionsStr = """{
			hAxis : {
				title : 'Frequency',
		        gridlines : {
					count : $numHorizontalGridLines
				},
				minorGridlines : {
					count : $numHorizontalMinGridLines
                },
				viewWindow : {
					min : 0,
					max : $hMaxValue,
				},
			},
			vAxis : {
				title : 'Amplitude',
		        gridlines : {
				},
				minorGridlines : {
					count : $numVerticalMinGridLines
                },
			},
            width : $horizontalChartWidth,
            height : $verticalChartHeight,
			
			chartArea : { 
                left: $horizontalChartLeftBorder,
                top: $verticalChartTopBorder,
                width: $horizontalChartExtend,
				height: $verticalChartExtend
			}
		}"""
		
		appLog.log "Options String"
		appLog.log graphOptionsStr
		
		return graphOptionsStr
	}
	
	String getGraphDataString () {
		
		def dataArray = []
		
		// iterate over all time values
		def EcgLead lead = leads[0]
		def sizeOfLeadArray = lead.timeValueArray.size()
		
		def verticalOffset = getGraphOffset()
		
		for (def i = 0; i < sizeOfLeadArray; i++) {
			def row = []		
			row.add( lead.timeValueArray[i][0] )
			
			for (def j = 0; j < leads.size(); j++) {
				lead = leads[j]
				// appLog.log " j = " + j + " i = " + i
				row.add( lead.timeValueArray[i][1] + (leads.size-1-j)*verticalOffset )
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
		
		
		def numLeads = leads.size()
		for ( def i = 0; i < numLeads; i++ ) {
			def EcgLead lead = leads[i]
			graphColumnElement = []
			graphColumnElement.add(dataType)
			graphColumnElement.add(EcgUtil.quote( lead.getCode() ))
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
