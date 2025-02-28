package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.rest.data.requests

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.balhom.currencyprofilesapi.common.data.enums.CurrencyCodeEnum
import org.balhom.currencyprofilesapi.common.data.models.AuditableData
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.CurrencyProfile
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class CurrencyProfilePostRequest(
    @NotBlank
    @Size(max = 15)
    val name: String?,

    @NotNull
    val currencyCode: CurrencyCodeEnum?,

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
    fun toDomain(
        userId: UUID
    ): CurrencyProfile = CurrencyProfile(
        UUID.randomUUID(),
        userId,
        name !!,
        currencyCode !!,
        balance !!,
        initDate !!,
        goalMonthlySaving !!,
        goalYearlySaving !!,
        null,
        ArrayList(),
        AuditableData(
            LocalDateTime.now(),
            userId.toString(),
            null,
            null,
        ),
    )
}
