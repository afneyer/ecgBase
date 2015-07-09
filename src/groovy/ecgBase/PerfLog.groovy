package ecgBase

import grails.util.Holders;

import java.io.File;

class PerfLog {

	def private static PerfLog logServiceInstance = null
	def private lineSep = System.lineSeparator
	def private File outFile = null
	def private PrintWriter printWriter = null
	def private titleString = null

	private PerfLog() {
	}

	public static PerfLog getLogService() {

		if ( logServiceInstance == null ) {
			logServiceInstance = new PerfLog()
		}
		
		return logServiceInstance
	}

	def initialize() {

		def appLogFileDir = Holders.config.appLogFileDir
		def logFileName = Holders.config.perflogFileName
		
		outFile = new File(appLogFileDir,logFileName)
		printWriter = new PrintWriter(outFile)

		def appName = grails.util.Metadata.current.'app.name'
		titleString = "Performance Log File for " + appName + " Created on " + new Date() + lineSep + lineSep
		
		printWriter.append(titleString)
		printWriter.flush()
	
	}	

	def getPrintWriter() {
		
		if (outFile == null) { initialize() }
		return printWriter
		
	}

}