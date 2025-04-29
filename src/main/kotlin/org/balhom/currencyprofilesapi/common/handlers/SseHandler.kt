package org.balhom.currencyprofilesapi.common.handlers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.sse.Sse
import jakarta.ws.rs.sse.SseEventSink
import java.util.*
import java.util.concurrent.*

@ApplicationScoped
class SseHandler(private val sse: Sse) {
    companion object {
        const val EVENT_NAME = "currency-profile-event"
    }

    private val subscriptions = ConcurrentHashMap<String, SseEventSink>()

    private final val mapper = jacksonObjectMapper()

    init {
        mapper.findAndRegisterModules()
    }

    fun addSink(
        userId: UUID,
        eventSink: SseEventSink
    ) {
        // Check if a subscription already exists with this ID
        val existingSubscription = subscriptions[userId.toString()]
        if (existingSubscription != null && !existingSubscription.isClosed) {
            // Close the previous connection if it is still open
            existingSubscription.close()
        }

        Log.debug("Adding new SSE subscription")
        subscriptions[userId.toString()] = eventSink
    }

    fun sendMessage(
        userId: UUID,
        data: Any
    ) {
        val dataStr = mapper.writeValueAsString(data)

        Log.debug("Sending SSE event: $dataStr")

        val userIdStr = userId.toString()

        // Check if a subscription already exists with this ID
        val existingSubscription = subscriptions[userIdStr] ?: return

        if (existingSubscription.isClosed) {
            // If the connection is already closed, delete the ID of the subscriptions
            subscriptions.remove(userIdStr)

            Log.debug("SSE connection closed for user: $userIdStr")
            return
        }

        try {
            existingSubscription.send(
                sse.newEventBuilder()
                    .name(EVENT_NAME)
                    .data(dataStr)
                    .build()
            )
        } catch (e: Exception) {
            // In case of error when sending, unsubscribe and report the problem
            subscriptions.remove(userIdStr)

            existingSubscription.close()

            Log.info("Error sending SSE event: $e")
        }
    }
}