package org.balhom.currencyprofilesapi.common.clients.idp

import org.balhom.currencyprofilesapi.common.data.models.IdpUser
import java.util.UUID

interface IdpAdminClient {
    fun getUserByEmail(email: String): IdpUser?

    fun getUserById(id: UUID): IdpUser?
}
