package org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.repositories

import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.CurrencyProfile
import java.util.*

interface CurrencyProfileRepository {
    fun findByIdAndUserId(id: UUID, userId: UUID): CurrencyProfile?

    fun findByIdAndUserIdOrSharedUserId(id: UUID, userId: UUID): CurrencyProfile?

    fun findAllByUserIdOrSharedUserId(userId: UUID): List<CurrencyProfile>

    fun countByUserIdOrSharedUserId(userId: UUID): Long

    fun save(currencyProfile: CurrencyProfile): CurrencyProfile

    fun delete(currencyProfile: CurrencyProfile)
}
