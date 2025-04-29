package org.balhom.currencyprofilesapi.common.handlers

import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import org.balhom.currencyprofilesapi.common.data.exceptions.ApiCodeException

@Provider
class ApiCodeExceptionHandler : ExceptionMapper<ApiCodeException> {
    override fun toResponse(exception: ApiCodeException): Response {
        val response = mapOf(
            "errorCode" to exception.errorCode,
            "message" to exception.message
        )

        return Response
            .status(Response.Status.BAD_REQUEST)
            .entity(response)
            .build()
    }
}
