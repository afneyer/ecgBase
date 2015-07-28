package ecgBase

import grails.test.mixin.Mock
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 * 
 * rename this class
 * 
 */
@Mock(EcgDataFile)
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
		
		EcgDataFile.findByFileName("ecg01.xml").dataFormat == EcgDataFile.formatHL7		
		EcgDataFile.findByFileName("Example.dcm").dataFormat == EcgDataFile.formatBinary
		EcgDataFile.findByFileName("Example.scp").dataFormat == EcgDataFile.formatBinary
		EcgDataFile.findByFileName("Example.xml").dataFormat == EcgDataFile.formatHL7
		EcgDataFile.findByFileName("OpenBCI01.txt").dataFormat == EcgDataFile.formatOpenBCI
		
	}
}