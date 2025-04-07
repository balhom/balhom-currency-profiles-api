package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.rest.data.requests

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.balhom.currencyprofilesapi.common.data.enums.CurrencyCodeEnum
import org.balhom.currencyprofilesapi.common.data.models.AuditableData
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.CurrencyProfile
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class CurrencyProfilePostRequest(
    @field:NotBlank
    @field:Size(max = 15)
    val name: String?,

    @field:NotNull
    val currencyCode: CurrencyCodeEnum?,

    @field:NotNull
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer::class)
    val balance: BigDecimal?,

    @field:NotNull
    val initDate: LocalDate?,

    @field:NotNull
    @field:DecimalMin(value = "0.0", inclusive = true)
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer::class)
    val goalMonthlySaving: BigDecimal?,

    @field:NotNull
    @field:DecimalMin(value = "0.0", inclusive = true)
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer::class)
    val goalYearlySaving: BigDecimal?
) {
    fun toDomain(
        userId: UUID
    ): CurrencyProfile = CurrencyProfile(
        UUID.randomUUID(),
        userId,
        name!!,
        currencyCode!!,
        balance!!,
        initDate!!,
        goalMonthlySaving!!,
        goalYearlySaving!!,
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
