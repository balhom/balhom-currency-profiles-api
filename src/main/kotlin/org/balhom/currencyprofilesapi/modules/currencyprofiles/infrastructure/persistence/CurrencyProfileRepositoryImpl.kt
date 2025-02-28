package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.persistence

import jakarta.enterprise.context.ApplicationScoped
import org.balhom.currencyprofilesapi.common.clients.storage.ObjectStorageClient
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.CurrencyProfile
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.repositories.CurrencyProfileRepository
import org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.persistence.mongo.CurrencyProfileMongoRepository
import org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.persistence.mongo.data.CurrencyProfileMongoEntity
import java.util.UUID


@ApplicationScoped
class CurrencyProfileRepositoryImpl(
    private val currencyProfileMongoRepository: CurrencyProfileMongoRepository,
    private val objectStorageClient: ObjectStorageClient,
) : CurrencyProfileRepository {

    override fun findByIdAndUserId(id: UUID, userId: UUID): CurrencyProfile? {
        return currencyProfileMongoRepository
            .find(
                "_id = ?1 and "
                        + CurrencyProfileMongoEntity.USER_ID_FIELD_NAME
                        + " = ?2",
                id,
                userId
            )
            .firstResult()
            ?.toDomain(currencyProfileMongoRepository, objectStorageClient)
    }

    override fun findByIdAndUserIdOrSharedUserId(id: UUID, userId: UUID): CurrencyProfile? {
        return currencyProfileMongoRepository
            .find(
                """{
                "_id": ?1,
                "${'$'}or": [
                    { "${CurrencyProfileMongoEntity.USER_ID_FIELD_NAME}": ?2 },
                    { "sharedUsers": { "${'$'}in": [?2] } }
                ]
            }""",
                id,
                userId
            )
            .firstResult()
            ?.toDomain(currencyProfileMongoRepository, objectStorageClient)
    }

    override fun findAllByUserIdOrSharedUserId(
        userId: UUID
    ): List<CurrencyProfile> {
        return currencyProfileMongoRepository
            .find(
                """{
                "${'$'}or": [
                    { "${CurrencyProfileMongoEntity.USER_ID_FIELD_NAME}": ?1 },
                    { "sharedUsers": { "${'$'}in": [?1] } }
                ]
            }""",
                userId
            )
            .list()
            .map { it.toDomain(currencyProfileMongoRepository, objectStorageClient) }
            .toList()
    }

    override fun save(currencyProfile: CurrencyProfile): CurrencyProfile {
        val entity = CurrencyProfileMongoEntity
            .fromDomain(currencyProfile)

        currencyProfileMongoRepository
            .persist(entity)

        return entity.toDomain(currencyProfileMongoRepository, objectStorageClient)
    }

    override fun update(currencyProfile: CurrencyProfile): CurrencyProfile {
        val entity = CurrencyProfileMongoEntity.fromDomain(currencyProfile)

        currencyProfileMongoRepository
            .update(entity)

        return entity.toDomain(currencyProfileMongoRepository, objectStorageClient)
    }

    override fun delete(currencyProfile: CurrencyProfile) {
        currencyProfileMongoRepository
            .delete(
                "_id = ?1 and "
                        + CurrencyProfileMongoEntity.USER_ID_FIELD_NAME
                        + " = ?2",
                currencyProfile.id,
                currencyProfile.userId
            )
    }
}