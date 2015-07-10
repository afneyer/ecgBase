import ecgBase.MyLog;
import ecgBase.EcgData;
import ecgBase.EcgUtil;

class BootStrap {
	
	def applog = MyLog.getLogService()

	def grailsApplication

	def init = { servletContext ->

		applog.log "Entering Bootstrap"
		
		EcgUtil.uploadSampleFiles()
		
		applog.log "Completed Bootstrap"
	}
	
	
	def destroy = {
	}
}
