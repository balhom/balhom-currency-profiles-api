package org.balhom.currencyprofilesapi.modules.transactionchanges.application

import org.balhom.currencyprofilesapi.common.data.enums.EventChangeTypeEnum
import org.balhom.currencyprofilesapi.common.data.props.ObjectIdUserProps
import org.balhom.currencyprofilesapi.modules.currencyprofiles.application.CurrencyProfileService
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.CurrencyProfile
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.MockCurrencyProfileFactory
import org.balhom.currencyprofilesapi.modules.transactionchanges.domain.enums.TransactionTypeEnum
import org.balhom.currencyprofilesapi.modules.transactionchanges.domain.props.MockTransactionChangePropsFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.capture
import org.mockito.kotlin.doReturn

class TransactionChangesServiceTest {

    private lateinit var currencyProfileService: CurrencyProfileService

    private lateinit var transactionChangesService: TransactionChangesService

    @BeforeEach
    fun setUp() {
        currencyProfileService = mock(
            CurrencyProfileService::class.java
        )

        transactionChangesService = TransactionChangesService(
            currencyProfileService
        )
    }

    @Test
    fun `processChange should handle income CREATE correctly`() {
        val currencyProfile = MockCurrencyProfileFactory.create()
        val props = MockTransactionChangePropsFactory.create(
            eventChangeType = EventChangeTypeEnum.CREATE,
            type = TransactionTypeEnum.INCOME,
            currencyProfileId = currencyProfile.id
        )

        val expectedBalance = currencyProfile.balance + props.amount

        val currencyProfileCaptor = ArgumentCaptor.forClass(
            CurrencyProfile::class.java
        )

        `when`(
            currencyProfileService
                .getCurrencyProfile(
                    ObjectIdUserProps(
                        props.currencyProfileId,
                        props.userId
                    )
                )
        ).thenReturn(currencyProfile)

        `when`(
            currencyProfileService
                .internalUpdateCurrencyProfile(
                    capture<CurrencyProfile>(currencyProfileCaptor)
                )
        ).doReturn(currencyProfile)

        transactionChangesService.processChange(props)

        with(currencyProfileCaptor.value) {
            assertEquals(expectedBalance, balance)
        }
    }

    @Test
    fun `processChange should handle expense CREATE correctly`() {
        val currencyProfile = MockCurrencyProfileFactory.create()
        val props = MockTransactionChangePropsFactory.create(
            eventChangeType = EventChangeTypeEnum.CREATE,
            type = TransactionTypeEnum.EXPENSE,
            currencyProfileId = currencyProfile.id
        )

        val expectedBalance = currencyProfile.balance - props.amount

        val currencyProfileCaptor = ArgumentCaptor.forClass(
            CurrencyProfile::class.java
        )

        `when`(
            currencyProfileService
                .getCurrencyProfile(
                    ObjectIdUserProps(
                        props.currencyProfileId,
                        props.userId
                    )
                )
        ).thenReturn(currencyProfile)

        `when`(
            currencyProfileService
                .internalUpdateCurrencyProfile(
                    capture<CurrencyProfile>(currencyProfileCaptor)
                )
        ).doReturn(currencyProfile)

        transactionChangesService.processChange(props)

        with(currencyProfileCaptor.value) {
            assertEquals(expectedBalance, balance)
        }
    }

    @Test
    fun `processChange should handle income UPDATE correctly`() {
        val currencyProfile = MockCurrencyProfileFactory.create()
        val props = MockTransactionChangePropsFactory.create(
            eventChangeType = EventChangeTypeEnum.UPDATE,
            type = TransactionTypeEnum.INCOME,
            currencyProfileId = currencyProfile.id
        )

        val expectedBalance = currencyProfile
            .balance - props.oldData !!.oldAmount + props.amount

        val currencyProfileCaptor = ArgumentCaptor.forClass(
            CurrencyProfile::class.java
        )

        `when`(
            currencyProfileService
                .internalUpdateCurrencyProfile(
                    capture<CurrencyProfile>(currencyProfileCaptor)
                )
        ).doReturn(currencyProfile)

        `when`(
            currencyProfileService
                .getCurrencyProfile(
                    ObjectIdUserProps(
                        props.currencyProfileId,
                        props.userId
                    )
                )
        ).thenReturn(currencyProfile)

        transactionChangesService.processChange(props)

        with(currencyProfileCaptor.value) {
            assertEquals(expectedBalance, balance)
        }
    }

    @Test
    fun `processChange should handle expense UPDATE correctly`() {
        val currencyProfile = MockCurrencyProfileFactory.create()
        val props = MockTransactionChangePropsFactory.create(
            eventChangeType = EventChangeTypeEnum.UPDATE,
            type = TransactionTypeEnum.EXPENSE,
            currencyProfileId = currencyProfile.id
        )

        val expectedBalance = currencyProfile
            .balance + props.oldData !!.oldAmount - props.amount

        val currencyProfileCaptor = ArgumentCaptor.forClass(
            CurrencyProfile::class.java
        )

        `when`(
            currencyProfileService
                .getCurrencyProfile(
                    ObjectIdUserProps(
                        props.currencyProfileId,
                        props.userId
                    )
                )
        ).thenReturn(currencyProfile)

        `when`(
            currencyProfileService
                .internalUpdateCurrencyProfile(
                    capture<CurrencyProfile>(currencyProfileCaptor)
                )
        ).doReturn(currencyProfile)

        transactionChangesService.processChange(props)

        with(currencyProfileCaptor.value) {
            assertEquals(expectedBalance, balance)
        }
    }

    @Test
    fun `processChange should handle income DELETE correctly`() {
        val currencyProfile = MockCurrencyProfileFactory.create()
        val props = MockTransactionChangePropsFactory.create(
            eventChangeType = EventChangeTypeEnum.DELETE,
            type = TransactionTypeEnum.INCOME,
            currencyProfileId = currencyProfile.id
        )

        val expectedBalance = currencyProfile.balance - props.amount

        val currencyProfileCaptor = ArgumentCaptor.forClass(
            CurrencyProfile::class.java
        )

        `when`(
            currencyProfileService
                .getCurrencyProfile(
                    ObjectIdUserProps(
                        props.currencyProfileId,
                        props.userId
                    )
                )
        ).thenReturn(currencyProfile)

        `when`(
            currencyProfileService
                .internalUpdateCurrencyProfile(
                    capture<CurrencyProfile>(currencyProfileCaptor)
                )
        ).doReturn(currencyProfile)

        transactionChangesService.processChange(props)

        with(currencyProfileCaptor.value) {
            assertEquals(expectedBalance, balance)
        }
    }

    @Test
    fun `processChange should handle expense DELETE correctly`() {
        val currencyProfile = MockCurrencyProfileFactory.create()
        val props = MockTransactionChangePropsFactory.create(
            eventChangeType = EventChangeTypeEnum.DELETE,
            type = TransactionTypeEnum.EXPENSE,
            currencyProfileId = currencyProfile.id
        )

        val expectedBalance = currencyProfile.balance + props.amount

        val currencyProfileCaptor = ArgumentCaptor.forClass(
            CurrencyProfile::class.java
        )

        `when`(
            currencyProfileService
                .getCurrencyProfile(
                    ObjectIdUserProps(
                        props.currencyProfileId,
                        props.userId
                    )
                )
        ).thenReturn(currencyProfile)

        `when`(
            currencyProfileService
                .internalUpdateCurrencyProfile(
                    capture<CurrencyProfile>(currencyProfileCaptor)
                )
        ).doReturn(currencyProfile)

        transactionChangesService.processChange(props)

        with(currencyProfileCaptor.value) {
            assertEquals(expectedBalance, balance)
        }
    }
}
