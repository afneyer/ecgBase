import ecgBase.AppLog;
import ecgBase.EcgDataFile;
import ecgBase.EcgUtil;

class BootStrap {
	
	def applog = AppLog.getLogService()

	def grailsApplication

	def init = { servletContext ->

		applog.log "Entering Bootstrap"
		
		EcgUtil.uploadSampleFiles()
		
		applog.log "Completed Bootstrap"
	}
	
	
	def destroy = {
	}
}
