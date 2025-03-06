package org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models

import org.balhom.currencyprofilesapi.common.data.enums.CurrencyCodeEnum
import org.balhom.currencyprofilesapi.common.data.models.AuditableData
import org.balhom.currencyprofilesapi.common.data.models.FileReferenceData
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.exceptions.CurrencyProfileSharedUsersExceededException
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.exceptions.InvalidCurrencyProfileInitDateException
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.props.UpdateCurrencyProfileProps
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class CurrencyProfile(
    var id: UUID,
    var userId: UUID,
    var name: String,
    var currencyCode: CurrencyCodeEnum,
    var balance: Double,
    var initDate: LocalDate,
    var goalMonthlySaving: Double,
    var goalYearlySaving: Double,
    var imageData: FileReferenceData?,
    var sharedUsers: MutableList<CurrencyProfileSharedUser>,
    var auditableData: AuditableData,
) {
    companion object {
        const val MAX_ALLOWED_SHARED_USERS = 5
    }

    fun update(props: UpdateCurrencyProfileProps) {
        id = props.id
        userId = props.userId
        name = props.name
        balance = props.balance
        initDate = props.initDate
        goalMonthlySaving = props.goalMonthlySaving
        goalYearlySaving = props.goalYearlySaving

        // Auditable section
        auditableData.updatedAt = LocalDateTime.now()
        auditableData.updatedBy = userId.toString()

        validate()
    }

    fun validate() {
        if (initDate.isAfter(LocalDate.now())) {
            throw InvalidCurrencyProfileInitDateException()
        }
        if (sharedUsers.size >= MAX_ALLOWED_SHARED_USERS) {
            throw CurrencyProfileSharedUsersExceededException()
        }
    }
}
