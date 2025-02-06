package org.balhom.currencyprofilesapi.modules.currencyprofiles.application

import jakarta.enterprise.context.ApplicationScoped
import org.balhom.currencyprofilesapi.common.data.props.ObjectIdUserProps
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.exceptions.CurrencyProfileNotFoundException
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.CurrencyProfile
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.producers.CurrencyProfileEventProducer
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.props.UpdateCurrencyProfileProps
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.repositories.CurrencyProfileRepository
import java.util.UUID

@ApplicationScoped
class CurrencyProfileService(
    private val currencyProfileRepository: CurrencyProfileRepository,
    private val currencyProfileEventProducer: CurrencyProfileEventProducer,
) {

    fun getAllCurrencyProfiles(userId: UUID): List<CurrencyProfile> = currencyProfileRepository
        .findAllByUserId(userId)

    fun getCurrencyProfile(props: ObjectIdUserProps): CurrencyProfile = currencyProfileRepository
        .findByIdAndUserId(
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


    fun deleteCurrencyProfile(props: ObjectIdUserProps) {
        val currencyProfile = getCurrencyProfile(props)

        currencyProfileEventProducer
            .sendDeleteEvent(
                currencyProfile
            )

        currencyProfileRepository
            .delete(currencyProfile)
    }
}
