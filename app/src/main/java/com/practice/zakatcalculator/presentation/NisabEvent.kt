package com.practice.zakatcalculator.presentation

import com.practice.zakatcalculator.domain.model.ZakatEntity

/**
 * @author Hazrat Ummar Shaikh
 */

sealed interface NisabEvent {

    data class OnPriceChange(val price: String) : NisabEvent
    data class SubmitNisab(val price: String) : NisabEvent

    data object OpenMoneyDialog : NisabEvent
    data object OpenBankCashDialog : NisabEvent
    data object OpenGoldDialog : NisabEvent
    data object OpenSilverDialog : NisabEvent
    data object OpenTradeAmountDialog : NisabEvent
    data object OpenMonthCostDialog : NisabEvent
    data object OpenDebtDialog : NisabEvent


    data class OnMoneyValueChange(val price : String): NisabEvent
    data class OnMoneySubmit(val price : String): NisabEvent

    data class OnGoldPriceChange(val price : String): NisabEvent
    data class OnSubmitGold(val price : String): NisabEvent

    data class OnSilverPriceChange(val price : String): NisabEvent
    data class OnSubmitSilver(val price : String): NisabEvent

    data class OnTradeAmountPriceChange(val price : String): NisabEvent
    data class OnSubmitTradeAmount(val price : String): NisabEvent

    data class OnMonthCostPriceChange(val price : String): NisabEvent
    data class OnSubmitMonthCost(val price : String): NisabEvent

    data class OnDebtPriceChange(val price : String): NisabEvent
    data class OnSubmitDebt(val price : String): NisabEvent

    data object ResetAllState : NisabEvent

    data object SaveZakat: NisabEvent

    data object ToggleSortType: NisabEvent

    data class DeleteZakat(val zakatId: Int): NisabEvent
}