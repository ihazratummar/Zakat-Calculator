package com.practice.zakatcalculator.navigation

import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.practice.zakatcalculator.presentation.NisabEvent
import com.practice.zakatcalculator.presentation.NisabState
import com.practice.zakatcalculator.presentation.screen.CalculationScreen
import com.practice.zakatcalculator.presentation.screen.GoldInfo
import com.practice.zakatcalculator.presentation.screen.MoneyInfo
import com.practice.zakatcalculator.presentation.screen.MonthlyLivingCostInfo
import com.practice.zakatcalculator.presentation.screen.NisabScreen
import com.practice.zakatcalculator.presentation.screen.SilverInfo
import com.practice.zakatcalculator.presentation.screen.TotalDebtInfo
import com.practice.zakatcalculator.presentation.screen.ZakatDetails
import com.practice.zakatcalculator.presentation.screen.ZakatScreen

/**
 * @author Hazrat Ummar Shaikh
 */

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    nisabState: NisabState,
    nisabEvent: (NisabEvent) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = ZakatScreen,
        exitTransition = { ExitTransition.None },
        enterTransition = { EnterTransition.None }
    ) {
        composable<ZakatScreen> {
            ZakatScreen(
                modifier = modifier,
                nisabState = nisabState,
                nisabEvent = nisabEvent,
                onNewAddClick = {
                    navController.navigate(NisabScreen)
                },
                openZakat = { id ->
                    navController.navigate(ZakatDetailsScreen(zakatId = id))
                },
            )
        }
        composable<NisabScreen> {
            NisabScreen(
                modifier = modifier,
                nisabState = nisabState,
                nisabEvent = nisabEvent,
                onSubmit = {
                    navController.navigate(CalculationScreen)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable<ZakatDetailsScreen> { backStackEntry ->
            val zakatId = backStackEntry.toRoute<ZakatDetailsScreen>()
            val zakat = nisabState.zakatEntity.find { it.id == zakatId.zakatId }
            if (zakat != null) {
                ZakatDetails(
                    zakatEntity = zakat,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
        composable<CalculationScreen> {
            CalculationScreen(
                nisabState = nisabState,
                nisabEvent = nisabEvent,
                onBackClick = {
                    navController.popBackStack()
                },
                moneyInfoNav = {
                    navController.navigate(MoneyInfoScreen)
                },
                goldInfoNav = {
                    navController.navigate(GoldInfoScreen)
                },
                silverInfoNav = {
                    navController.navigate(SilverInfoScreen)
                },
                monthInfoNav = {
                    navController.navigate(MonthlyLivingCostInfoScreen)
                },
                totalDebtInfoNav = {
                    navController.navigate(TotalDebtInfoScreen)
                }
            )
        }

        composable<MonthlyLivingCostInfoScreen> {
            MonthlyLivingCostInfo(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<TotalDebtInfoScreen> {
            TotalDebtInfo(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<GoldInfoScreen> {
            GoldInfo(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<SilverInfoScreen> {
            SilverInfo(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<MoneyInfoScreen> {

            MoneyInfo(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}