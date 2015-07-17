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
}
