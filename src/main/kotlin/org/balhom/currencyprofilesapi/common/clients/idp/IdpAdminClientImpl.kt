package org.balhom.currencyprofilesapi.common.clients.idp

import jakarta.enterprise.context.ApplicationScoped
import org.balhom.currencyprofilesapi.common.data.models.IdpUser
import java.util.UUID

@ApplicationScoped
class IdpAdminClientImpl(
    val keycloakAdminClient: KeycloakAdminClient
) : IdpAdminClient {

    override fun getUserByEmail(email: String): IdpUser? {
        val userRepresentation = keycloakAdminClient
            .getUserByEmail(email)

        if (userRepresentation == null) {
            return null
        }

        return IdpUser(
            UUID.fromString(
                userRepresentation.id
            ),
            userRepresentation.email
        )
    }

    override fun getUserById(id: UUID): IdpUser? {
        val userRepresentation = keycloakAdminClient
            .getUserById(id)

        if (userRepresentation == null) {
            return null
        }

        return IdpUser(
            UUID.fromString(
                userRepresentation.id
            ),
            userRepresentation.email
        )
    }

    override fun deleteUser(userId: UUID) =
            keycloakAdminClient.deleteUser(userId)
}
