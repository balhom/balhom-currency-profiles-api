package org.balhom.currencyprofilesapi.common.clients

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.balhom.currencyprofilesapi.common.clients.idp.IdpAdminClient
import org.balhom.currencyprofilesapi.common.containers.BaseTestContainersTest
import org.balhom.currencyprofilesapi.common.seeders.IdpUserTestSeeder
import org.balhom.currencyprofilesapi.common.utils.TestDataUtils.Companion.randomEmail
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.util.UUID


@QuarkusTest
class IdpAdminClientTest : BaseTestContainersTest() {

    @Inject
    lateinit var idpAdminClient: IdpAdminClient

    @Test
    fun `getUserById with a stored user id should return an Idp user`() {
        val expectedIdpUser = IdpUserTestSeeder
            .idpUsers
            .first()

        val idpUser = idpAdminClient.getUserById(
            expectedIdpUser.id
        )

        assertNotNull(idpUser)
        assertEquals(expectedIdpUser, idpUser)
    }

    @Test
    fun `getUserById with random id should not return an Idp user`() {
        val userId = UUID.randomUUID()

        val idpUser = idpAdminClient.getUserById(userId)

        assertNull(idpUser)
    }

    @Test
    fun `getUserByEmail with a stored user email should return an Idp user`() {
        val expectedIdpUser = IdpUserTestSeeder
            .idpUsers
            .first()

        val idpUser = idpAdminClient.getUserByEmail(
            expectedIdpUser.email
        )

        assertNotNull(idpUser)
        assertEquals(expectedIdpUser, idpUser)
    }

    @Test
    fun `getUserByEmail with random email should not return an Idp user`() {
        val idpUser = idpAdminClient.getUserByEmail(
            randomEmail()
        )

        assertNull(idpUser)
    }
}