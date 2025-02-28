package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.persistence.mongo.data

import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.CurrencyProfileSharedUser
import java.util.UUID

data class CurrencyProfileSharedUserMongoEntity(
    var id: UUID?,
    var email: String?,
) {
    constructor() : this(null, null)

    fun toDomain(): CurrencyProfileSharedUser {
        return CurrencyProfileSharedUser(
            id !!,
            email !!
        )
    }

    companion object {
        fun fromDomain(domain: CurrencyProfileSharedUser): CurrencyProfileSharedUserMongoEntity =
            CurrencyProfileSharedUserMongoEntity(
                domain.id,
                domain.email,
            )
    }
}
