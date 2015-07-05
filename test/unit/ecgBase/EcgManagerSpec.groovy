package ecgBase

import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import org.jboss.logging.JBossLogManagerLogger
import spock.lang.Specification
import groovyx.gprof.Profiler



/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@Mock(EcgData)
@TestMixin(GrailsUnitTestMixin)
class EcgManagerSpec extends Specification {
	
	def AppLog applog = AppLog.getLogService()
	def EcgManager ecgManager = null
	def String testSampleFileName = "ecg01.xml"
	def Integer squareLeadIndex = 12
	def String squareLeadCode = 'Square Lead'
	def profilerLog
	
		def setup() {
			applog.log "Running EcgManagerSpec.setup"
			
			EcgUtil.uploadSampleFile(testSampleFileName)
			def testSample = EcgData.findByFileName(testSampleFileName)
			ecgManager = new EcgManager( testSample.id )
			ecgManager.createLeads()
			
			applog.log "Completed EcgManagerSpec.setup"
		}
	
		def cleanup() {
		}
	
		void "test testSetup"() {
		
			expect:
			ecgManager.ecgDat != null
			ecgManager.ecgDat.fileName == testSampleFileName
			// there should be 12 standard lead codes
			ecgManager.getLeadCodes().size == 12
			// there should be 12 standard leads plus one computed Square Lead
			ecgManager.getLeads().size == squareLeadIndex+1
			
		}
		
		void "test determineHeartRate"() {
			applog.log "Entering test determineHeartRate"
			
	        EcgLead squareLead = ecgManager.leads[squareLeadIndex]
			ecgManager.determineHeartRate(squareLead)
			
			expect:
			squareLead.code == squareLeadCode
			
			applog.log "Exiting test determineHeartRate"
		}
		
		void "test profileGprof"() {
			applog.log "Entering test profile"
			
			
			// def prof = new Profiler()
			// profilerLog.startProfiling("test")
			// prof.start()
		    EcgUtil.uploadSampleFiles()
			// prof.stop()
			// applog.log prof.report.prettyPrint()
			
			expect:
			squareLeadCode != null
			
			applog.log "Exiting test profile"
			
		}

}
