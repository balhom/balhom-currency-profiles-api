package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.rest

import io.quarkus.security.Authenticated
import jakarta.inject.Inject
import jakarta.validation.Valid
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.balhom.currencyprofilesapi.common.data.props.ObjectIdUserProps
import org.balhom.currencyprofilesapi.modules.currencyprofiles.application.CurrencyProfileService
import org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.rest.data.requests.CurrencyProfilePostRequest
import org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.rest.data.requests.CurrencyProfilePutRequest
import org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.rest.data.responses.CurrencyProfileResponse
import org.eclipse.microprofile.jwt.JsonWebToken
import java.util.UUID

@Path(CurrencyProfileResource.RESOURCE_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
class CurrencyProfileResource(private val service: CurrencyProfileService) {

    companion object {
        const val RESOURCE_PATH = "/api/v1/currency-profile"
    }

    @Inject
    lateinit var jwt: JsonWebToken

    @GET
    fun getAll(): List<CurrencyProfileResponse> {
        return service
            .getAllCurrencyProfiles(
                UUID.fromString(jwt.subject)
            )
            .map { CurrencyProfileResponse.fromDomain(it) }
            .toList()
    }

    @GET
    @Path("/{id}")
    fun getById(@PathParam("id") id: UUID):
            CurrencyProfileResponse {
        val currencyProfile = service
            .getCurrencyProfile(
                ObjectIdUserProps(
                    id,
                    UUID.fromString(jwt.subject)
                )
            )
        return CurrencyProfileResponse.fromDomain(
            currencyProfile
        )
    }

    @POST
    fun create(@Valid request: CurrencyProfilePostRequest): Response {
        val createdProfile = service
            .createCurrencyProfile(
                request.toDomain(
                    UUID.fromString(jwt.subject)
                )
            )
        return Response.status(
            Response.Status.CREATED
        )
            .entity(
                CurrencyProfileResponse
                    .fromDomain(createdProfile)
            )
            .build()
    }

    @PUT
    @Path("/{id}")
    fun update(
        @PathParam("id") id: UUID,
        @Valid request: CurrencyProfilePutRequest
    ): Response {
        service.updateCurrencyProfile(
            request.toUpdateProps(
                id,
                UUID.fromString(jwt.subject)
            )
        )
        return Response
            .noContent()
            .build()
    }

    @DELETE
    @Path("/{id}")
    fun deleteById(@PathParam("id") id: UUID): Response {
        service.deleteCurrencyProfile(
            ObjectIdUserProps(
                id,
                UUID.fromString(jwt.subject)
            )
        )
        return Response
            .noContent()
            .build()
    }

}