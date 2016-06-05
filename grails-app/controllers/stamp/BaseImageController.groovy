package stamp

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class BaseImageController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond BaseImage.list(params), model:[baseImageCount: BaseImage.count()]
    }

    def show(BaseImage baseImage) {
        respond baseImage
    }

    def showImage(BaseImage baseImage) {
        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        os.write(baseImage.imageContent)
        os.close();
    }

    def create() {
        respond new BaseImage(params)
    }

    @Transactional
    def save(BaseImage baseImage) {
        if (baseImage == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (baseImage.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond baseImage.errors, view:'create'
            return
        }

        baseImage.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'baseImage.label', default: 'BaseImage'), baseImage.id])
                redirect baseImage
            }
            '*' { respond baseImage, [status: CREATED] }
        }
    }

    def edit(BaseImage baseImage) {
        respond baseImage
    }

    @Transactional
    def update(BaseImage baseImage) {
	    println params
println "-------------"+baseImage.imageContent[0..20]
        if (baseImage == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (baseImage.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond baseImage.errors, view:'edit'
            return
        }

        baseImage.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'baseImage.label', default: 'BaseImage'), baseImage.id])
                redirect baseImage
            }
            '*'{ respond baseImage, [status: OK] }
        }
    }

    @Transactional
    def delete(BaseImage baseImage) {

        if (baseImage == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        baseImage.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'baseImage.label', default: 'BaseImage'), baseImage.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'baseImage.label', default: 'BaseImage'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
