package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.producers

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.CurrencyProfile
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.producers.CurrencyProfileEventProducer
import org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.producers.data.CurrencyProfileEvent
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter

@ApplicationScoped
class CurrencyProfileEventProducerImpl(
    @Channel("currency-profile-events-out") private val emitter: Emitter<CurrencyProfileEvent>
) : CurrencyProfileEventProducer {
    fun send(event: CurrencyProfileEvent) {
        Log.debug("Producing Kafka currency profile event: " + event.id)

        emitter
            .send(event)
            .toCompletableFuture()
            .get()
    }

    override fun sendCreateEvent(currencyProfile: CurrencyProfile) {
        send(
            CurrencyProfileEvent(
                "C",
                currencyProfile.id,
                currencyProfile.balance,
                currencyProfile.goalMonthlySaving,
                currencyProfile.goalYearlySaving,
                currencyProfile.userId,
            )
        )
    }

    override fun sendUpdateEvent(currencyProfile: CurrencyProfile) {
        send(
            CurrencyProfileEvent(
                "U",
                currencyProfile.id,
                currencyProfile.balance,
                currencyProfile.goalMonthlySaving,
                currencyProfile.goalYearlySaving,
                currencyProfile.userId,
            )
        )
    }

    override fun sendDeleteEvent(currencyProfile: CurrencyProfile) {
        send(
            CurrencyProfileEvent(
                "D",
                currencyProfile.id,
                currencyProfile.balance,
                currencyProfile.goalMonthlySaving,
                currencyProfile.goalYearlySaving,
                currencyProfile.userId,
            )
        )
    }
}