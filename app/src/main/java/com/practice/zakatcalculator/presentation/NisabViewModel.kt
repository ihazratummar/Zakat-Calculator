package com.practice.zakatcalculator.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.zakatcalculator.domain.Repository
import com.practice.zakatcalculator.domain.model.NisabEntity
import com.practice.zakatcalculator.domain.model.ZakatEntity
import com.practice.zakatcalculator.util.PreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */

@HiltViewModel
class NisabViewModel @Inject constructor(
    private val repository: Repository,
    @ApplicationContext private val context: Context
) : ViewModel() {


    private val _sortType = MutableStateFlow(PreferenceUtil.getSortType(context))
    private val _state = MutableStateFlow(NisabState(zakatEntity = emptyList()))
    //    MutableStateFlow(NisabState(zakatEntity = emptyList()))
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _zakats = combine(_sortType) { sortType ->
        sortType
    }.flatMapLatest { (sortType) ->
        when(sortType) {
            DateType.DATE_ASC -> repository.getZakatDetailsByDateAsc()
            DateType.DATE_DESC -> repository.getZakatDetailsByDateDesc()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    val nisabState = combine(_state, _zakats){ state, zakats  ->
        state.copy(
            zakatEntity = zakats,
            sortType = state.sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NisabState(zakatEntity = emptyList()))


    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getNisab().collectLatest {
                _state.update {
                    it.copy(
                        silverPrice = it.silverPrice,
                    )
                }
            }
        }
    }

    // Function to recalculate total asset
    private fun recalculateTotalAsset() {
        val state = _state.value
        val totalAsset = calculateTotalAsset(
            handCash = state.money,
            bankCash = state.bankCash,
            gold = state.gold,
            silver = state.silver,
            tradeAmount = state.tradeAmount,
            monthCost = state.monthCost,
            debt = state.debt
        )

        _state.update {
            it.copy(
                totalAsset = totalAsset
            )
        }
    }

    fun event(event: NisabEvent) {
        when (event) {
            is NisabEvent.OnPriceChange -> {
                if (isValidNumber(event.price)) {
                    _state.update {
                        it.copy(
                            silverPrice = event.price,
                            isSilverPriceValid = true
                        )
                    }
                    if (event.price.isNotBlank()) {
                        _state.update {
                            it.copy(
                                nisabAmount = calculateSilverPrice(event.price)
                            )
                        }
                    }
                } else {
                    _state.update {
                        it.copy(
                            isSilverPriceValid = false
                        )
                    }
                }
            }

            is NisabEvent.SubmitNisab -> {
                viewModelScope.launch {
                    repository.insertNisab(
                        nisabEntity = NisabEntity(
                            silverPrice = event.price.toDouble(),
                        )
                    )
                }
            }

            NisabEvent.OpenBankCashDialog -> {
                _state.update {
                    it.copy(
                        isBankCashDialogOpen = !it.isBankCashDialogOpen
                    )
                }
            }

            NisabEvent.OpenDebtDialog -> {
                _state.update {
                    it.copy(
                        isDebtDialogOpen = !it.isDebtDialogOpen
                    )
                }
            }

            NisabEvent.OpenGoldDialog -> {
                _state.update {
                    it.copy(
                        isGoldDialogOpen = !it.isGoldDialogOpen
                    )
                }
            }

            NisabEvent.OpenMoneyDialog -> {
                _state.update {
                    it.copy(
                        isMoneyDialogOpen = !it.isMoneyDialogOpen
                    )
                }

            }

            NisabEvent.OpenMonthCostDialog -> {
                _state.update {
                    it.copy(
                        isMonthCostDialogOpen = !it.isMonthCostDialogOpen
                    )
                }
            }

            NisabEvent.OpenSilverDialog -> {
                _state.update {
                    it.copy(
                        isSilverDialogOpen = !it.isSilverDialogOpen
                    )
                }
            }

            NisabEvent.OpenTradeAmountDialog -> {
                _state.update {
                    it.copy(
                        isTradeAmountDialogOpen = !it.isTradeAmountDialogOpen
                    )
                }
            }

            is NisabEvent.OnMoneyValueChange -> {
                if (isValidNumber(event.price)) {
                    _state.update {
                        it.copy(
                            money = event.price,
                            isHandCashPriceValid = true
                        )
                    }
                    recalculateTotalAsset()
                    zakatAmount()
                } else {
                    _state.update {
                        it.copy(
                            isHandCashPriceValid = false
                        )
                    }
                }
            }

            is NisabEvent.OnMoneySubmit -> {
                _state.update {
                    it.copy(
                        money = event.price,
                    )
                }
                recalculateTotalAsset()
                zakatAmount()
            }


            is NisabEvent.OnGoldPriceChange -> {
                if (isValidNumber(event.price)) {
                    _state.update {
                        it.copy(
                            gold = event.price,
                            isGoldPriceValid = true
                        )
                    }
                    recalculateTotalAsset()
                    zakatAmount()
                } else {
                    _state.update {
                        it.copy(
                            isGoldPriceValid = false
                        )
                    }
                }
            }

            is NisabEvent.OnSubmitGold -> {
                _state.update {
                    it.copy(
                        gold = event.price,
                    )
                }
                recalculateTotalAsset()
                zakatAmount()

            }

            is NisabEvent.OnSilverPriceChange -> {
                if (isValidNumber(event.price)) {
                    _state.update {
                        it.copy(
                            silver = event.price,
                            isSilverValid = true
                        )
                    }
                    recalculateTotalAsset()
                    zakatAmount()
                } else {
                    _state.update {
                        it.copy(
                            isSilverValid = false
                        )
                    }
                }
            }

            is NisabEvent.OnSubmitSilver -> {
                _state.update {
                    it.copy(
                        silver = event.price,
                    )
                }
                recalculateTotalAsset()
                zakatAmount()
            }

            is NisabEvent.OnSubmitTradeAmount -> {
                if (isValidNumber(event.price)) {
                    _state.update {
                        it.copy(
                            tradeAmount = event.price,
                            isTradeAmountValid = true
                        )
                    }
                    recalculateTotalAsset()
                    zakatAmount()
                } else {
                    _state.update {
                        it.copy(
                            isTradeAmountValid = false
                        )
                    }
                }
            }

            is NisabEvent.OnTradeAmountPriceChange -> {
                _state.update {
                    it.copy(
                        tradeAmount = event.price,
                    )
                }
                recalculateTotalAsset()
                zakatAmount()
            }

            is NisabEvent.OnDebtPriceChange -> {
                if (isValidNumber(event.price)) {
                    _state.update {
                        it.copy(
                            debt = event.price,
                            isDebtAmountValid = true
                        )
                    }
                    recalculateTotalAsset()
                    zakatAmount()
                } else {
                    _state.update {
                        it.copy(
                            isDebtAmountValid = false
                        )
                    }
                }
            }

            is NisabEvent.OnSubmitDebt -> {
                _state.update {
                    it.copy(
                        debt = event.price,
                    )
                }
                recalculateTotalAsset()
                zakatAmount()
            }

            is NisabEvent.OnMonthCostPriceChange -> {
                if (isValidNumber(event.price)) {
                    _state.update {
                        it.copy(
                            monthCost = event.price,
                            isMonthCostValid = true
                        )
                    }
                    recalculateTotalAsset()
                    zakatAmount()
                } else {
                    _state.update {
                        it.copy(
                            isMonthCostValid = false
                        )
                    }
                }
            }

            is NisabEvent.OnSubmitMonthCost -> {
                _state.update {
                    it.copy(
                        monthCost = event.price,
                    )
                }
                recalculateTotalAsset()
                zakatAmount()

            }

            NisabEvent.ResetAllState -> {
                _state.update {
                    it.copy(
                        money = "0.0",
                        bankCash = "0.0",
                        gold = "0.0",
                        silver = "0.0",
                        tradeAmount = "0.0",
                        monthCost = "0.0",
                        debt = "0.0"
                    )
                }
                recalculateTotalAsset()
                zakatAmount()
            }

            is NisabEvent.SaveZakat -> {
                viewModelScope.launch {
                    repository.insertZakat(
                        zakatEntity = ZakatEntity(
                            date = System.currentTimeMillis(),
                            totalAsset = _state.value.totalAsset,
                            money = _state.value.money.toDouble(),
                            gold = _state.value.gold.toDouble(),
                            silver = _state.value.silver.toDouble(),
                            tradeAmount = _state.value.tradeAmount.toDouble(),
                            monthCost = _state.value.monthCost.toDouble(),
                            debt = _state.value.debt.toDouble(),
                            zakatAmount = _state.value.zakatAmount,

                        )
                    )
                }
            }

            NisabEvent.ToggleSortType -> {
                val newSortType =
                    if (_sortType.value == DateType.DATE_DESC) DateType.DATE_ASC else DateType.DATE_DESC
                _sortType.value = newSortType
                PreferenceUtil.setSortType(context, newSortType)
            }

            is NisabEvent.DeleteZakat -> {
                viewModelScope.launch {
                    repository.deleteZakat(event.zakatId)
                }
            }
        }
    }

    private fun zakatAmount() {
        if (_state.value.totalAsset >= _state.value.nisabAmount.toDouble()) {
            val zakatAmount = _state.value.totalAsset / 40
            _state.update {
                it.copy(
                    zakatAmount = zakatAmount
                )
            }
        } else {
            _state.update {
                it.copy(
                    zakatAmount = 0.0
                )
            }
        }
    }

    private fun isValidNumber(input: String): Boolean {
        // Check if the input contains a comma
        return !input.contains(',')
    }
}

fun calculateSilverPrice(silverPrice: String?): String {
    val silverPriceOneTola = silverPrice?.toDouble()?.times(11.6638)
    val silverNisabAmount = silverPriceOneTola?.times(52.50)
    val finalAmount = silverNisabAmount?.minus(1)
    val decimalFormat = DecimalFormat("#.##")
    return decimalFormat.format(finalAmount)
}


fun calculateTotalAsset(
    handCash: String = "",
    bankCash: String = "",
    gold: String = "",
    silver: String = "",
    tradeAmount: String = "",
    monthCost: String = "",
    debt: String = ""
): Double {
    val handCashValue = handCash.toDoubleOrNull() ?: 0.0
    val bankCashValue = bankCash.toDoubleOrNull() ?: 0.0
    val goldValue = gold.toDoubleOrNull() ?: 0.0
    val silverValue = silver.toDoubleOrNull() ?: 0.0
    val tradeAmountValue = tradeAmount.toDoubleOrNull() ?: 0.0
    val monthCostValue = monthCost.toDoubleOrNull() ?: 0.0
    val debtValue = debt.toDoubleOrNull() ?: 0.0

    return (handCashValue + bankCashValue + goldValue + silverValue + tradeAmountValue) -
            (monthCostValue + debtValue)
}

