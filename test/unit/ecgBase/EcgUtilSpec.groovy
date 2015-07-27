package ecgBase

import org.junit.Test;

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.test.mixin.Mock
import groovyx.gprof.Profiler

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@Mock(EcgData)
class EcgUtilSpec extends Specification {

	def AppLog applog = AppLog.getLogService()

	def setup() {
		applog.log "running setup again"
	}

	def cleanup() {
	}

	void "test uploadSampleFiles"() {

		EcgUtil.uploadSampleFiles()
		def testSample = EcgData.findByFileName("ecg01.xml")
		applog.log "testSample = " + testSample.getFileDataStr32()

		expect:
		testSample != null
	}

	@Test
	void "test uploadSampleFilePerformance"() {

		// Sample performance test
		
		def PerfLog perflog = PerfLog.getLogService()
		def prof = new Profiler()

		prof.start()
		EcgUtil.uploadSampleFiles()
		applog.log("Performance testing uploading files")
		def testSample = EcgData.findByFileName("ecg01.xml")
		prof.stop()
		applog.log prof.report.prettyPrint( perflog.getPrintWriter() )

		expect:
		testSample != null
	}

	void "test "() {
		applog.log "testing log for second test"
		expect:
		grailsApplication != null
	}
	
	void "test isHL7"() {
		
		String str = '''<?xml version="1.0" encoding="utf-8"?>
		    <AnnotatedECG xmlns="urn:hl7-org:v3" xmlns:voc="urn:hl7-org:v3/voc"
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="urn:hl7-org:v3 ../schema/PORT_MT020001.xsd" type="Observation">
			<id root="61d1a24f-b47e-41aa-ae95-f8ac302f4eeb"/>
			</AnnotatedECG>'''
		
		byte[] bytes = str.getBytes();
		
		boolean isHL7 = EcgUtil.isHL7(bytes)
		
		expect:
		isHL7 == true
		grailsApplication != null
		
	}
	
	void "test isHL7Binary"() {
		
		byte[] bytes = new byte[1];
		byte b = 0x08;
		bytes[0] = b;
		
		boolean isHL7 = EcgUtil.isHL7(bytes)
		
		expect:
		isHL7 == false
	
	}
	
	void "test isOpenBCI"() {
		
		String str = '''%OpenBCI Raw EEG Data
             %
             %Sample Rate = 250.0 Hz'''
		
		byte[] bytes = str.getBytes();
		
		boolean isOpenBCI = EcgUtil.isOpenBCI(bytes)
		
		expect:
		isOpenBCI == true
	
	}
	
	void "test isOpenBCIBinary"() {
		
		byte[] bytes = new byte[1];
		byte b = 0x08;
		bytes[0] = b;
		
		boolean isOpenBCI = EcgUtil.isOpenBCI(bytes)
		
		expect:
		isOpenBCI == false
	
	}
		
}
