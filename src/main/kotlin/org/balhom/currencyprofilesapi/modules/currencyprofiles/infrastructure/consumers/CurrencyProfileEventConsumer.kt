package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.consumers

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import org.balhom.currencyprofilesapi.common.handlers.SseHandler
import org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.producers.data.CurrencyProfileEvent
import org.eclipse.microprofile.reactive.messaging.Incoming

@ApplicationScoped
class CurrencyProfileEventConsumer(
    private val sseHandler: SseHandler
) {
    @Incoming("currency-profile-events-in")
    fun receive(event: CurrencyProfileEvent) {
        Log.debug("Consuming Kafka currency profile event: " + event.id)

        sseHandler.sendMessage(
            event.ownerId,
            event
        )

        event.sharedUsers.forEach {
            sseHandler.sendMessage(
                it.id,
                event
            )
        }
    }
}
