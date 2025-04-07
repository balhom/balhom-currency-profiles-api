package org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.props

import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

data class UpdateCurrencyProfileProps(
    val id: UUID,
    val userId: UUID,
    val name: String,
    val balance: BigDecimal,
    val initDate: LocalDate,
    val goalMonthlySaving: BigDecimal,
    val goalYearlySaving: BigDecimal,
)
