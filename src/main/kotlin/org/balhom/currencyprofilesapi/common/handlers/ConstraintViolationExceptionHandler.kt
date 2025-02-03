package org.balhom.currencyprofilesapi.common.handlers

import jakarta.validation.ConstraintViolationException
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import org.balhom.currencyprofilesapi.common.data.responses.ApiConstraintsErrorResponse
import org.balhom.currencyprofilesapi.common.data.responses.ApiFieldErrorResponse

@Provider
class ConstraintViolationExceptionHandler : ExceptionMapper<ConstraintViolationException> {
    override fun toResponse(exception: ConstraintViolationException): Response {
        val fields = exception.constraintViolations.map {
            ApiFieldErrorResponse(
                it.propertyPath.toString(),
                it.message,
            )
        }.toList()

        return Response
            .status(Response.Status.BAD_REQUEST)
            .entity(
                ApiConstraintsErrorResponse(fields)
            )
            .build()
    }
}
