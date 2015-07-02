import ecgBase.AppLog;
import ecgBase.EcgData;

class BootStrap {
	
	def applog = AppLog.getLogService()

	def grailsApplication

	def init = { servletContext ->

		applog.log "Entering Bootstrap"
		def filePath = grailsApplication.config.uploadFolder

		new File(filePath).eachFile() { file ->
			applog.log "Uploading " + file.getName()
			def ecgDataInstance = new EcgData()
			ecgDataInstance.fileName = file.getName()
			ecgDataInstance.fileData = file.getBytes()
			ecgDataInstance.identifier = EcgData.count()
			ecgDataInstance.uploadDate = new Date()
			ecgDataInstance.save()
		}
	}
	
	
	def destroy = {
	}
}
