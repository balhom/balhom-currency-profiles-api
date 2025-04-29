package org.balhom.currencyprofilesapi.common.handlers

import jakarta.validation.ConstraintViolationException
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider

@Provider
class ConstraintViolationExceptionHandler : ExceptionMapper<ConstraintViolationException> {
    override fun toResponse(exception: ConstraintViolationException): Response {
        val fields = exception.constraintViolations.map {
            mapOf(
                "field" to it.propertyPath.toString(),
                "message" to it.message
            )
        }.toList()

        return Response
            .status(Response.Status.BAD_REQUEST)
            .entity(
                mapOf(
                    "fields" to fields
                )
            )
            .build()
    }
}
