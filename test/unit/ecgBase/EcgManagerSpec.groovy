package ecgBase

import grails.test.mixin.Mock
import grails.test.mixin.TestMixin
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

		ecgManager.determineHeartRate()

		expect:
		ecgManager.heartRate < 71.7
		ecgManager.heartRate > 71.5

		applog.log "Exiting test determineHeartRate"
	}
	
	void "test determineQrsInterval"() {
		applog.log "Entering test determineHeartQrsInterval"

		ecgManager.determineHeartRate()
		EcgLead lead2 = ecgManager.leads[1];
		ecgManager.determineQrsInterval( lead2 );
		Double[] qrsInd = ecgManager.getQrsIndex( lead2 );
		
		Double[] tim = lead2.getTimes()
		Double[] val = lead2.getValues()
		Double[] absSlope = ArrUtil.absSlope(val) 
		Double[] slope = ArrUtil.slope(val)
		Double[] imag = ArrUtil.constant(0.0, slope.length)
		Double[] fft = Fft.transform(absSlope);
		Double[] valFiltered = Fft.fftFilter(absSlope,200)
		
		int trimPoint = 500;
		
		applog.logChart("a1ecgValues","tim","val","val(tim)",ArrUtil.trim(tim,trimPoint), ArrUtil.trim(val,trimPoint))
		applog.logChart("a1ecgValuesSlope","tim","slope","slope(tim)",ArrUtil.trim(tim,trimPoint), ArrUtil.trim(absSlope,trimPoint))
 		applog.logChart("a1qrsInd","tim","qrsInd","qrsInd(time)",ArrUtil.trim(tim, trimPoint), ArrUtil.trim(qrsInd, trimPoint))
		applog.logChart("a1fftSlope","fre","fft","fftSlope(fre)",ArrUtil.sequence(1.0,fft.length),fft)
		applog.logChart("a1SlopeFiltered","fre","fft","fftSlopeFiltered(tim)",ArrUtil.trim(tim,trimPoint), ArrUtil.trim(valFiltered,trimPoint))
		
		// trim all the results to 1000 points (2 heart beats)
		tim = ArrUtil.trim(tim, trimPoint);
		val = ArrUtil.trim(val, trimPoint);
		absSlope = ArrUtil.trim(absSlope, trimPoint);
		qrsInd = ArrUtil.trim(qrsInd, trimPoint);
		
		// log all arrays
		applog.log('a1Tim', tim);
		applog.log('a1Val', val);
		applog.log('a1absSlope', absSlope);
		applog.log('a1qrsInd', qrsInd);
		
		// log averages
		applog.log('a1absSlopeAverage', ArrUtil.rangeAverage(absSlope, 0, absSlope.length-1))
		applog.log('a1absSlopeAverageFiltered', ArrUtil.rangeAverage(valFiltered, 0, valFiltered.length-1))
		
		Double[][] y = new Double[2][val.length]
		y[0] = val
		y[1] = qrsInd
		
		String[] s = new String[2]
		s[0] = "val(tim)"
		s[1] = "qrsInd(time)"
		applog.logChart("a1qrsIndComb","tim","qrsInd",s,tim, y)
		
		y = new Double[2][absSlope.length]
		y[0] = absSlope
		y[1] = qrsInd
		
		s = new String[2]
		s[0] = "val(tim)"
		s[1] = "absSlope(time)"
		applog.logChart("a1qrsIndAbsSlope","tim","absSlope",s,tim, y)
		
		
		expect:
		ecgManager.heartRate < 71.7
		ecgManager.heartRate > 71.5

		applog.log "Exiting test determineHeartQrsInterval"
	}
	
	

	void "test profileGprof"() {
		applog.log "Entering test profile"


		def PerfLog perflog = PerfLog.getLogService()
		def prof = new Profiler()

		prof.start()
		EcgUtil.uploadSampleFiles()
		prof.stop()
		prof.report.prettyPrint(perflog.getPrintWriter())

		expect:
		squareLeadCode != null

		applog.log "Exiting test profile"

	}
	
	

	void "test manualExploration"() {

		applog.log "Entering test manualExploration"

		ecgManager.determineHeartRate()
		def EcgLead lead = ecgManager.leads[1]
		def arrSize = lead.getNumSamples()
		def values = lead.getValues()
		
		def tim = lead.getTimes()
		def slope = ArrUtil.slope(values)
		def absSlope = ArrUtil.absSlope( values )
		def trimPoint = 500;

		applog.logChart("a2Values","tim","val","val(tim)",ArrUtil.trim(tim,trimPoint), ArrUtil.trim(values,trimPoint))
		
		applog.logChart("a2Slope","tim","slope","slope(tim)",ArrUtil.trim(tim,trimPoint), ArrUtil.trim(slope,trimPoint))
		applog.logChart("a2AbsSlope","tim","slope","slope(tim)",ArrUtil.trim(tim,trimPoint), ArrUtil.trim(absSlope,trimPoint))

		Double[] slopeFiltered = Fft.fftFilter(absSlope,500)
		applog.logChart("a2SlopeFiltered","tim","slopeFilt","slopeFiltered(tim)",ArrUtil.trim(tim,trimPoint), ArrUtil.trim(slopeFiltered,trimPoint))

		// kMeans algorithm with raw slope
		Double[] clusterMeans = null;
		Integer[] clusterIndex = ArrUtil.kMeans(absSlope,2,clusterMeans);
		Double[] clusterDbl = ArrUtil.getCopy(clusterIndex);
		applog.logChart("a2ClusterIndexRaw","tim","index","index(tim)",ArrUtil.trim(tim,trimPoint), ArrUtil.trim(clusterDbl,trimPoint))
		
		// kMeans algorithm with fft-filtered slope
		clusterMeans = null;
		clusterIndex = ArrUtil.kMeans(slopeFiltered,2,clusterMeans);
		clusterDbl = ArrUtil.getCopy(clusterIndex);
		applog.logChart("a2ClusterIndexFftFiltered","tim","index","index(tim)",ArrUtil.trim(tim,trimPoint), ArrUtil.trim(clusterDbl,trimPoint))
		
		// peak algorithm
		Double factor = 0.2;
		Integer[][] peakIntervals = ArrUtil.peakIntervals(slopeFiltered,factor,1.0)
		applog.log("qrsStart",peakIntervals[0]);
		applog.log("qrsPeak",peakIntervals[1]);
		applog.log("qrsEnd",peakIntervals[2]);
		ecgManager.qrsStart = peakIntervals[0];
		ecgManager.qrsEnd = peakIntervals[2];

		// applog.logChart("a2ClusterIndexFftFiltered","tim","index","intex(tim)",ArrUtil.trim(tim,trimPoint), ArrUtil.trim(clusterDbl,trimPoint))

		expect:
		squareLeadCode != null

		applog.log "Exiting test manualExloration"

	}

}
