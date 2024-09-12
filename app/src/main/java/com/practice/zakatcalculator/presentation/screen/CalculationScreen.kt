package com.practice.zakatcalculator.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.practice.zakatcalculator.presentation.NisabEvent
import com.practice.zakatcalculator.presentation.NisabState

/**
 * @author Hazrat Ummar Shaikh
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculationScreen(
    modifier: Modifier = Modifier,
    nisabState: NisabState,
    nisabEvent: (NisabEvent) -> Unit,
    onBackClick: () -> Unit,
    moneyInfoNav: () -> Unit = {},
    goldInfoNav: () -> Unit = {},
    silverInfoNav: () -> Unit = {},
    monthInfoNav: () -> Unit = {},
    totalDebtInfoNav: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Zakat Calculator") },
                actions = {
                    TextButton(
                        onClick = { nisabEvent(NisabEvent.SaveZakat) },
                        enabled = nisabState.money != "0.0" || nisabState.gold != "0.0" ||
                                nisabState.silver != "0.0" || nisabState.tradeAmount != "0.0"
                                || nisabState.monthCost != "0.0" || nisabState.debt != "0.0"
                    ) {
                        Text(text = "Save")
                    }
                    IconButton(
                        onClick = {
                            nisabEvent(NisabEvent.ResetAllState)
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBackClick()
                            nisabEvent(NisabEvent.ResetAllState)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .align(Alignment.TopCenter)
            ) {
                item {
                    CalculateItems(
                        text = "Money",
                        amount = nisabState.money,
                        onClick = { nisabEvent(NisabEvent.OpenMoneyDialog) },
                        infoNavigation = {
                            moneyInfoNav()
                        }
                    )
                    CalculateItems(
                        text = "Gold",
                        amount = nisabState.gold,
                        onClick = { nisabEvent(NisabEvent.OpenGoldDialog) },
                        infoNavigation = { goldInfoNav() }
                    )
                    CalculateItems(
                        text = "Silver",
                        amount = nisabState.silver,
                        onClick = { nisabEvent(NisabEvent.OpenSilverDialog) },
                        infoNavigation = { silverInfoNav() }
                    )
                    CalculateItems(
                        text = "Trade Amount",
                        amount = nisabState.tradeAmount,
                        onClick = { nisabEvent(NisabEvent.OpenTradeAmountDialog) }
                    )
                    CalculateItems(
                        text = "Monthly Living Cost",
                        amount = nisabState.monthCost,
                        onClick = { nisabEvent(NisabEvent.OpenMonthCostDialog) },
                        infoNavigation = { monthInfoNav() },
                        fontColor = MaterialTheme.colorScheme.error
                    )
                    CalculateItems(
                        text = "Total Debt on you",
                        amount = nisabState.debt,
                        onClick = { nisabEvent(NisabEvent.OpenDebtDialog) },
                        infoNavigation = { totalDebtInfoNav() },
                        fontColor = MaterialTheme.colorScheme.error
                    )
                }
            }

            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    BottomBarItem(
                        text = "Nisab",
                        amount = nisabState.nisabAmount
                    )
                    BottomBarItem(
                        text = "Total Asset",
                        amount = "${nisabState.totalAsset}"
                    )
                    BottomBarItem(
                        text = "Zakat Amount",
                        amount = "${nisabState.zakatAmount}"
                    )
                }
            }
        }
    }

    if (nisabState.isMoneyDialogOpen) {
        CalculationItemDialogs(
            onDismiss = { nisabEvent(NisabEvent.OpenMoneyDialog) },
            value = nisabState.money,
            onValueChange = {
                nisabEvent(NisabEvent.OnMoneyValueChange(it))
            },
            onClick = {
                nisabEvent(NisabEvent.OnMoneySubmit(nisabState.money))
                nisabEvent(NisabEvent.OpenMoneyDialog)
            },
            onDone = {
                nisabEvent(NisabEvent.OnMoneySubmit(nisabState.money))
                nisabEvent(NisabEvent.OpenMoneyDialog)
            }
        )
    }

    if (nisabState.isGoldDialogOpen) {
        CalculationItemDialogs(
            onDismiss = { nisabEvent(NisabEvent.OpenGoldDialog) },
            value = nisabState.gold,
            onValueChange = {
                nisabEvent(NisabEvent.OnGoldPriceChange(it))
            },
            onClick = {
                nisabEvent(NisabEvent.OnSubmitGold(nisabState.gold))
                nisabEvent(NisabEvent.OpenGoldDialog)
            },
            onDone = {
                nisabEvent(NisabEvent.OnSubmitGold(nisabState.gold))
                nisabEvent(NisabEvent.OpenGoldDialog)
            }
        )
    }

    if (nisabState.isSilverDialogOpen) {
        CalculationItemDialogs(
            onDismiss = { nisabEvent(NisabEvent.OpenSilverDialog) },
            value = nisabState.silver,
            onValueChange = {
                nisabEvent(NisabEvent.OnSilverPriceChange(it))
            },
            onClick = {
                nisabEvent(NisabEvent.OnSubmitSilver(nisabState.silver))
                nisabEvent(NisabEvent.OpenSilverDialog)
            },
            onDone = {
                nisabEvent(NisabEvent.OnSubmitSilver(nisabState.silver))
                nisabEvent(NisabEvent.OpenSilverDialog)
            }
        )
    }
    if (nisabState.isTradeAmountDialogOpen) {
        CalculationItemDialogs(
            onDismiss = { nisabEvent(NisabEvent.OpenTradeAmountDialog) },
            value = nisabState.tradeAmount,
            onValueChange = {
                nisabEvent(NisabEvent.OnTradeAmountPriceChange(it))
            },
            onClick = {
                nisabEvent(NisabEvent.OnSubmitTradeAmount(nisabState.tradeAmount))
                nisabEvent(NisabEvent.OpenTradeAmountDialog)
            },
            onDone = {
                nisabEvent(NisabEvent.OnSubmitTradeAmount(nisabState.tradeAmount))
                nisabEvent(NisabEvent.OpenTradeAmountDialog)
            }
        )
    }
    if (nisabState.isMonthCostDialogOpen) {
        CalculationItemDialogs(
            onDismiss = { nisabEvent(NisabEvent.OpenMonthCostDialog) },
            value = nisabState.monthCost,
            onValueChange = {
                nisabEvent(NisabEvent.OnMonthCostPriceChange(it))
            },
            onClick = {
                nisabEvent(NisabEvent.OnSubmitMonthCost(nisabState.monthCost))
                nisabEvent(NisabEvent.OpenMonthCostDialog)
            },
            onDone = {
                nisabEvent(NisabEvent.OnSubmitMonthCost(nisabState.monthCost))
                nisabEvent(NisabEvent.OpenMonthCostDialog)
            },

            )
    }
    if (nisabState.isDebtDialogOpen) {
        CalculationItemDialogs(
            onDismiss = { nisabEvent(NisabEvent.OpenDebtDialog) },
            value = nisabState.debt,
            onValueChange = {
                nisabEvent(NisabEvent.OnDebtPriceChange(it))
            },
            onClick = {
                nisabEvent(NisabEvent.OnSubmitDebt(nisabState.debt))
                nisabEvent(NisabEvent.OpenDebtDialog)
            },
            onDone = {
                nisabEvent(NisabEvent.OnSubmitDebt(nisabState.debt))
                nisabEvent(NisabEvent.OpenDebtDialog)
            }
        )
    }


}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CalculationItemDialogs(
    value: String = "",
    onValueChange: (String) -> Unit = {},
    onClick: () -> Unit,
    onDismiss: () -> Unit = {},
    onDone: () -> Unit = {}
) {

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    ModalBottomSheet(
        modifier = Modifier.imePadding(),
        onDismissRequest = {
            onDismiss()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Enter Amount")
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = value,
                onValueChange = onValueChange,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onDone() }
                )
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onClick()
                }
            ) {
                Text(text = "Add")
            }
        }
    }
}

@Composable
fun CalculateItems(
    text: String,
    amount: String = "0.0",
    onClick: () -> Unit = {},
    infoNavigation: () -> Unit = {},
    fontColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clickable {
                onClick()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text, fontWeight = FontWeight.SemiBold)
            IconButton(onClick = { infoNavigation() }) {
                Icon(imageVector = Icons.Default.Info, contentDescription = "Information")
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = amount, fontWeight = FontWeight.SemiBold, color = fontColor)
        }
    }
}

@Composable
private fun BottomBarItem(
    text: String,
    onClick: () -> Unit = {},
    amount: String = "0.0"
) {
    Card(
        modifier = Modifier.padding(vertical = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        ),
        shape = RoundedCornerShape(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = text)
            Text(text = amount)
        }
    }
}

