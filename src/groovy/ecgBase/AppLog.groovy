package ecgBase

import grails.util.Holders;

import java.io.File;

class AppLog {

	def private static AppLog logServiceInstance = null
	def private lineSep = System.lineSeparator
	def private File outFile = null
	def private titleString = null

	private AppLog() {
	}

	public static AppLog getLogService() {

		if ( logServiceInstance == null ) {
			logServiceInstance = new AppLog()
		}
		
		return logServiceInstance
	}

	def initialize() {

		def appLogFileDir = Holders.config.appLogFileDir
		def logFileName = Holders.config.applogFileName
		outFile = new File(appLogFileDir,logFileName)
		outFile.createNewFile()
		// TODO : improve log performance with file writer
		// new File("foo").withWriterAppend("UTF-8")

		def appName = grails.util.Metadata.current.'app.name'
		titleString = "Application Log File for " + appName + " Created on " + new Date() + lineSep + lineSep
		outFile.write(titleString)
	
	}	

	def log( String arrayName, array ) {
		
		if (outFile == null) { initialize() }
		def StringBuffer sout = new StringBuffer("")
		def aSize = array.size()
		
		for (def i=0; i<aSize; i++) {
			sout.append(arrayName + "["+ i + "] = " + array[i] + lineSep)
		}
		outFile <<  sout
	}
	
	def log( String s ) {
		
		if (outFile == null) {
			initialize()
		}

		outFile << s + lineSep
	}


}