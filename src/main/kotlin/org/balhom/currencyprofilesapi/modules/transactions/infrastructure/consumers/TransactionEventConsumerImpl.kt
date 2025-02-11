package org.balhom.currencyprofilesapi.modules.transactions.infrastructure.consumers

import jakarta.enterprise.context.ApplicationScoped
import org.balhom.currencyprofilesapi.modules.transactions.application.TransactionChangesService
import org.balhom.currencyprofilesapi.modules.transactions.infrastructure.consumers.data.TransactionEvent
import org.eclipse.microprofile.reactive.messaging.Incoming

@ApplicationScoped
class TransactionEventConsumerImpl(
    private val transactionChangesService: TransactionChangesService
) {

    @Incoming("transaction-events")
    fun receive(event: TransactionEvent) {
        transactionChangesService
            .processChange(
                event.toChangeProps()
            )
    }
}