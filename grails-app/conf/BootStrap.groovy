import ecgBase.EcgData;

class BootStrap {
	
	def appLog = AlogService.getAppLogService()

	def grailsApplication

	def init = { servletContext ->

		println "Entering Bootstrap"
		def filePath = grailsApplication.config.uploadFolder

		new File(filePath).eachFile() { file ->
			appLog.log "Uploading "+file.getName()
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
