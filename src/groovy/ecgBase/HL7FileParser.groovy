package ecgBase

class HL7FileParser extends EcgFileParser {
	
	def AppLog applog = AppLog.getLogService()
	
	HL7FileParser( EcgDataFile inFile ) {
		super(inFile);
	}

	@Override
	public EcgDAO parse() {
		// TODO Auto-generated method stub
		EcgDAO ecgDAO = new EcgDAO( ecgDataFile.id )
		
		createLeads(ecgDAO)
		
		return ecgDAO
	}
	
	EcgLead[] createLeads( EcgDAO inEcgDAO ) {
		
		def EcgLead leads = []
		applog.log "---Entering Create Leads ----------------------------------------------------------------"
		inEcgDAO.leadCodes.eachWithIndex { it, i -> // `it` is the current element, while `i` is the index
			def EcgLead lead = createLead(i,it)
			leads[i] = lead
		}
		
		applog.log "---Exiting CreateLeads ------------------------------------------------------------------"
		
		return leads
		
	}
	
	EcgLead createLead( leadIndex, leadCode ) {
		
		applog.log "Entering Lead Creations for " + leadCode
		applog.log "----------------------------------------"
		
		def byte[] fileData = ecgDataFile.fileData
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

}
