package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.rest

import io.quarkus.security.Authenticated
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.balhom.currencyprofilesapi.common.data.props.ObjectIdUserProps
import org.balhom.currencyprofilesapi.modules.currencyprofiles.application.CurrencyProfileService
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.props.UploadCurrencyProfileImageProps
import org.eclipse.microprofile.jwt.JsonWebToken
import org.jboss.resteasy.reactive.RestForm
import java.io.File
import java.util.UUID

@Path(CurrencyProfileImageResource.RESOURCE_PATH)
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Authenticated
class CurrencyProfileImageResource(private val service: CurrencyProfileService) {

    companion object {
        const val RESOURCE_PATH = "/api/v1/currency-profile"
    }

    @Inject
    lateinit var jwt: JsonWebToken

    @POST
    @Path("/{id}/image")
    fun uploadImage(
        @PathParam("id") id: UUID,
        @RestForm("file") image: File,
        @RestForm mimetype: String
    ): Response {
        service.uploadCurrencyProfileImage(
            UploadCurrencyProfileImageProps(
                ObjectIdUserProps(
                    id,
                    UUID.fromString(jwt.subject),
                ),
                image,
                mimetype,
            )
        )

        return Response
            .ok()
            .build()
    }
}