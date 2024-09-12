package com.practice.zakatcalculator.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.practice.zakatcalculator.presentation.NisabEvent
import com.practice.zakatcalculator.presentation.NisabState
import java.util.Locale

/**
 * @author Hazrat Ummar Shaikh
 */


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NisabScreen(
    modifier: Modifier = Modifier,
    nisabState: NisabState,
    nisabEvent: (NisabEvent) -> Unit,
    onSubmit: () -> Unit,
    onBackClick: () -> Unit
) {

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {  },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(10.dp)
                .padding(innerPadding),
        ) {
            Text(text = "Nisab is a threshold, referring to the minimum amount of wealth")
            val nisabAmount = 11.66638 * 52.50
            val roundAmount = String.format(Locale.getDefault(), "%.2f", nisabAmount)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "The nisab threshold for silver is ${roundAmount}gm (52.50 Tola/Vori) or the cash equivalent")
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "To calculate nisab, enter the current value of silver/gm:")
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = nisabState.silverPrice,
                onValueChange = { nisabEvent(NisabEvent.OnPriceChange(it)) },
                placeholder = {
                    Text(
                        text = "Write the silver price here.",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        nisabEvent(NisabEvent.SubmitNisab(nisabState.silverPrice))
                        onSubmit()
                    }
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    nisabEvent(NisabEvent.SubmitNisab(nisabState.silverPrice))
                    onSubmit()
                },
                enabled = nisabState.silverPrice != "" && nisabState.isSilverPriceValid,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "Submit")
            }
        }
    }
}