package org.balhom.currencyprofilesapi.modules.currencyprofiles.application

import jakarta.enterprise.context.ApplicationScoped
import org.balhom.currencyprofilesapi.common.clients.storage.ObjectStorageClient
import org.balhom.currencyprofilesapi.common.data.models.FileData
import org.balhom.currencyprofilesapi.common.data.models.FileReferenceData
import org.balhom.currencyprofilesapi.common.data.props.ObjectIdUserProps
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.exceptions.CurrencyProfileNotFoundException
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.CurrencyProfile
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.producers.CurrencyProfileEventProducer
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.props.UpdateCurrencyProfileProps
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.props.UploadCurrencyProfileImageProps
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.repositories.CurrencyProfileRepository
import java.util.UUID


@ApplicationScoped
class CurrencyProfileService(
    private val currencyProfileRepository: CurrencyProfileRepository,
    private val currencyProfileEventProducer: CurrencyProfileEventProducer,
    private val objectStorageClient: ObjectStorageClient,
) {
    companion object {
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

        currencyProfileEventProducer
            .sendCreateEvent(
                currencyProfile
            )

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
            .update(currencyProfile)
    }

    fun updateCurrencyProfile(props: UpdateCurrencyProfileProps): CurrencyProfile {
        val currencyProfile = getCurrencyProfile(
            ObjectIdUserProps(
                props.id,
                props.userId,
            )
        )

        currencyProfile.update(props)


        currencyProfileEventProducer
            .sendUpdateEvent(
                currencyProfile
            )

        return currencyProfileRepository
            .update(currencyProfile)
    }

    fun updateCurrencyProfile(currencyProfile: CurrencyProfile): CurrencyProfile {
        return currencyProfileRepository
            .update(currencyProfile)
    }


    fun deleteCurrencyProfile(props: ObjectIdUserProps) {
        val currencyProfile = currencyProfileRepository
            .findByIdAndUserId(
                props.id,
                props.userId
            ) ?: throw CurrencyProfileNotFoundException()

        currencyProfileEventProducer
            .sendDeleteEvent(
                currencyProfile
            )

        currencyProfileRepository
            .delete(currencyProfile)
    }
}
