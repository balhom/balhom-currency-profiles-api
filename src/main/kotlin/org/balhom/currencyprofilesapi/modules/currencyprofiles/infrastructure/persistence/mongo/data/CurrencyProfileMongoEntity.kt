package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.persistence.mongo.data

import io.quarkus.mongodb.panache.common.MongoEntity
import org.balhom.currencyprofilesapi.common.data.enums.CurrencyCodeEnum
import org.balhom.currencyprofilesapi.common.data.models.AuditableData
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.CurrencyProfile
import java.time.LocalDate
import java.util.UUID

@MongoEntity(collection = CurrencyProfileMongoEntity.COLLECTION_NAME)
data class CurrencyProfileMongoEntity(
    var id: UUID,
    var userId: UUID,
    var name: String,
    var currencyCode: CurrencyCodeEnum,
    var balance: Double,
    var initDate: LocalDate,
    var goalMonthlySaving: Double,
    var goalYearlySaving: Double,
    var auditableData: AuditableData,
) {
    fun toDomain(): CurrencyProfile {
        return CurrencyProfile(
            id,
            userId,
            name,
            currencyCode,
            balance,
            initDate,
            goalMonthlySaving,
            goalYearlySaving,
            auditableData,
        )
    }

    companion object {
        const val COLLECTION_NAME = "currencyProfiles"

        const val USER_ID_FIELD_NAME = "userId"

        fun fromDomain(domain: CurrencyProfile): CurrencyProfileMongoEntity = CurrencyProfileMongoEntity(
            domain.id,
            domain.userId,
            domain.name,
            domain.currencyCode,
            domain.balance,
            domain.initDate,
            domain.goalMonthlySaving,
            domain.goalYearlySaving,
            domain.auditableData,
        )
    }
}