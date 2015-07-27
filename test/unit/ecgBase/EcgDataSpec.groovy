package ecgBase

import grails.test.mixin.Mock
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@Mock(EcgData)
@TestMixin(GrailsUnitTestMixin)
class EcgDataSpec extends Specification {
	
	def AppLog applog = AppLog.getLogService()

	def setup() {
	}

	def cleanup() {
	}

	void "test determine file type"() {

		// uploading files includes determining the data type of the file
		EcgUtil.uploadSampleFiles();
		
		expect:
		
		EcgData.findByFileName("ecg01.xml").dataFormat == EcgData.formatHL7		
		EcgData.findByFileName("Example.dcm").dataFormat == EcgData.formatBinary
		EcgData.findByFileName("Example.scp").dataFormat == EcgData.formatBinary
		EcgData.findByFileName("Example.xml").dataFormat == EcgData.formatHL7
		EcgData.findByFileName("OpenBCI01.txt").dataFormat == EcgData.formatOpenBCI
		
	}
}