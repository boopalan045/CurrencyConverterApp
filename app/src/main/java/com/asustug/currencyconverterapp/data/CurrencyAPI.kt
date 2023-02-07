package com.asustug.currencyconverterapp.data

import com.asustug.currencyconverterapp.data.model.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CurrencyAPI {

    @Headers("apikey: 2x9HAEiqqpGS1pFsc0c0eWawJZ1qZxKt")
    @GET("/exchangerates_data/latest")
    suspend fun getCurrencyRate(
        @Query("symbols") symbols : String,
        @Query("base") base : String
    ): Response<CurrencyResponse>

}