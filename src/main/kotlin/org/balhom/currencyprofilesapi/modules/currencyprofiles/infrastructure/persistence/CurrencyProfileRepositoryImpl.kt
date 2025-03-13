package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.persistence

import com.mongodb.client.model.Filters
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
                Filters.and(
                    Filters.eq("_id", id),
                    Filters.eq(
                        CurrencyProfileMongoEntity.USER_ID_FIELD_NAME,
                        userId
                    ),
                )
            )
            .firstResult()
            ?.toDomain(currencyProfileMongoRepository, objectStorageClient)
    }

    override fun findByIdAndUserIdOrSharedUserId(id: UUID, userId: UUID): CurrencyProfile? {
        return currencyProfileMongoRepository
            .find(
                Filters.and(
                    Filters.eq("_id", id),
                    Filters.or(
                        Filters.eq(
                            CurrencyProfileMongoEntity.USER_ID_FIELD_NAME,
                            userId
                        ),
                        Filters.`in`(
                            CurrencyProfileMongoEntity.SHARED_USERS_FIELD_NAME,
                            userId
                        )
                    )
                )
            )
            .firstResult()
            ?.toDomain(currencyProfileMongoRepository, objectStorageClient)
    }

    override fun findAllByUserIdOrSharedUserId(
        userId: UUID
    ): List<CurrencyProfile> {
        return currencyProfileMongoRepository
            .find(
                Filters.or(
                    Filters.eq(
                        CurrencyProfileMongoEntity.USER_ID_FIELD_NAME,
                        userId
                    ),
                    Filters.`in`(
                        CurrencyProfileMongoEntity.SHARED_USERS_FIELD_NAME,
                        userId
                    )
                )
            )
            .list()
            .map { it.toDomain(currencyProfileMongoRepository, objectStorageClient) }
            .toList()
    }

    override fun countByUserIdOrSharedUserId(
        userId: UUID
    ): Long {
        return currencyProfileMongoRepository
            .count(
                Filters.or(
                    Filters.eq(
                        CurrencyProfileMongoEntity.USER_ID_FIELD_NAME,
                        userId
                    ),
                    Filters.`in`(
                        CurrencyProfileMongoEntity.SHARED_USERS_FIELD_NAME,
                        userId
                    )
                )
            )
    }

    override fun save(currencyProfile: CurrencyProfile): CurrencyProfile {
        val entity = CurrencyProfileMongoEntity
            .fromDomain(currencyProfile)

        currencyProfileMongoRepository
            .persistOrUpdate(entity)

        return entity.toDomain(currencyProfileMongoRepository, objectStorageClient)
    }

    override fun delete(currencyProfile: CurrencyProfile) {
        currencyProfileMongoRepository
            .delete(
                Filters.and(
                    Filters.eq("_id", currencyProfile.id),
                    Filters.eq(
                        CurrencyProfileMongoEntity.USER_ID_FIELD_NAME,
                        currencyProfile.userId
                    ),
                )
            )
    }
}