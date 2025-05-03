package org.balhom.currencyprofilesapi.common.data.models

import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.CurrencyProfile
import java.util.*

class MockFileReferenceDataFactory {
    companion object {
        fun create(currencyProfileId: UUID): FileReferenceData {
            return FileReferenceData(
                CurrencyProfile.IMAGE_PATH_PREFIX
                        + "/" + currencyProfileId + "/"
                        + UUID.randomUUID().toString()
            )
        }
    }
}