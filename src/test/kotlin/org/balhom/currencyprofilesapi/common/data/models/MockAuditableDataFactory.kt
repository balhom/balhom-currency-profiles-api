package org.balhom.currencyprofilesapi.common.data.models

import java.time.LocalDateTime
import java.util.UUID
import kotlin.random.Random

class MockAuditableDataFactory {
    companion object {
        fun create(): AuditableData {
            return AuditableData(
                createdAt = LocalDateTime
                    .now()
                    .minusDays(
                        Random.nextLong(0, 100)
                    ),
                createdBy = UUID
                    .randomUUID()
                    .toString(),
                updatedAt = null,
                updatedBy = null
            )
        }
    }
}