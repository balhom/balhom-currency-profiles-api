package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.producers.data

import io.quarkus.runtime.annotations.RegisterForReflection
import java.util.*

@RegisterForReflection
data class CurrencyProfileSharedUserChangeEvent(
    var id: UUID,
    var email: String,
)
