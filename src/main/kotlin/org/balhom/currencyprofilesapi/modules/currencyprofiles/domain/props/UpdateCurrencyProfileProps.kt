package org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.props

import java.time.LocalDate
import java.util.UUID

data class UpdateCurrencyProfileProps(
    val id: UUID,
    val userId: UUID,
    val name: String,
    val balance: Double,
    val initDate: LocalDate,
    val goalMonthlySaving: Double,
    val goalYearlySaving: Double
)
