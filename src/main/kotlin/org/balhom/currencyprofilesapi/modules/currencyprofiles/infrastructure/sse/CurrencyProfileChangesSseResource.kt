package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.sse

import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.sse.SseEventSink
import org.balhom.currencyprofilesapi.common.handlers.SseHandler
import org.eclipse.microprofile.jwt.JsonWebToken
import java.util.UUID


@Path(CurrencyProfileChangesSseResource.RESOURCE_PATH)
@Produces(MediaType.SERVER_SENT_EVENTS)
class CurrencyProfileChangesSseResource(
    private val sseHandler: SseHandler
) {
    companion object {
        const val RESOURCE_PATH = "/api/v1/currency-profile"
    }

    @Inject
    lateinit var jwt: JsonWebToken

    @GET
    @Path("/subscribe")
    fun subscribe(@Context eventSink: SseEventSink) {
        sseHandler.addSink(
            UUID.fromString(jwt.subject),
            eventSink
        )
    }
}