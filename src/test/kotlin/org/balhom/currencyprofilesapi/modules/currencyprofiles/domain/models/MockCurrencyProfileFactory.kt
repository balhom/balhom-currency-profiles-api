package org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models

import org.balhom.currencyprofilesapi.common.data.enums.CurrencyCodeEnum
import org.balhom.currencyprofilesapi.common.data.models.MockAuditableDataFactory
import org.balhom.currencyprofilesapi.common.data.models.MockFileReferenceDataFactory
import java.time.LocalDate
import java.util.UUID
import kotlin.random.Random

class MockCurrencyProfileFactory {
    companion object {
        fun create(): CurrencyProfile {
            val currencyProfileId = UUID.randomUUID()

            return CurrencyProfile(
                id = currencyProfileId,
                userId = UUID.randomUUID(),
                name = "CurrencyProfile_${Random.nextInt(1000, 9999)}",
                currencyCode = CurrencyCodeEnum
                    .entries
                    .toTypedArray()
                    .random(),
                balance = Random
                    .nextDouble(0.0, 10000.0),
                initDate = LocalDate
                    .now()
                    .minusDays(
                        Random.nextLong(0, 365)
                    ),
                goalMonthlySaving = Random
                    .nextDouble(0.0, 1000.0),
                goalYearlySaving = Random
                    .nextDouble(0.0, 12000.0),
                imageData = if (Random.nextBoolean())
                    MockFileReferenceDataFactory.create(
                        currencyProfileId
                    ) else null,
                auditableData = MockAuditableDataFactory
                    .create()
            )
        }
    }
}