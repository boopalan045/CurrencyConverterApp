package com.asustug.currencyconverterapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asustug.currencyconverterapp.data.model.Rates
import com.asustug.currencyconverterapp.repository.MainRepository
import com.asustug.currencyconverterapp.utils.DispatcherProvider
import com.asustug.currencyconverterapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val dispatchers : DispatcherProvider
) : ViewModel() {

    sealed class CurrencyEvent{
        class Success(val resultText : String) : CurrencyEvent()
        class Failure(val result : String) : CurrencyEvent()
        object Loading : CurrencyEvent()
        object Empty: CurrencyEvent()
    }

    private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)

    val conversation : StateFlow<CurrencyEvent> = _conversion

    fun convert(
        amount: String,
        fromCurrency : String,
        toCurrency: String
    ){
        val fromAmount = amount.toFloatOrNull()
        if(fromAmount == null){
            _conversion.value = CurrencyEvent.Failure("Not a valid Amount")
            return
        }

        viewModelScope.launch(dispatchers.io){
            _conversion.value = CurrencyEvent.Loading
            when(val rateResponse = repository.getCurrencyRate(toCurrency, fromCurrency)){
                is Resource.Error ->
                    _conversion.value = CurrencyEvent.Failure(rateResponse.message.toString())
                is Resource.Success -> {
                    val rates = rateResponse.data!!.rates
                    val rate = getRateForCurrency(toCurrency, rates)
                    if(rate == null){
                        _conversion.value = CurrencyEvent.Failure("Something went wrong...")
                    } else{
                       val conversionResult = round(fromAmount * rate )
                        _conversion.value = CurrencyEvent.Success(
                            "$fromAmount $fromCurrency = $conversionResult $toCurrency"
                        )
                    }
                }
            }
        }
    }

    private fun getRateForCurrency(currency: String, rates: Rates) : Double = when (currency) {
        "CAD" -> rates.cAD
        "HKD" -> rates.hKD
        "ISK" -> rates.iSK
        "DKK" -> rates.dKK
        "HUF" -> rates.hUF
        "CZK" -> rates.cZK
        "AUD" -> rates.aUD
        "IDR" -> rates.iDR
        "INR" -> rates.iNR
        "BRL" -> rates.bRL
        "HRK" -> rates.hRK
        "JPY" -> rates.jPY
        "CHF" -> rates.cHF
        "BGN" -> rates.bGN
        "CNY" -> rates.cNY
        "ILS" -> rates.iLS
        "GBP" -> rates.gBP
        else -> 0.00
    }
}

/*
"success": true,
"timestamp": 1675783683,
"base": "INR",
"date": "2023-02-07",
"rates": {
    "JPY": 1.591419
}*/
