package ecgBase

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.test.mixin.Mock

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
	
	void "test applog"() {
		applog.log "testing log for second test"
		expect:
		grailsApplication != null
	}

}
