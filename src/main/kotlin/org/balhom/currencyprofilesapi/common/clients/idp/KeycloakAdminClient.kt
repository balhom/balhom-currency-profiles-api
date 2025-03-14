package org.balhom.currencyprofilesapi.common.clients.idp

import io.quarkus.logging.Log
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.core.Response
import org.balhom.currencyprofilesapi.common.clients.idp.exceptions.CannotDeleteKeycloakUserException
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.jboss.resteasy.reactive.ClientWebApplicationException
import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.admin.client.resource.UsersResource
import org.keycloak.representations.idm.UserRepresentation
import java.util.UUID


@ApplicationScoped
class KeycloakAdminClient(
    @ConfigProperty(name = "keycloak.admin.url") val serverUrl: String,
    @ConfigProperty(name = "keycloak.admin.realm") val realm: String,
    @ConfigProperty(name = "keycloak.admin.client-id") val clientId: String,
    @ConfigProperty(name = "keycloak.admin.client-secret") val clientSecret: String
) {
    private lateinit var keycloak: Keycloak

    @PostConstruct
    fun setup() {
        keycloak = KeycloakBuilder
            .builder()
            .serverUrl(serverUrl)
            .realm(realm)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
            .build()
    }

    private fun getUsersResource(): UsersResource {
        return keycloak
            .realm(realm)
            .users()
    }

    fun getUserById(id: UUID): UserRepresentation? {
        val usersResource: UsersResource = getUsersResource()

        val userResource = usersResource[id.toString()]

        return try {
            userResource.toRepresentation()
        } catch (ex: ClientWebApplicationException) {
            null
        }
    }

    fun getUserByEmail(userEmail: String): UserRepresentation? {
        val usersResource: UsersResource = getUsersResource()

        return usersResource
            .searchByEmail(
                userEmail,
                true
            ).firstOrNull()
    }

    fun deleteUser(userId: UUID) {
        val usersResource: UsersResource = getUsersResource()

        val response: Response = usersResource.delete(userId.toString())
        val responseStatusInfo: Response.StatusType = response.getStatusInfo()

        if (Response.Status.NO_CONTENT.statusCode != responseStatusInfo.statusCode) {
            Log.error(
                "Unexpected result while trying to delete user: ${userId}," +
                        " status: ${responseStatusInfo.statusCode}," +
                        " reason: ${responseStatusInfo.reasonPhrase}"
            )
            throw CannotDeleteKeycloakUserException()
        }
    }
}
