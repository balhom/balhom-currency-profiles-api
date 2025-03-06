package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.producers

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.CurrencyProfile
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.producers.CurrencyProfileChangeEventProducer
import org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.producers.data.CurrencyProfileChangeEvent
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter

@ApplicationScoped
class CurrencyProfileChangeEventProducerImpl(
    @Channel("currency-profile-events-out") private val emitter: Emitter<CurrencyProfileChangeEvent>
) : CurrencyProfileChangeEventProducer {
    fun send(event: CurrencyProfileChangeEvent) {
        Log.debug("Producing Kafka currency profile event: " + event.id)

        emitter
            .send(event)
            .toCompletableFuture()
            .get()
    }

    override fun sendCreateEvent(currencyProfile: CurrencyProfile) {
        send(
            CurrencyProfileChangeEvent(
                "C",
                currencyProfile.id,
                currencyProfile.balance,
                currencyProfile.goalMonthlySaving,
                currencyProfile.goalYearlySaving,
                currencyProfile.userId,
                currencyProfile.sharedUsers,
            )
        )
    }

    override fun sendUpdateEvent(currencyProfile: CurrencyProfile) {
        send(
            CurrencyProfileChangeEvent(
                "U",
                currencyProfile.id,
                currencyProfile.balance,
                currencyProfile.goalMonthlySaving,
                currencyProfile.goalYearlySaving,
                currencyProfile.userId,
                currencyProfile.sharedUsers,
            )
        )
    }

    override fun sendDeleteEvent(currencyProfile: CurrencyProfile) {
        send(
            CurrencyProfileChangeEvent(
                "D",
                currencyProfile.id,
                currencyProfile.balance,
                currencyProfile.goalMonthlySaving,
                currencyProfile.goalYearlySaving,
                currencyProfile.userId,
                currencyProfile.sharedUsers,
            )
        )
    }
}