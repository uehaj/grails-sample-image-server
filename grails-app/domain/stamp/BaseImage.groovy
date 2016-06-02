package stamp

import grails.rest.Resource

//@Resource
class BaseImage {
    byte[] imageContent

    static mapping = { imageContent( type: 'materialized_blob' ) }
    static constraints = {
            imageContent maxSize: 1024 * 1024 * 2
    }

}
