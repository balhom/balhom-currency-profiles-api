package org.balhom.currencyprofilesapi.common.handlers

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.sse.Sse
import jakarta.ws.rs.sse.SseEventSink
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@ApplicationScoped
class SseHandler(private val sse: Sse) {
    companion object {
        const val EVENT_NAME = "currency-profile-event"
    }

    private val subscriptions = ConcurrentHashMap<String, SseEventSink>()

    fun addSink(
        userId: UUID,
        eventSink: SseEventSink
    ) {
        subscriptions[userId.toString()]?.close()
        subscriptions[userId.toString()] = eventSink
    }

    fun sendMessage(
        userId: UUID,
        data: Any
    ) {
        Log.debug("Sending SSE event: $data")

        val eventSink = subscriptions[userId.toString()]

        if (eventSink != null && ! eventSink.isClosed) {
            eventSink.send(
                sse.newEventBuilder()
                    .name(EVENT_NAME)
                    .data(data)
                    .build()
            )
        }
    }
}