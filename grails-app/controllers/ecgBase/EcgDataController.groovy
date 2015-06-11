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
            outputStream.write(buffer, 0, len)
            outputStream.flush()
            outputStream.close()
        }
    }
	
	@Transactional
	def graph(long id) {
		
		println ""
		println "Entering Evaluated and Learn " + new Date()
		def EcgManager ecgManager = new EcgManager(id)
		def Object retValue = ecgManager.evaluatedAndLearn()
	    println "Exited Evaluated and Learn"
		
		def ecgColumns = [['number', 'time'], ['number', 'ecg value [mv]']]
	    def graphData =
			      [
			        [0, 0],   [1, 10],  [2, 23],  [3, 17],  [4, 18],  [5, 9],
			        [6, 11],  [7, 27],  [8, 33],  [9, 40],  [10, 32], [11, 35],
			        [12, 30], [13, 40], [14, 42], [15, 47], [16, 44], [17, 48],
			        [18, 52], [19, 54], [20, 42], [21, 55], [22, 56], [23, 57],
			        [24, 60], [25, 50], [26, 52], [27, 51], [28, 49], [29, 53],
			        [30, 55], [31, 60], [32, 61], [33, 59], [34, 62], [35, 65],
			        [36, 62], [37, 58], [38, 55], [39, 61], [40, 64], [41, 65],
			        [42, 63], [43, 66], [44, 67], [45, 69], [46, 69], [47, 70],
			        [48, 72], [49, 68], [50, 66], [51, 65], [52, 67], [53, 70],
			        [54, 71], [55, 72], [56, 73], [57, 75], [58, 70], [59, 68],
			        [60, 64], [61, 60], [62, 65], [63, 67], [64, 68], [65, 69],
			        [66, 70], [67, 72], [68, 75], [69, 80]
			      ]
		[ecgColumns:ecgColumns, graphData:graphData]		    		

    }
	
	@Transactional
	def graphJsp(long id) {
		
		def EcgManager ecgManager = new EcgManager(id)
		println "Entering Graph Action"
		
				def defineDataTypeOfVariables = [['string', 'Task'], ['number', 'Total Sales($)'], ['number', 'Total Revenue($)']]
				def salesExpenses = [ ['2004', '10000', '6000'],['2005', '8000', '5000'],['2006', '12500', '9000'],['2007', '15500', '12000']]
		
		def graphData =
				      [
				        [0, 0],   [1, 10],  [2, 23],  [3, 17],  [4, 18],  [5, 9],
				        [6, 11],  [7, 27],  [8, 33],  [9, 40],  [10, 32], [11, 35],
				        [12, 30], [13, 40], [14, 42], [15, 47], [16, 44], [17, 48],
				        [18, 52], [19, 54], [20, 42], [21, 55], [22, 56], [23, 57],
				        [24, 60], [25, 50], [26, 52], [27, 51], [28, 49], [29, 53],
				        [30, 55], [31, 60], [32, 61], [33, 59], [34, 62], [35, 65],
				        [36, 62], [37, 58], [38, 55], [39, 61], [40, 64], [41, 65],
				        [42, 63], [43, 66], [44, 67], [45, 69], [46, 69], [47, 70],
				        [48, 72], [49, 68], [50, 66], [51, 65], [52, 67], [53, 70],
				        [54, 71], [55, 72], [56, 73], [57, 75], [58, 70], [59, 68],
				        [60, 64], [61, 60], [62, 65], [63, 67], [64, 68], [65, 69],
				        [66, 70], [67, 72], [68, 75], [69, 80]
				      ]
		
		
		def Object retValue = ecgManager.evaluatedAndLearn()
        	
        println "Completed Graph Action"
 
/*        def EcgData ecgDataInstance = ecgManager.ecgDat
        respond ecgDataInstance*/
        
        [dataTypeOfVariables:defineDataTypeOfVariables, salesExpenses:salesExpenses, graphData:graphData]
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
