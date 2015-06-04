package ecgBase

import static org.springframework.http.HttpStatus.*
import ecgBase.EcgData;
import ecgBase.EcgManager;
import grails.transaction.Transactional



@Transactional(readOnly = true)

class EcgDataController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond EcgData.list(params), model:[ecgDataInstanceCount: EcgData.count()]
    }

    def show(EcgData ecgDataInstance) {
        respond ecgDataInstance
    }

    def create() {
        respond new EcgData(params)
    }
	
	@Transactional
	def upload() {
		def file = request.getFile('file')
		if(file.empty) {
			flash.message = "File cannot be empty"
		} else {
			def ecgDataInstance = new EcgData()
			ecgDataInstance.fileName = file.originalFilename
			ecgDataInstance.fileData = file.getBytes()
			ecgDataInstance.identifier = EcgData.count()
			ecgDataInstance.save()
		}
		redirect (action:'index')
	}
	
	@Transactional
	def download(long id) {
        def ecgDataSample = EcgData.get(id)
        if ( ecgDataSample == null) {
            flash.message = "ECG record not found."
            redirect (action:'list')
        } else {
            response.setContentType("APPLICATION/OCTET-STREAM")
            response.setHeader("Content-Disposition", "Attachment;Filename=\"${ecgDataSample.fileName}\"")
            def fullFilename = "c:/Temp/"+ecgDataSample.fileName
            def outputStream = response.getOutputStream()
            def byte[] buffer = ecgDataSample.fileData
            def int len = buffer.size()
            outputStream.write(buffer, 0, len);
            outputStream.flush()
            outputStream.close()
        }
    }
	
	@Transactional
	def graph(long id) {
		
		def EcgManager ecgManager = new EcgManager(id)
		println "Entering Graph Action"
            
		def Object retValue = ecgManager.evaluatedAndLearn()
        	
        println "Completed Graph Action"
 
        redirect (action:'index')
    }

    @Transactional
    def save(EcgData ecgDataInstance) {
        if (ecgDataInstance == null) {
            notFound()
            return
        }

        if (ecgDataInstance.hasErrors()) {
            respond ecgDataInstance.errors, view:'create'
            return
        }

        ecgDataInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'ecgData.label', default: 'EkgData'), ecgDataInstance.id])
                redirect ecgDataInstance
            }
            '*' { respond ecgDataInstance, [status: CREATED] }
        }
    }

    def edit(EcgData ecgDataInstance) {
        respond ecgDataInstance
    }

    @Transactional
    def update(EcgData ecgDataInstance) {
        if (ecgDataInstance == null) {
            notFound()
            return
        }

        if (ecgDataInstance.hasErrors()) {
            respond ecgDataInstance.errors, view:'edit'
            return
        }

        ecgDataInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'EkgData.label', default: 'EkgData'), ecgDataInstance.id])
                redirect ecgDataInstance
            }
            '*'{ respond ecgDataInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(EcgData ecgDataInstance) {

        if (ecgDataInstance == null) {
            notFound()
            return
        }

        ecgDataInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'EkgData.label', default: 'EkgData'), ecgDataInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'ecgData.label', default: 'EkgData'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
