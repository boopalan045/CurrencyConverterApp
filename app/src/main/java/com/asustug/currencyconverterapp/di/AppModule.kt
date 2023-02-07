package com.asustug.currencyconverterapp.di

import com.asustug.currencyconverterapp.data.CurrencyAPI
import com.asustug.currencyconverterapp.repository.DefaultRepository
import com.asustug.currencyconverterapp.repository.MainRepository
import com.asustug.currencyconverterapp.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val BASE_URL = "https://api.apilayer.com/" ;


    @Singleton
    @Provides
    fun provideCurrencyAPI(): CurrencyAPI = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CurrencyAPI::class.java)

    val okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun provideMainRepository(api: CurrencyAPI): MainRepository = DefaultRepository(api)

    @Singleton
    @Provides
    fun provideDispatchers() : DispatcherProvider = object : DispatcherProvider{
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined

    }

}