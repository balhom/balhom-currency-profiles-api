package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.persistence.mongo.data

import io.quarkus.mongodb.panache.common.MongoEntity
import org.balhom.currencyprofilesapi.common.clients.storage.ObjectStorageClient
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
    var sharedUsers: MutableList<CurrencyProfileSharedUserMongoEntity>,
    var auditableData: AuditableData,
) {
    private fun updateImageData(objectStorageClient: ObjectStorageClient) {
        imageData !!.url = objectStorageClient.getObjectUrl(
            imageData !!.filePath,
            IMAGE_EXPIRATION_MINUTES
        )
        imageData !!.expiration = LocalDateTime.now()
            .plusMinutes(IMAGE_EXPIRATION_MINUTES)
    }

    fun toDomain(
        currencyProfileMongoRepository: CurrencyProfileMongoRepository,
        objectStorageClient: ObjectStorageClient,
    ): CurrencyProfile {
        if (imageData?.filePath != null) {
            if (! objectStorageClient.doesObjectExist(imageData !!.filePath)) {
                imageData = null
            } else if (
                (imageData?.url == null)
                || (
                        imageData?.expiration != null
                                && imageData !!.expiration !!.isBefore(LocalDateTime.now()))
            ) {
                updateImageData(objectStorageClient)
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
            sharedUsers.map { it.toDomain() }.toMutableList(),
            auditableData,
        )
    }

    companion object {
        const val COLLECTION_NAME = "currencyProfiles"

        const val USER_ID_FIELD_NAME = "userId"
        const val SHARED_USERS_FIELD_NAME = "sharedUsers"

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
            domain.sharedUsers.map {
                CurrencyProfileSharedUserMongoEntity.fromDomain(
                    it
                )
            }.toMutableList(),
            domain.auditableData,
        )
    }
}