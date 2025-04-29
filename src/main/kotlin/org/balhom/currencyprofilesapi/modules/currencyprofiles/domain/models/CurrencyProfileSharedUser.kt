package org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models

import io.quarkus.runtime.annotations.RegisterForReflection
import java.util.UUID

@RegisterForReflection
data class CurrencyProfileSharedUser(
    var id: UUID,
    var email: String,
)
