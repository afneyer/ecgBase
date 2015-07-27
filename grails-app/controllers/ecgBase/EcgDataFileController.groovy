package ecgBase

import static org.springframework.http.HttpStatus.*

import org.springframework.aop.aspectj.RuntimeTestWalker.ThisInstanceOfResidueTestVisitor;

import ecgBase.EcgDataFile
import ecgBase.EcgDAO
import ecgBase.AppLog
import grails.transaction.Transactional



@Transactional(readOnly = true)

class EcgDataFileController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
	
	static AppLog appLog = AppLog.getLogService()

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond EcgDataFile.list(params), model:[ecgDataInstanceCount: EcgDataFile.count()]
    }

    def show(EcgDataFile ecgDataInstance) {
    	EcgDAO dao = ecgDataInstance.initDAO()
        respond ecgDataInstance
    }

    def create() {
        respond new EcgDataFile(params)
    }
	
	@Transactional
	def upload() {
		def file = request.getFile('file')
		if(file.empty) {
			flash.message = "File cannot be empty"
		} else {
			def ecgDataInstance = new EcgDataFile( file )
		}
		redirect (action:'index')
	}
	
	@Transactional
	def download(long id) {
        def ecgDataSample = EcgDataFile.get(id)
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
            outputStream.write(buffer, 0, len)
            outputStream.flush()
            outputStream.close()
        }
    }
	
	
	
	
	@Transactional
	def graphFft(long id) {
		
		appLog.log(" ")
		appLog.log( "Entering FFT and Graph" + "   " + new Date() )
		
		def EcgDAO ecgManager = new EcgDAO(id)
		ecgManager.initData()
		
		// TODO : remove
		// def obj = ecgManager.createLeads()
		
		def String graphDataStr = ecgManager.getFftGraphDataString( 'MDC_ECG_LEAD_II' )		
		
		def String graphColumnStr = ecgManager.getGraphColumnString( 'Amplitude' )
				
		def graphOptionStr = ecgManager.getFftGraphOptions()
		
		appLog.log "Before Logging in graphFft"

	    // appLog.log graphData
		appLog.log "Completed FFT and Graph"
	    
		[graphColumns:graphColumnStr, graphData:graphDataStr, graphOptions:graphOptionStr]
    }
	
	@Transactional
	def graphSelected(long id) {
		
		appLog.log(" ")
		appLog.log( "Entering Graph Selected Sequence" + "   " + new Date() )
		
		def EcgDAO ecgManager = new EcgDAO(id)
		ecgManager.initData()
		
		def String graphDataStr = ecgManager.getSelectedGraphDataString( ecgManager.leadCodes[1] )		
		
		def String graphColumnStr = ecgManager.getGraphColumnString( 'Amplitude' )
				
		def graphOptionStr = ecgManager.getSelectedGraphOptions()
	    
		[graphColumns:graphColumnStr, graphData:graphDataStr, graphOptions:graphOptionStr]
    }
	
	@Transactional
	def graphJsp(long id) {
		
		def EcgDAO ecgManager = new EcgDAO(id)
		ecgManager.initData()
		appLog.log "Entering Graph Action"
					    		 
//	    appLog.log ""
//      appLog.log "Entering Evaluated and Learn " + new Date()
		
		def graphData = ecgManager.createEcgGraphDataArray()
		
		// TODO : remove
		// def obj = ecgManager.createLeads()
		
		appLog.log "Exited Evaluated and Learn"
		
		def String ecgColumns = ecgManager.getGraphColumnString()
	    def graphColumnsStr = ecgColumns
	    // appLog.log "ecgColumns = " + ecgColumns
		
		// def String graphColumnStr = ecgManager.getGraphColumnString('MDC_ECG_LEAD_II')
		// appLog.log "graphColumStr = " + graphColumnStr
		
		// appLog.log graphData
	
		// def graphDataStr = graphData.toString()
		
		// def graphDataStr = ecgManager.leads[0].timeValueArray.toString()
		
		def graphDataStrTemp = ecgManager.getGraphDataString()
		appLog.log "graphDataStrTemp " + graphDataStrTemp[0..1000] 
				
	    def graphDataStr = ecgManager.getGraphDataString('MDC_ECG_LEAD_II')
		
	    def graphOptionsStr = ecgManager.getGraphOptions()
		
        appLog.log "Completed Graph Action"
        
        [graphColumns:graphColumnsStr, graphData:graphDataStrTemp, graphOptions:graphOptionsStr]
    }
	
	

    @Transactional
    def save(EcgDataFile ecgDataInstance) {
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

    def edit(EcgDataFile ecgDataInstance) {
        respond ecgDataInstance
    }

    @Transactional
    def update(EcgDataFile ecgDataInstance) {
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
    def delete(EcgDataFile ecgDataInstance) {

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
