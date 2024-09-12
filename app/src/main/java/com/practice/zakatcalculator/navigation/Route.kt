package com.practice.zakatcalculator.navigation

import kotlinx.serialization.Serializable

/**
 * @author Hazrat Ummar Shaikh
 */

@Serializable
data object ZakatScreen

@Serializable
data class ZakatDetailsScreen(val zakatId: Int)

@Serializable
data object NisabScreen

@Serializable
data object CalculationScreen

@Serializable
data object MonthlyLivingCostInfoScreen

@Serializable
data object TotalDebtInfoScreen

@Serializable
data object GoldInfoScreen

@Serializable
data object SilverInfoScreen

@Serializable
data object MoneyInfoScreen
