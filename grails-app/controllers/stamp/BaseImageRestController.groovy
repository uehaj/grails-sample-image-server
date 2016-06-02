package stamp

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class BaseImageRestController {

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond BaseImage.list(params), model:[baseImageCount: BaseImage.count()]
    }

    def show(BaseImage baseImage) {
        respond baseImage
    }

    def showImage(BaseImage baseImage) {
        baseImage
    }

    @Transactional
    def save(BaseImage baseImage) {
        if (baseImage == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (baseImage.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond baseImage.errors, view:'create'
            return
        }

        baseImage.save flush:true

        respond baseImage, [status: CREATED, view:"show"]
    }

    @Transactional
    def update(BaseImage baseImage) {
        if (baseImage == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (baseImage.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond baseImage.errors, view:'edit'
            return
        }

        baseImage.save flush:true

        respond baseImage, [status: OK, view:"show"]
    }

    @Transactional
    def delete(BaseImage baseImage) {

        if (baseImage == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        baseImage.delete flush:true

        render status: NO_CONTENT
    }
}
