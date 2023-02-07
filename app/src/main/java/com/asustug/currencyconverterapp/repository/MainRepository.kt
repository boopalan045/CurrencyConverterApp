package com.asustug.currencyconverterapp.repository

import com.asustug.currencyconverterapp.data.model.CurrencyResponse
import com.asustug.currencyconverterapp.utils.Resource

interface MainRepository {

    suspend fun getCurrencyRate(base : String, symbols : String) : Resource<CurrencyResponse>

}
