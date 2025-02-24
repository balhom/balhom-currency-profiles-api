package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.persistence

import jakarta.enterprise.context.ApplicationScoped
import org.balhom.currencyprofilesapi.common.clients.ObjectClient
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.CurrencyProfile
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.repositories.CurrencyProfileRepository
import org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.persistence.mongo.CurrencyProfileMongoRepository
import org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.persistence.mongo.data.CurrencyProfileMongoEntity
import java.util.UUID


@ApplicationScoped
class CurrencyProfileRepositoryImpl(
    private val currencyProfileMongoRepository: CurrencyProfileMongoRepository,
    private val objectClient: ObjectClient,
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
            ?.toDomain(currencyProfileMongoRepository, objectClient)
    }

    override fun findAllByUserId(
        userId: UUID
    ): List<CurrencyProfile> {
        return currencyProfileMongoRepository
            .find(
                CurrencyProfileMongoEntity.USER_ID_FIELD_NAME,
                userId
            )
            .list()
            .map { it.toDomain(currencyProfileMongoRepository, objectClient) }
            .toList()
    }

    override fun save(currencyProfile: CurrencyProfile): CurrencyProfile {
        val entity = CurrencyProfileMongoEntity
            .fromDomain(currencyProfile)

        currencyProfileMongoRepository
            .persist(entity)

        return entity.toDomain(currencyProfileMongoRepository, objectClient)
    }

    override fun update(currencyProfile: CurrencyProfile): CurrencyProfile {
        val entity = CurrencyProfileMongoEntity.fromDomain(currencyProfile)

        currencyProfileMongoRepository
            .update(entity)

        return entity.toDomain(currencyProfileMongoRepository, objectClient)
    }

    override fun delete(currencyProfile: CurrencyProfile) {
        currencyProfileMongoRepository
            .find(
                "_id = ?1 and "
                        + CurrencyProfileMongoEntity.USER_ID_FIELD_NAME
                        + " = ?2",
                currencyProfile.id,
                currencyProfile.userId
            )
    }
}