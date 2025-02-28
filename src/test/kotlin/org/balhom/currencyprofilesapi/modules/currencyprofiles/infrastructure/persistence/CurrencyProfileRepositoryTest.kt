package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.persistence

import com.mongodb.client.model.Filters
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.balhom.currencyprofilesapi.common.clients.storage.ObjectStorageClient
import org.balhom.currencyprofilesapi.common.containers.BaseTestContainersTest
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.MockCurrencyProfileFactory
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.repositories.CurrencyProfileRepository
import org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.persistence.mongo.CurrencyProfileMongoRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test


@QuarkusTest
class CurrencyProfileRepositoryTest : BaseTestContainersTest() {

    @Inject
    lateinit var currencyProfileRepository: CurrencyProfileRepository

    @Inject
    lateinit var objectStorageClient: ObjectStorageClient

    @Inject
    lateinit var currencyProfileMongoRepository: CurrencyProfileMongoRepository

    @Test
    fun `save with valid data should store a Currency Profile`() {
        val expectedCurrencyProfile = MockCurrencyProfileFactory
            .create()

        currencyProfileRepository.save(
            expectedCurrencyProfile
        )

        // Get stored data
        val result = currencyProfileMongoRepository.find(
            Filters.eq(
                "_id",
                expectedCurrencyProfile.id
            )
        )
            .firstResult()
            ?.toDomain(
                currencyProfileMongoRepository,
                objectStorageClient
            )
        result?.auditableData = expectedCurrencyProfile.auditableData

        assertNotNull(result)
        assertEquals(expectedCurrencyProfile, result)
    }
}
