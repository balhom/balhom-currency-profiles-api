package org.balhom.currencyprofilesapi.modules.currencyprofiles.application

import jakarta.enterprise.context.ApplicationScoped
import org.balhom.currencyprofilesapi.common.clients.idp.IdpAdminClient
import org.balhom.currencyprofilesapi.common.clients.storage.ObjectStorageClient
import org.balhom.currencyprofilesapi.common.data.models.FileData
import org.balhom.currencyprofilesapi.common.data.models.FileReferenceData
import org.balhom.currencyprofilesapi.common.data.props.ObjectIdUserProps
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.exceptions.CurrencyProfileNotFoundException
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.exceptions.CurrencyProfilesExceededException
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.CurrencyProfile
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.producers.CurrencyProfileChangeEventProducer
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.props.UpdateCurrencyProfileProps
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.props.UploadCurrencyProfileImageProps
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.repositories.CurrencyProfileRepository
import java.util.UUID


@ApplicationScoped
class CurrencyProfileService(
    private val currencyProfileRepository: CurrencyProfileRepository,
    private val currencyProfileChangeEventProducer: CurrencyProfileChangeEventProducer,
    private val objectStorageClient: ObjectStorageClient,
    private val idpAdminClient: IdpAdminClient,
) {
    companion object {
        const val MAX_ALLOWED_PER_USER = 10

        const val CURRENCY_PROFILE_PATH_PREFIX = "currency-profiles"
    }

    fun getAllCurrencyProfiles(userId: UUID): List<CurrencyProfile> = currencyProfileRepository
        .findAllByUserIdOrSharedUserId(userId)

    fun getCurrencyProfile(props: ObjectIdUserProps): CurrencyProfile = currencyProfileRepository
        .findByIdAndUserIdOrSharedUserId(
            props.id,
            props.userId
        ) ?: throw CurrencyProfileNotFoundException()

    fun createCurrencyProfile(currencyProfile: CurrencyProfile): CurrencyProfile {
        currencyProfile.validate()

        // Check max allowed currency profiles per user
        if (
            currencyProfileRepository.countByUserIdOrSharedUserId(
                currencyProfile.userId
            ) >= MAX_ALLOWED_PER_USER
        ) {
            throw CurrencyProfilesExceededException()
        }

        currencyProfileChangeEventProducer
            .sendCreateEvent(currencyProfile)

        return currencyProfileRepository
            .save(currencyProfile)
    }

    fun uploadCurrencyProfileImage(props: UploadCurrencyProfileImageProps) {
        val currencyProfile = getCurrencyProfile(
            props.objectIdUserProps
        )

        if (currencyProfile.imageData?.filePath != null) {
            objectStorageClient.deleteObject(
                currencyProfile.imageData !!.filePath
            )
        }

        val imagePath: String = (
                CURRENCY_PROFILE_PATH_PREFIX
                        + "/" + currencyProfile.userId + "/"
                        + UUID.randomUUID().toString()
                )

        objectStorageClient.uploadObject(
            FileData(
                props.image,
                imagePath,
                props.mimetype,
            )
        )

        currencyProfile.imageData = FileReferenceData(
            imagePath
        )

        currencyProfileRepository
            .save(currencyProfile)
    }

    fun updateCurrencyProfile(props: UpdateCurrencyProfileProps): CurrencyProfile {
        val currencyProfile = getCurrencyProfile(
            ObjectIdUserProps(
                props.id,
                props.userId,
            )
        )

        currencyProfile.update(props)

        currencyProfileChangeEventProducer
            .sendUpdateEvent(currencyProfile)

        return currencyProfileRepository
            .save(currencyProfile)
    }

    fun updateCurrencyProfile(currencyProfile: CurrencyProfile): CurrencyProfile {
        return currencyProfileRepository
            .save(currencyProfile)
    }

    fun deleteCurrencyProfile(props: ObjectIdUserProps) {
        val currencyProfile = currencyProfileRepository
            .findByIdAndUserId(
                props.id,
                props.userId
            ) ?: throw CurrencyProfileNotFoundException()

        handleCurrencyProfileDelete(currencyProfile)
    }

    private fun handleCurrencyProfileDelete(currencyProfile: CurrencyProfile) {
        currencyProfileChangeEventProducer
            .sendDeleteEvent(currencyProfile)

        if (currencyProfile.imageData?.filePath != null) {
            objectStorageClient.deleteObject(
                currencyProfile.imageData !!.filePath
            )
        }

        currencyProfileRepository
            .delete(currencyProfile)
    }

    fun deleteAllCurrencyProfiles(userId: UUID) {
        val currencyProfiles = currencyProfileRepository
            .findAllByUserIdOrSharedUserId(userId)

        for (currencyProfile in currencyProfiles) {
            // If userId is the currency profile owner then profile must be deleted
            if (currencyProfile.userId == userId) {
                handleCurrencyProfileDelete(currencyProfile)
            }
            // If userId is a shared user then its reference must be deleted
            else {
                currencyProfile
                    .sharedUsers
                    .removeIf({ it.id == userId })

                currencyProfileRepository
                    .save(currencyProfile)
            }
        }

        idpAdminClient.deleteUser(userId)
    }
}
