package ecgBase

import java.io.File;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

class AppLog {
		
    def private static AppLog logServiceInstance = null;
	
    private AppLog() {}
	
	public static AppLog getLogService() {
		
		if ( logServiceInstance == null ) {
			logServiceInstance = new AppLog()
		}
		return logServiceInstance
	
	}
		
		def File outFile = null
		
		def log( String s ) {
			
			if (outFile == null || ! outFile.exists() ) {
	
				// TODO : make this a configuration
				outFile = new File("c:/afndev/apps/ecgBase/logs/applog.log")
				outFile.createNewFile()
			}
			
			outFile << s + System.lineSeparator
		
		}
		
		def clear() {
			outFile = null
		}
		
	}