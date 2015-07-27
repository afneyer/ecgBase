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
	
	def Integer squareLeadIndex = 12
	def String squareLeadCode = 'Square Lead'
	
	def leads = []
	
	def EcgData ecgDat
	
	def initialized = false
	
	def AppLog applog = AppLog.getLogService()
	
	// values set by determineHeartRate()
	def rPeaks = []
	def Double[] RRIntervals
	def Double[] heartRates
	def heartRate
	def heartRateStdDev
	
	def qrsStart = []
	def qrsEnd = []
	
	EcgManager(long id) {
		ecgDat = EcgData.get(id)
		ecgDat.ecgDAO = this
		applog.log "initialized ecgDat.ecgDAO: " + ecgDat.ecgDAO.timeAbsCode
	}
	
	void initData() {
		if (!initialized) {
			createLeads()
			determineHeartRate()
		}
	}
	
	Object createLeads() {
	    
		applog.log "---Entering Create Leads ----------------------------------------------------------------"
		leadCodes.eachWithIndex { it, i -> // `it` is the current element, while `i` is the index
			def EcgLead lead = createLead(i,it)
			leads[i] = lead
		}
		
		createSquareLead()
		
		applog.log "---Exiting CreateLeads ------------------------------------------------------------------"
	}
	
	Object createLead( leadIndex, leadCode ) {
		
		applog.log "Entering Lead Creations for " + leadCode
		applog.log "----------------------------------------"
		
		def byte[] fileData = ecgDat.fileData
		def xmlDataStr = new String(fileData)
		
		def result = new XmlSlurper().parseText(xmlDataStr)
		
		def seriesId = result.component.series.id.@root
		applog.log 'seriesId = ' + seriesId
		
		def series = result.component.series
		
		def timeElement = series.component.sequenceSet.component.'*'.find{ seq->
			seq.code.@code == timeAbsCode
		}
		
		def timeInc = new Double(timeElement.value.increment.@value.toString())
		applog.log 'timeInc ' + timeInc
		
		def seq = series.component.sequenceSet.component.'*'.find{ sequence->
			sequence.code.@code == leadCode
		}
		applog.log 'Code = ' + seq.code.@code
		
		def Double origin = new Double(seq.value.origin.@value.toString())
		applog.log 'origin ' + origin
		
		def Double scale = new Double(seq.value.scale.@value.toString())
		applog.log 'scale ' + scale
		
		def sequenceData = seq.value.digits.toString()
		applog.log 'Sequence Data = ' + sequenceData[0..80]
		// appLog.log 'Type of Sequence Data = ' + sequenceData.getClass()
		
		def leadArray = EcgUtil.createEcgGraphArray('20021122091000.000',timeInc,scale,sequenceData)
		
		// appLog.log "leadArray = " + leadArray
		
		def lead = new EcgLead( leadCode )
		lead.setOrigin(origin)
		lead.setScale(scale)
		lead.setTimeValueArray(leadArray)
		
		applog.log "x-axis max value = " + leadArray[leadArray.size()-1]
		applog.log"---------------------"
		applog.log "Done Xml-Evaluations"
		
		return lead
	}
	
	void createSquareLead() {
		
		def numLeads = leads.size()
		def EcgLead newLead = new EcgLead("Square Lead")
		
		def EcgLead sqrLeadBase = leads[1]
		def leadArraySize = sqrLeadBase.timeValueArray.size()
		
		def EcgLead lead
		
		def leadArray = []
		
		for (def i = 0; i < leadArraySize; i++) {
			
			def row = []
			
			def squareVal = 0.0
					
			row.add( sqrLeadBase.timeValueArray[i][0] )
			
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
	    newLead.setScale( sqrLeadBase.getScale() )
	    newLead.setTimeValueArray(leadArray)
			
	    leads[leads.size()] = newLead
		applog.log "new lead = " + newLead.code
	}
	
	Object createEcgGraphDataArray() {
		
			def leadI = 'MDC_ECG_LEAD_I'
			
			applog.log "Entering Xml-Evaluation"
			applog.log "-----------------------"
			
			def byte[] fileData = ecgDat.fileData
			def xmlDataStr = new String(fileData)
			
			def result = new XmlSlurper().parseText(xmlDataStr)
			// appLog.log "GpathResult = "
			// appLog.log "GpathResult Type = " + result.getClass()
			// appLog.log result.attributes()
			// appLog.log result.text()
			
			def headId = result.@xmlns
			applog.log 'headId = ' + headId
			
			def seriesId = result.component.series.id.@root
			applog.log 'seriesId = ' + seriesId
			
			def seq = result.'**'.find { sequence->
				sequence.code.@code == leadI
			}
			applog.log "Seq Type = " + seq.getClass()
			
			// appLog.log 'Code System = ' + seq.code.@codeSystem
			
			def sequenceData = seq.value.digits.toString()
			// appLog.log 'Sequence Data = ' + sequenceData
			applog.log 'Type of Sequence Data = ' + sequenceData.getClass()
			
			
			
			def ecgData = EcgUtil.createEcgGraphArray('20021122091000.000',new Double(0.002),new Double(2.5),sequenceData)
			
			
			applog.log 'Returned graphArray' + ecgData[0..80]
			
			applog.log"---------------------"
			applog.log "Done Xml-Evaluations"
			
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
		applog.log 'minTime = ' + minTime
		def maxTime = dataArray[dataArray.size()-1][0]
		applog.log 'maxTime = ' + maxTime
		
        def maxVerticalValue = 1500.00 // max extend in microVolt
		
		def hMinValue = minTime
		def hMaxValue = maxTime-minTime
		
		def numHorizontalGridIntervals = (hMaxValue)/ecgHorizontalMajorInterval
		// round the number of Gridlines to a whole number and re-adjust the hMaxValue
		numHorizontalGridIntervals = Math.round(numHorizontalGridIntervals)
		hMaxValue = numHorizontalGridIntervals * ecgHorizontalMajorInterval
		
		def numHorizontalGridLines = numHorizontalGridIntervals + 1
		applog.log 'numHorizontalGridLines = ' + numHorizontalGridLines
		
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
		
		applog.log 'numVerticalGridLines = ' + numVerticalGridLines
		
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
		
		applog.log "Options String"
		applog.log graphOptionsStr
		
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
		def Double[] valArray = curLead.getValues()
		def Double[] imagArray = new Double[arrSize]
		for (def i=0; i<timeValArray.size(); i++) {
			imagArray[i] = 0.0
		}
		
		def Double[] amp = Fft.transform(valArray)
		
		// def deltaf = 2 * Math.PI / ( curLead.getNumSamples() * curLead.getSampleInterval() )
		def deltaf = 1.0 / ( curLead.getNumSamples() * curLead.getSampleInterval() )
		
		applog.log "numSamples = " + curLead.getNumSamples()
		applog.log "sampleIntervale = " + curLead.getSampleInterval()
		applog.log "deltaf = " + deltaf
		
		def freq = deltaf
		def freqGraphSize = amp.length
		def freqArray = []
		
		applog.log "TimeValueArray for FFT"
		for (def i=0; i<freqGraphSize; i++) {
			
		    def row = [freq,amp[i]]
			freqArray.add(row)
			
			freq += deltaf
			applog.log( " Index i=" + i + " [ " + freq + " , " + amp[i] + " ]" )
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
		applog.log 'numHorizontalGridLines = ' + numHorizontalGridLines
		
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
		
		applog.log "Options String"
		applog.log graphOptionsStr
		
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
			
			for (def j = 0; j < leads.length(); j++) {
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
	
	EcgLead getSquareLead() {
		return leads[squareLeadIndex]
	}
	
	void determineHeartRate() {
		determineHeartRateForLead( getSquareLead() )
	}
	
	private void determineHeartRateForLead( EcgLead inLead ) {
		
		rPeaks = []
		Double[] RRIntervals = []
		Double[] heartRates = []
		
		def Object[] valueArray = inLead.getValues()
		
		// determine the top percent of values
		def topPercent = 0.8
		

		def numSamples = inLead.getNumSamples()

		rPeaks = ArrUtil.peaks(valueArray, 0.8, 1.0)

		applog.log "rPeakArray = " + rPeaks.toString()
		
		def numBeats = rPeaks.size()-1
		
		RRIntervals = new Double[numBeats]
		def beatLength = 0
		heartRates = new Double[numBeats]

		for (def i=0; i<numBeats; i++) {
			beatLength = ( rPeaks[i+1] - rPeaks[i] ) * inLead.sampleInterval
			RRIntervals[i] = beatLength
			heartRates[i] = 1/beatLength
		}
		applog.log "beatLength = " + RRIntervals.toString()
		
		def averageBeatLength = Stat.mean(RRIntervals)
		applog.log "average of beatLength = " + averageBeatLength

		heartRate = Stat.mean( heartRates )
		applog.log "heartRate [bps] = " + heartRate
		
		heartRate = 1/averageBeatLength * 60.0
		applog.log "heartRate [bpm] = " + heartRate	
		
		heartRateStdDev = Stat.StdDev( heartRates )
		applog.log "heartRate standard deviation [bpm] = " + heartRateStdDev
		
	}
	
	private void determineQrsInterval( EcgLead inLead ) {
		
		// determine initial approximation of QRS boundaries
		def numBeats = rPeaks.size();
		def factor = 0.2;
		for (int i=0; i<numBeats; i++) {
			def interval = 0;
			if (i==0) { 
				interval = rPeaks[1] - rPeaks[0];
			} else {
				interval = rPeaks[i] - rPeaks[i-1];
		    }
			def qrsExt = new Long( (long) Math.floor(factor * interval) );
			applog.log("qrsExt = " + qrsExt);
			
			if (i==0) {
				qrsStart[i] = Math.max(rPeaks[i]-qrsExt, 0);
			} else {
				qrsStart[i] = rPeaks[i]-qrsExt;
			}
			
			if (i==numBeats-1) {
				qrsEnd[i] = Math.min( rPeaks[i] + qrsExt, getNumSamples() )
			} else {
				qrsEnd[i] = rPeaks[i] + qrsExt
			}
		}
		
		applog.log("qrsStart", qrsStart);
		applog.log("rPeaks", rPeaks);
		applog.log("qrsEnd", qrsEnd);
		
		// TODO remove logging
		Double[] values = inLead.getValues()
		int trimPoint = 500;
		Double [] tim = ArrUtil.sequence(1.0, trimPoint);
		applog.logChart("DetQrsIntervalValues","tim","val","val(tim)",ArrUtil.trim(tim,trimPoint), ArrUtil.trim(values,trimPoint))
		
		
		Double[] slope = ArrUtil.absSlope( values )
		Double[] slopeFilt = Fft.fftFilter( slope, 200.0 )
		slope = ArrUtil.getCopy(slopeFilt)
		
		// TODO remove logging
		int trimPoint1 = 500;
		Double [] tim1 = ArrUtil.sequence(1.0, trimPoint);
		applog.logChart("DetQrsIntervalSlopeFiltered","tim","slope","fftSlopeFiltered(tim)",ArrUtil.trim(tim1,trimPoint1), ArrUtil.trim(slope,trimPoint1))
		
		for (int i=0; i<numBeats; i++) {
			
			Double maxDiff = 0.0;
			int newStartIndex = qrsStart[i];
			
			// TODO : introduce delta i (minimum QRS size)
			
			// move the start index to a better place
			for (int j=qrsStart[i]; j<rPeaks[i]; j++) {
				Double qrsAverage = ArrUtil.rangeAverage(slope,j,rPeaks[i])
				int befIndex
				if (i==0) {
					befIndex = 0
				} else {
					befIndex = qrsEnd[i-1]
				}
				Double beforeAverage = ArrUtil.rangeAverage(slope, befIndex, j-1)
				Double diff = qrsAverage - beforeAverage;
				if (diff > maxDiff) {
					newStartIndex = j;
					maxDiff = diff;
				}
			}
			qrsStart[i] = newStartIndex;
			applog.log("qrsStart[" + i + "] = " + qrsStart[i]);
			
			maxDiff = 0.0;
			int newEndIndex = qrsEnd[i];
			
			// move the end index to a better place
			for (int j=rPeaks[i]; j<=qrsEnd[i]; j++) {
				Double qrsAverage = ArrUtil.rangeAverage(slope,rPeaks[i],j)
				int afterIndex
				if (i == numBeats-1) {
					afterIndex = slope.length-1
				} else {
					afterIndex = qrsStart[i+1]
				}
				Double afterAverage = ArrUtil.rangeAverage(slope, j, afterIndex)
				Double diff = qrsAverage - afterAverage;
				if (diff > maxDiff) {
					newEndIndex = j;
					maxDiff = diff;
				}
			}
			qrsEnd[i] = newEndIndex;
			applog.log("qrsEnd[" + i + "] = " + qrsEnd[i]);
		}
			
	
	}
	
	private void determineQrsInterval01( EcgLead inLead ) {
		
		// TODO remove logging
		Double[] values = inLead.getValues()
		int trimPoint = 500;
		Double [] tim = ArrUtil.sequence(1.0, trimPoint);
		applog.logChart("DetQrsIntervalValues","tim","val","val(tim)",ArrUtil.trim(tim,trimPoint), ArrUtil.trim(values,trimPoint))
		
		
		Double[] slope = ArrUtil.absSlope( values )
		Double[] slopeFilt = Fft.fftFilter( slope, 200.0 )
		slope = ArrUtil.getCopy(slopeFilt)
		
		// TODO remove logging
		int trimPoint1 = 500;
		Double [] tim1 = ArrUtil.sequence(1.0, trimPoint);
		applog.logChart("DetQrsIntervalSlopeFiltered","tim","slope","fftSlopeFiltered(tim)",ArrUtil.trim(tim1,trimPoint1), ArrUtil.trim(slope,trimPoint1))
		
		Double [] centers = null;
		Integer [] clusterIndex = ArrUtil.kMeans( slopeFilt, 2, centers )
		
		Integer count = 0;
		for (int i=0; i<values.size(); i++) {
			if (clusterIndex[i] == 0) {
				if (i>0 && clusterIndex[i-1] == 1) {
					qrsStart[count]=i;
					applog.log("qrsStart[" + count + "] = " + qrsStart[i]);
				}
			} else {
				if (i>0 && clusterIndex[i-1] == 0) {
					qrsEnd[count] = i;
					applog.log("qrsEnd[" + count + "] = " + qrsEnd[i]);
					
					count++;	
				}
				if (i==0) {
					qrsStart[count] = i;
					applog.log("qrsStart[" + count + "] = " + qrsStart[i]);
				}
				if (i==values.size()-1) {
					qrsEnd[count] = i;
					applog.log("qrsEnd[" + count + "] = " + qrsEnd[i]);
				}
			}
			applog.log("qrsStart[" + count + "] = " + qrsStart[i]);	
		}	
	}
	
	private Double[] getQrsIndex( EcgLead inLead ) {
		
		Double[] qrsInd = ArrUtil.constant(0.0, getNumSamples() );
		
		for (int i=0; i<qrsStart.size; i++) {
			for (int j=qrsStart[i]; j<=qrsEnd[i]; j++) {
				qrsInd[j] = getRPeakAverage( inLead );
			}
		}

		return qrsInd;
	}
	
	private Double getRPeakAverage( EcgLead inLead ) {
		
		Double rPeakAverage = 0.0;
		int pSize = rPeaks.size();
		
		for (int i=0; i<pSize; i++) {
			rPeakAverage += inLead.values[rPeaks[i]];
		}
		
		return rPeakAverage;
	}
	
	public Integer getNumSamples() { 
		EcgLead lead = leads[0];
		return lead.getNumSamples()
	}
	
	String getSelectedGraphDataString( String inCode ) {
		
			applog.log "getSelectedGraphDataString LeadCode = " + inCode
			
			def leadIndex = leadCodes.findIndexOf { it == inCode }
			def timeValArray = leads[leadIndex].timeValueArray
			def timeValShort = []
			
			for (def i=1; i<1000; i++) {
				timeValShort.add( timeValArray[i] )
			}	
			
			def graphDataStr = timeValShort.toString()
			
			return graphDataStr
			
	}	

	
	String getSelectedGraphOptions() {
		
		def pixelsPerMinorGridline = 5
		def pixelsPerMajorGridline = 25
		
		def horizontalChartLeftBorder = 200
		def horizontalChartRightBorder = 200
		def verticalChartTopBorder = 100
		def verticalChartBottomBorder = 100
		
		
		def hMinValue = 0.0
		def hMaxValue = 60.0
		
		def numHorizontalGridLines = 12
		applog.log 'numHorizontalGridLines = ' + numHorizontalGridLines
		
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
		
		applog.log "Options String"
		applog.log graphOptionsStr
		
		return graphOptionsStr
	}
	
	
	    

}
