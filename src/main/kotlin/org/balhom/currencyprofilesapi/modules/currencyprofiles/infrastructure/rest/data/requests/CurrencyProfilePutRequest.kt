package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.rest.data.requests

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.props.UpdateCurrencyProfileProps
import java.time.LocalDate
import java.util.UUID

data class CurrencyProfilePutRequest(
    @NotBlank
    @Size(max = 15)
    val name: String?,

    @NotNull
    val balance: Double?,

    @NotNull
    val initDate: LocalDate?,

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    val goalMonthlySaving: Double?,

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    val goalYearlySaving: Double?
) {
    fun toUpdateProps(
        id: UUID,
        userId: UUID,
    ): UpdateCurrencyProfileProps = UpdateCurrencyProfileProps(
        id,
        userId,
        name !!,
        balance !!,
        initDate !!,
        goalMonthlySaving !!,
        goalYearlySaving !!,
    )
}
