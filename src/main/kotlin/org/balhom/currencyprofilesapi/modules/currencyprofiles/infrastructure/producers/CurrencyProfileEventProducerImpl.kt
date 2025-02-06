package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.producers

import jakarta.enterprise.context.ApplicationScoped
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.CurrencyProfile
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.producers.CurrencyProfileEventProducer
import org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.producers.data.CurrencyProfileEvent
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter

@ApplicationScoped
class CurrencyProfileEventProducerImpl(
    @Channel("currency-profile-events") private val emitter: Emitter<CurrencyProfileEvent>
) : CurrencyProfileEventProducer {
    fun send(event: CurrencyProfileEvent) {
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
                currencyProfile.goalMonthlySaving,
                currencyProfile.goalYearlySaving,
            )
        )
    }

    override fun sendUpdateEvent(currencyProfile: CurrencyProfile) {
        send(
            CurrencyProfileEvent(
                "U",
                currencyProfile.id,
                currencyProfile.goalMonthlySaving,
                currencyProfile.goalYearlySaving,
            )
        )
    }

    override fun sendDeleteEvent(currencyProfile: CurrencyProfile) {
        send(
            CurrencyProfileEvent(
                "D",
                currencyProfile.id,
                currencyProfile.goalMonthlySaving,
                currencyProfile.goalYearlySaving,
            )
        )
    }
}