package com.practice.zakatcalculator.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.practice.zakatcalculator.domain.model.ZakatEntity
import com.practice.zakatcalculator.presentation.NisabEvent
import com.practice.zakatcalculator.presentation.NisabState
import com.practice.zakatcalculator.util.getDateFromLong

/**
 * @author Hazrat Ummar Shaikh
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZakatScreen(
    modifier: Modifier = Modifier,
    nisabState: NisabState,
    nisabEvent: (NisabEvent) -> Unit,
    onNewAddClick: () -> Unit = {},
    openZakat:(Int) -> Unit ={}

    ) {
    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                title = { },
                actions = {
                    IconButton(onClick = {
                        nisabEvent(NisabEvent.ToggleSortType)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Sort,
                            contentDescription = "sorting"
                        )
                    }
                }
            )
        }
    ) { innerpadding ->
        LazyColumn(
            modifier = modifier.padding(innerpadding)
        ) {
            item {
                Card(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .clickable {
                            onNewAddClick()
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(65.dp)
                            .padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Start Calculation", fontWeight = FontWeight.SemiBold )
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                    }
                }
            }
            item {
                if (nisabState.zakatEntity.isNotEmpty()) {
                    nisabState.zakatEntity.forEach {
                        Card(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth()
                                .clickable {
                                    openZakat(it.id)
                                }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp)
                                ) {
                                    Text(
                                        text = getDateFromLong(it.date),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "Total Assets: ${it.totalAsset}",
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "Zakat Amount: ${it.zakatAmount}",
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        nisabEvent(
                                            NisabEvent.DeleteZakat(
                                                zakatId = it.id
                                            )
                                        )
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete"
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Column (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(text = "No data found")
                    }
                }
            }

        }

    }
}