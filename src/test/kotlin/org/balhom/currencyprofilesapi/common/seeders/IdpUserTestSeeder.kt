package org.balhom.currencyprofilesapi.common.seeders

import io.quarkus.logging.Log
import org.balhom.currencyprofilesapi.common.data.models.IdpUser
import org.balhom.currencyprofilesapi.common.utils.TestDataUtils.Companion.randomEmail
import org.balhom.currencyprofilesapi.common.utils.TestDataUtils.Companion.randomText
import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import java.util.UUID

class IdpUserTestSeeder {
    companion object {
        val idpUsers: MutableList<IdpUser> = ArrayList()

        private val testPassword: String = randomText()

        fun insertData(keycloakUrl: String) {
            Log.info("Creating IdpUser Test Data...")

            for (i in 0..2) {
                val userRepresentation = insertKeycloakProfile(
                    randomEmail(),
                    keycloakUrl
                )

                val idpUser = IdpUser(
                    UUID.fromString(
                        userRepresentation.id
                    ),
                    userRepresentation.email
                )

                idpUsers.add(idpUser)
            }

            Log.info("IdpUser Test Data created")
        }

        private fun insertKeycloakProfile(email: String, keycloakUrl: String): UserRepresentation {
            val userRepresentation: UserRepresentation = createUserRepresentation(
                email
            )

            val realm = "balhom-realm"
            val clientId = "balhom-api-client"
            val clientSecret = "secretKey"

            val keycloak = KeycloakBuilder
                .builder()
                .serverUrl(keycloakUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build()

            val usersResource = keycloak
                .realm(realm)
                .users()

            usersResource.create(userRepresentation)

            return usersResource
                .search(email)
                .first()
        }

        private fun createUserRepresentation(
            email: String,
        ): UserRepresentation {
            val credentialRepresentation = CredentialRepresentation()
            credentialRepresentation.isTemporary = false
            credentialRepresentation.type = CredentialRepresentation.PASSWORD
            credentialRepresentation.value = testPassword

            val userRepresentation = UserRepresentation()
            userRepresentation.email = email
            userRepresentation.username = email
            userRepresentation.firstName = ""
            userRepresentation.lastName = ""
            userRepresentation.isEnabled = true
            userRepresentation.isEmailVerified = true
            userRepresentation.credentials = listOf(credentialRepresentation)

            return userRepresentation
        }
    }
}
