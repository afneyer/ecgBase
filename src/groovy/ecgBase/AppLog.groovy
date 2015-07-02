package ecgBase

import grails.util.Holders;

import java.io.File;

class AppLog {
		
    def private static AppLog logServiceInstance = null;
	
	def private static logFileName = 'applog.log'
	
    private AppLog() {}
	
	public static AppLog getLogService() {
		
		if ( logServiceInstance == null ) {
			logServiceInstance = new AppLog()
		}
		return logServiceInstance
	}
		
		def File outFile = null
		
		def log( String s ) {
			
			if (outFile == null ) {
	
				def appLogFileDir = Holders.config.appLogFileDir
				outFile = new File(appLogFileDir,logFileName)
				outFile.createNewFile()
			}
			
			outFile << s + System.lineSeparator
		
		}
		
		def clear() {
			outFile = null
		}
		
	}