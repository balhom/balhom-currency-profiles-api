package org.balhom.currencyprofilesapi.common.data.models

import org.balhom.currencyprofilesapi.modules.currencyprofiles.application.CurrencyProfileService.Companion.CURRENCY_PROFILE_PATH_PREFIX
import java.util.UUID

class MockFileReferenceDataFactory {
    companion object {
        fun create(currencyProfileId: UUID): FileReferenceData {
            return FileReferenceData(
                CURRENCY_PROFILE_PATH_PREFIX
                        + "/" + currencyProfileId + "/"
                        + UUID.randomUUID().toString()
            )
        }
    }
}