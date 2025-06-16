package org.balhom.currencyprofilesapi.modules.transactionchanges.infrastructure.consumers

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import org.balhom.currencyprofilesapi.modules.transactionchanges.application.TransactionChangesService
import org.balhom.currencyprofilesapi.modules.transactionchanges.infrastructure.consumers.data.TransactionChangeEvent
import org.eclipse.microprofile.reactive.messaging.Incoming

@ApplicationScoped
class TransactionChangeEventConsumer(
    private val transactionChangesService: TransactionChangesService
) {

    @Incoming("transaction-events")
    fun receive(event: TransactionChangeEvent) {
        Log.info("Consuming Kafka transaction change event: " + event.id)

        transactionChangesService
            .processChange(
                event.toChangeProps()
            )
    }
}