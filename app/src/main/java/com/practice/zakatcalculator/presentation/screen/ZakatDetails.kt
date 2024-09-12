package com.practice.zakatcalculator.presentation.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.practice.zakatcalculator.domain.model.ZakatEntity
import com.practice.zakatcalculator.presentation.NisabEvent
import com.practice.zakatcalculator.util.getDateFromLong

/**
 * @author Hazrat Ummar Shaikh
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZakatDetails(
    modifier: Modifier = Modifier,
    zakatEntity: ZakatEntity,
    onBackClick:() -> Unit ={}
) {
    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = getDateFromLong(zakatEntity.date)) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBackClick()
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
        LazyColumn(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            item {
                CalculateItems(
                    text = "Money",
                    amount = zakatEntity.money.toString()
                )
                CalculateItems(
                    text = "Gold",
                    amount = zakatEntity.gold.toString()
                )
                CalculateItems(
                    text = "Silver",
                    amount = zakatEntity.silver.toString()
                )
                CalculateItems(
                    text = "Trade Amount",
                    amount = zakatEntity.tradeAmount.toString()
                )
                CalculateItems(
                    text = "Monthly Living Cost",
                    amount = zakatEntity.monthCost.toString()
                )
                CalculateItems(
                    text = "Debt",
                    amount = zakatEntity.debt.toString()
                )
            }
        }
    }
}