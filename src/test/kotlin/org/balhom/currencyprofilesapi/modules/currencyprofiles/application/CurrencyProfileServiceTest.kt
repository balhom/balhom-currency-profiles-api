package org.balhom.currencyprofilesapi.modules.currencyprofiles.application

import org.balhom.currencyprofilesapi.common.clients.ObjectClient
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
    private lateinit var objectClient: ObjectClient

    private lateinit var currencyProfileService: CurrencyProfileService

    @BeforeEach
    fun setUp() {
        currencyProfileRepository = mock(
            CurrencyProfileRepository::class.java
        )
        currencyProfileEventProducer = mock(
            CurrencyProfileEventProducer::class.java
        )
        objectClient = mock(
            ObjectClient::class.java
        )

        currencyProfileService = CurrencyProfileService(
            currencyProfileRepository,
            currencyProfileEventProducer,
            objectClient
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
                .findAllByUserId(userId)
        ).thenReturn(expectedProfiles)

        val result = currencyProfileService.getAllCurrencyProfiles(userId)

        assertEquals(expectedProfiles, result)
        verify(currencyProfileRepository)
            .findAllByUserId(userId)
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
                .findByIdAndUserId(
                    props.objectIdUserProps.id,
                    props.objectIdUserProps.userId
                )
        ).thenReturn(currencyProfile)

        doNothing()
            .`when`(objectClient)
            .uploadObject(capture<FileData>(newPathCaptor))
        doNothing()
            .`when`(objectClient)
            .deleteObject(anyString())

        currencyProfileService
            .uploadCurrencyProfileImage(props)

        verify(objectClient)
            .deleteObject(expectedDeletedPath)
        verify(objectClient)
            .uploadObject(any<FileData>())
        verify(currencyProfileRepository)
            .update(currencyProfile)

        assertNotNull(newPathCaptor.value)
    }

}