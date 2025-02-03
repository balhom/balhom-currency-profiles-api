package org.balhom.currencyprofilesapi.modules.currencyprofiles.application

import jakarta.enterprise.context.ApplicationScoped
import org.balhom.currencyprofilesapi.common.data.props.ObjectIdUserProps
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.exceptions.CurrencyProfileNotFoundException
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.CurrencyProfile
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.props.UpdateCurrencyProfileProps
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.repositories.CurrencyProfileRepository
import java.util.UUID

@ApplicationScoped
class CurrencyProfileService(private val currencyProfileRepository: CurrencyProfileRepository) {

    fun getAllCurrencyProfiles(userId: UUID): List<CurrencyProfile> = currencyProfileRepository
        .findAllByUserId(userId)

    fun getCurrencyProfile(props: ObjectIdUserProps): CurrencyProfile = currencyProfileRepository
        .findByIdAndUserId(
            props.id,
            props.userId
        ) ?: throw CurrencyProfileNotFoundException()

    fun createCurrencyProfile(profile: CurrencyProfile): CurrencyProfile {
        profile.validate()

        return currencyProfileRepository
            .save(profile)
    }

    fun updateCurrencyProfile(props: UpdateCurrencyProfileProps): CurrencyProfile {
        val currencyProfile = getCurrencyProfile(
            ObjectIdUserProps(
                props.id,
                props.userId,
            )
        )

        currencyProfile.update(props)

        return currencyProfileRepository
            .update(currencyProfile);
    }
}
