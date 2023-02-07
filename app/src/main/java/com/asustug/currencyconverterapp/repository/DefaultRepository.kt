package com.asustug.currencyconverterapp.repository

import com.asustug.currencyconverterapp.data.CurrencyAPI
import com.asustug.currencyconverterapp.data.model.CurrencyResponse
import com.asustug.currencyconverterapp.utils.Resource
import javax.inject.Inject

class DefaultRepository @Inject constructor(
    private val api: CurrencyAPI
) : MainRepository{
    override suspend fun getCurrencyRate(base: String, symbols : String): Resource<CurrencyResponse> {
        return try {
            val response = api.getCurrencyRate(base,symbols)
            val result = response.body()
            if(response.isSuccessful && result != null){
                Resource.Success(result)
            } else{
                Resource.Error(response.message())
            }
        } catch (e : Exception){
            Resource.Error(e.localizedMessage)
        }
    }
}