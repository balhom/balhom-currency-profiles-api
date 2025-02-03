package org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.repositories

import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.CurrencyProfile
import java.util.UUID

interface CurrencyProfileRepository {
    fun findByIdAndUserId(id: UUID, userId: UUID): CurrencyProfile?

    fun findAllByUserId(userId: UUID): List<CurrencyProfile>

    fun save(currencyProfile: CurrencyProfile): CurrencyProfile

    fun update(currencyProfile: CurrencyProfile): CurrencyProfile
}
