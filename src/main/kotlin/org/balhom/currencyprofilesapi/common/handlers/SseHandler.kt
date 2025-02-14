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
        // Check if a subscription already exists with this ID
        val existingSubscription = subscriptions[userId.toString()]
        if (existingSubscription != null && ! existingSubscription.isClosed) {
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
        Log.debug("Sending SSE event: $data")

        val userIdStr = userId.toString()

        // Check if a subscription already exists with this ID
        val existingSubscription = subscriptions[userIdStr] ?: return

        if (! existingSubscription.isClosed) {
            // If the connection is already closed, delete the ID of the subscriptions
            subscriptions.remove(userIdStr)
            return
        }

        try {
            existingSubscription.send(
                sse.newEventBuilder()
                    .name(EVENT_NAME)
                    .data(data)
                    .build()
            )
        } catch (e: Exception) {
            // In case of error when sending, unsubscribe and report the problem
            subscriptions.remove(userIdStr)

            Log.debug("Error sending SSE event: $e")
        }
    }
}