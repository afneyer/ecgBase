import ecgBase.EcgData;

class BootStrap {

	def grailsApplication

	def init = { servletContext ->

		println "Entering Bootstrap"
		def filePath = grailsApplication.config.uploadFolder

		new File(filePath).eachFile() { file ->
			println "Uploading "+file.getName()
			def ecgDataInstance = new EcgData()
			ecgDataInstance.fileName = file.originalFilename
			ecgDataInstance.fileData = file.getBytes()
			ecgDataInstance.identifier = EcgData.count()
			ecgDataInstance.uploadDate = new Date()
			ecgDataInstance.save()
		}
	}
	
	
	def destroy = {
	}
}
