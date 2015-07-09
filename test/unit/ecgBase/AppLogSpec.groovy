package ecgBase

import org.junit.Test;

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import groovyx.gprof.Profiler
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class AppLogSpec extends Specification {
	
	

    def setup() {
    }

    def cleanup() {
    }

    void "test log"() {
		
		def AppLog applog = AppLog.getLogService()

		applog.initialize()
		
		def logString = "Basic Log Test"
		applog.log logString
	
		def String[] lineList = applog.outFile.readLines()
		
		expect:
		lineList[1] == ""
		lineList[2] == logString
    }
	
	void "test logArray"() {
		
		def AppLog applog = AppLog.getLogService()
		applog.initialize()
		
		def arrayToLog = [1.0, 1.1, 1.2, 1.3]
		applog.log("ArrayToLog", arrayToLog)
		
		def String[] lineList = applog.outFile.readLines()
		String logLine3 = "ArrayToLog[0] = 1.0"
		String logLine5 = "ArrayToLog[2] = 1.2"
		
		expect:
		lineList[2] == logLine3
		lineList[4] == logLine5

	}
	
	@Test
	void "test logLargeArray"() {
		
		
		def PerfLog perflog = PerfLog.getLogService()
		def prof = new Profiler()
		
		def AppLog applog = AppLog.getLogService()
		Integer asize = 1000
			
		Double[] test = new Double[asize]
		for (int i=0; i<asize; i++) {
			test[i] = Math.random()
		}
		
		prof.start()
		applog.log("testArray",test)
		prof.stop()
		applog.log prof.report.prettyPrint( perflog.getPrintWriter() )
		
		expect:
		asize == asize
		
	}
}
