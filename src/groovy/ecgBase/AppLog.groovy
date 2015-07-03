package ecgBase

import grails.util.Holders;

import java.io.File;

class AppLog {

	def private static AppLog logServiceInstance = null
	def private lineSep = System.lineSeparator
	def private File outFile = null

	private AppLog() {
	}

	public static AppLog getLogService() {

		if ( logServiceInstance == null ) {
			logServiceInstance = new AppLog()
		}
		
		return logServiceInstance
	}

	

	def log( String s ) {
		
		if (outFile == null) {
			initialize()
		}

		outFile << s + lineSep
	}

	def initialize() {

		def appLogFileDir = Holders.config.appLogFileDir
		def logFileName = Holders.config.applogFileName
		outFile = new File(appLogFileDir,logFileName)
		outFile.createNewFile()

		def appName = grails.util.Metadata.current.'app.name'
		def titleString = "Application Log File for " + appName + " Created on " + new Date() + lineSep + lineSep
		outFile.write(titleString)
	
	}
}