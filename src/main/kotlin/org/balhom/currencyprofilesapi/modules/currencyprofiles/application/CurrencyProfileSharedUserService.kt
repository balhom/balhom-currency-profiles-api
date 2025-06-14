package org.balhom.currencyprofilesapi.modules.currencyprofiles.application

import jakarta.enterprise.context.ApplicationScoped
import org.balhom.currencyprofilesapi.common.clients.idp.IdpAdminClient
import org.balhom.currencyprofilesapi.common.data.models.IdpUser
import org.balhom.currencyprofilesapi.common.data.props.ObjectIdUserProps
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.exceptions.CurrencyProfileUserNotFoundException
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.exceptions.OperationNotAllowedForCurrencyProfileException
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.CurrencyProfileSharedUser
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.props.AddCurrencyProfileSharedUserProps
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.props.RemoveCurrencyProfileSharedUserProps
import java.util.*


@ApplicationScoped
class CurrencyProfileSharedUserService(
    private val currencyProfileService: CurrencyProfileService,
    private val idpAdminClient: IdpAdminClient
) {


    fun getAllCurrencyProfileSharedUsers(
        props: ObjectIdUserProps
    ): List<CurrencyProfileSharedUser> {
        val currencyProfile = currencyProfileService
            .getCurrencyProfile(props)

        if (props.userId != currencyProfile.userId) {
            return emptyList()
        }

        return currencyProfile
            .sharedUsers
    }

    fun addCurrencyProfileSharedUser(props: AddCurrencyProfileSharedUserProps) {
        val idpUser = getIdpUserEmail(
            props.userEmail
        )

        // If user to add is currency profile owner then nothing has to be done
        if (props.authUserId == idpUser.id) {
            return
        }

        val currencyProfile = currencyProfileService.getCurrencyProfile(
            ObjectIdUserProps(
                props.currencyProfileId,
                props.authUserId,
            )
        )

        // Shared user add action not allowed for non owners
        if (props.authUserId != currencyProfile.userId) {
            throw OperationNotAllowedForCurrencyProfileException()
        }

        // Add idp user id as shared user if it is not already in it
        if (!currencyProfile.sharedUsers.any { it.id == idpUser.id }) {
            currencyProfile
                .sharedUsers
                .add(
                    CurrencyProfileSharedUser(
                        idpUser.id,
                        idpUser.email
                    )
                )
        }

        currencyProfile.validate()

        currencyProfileService.internalUpdateCurrencyProfile(
            currencyProfile
        )
    }

    fun removeCurrencyProfileSharedUser(props: RemoveCurrencyProfileSharedUserProps) {
        val idpUser = getIdpUserId(
            props.userId
        )

        // If user to remove is currency profile owner then nothing has to be done
        if (props.authUserId == idpUser.id) {
            return
        }

        val currencyProfile = currencyProfileService.getCurrencyProfile(
            ObjectIdUserProps(
                props.currencyProfileId,
                props.authUserId,
            )
        )

        // Shared user add action not allowed for non owners
        if (props.authUserId != currencyProfile.userId) {
            throw OperationNotAllowedForCurrencyProfileException()
        }

        // Add idp user id as shared user if it is not already in it
        if (currencyProfile.sharedUsers.any { it.id == idpUser.id }) {
            currencyProfile
                .sharedUsers
                .removeIf({ it.id == idpUser.id })

            currencyProfileService.internalUpdateCurrencyProfile(
                currencyProfile
            )
        }
    }

    private fun getIdpUserEmail(userEmail: String): IdpUser {
        return idpAdminClient
            .getUserByEmail(userEmail)
            ?: throw CurrencyProfileUserNotFoundException()
    }

    private fun getIdpUserId(userId: UUID): IdpUser {
        return idpAdminClient
            .getUserById(userId)
            ?: throw CurrencyProfileUserNotFoundException()
    }
}
