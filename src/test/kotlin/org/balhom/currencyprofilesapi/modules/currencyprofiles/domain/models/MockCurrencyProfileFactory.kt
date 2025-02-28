package org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models

import org.balhom.currencyprofilesapi.common.data.enums.CurrencyCodeEnum
import org.balhom.currencyprofilesapi.common.data.models.MockAuditableDataFactory
import org.balhom.currencyprofilesapi.common.data.models.MockFileReferenceDataFactory
import org.balhom.currencyprofilesapi.common.utils.TestDataUtils.Companion.randomBool
import org.balhom.currencyprofilesapi.common.utils.TestDataUtils.Companion.randomDouble
import org.balhom.currencyprofilesapi.common.utils.TestDataUtils.Companion.randomPastDateTime
import org.balhom.currencyprofilesapi.common.utils.TestDataUtils.Companion.randomText
import java.util.UUID

class MockCurrencyProfileFactory {
    companion object {
        fun create(): CurrencyProfile {
            val currencyProfileId = UUID.randomUUID()

            return CurrencyProfile(
                id = currencyProfileId,
                userId = UUID.randomUUID(),
                name = "CurrencyProfile_${randomText(5)}",
                currencyCode = CurrencyCodeEnum
                    .entries
                    .toTypedArray()
                    .random(),
                balance = randomDouble(0.0, 10000.0),
                initDate = randomPastDateTime().toLocalDate(),
                goalMonthlySaving = randomDouble(0.0, 1000.0),
                goalYearlySaving = randomDouble(0.0, 12000.0),
                imageData = if (randomBool())
                    MockFileReferenceDataFactory.create(
                        currencyProfileId
                    ) else null,
                sharedUsers = ArrayList(),
                auditableData = MockAuditableDataFactory
                    .create()
            )
        }
    }
}