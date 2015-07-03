package ecgBase

class EcgLead {
	
	def String code = null
	def timeValueArray = []
	def origin = null
	def scale = null

	public EcgLead ( inCode ) {
		code = inCode
	}
	
	Integer getNumSamples() {
		return timeValueArray.size()
	}
	
	Double getSampleInterval() {
		def Double interval = timeValueArray[1][0] - timeValueArray[0][0]
		return interval
	}
	
	Double[] getTimes () {	
		def Double[] timeArray = new Double[getNumSamples()]
		for (def i=0; i<getNumSamples(); i++) {
			timeArray[i] = timeValueArray[i][0]
		}
		return timeArray
	}
	
	Double[] getValues () {
		def Double[] valArray = new Double[getNumSamples()]
		for (def i=0; i<getNumSamples(); i++) {
			valArray[i] = timeValueArray[i][1]
		}
		return valArray
	}

}
