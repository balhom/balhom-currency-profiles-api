package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.rest.data.responses

import com.fasterxml.jackson.annotation.JsonFormat
import org.balhom.currencyprofilesapi.common.data.enums.CurrencyCodeEnum
import org.balhom.currencyprofilesapi.common.data.models.FileReferenceData
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.CurrencyProfile
import java.time.LocalDate
import java.util.UUID

data class CurrencyProfileResponse(
    val id: UUID,
    val name: String,
    val currencyCode: CurrencyCodeEnum,
    val balance: Double,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    val initDate: LocalDate,
    val goalMonthlySaving: Double,
    val goalYearlySaving: Double,
    val imageData: FileReferenceData?,
) {
    companion object {
        fun fromDomain(domain: CurrencyProfile): CurrencyProfileResponse = CurrencyProfileResponse(
            domain.id,
            domain.name,
            domain.currencyCode,
            domain.balance,
            domain.initDate,
            domain.goalMonthlySaving,
            domain.goalYearlySaving,
            domain.imageData,
        )
    }
}
