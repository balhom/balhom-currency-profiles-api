package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.rest

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.quarkus.test.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.security.TestSecurity
import io.restassured.RestAssured.given
import org.balhom.currencyprofilesapi.common.containers.BaseTestContainersTest
import org.balhom.currencyprofilesapi.modules.currencyprofiles.application.CurrencyProfileService
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.MockCurrencyProfileFactory
import org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.rest.data.responses.CurrencyProfileResponse
import org.eclipse.microprofile.jwt.JsonWebToken
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.UUID


@QuarkusTest
class CurrencyProfileResourceTest : BaseTestContainersTest() {

    @InjectMock
    lateinit var currencyProfileService: CurrencyProfileService

    @InjectMock
    lateinit var jwt: JsonWebToken

    final val mapper = jacksonObjectMapper()

    init {
        mapper.findAndRegisterModules()
    }

    @Test
    @TestSecurity(authorizationEnabled = false)
    fun `get all CurrencyProfiles should return list of profiles responses`() {
        val userId = UUID.randomUUID()
        val mockList = listOf(
            MockCurrencyProfileFactory.create()
        )

        Mockito
            .`when`(jwt.subject)
            .thenReturn(userId.toString())

        Mockito
            .`when`(currencyProfileService.getAllCurrencyProfiles(userId))
            .thenReturn(mockList)

        val result = given()
            .`when`()
            .get(CurrencyProfileResource.RESOURCE_PATH)
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val expectedResponse = mockList.map { CurrencyProfileResponse.fromDomain(it) }
        assertEquals(mapper.writeValueAsString(expectedResponse), result)
    }
}