package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.persistence.mongo.data

import io.quarkus.mongodb.panache.common.MongoEntity
import org.balhom.currencyprofilesapi.common.clients.ObjectClient
import org.balhom.currencyprofilesapi.common.data.enums.CurrencyCodeEnum
import org.balhom.currencyprofilesapi.common.data.models.AuditableData
import org.balhom.currencyprofilesapi.common.data.models.FileReferenceData
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.CurrencyProfile
import org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.persistence.mongo.CurrencyProfileMongoRepository
import java.time.LocalDate
import java.time.LocalDateTime
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
    var imageData: FileReferenceData?,
    var auditableData: AuditableData,
) {
    private fun updateImageData(objectClient: ObjectClient) {
        imageData !!.url = objectClient.getObjectUrl(
            imageData !!.filePath,
            IMAGE_EXPIRATION_MINUTES
        )
        imageData !!.expiration = LocalDateTime.now()
            .plusMinutes(IMAGE_EXPIRATION_MINUTES)
    }

    fun toDomain(
        currencyProfileMongoRepository: CurrencyProfileMongoRepository,
        objectClient: ObjectClient,
    ): CurrencyProfile {
        if (imageData?.filePath != null) {
            if (! objectClient.doesObjectExist(imageData !!.filePath)) {
                imageData = null
            } else if (
                (imageData?.url == null)
                || (
                        imageData?.expiration != null
                                && imageData !!.expiration !!.isBefore(LocalDateTime.now()))
            ) {
                updateImageData(objectClient)
                currencyProfileMongoRepository.update(this)
            }
        }

        return CurrencyProfile(
            id,
            userId,
            name,
            currencyCode,
            balance,
            initDate,
            goalMonthlySaving,
            goalYearlySaving,
            imageData,
            auditableData,
        )
    }

    companion object {
        const val COLLECTION_NAME = "currencyProfiles"

        const val USER_ID_FIELD_NAME = "userId"

        const val IMAGE_EXPIRATION_MINUTES = 600L

        fun fromDomain(domain: CurrencyProfile): CurrencyProfileMongoEntity = CurrencyProfileMongoEntity(
            domain.id,
            domain.userId,
            domain.name,
            domain.currencyCode,
            domain.balance,
            domain.initDate,
            domain.goalMonthlySaving,
            domain.goalYearlySaving,
            domain.imageData,
            domain.auditableData,
        )
    }
}