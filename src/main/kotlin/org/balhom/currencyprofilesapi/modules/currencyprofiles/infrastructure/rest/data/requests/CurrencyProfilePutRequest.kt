package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.rest.data.requests

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.props.UpdateCurrencyProfileProps
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

data class CurrencyProfilePutRequest(
    @field:NotBlank
    @field:Size(max = 15)
    val name: String?,

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
    fun toUpdateProps(
        id: UUID,
        userId: UUID,
    ): UpdateCurrencyProfileProps = UpdateCurrencyProfileProps(
        id,
        userId,
        name!!,
        balance!!,
        initDate!!,
        goalMonthlySaving!!,
        goalYearlySaving!!,
    )
}
