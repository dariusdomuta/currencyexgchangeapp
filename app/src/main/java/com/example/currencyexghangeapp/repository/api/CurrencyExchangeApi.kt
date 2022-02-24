package com.example.currencyexghangeapp.repository.api

import com.example.currencyexghangeapp.repository.model.Exchange
import io.reactivex.Flowable
import retrofit2.http.GET

interface CurrencyExchangeApi {

    @GET("1")
    fun getCurrencyExchangeList(): Flowable<Exchange>
}