package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.rest

import io.quarkus.security.Authenticated
import jakarta.inject.Inject
import jakarta.validation.Valid
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.balhom.currencyprofilesapi.common.data.props.ObjectIdUserProps
import org.balhom.currencyprofilesapi.modules.currencyprofiles.application.CurrencyProfileSharedUserService
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.CurrencyProfileSharedUser
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.props.AddCurrencyProfileSharedUserProps
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.props.RemoveCurrencyProfileSharedUserProps
import org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.rest.data.requests.CurrencyProfileSharedUserRequest
import org.eclipse.microprofile.jwt.JsonWebToken
import java.util.UUID

@Path(CurrencyProfileSharedUserResource.RESOURCE_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
class CurrencyProfileSharedUserResource(private val service: CurrencyProfileSharedUserService) {

    companion object {
        const val RESOURCE_PATH = "/api/v1/currency-profile"
    }

    @Inject
    lateinit var jwt: JsonWebToken

    @GET
    @Path("/{id}/users")
    fun getAllSharedUsers(@PathParam("id") id: UUID):
            List<CurrencyProfileSharedUser> {
        return service
            .getAllCurrencyProfileSharedUsers(
                ObjectIdUserProps(
                    id,
                    UUID.fromString(jwt.subject)
                )
            )
    }

    @PUT
    @Path("/{id}/users")
    fun update(
        @PathParam("id") id: UUID,
        @Valid request: CurrencyProfileSharedUserRequest
    ): Response {
        if (request.userEmailToAdd != null) {
            service.addCurrencyProfileSharedUser(
                AddCurrencyProfileSharedUserProps(
                    id,
                    request.userEmailToAdd,
                    UUID.fromString(jwt.subject)
                )
            )
        }

        if (request.userIdToRemove != null) {
            service.removeCurrencyProfileSharedUser(
                RemoveCurrencyProfileSharedUserProps(
                    id,
                    request.userIdToRemove,
                    UUID.fromString(jwt.subject)
                )
            )
        }

        return Response
            .noContent()
            .build()
    }
}
