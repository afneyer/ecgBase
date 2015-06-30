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

}
