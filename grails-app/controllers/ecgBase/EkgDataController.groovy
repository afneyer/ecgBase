package ecgBase



import static org.springframework.http.HttpStatus.*
import ecgBase.EkgData;
import grails.transaction.Transactional

@Transactional(readOnly = true)
class EkgDataController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond EkgData.list(params), model:[ekgDataInstanceCount: EkgData.count()]
    }

    def show(EkgData ekgDataInstance) {
        respond ekgDataInstance
    }

    def create() {
        respond new EkgData(params)
    }

    @Transactional
    def save(EkgData ekgDataInstance) {
        if (ekgDataInstance == null) {
            notFound()
            return
        }

        if (ekgDataInstance.hasErrors()) {
            respond ekgDataInstance.errors, view:'create'
            return
        }

        ekgDataInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'ekgData.label', default: 'EkgData'), ekgDataInstance.id])
                redirect ekgDataInstance
            }
            '*' { respond ekgDataInstance, [status: CREATED] }
        }
    }

    def edit(EkgData ekgDataInstance) {
        respond ekgDataInstance
    }

    @Transactional
    def update(EkgData ekgDataInstance) {
        if (ekgDataInstance == null) {
            notFound()
            return
        }

        if (ekgDataInstance.hasErrors()) {
            respond ekgDataInstance.errors, view:'edit'
            return
        }

        ekgDataInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'EkgData.label', default: 'EkgData'), ekgDataInstance.id])
                redirect ekgDataInstance
            }
            '*'{ respond ekgDataInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(EkgData ekgDataInstance) {

        if (ekgDataInstance == null) {
            notFound()
            return
        }

        ekgDataInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'EkgData.label', default: 'EkgData'), ekgDataInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'ekgData.label', default: 'EkgData'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
