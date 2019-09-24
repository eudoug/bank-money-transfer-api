package com.doug.moneytransferapi.service

import com.doug.moneytransferapi.exception.ErrorResponse
import com.doug.moneytransferapi.exception.ExceptionHandler
import org.apache.log4j.Logger
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider


@Provider
class ExceptionService : ExceptionMapper<ExceptionHandler> {

    override fun toResponse(daoException: ExceptionHandler): Response {
        if (log.isDebugEnabled) {
            log.debug("Mapping exception to Response....")
        }
        val errorResponse = ErrorResponse()
        errorResponse.errorCode.equals(daoException.localizedMessage)

        // return internal server error for DAO exceptions
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse)
            .type(MediaType.APPLICATION_JSON).build()
    }

    companion object {
        private val log = Logger.getLogger(ExceptionService::class.java)
    }

}
