package org.balhom.currencyprofilesapi.modules.currencyprofiles.application

import org.balhom.currencyprofilesapi.common.clients.storage.ObjectStorageClient
import org.balhom.currencyprofilesapi.common.data.models.FileData
import org.balhom.currencyprofilesapi.common.data.models.MockFileReferenceDataFactory
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.MockCurrencyProfileFactory
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.producers.CurrencyProfileEventProducer
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.props.MockUploadCurrencyProfileImagePropsFactory
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.repositories.CurrencyProfileRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.anyString
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.capture
import java.util.UUID

class CurrencyProfileServiceTest {

    private lateinit var currencyProfileRepository: CurrencyProfileRepository
    private lateinit var currencyProfileEventProducer: CurrencyProfileEventProducer
    private lateinit var objectStorageClient: ObjectStorageClient

    private lateinit var currencyProfileService: CurrencyProfileService

    @BeforeEach
    fun setUp() {
        currencyProfileRepository = mock(
            CurrencyProfileRepository::class.java
        )
        currencyProfileEventProducer = mock(
            CurrencyProfileEventProducer::class.java
        )
        objectStorageClient = mock(
            ObjectStorageClient::class.java
        )

        currencyProfileService = CurrencyProfileService(
            currencyProfileRepository,
            currencyProfileEventProducer,
            objectStorageClient
        )
    }

    @Test
    fun `getAllCurrencyProfiles should return list of profiles`() {
        val userId = UUID.randomUUID()
        val expectedProfiles = listOf(
            MockCurrencyProfileFactory.create()
        )
        `when`(
            currencyProfileRepository
                .findAllByUserIdOrSharedUserId(userId)
        ).thenReturn(expectedProfiles)

        val result = currencyProfileService.getAllCurrencyProfiles(userId)

        assertEquals(expectedProfiles, result)
        verify(currencyProfileRepository)
            .findAllByUserIdOrSharedUserId(userId)
    }

    @Test
    fun `uploadCurrencyProfileImage should delete old image and upload new one`() {
        val props = MockUploadCurrencyProfileImagePropsFactory.create()

        val currencyProfile = MockCurrencyProfileFactory.create()
        currencyProfile.imageData = MockFileReferenceDataFactory.create(
            currencyProfile.id
        )
        val expectedDeletedPath = currencyProfile.imageData !!.filePath

        val newPathCaptor = ArgumentCaptor.forClass(
            FileData::class.java
        )

        `when`(
            currencyProfileRepository
                .findByIdAndUserIdOrSharedUserId(
                    props.objectIdUserProps.id,
                    props.objectIdUserProps.userId
                )
        ).thenReturn(currencyProfile)

        doNothing()
            .`when`(objectStorageClient)
            .uploadObject(capture<FileData>(newPathCaptor))
        doNothing()
            .`when`(objectStorageClient)
            .deleteObject(anyString())

        currencyProfileService
            .uploadCurrencyProfileImage(props)

        verify(objectStorageClient)
            .deleteObject(expectedDeletedPath)
        verify(objectStorageClient)
            .uploadObject(any<FileData>())
        verify(currencyProfileRepository)
            .update(currencyProfile)

        assertNotNull(newPathCaptor.value)
    }

}