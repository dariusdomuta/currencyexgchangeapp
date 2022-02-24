package com.example.currencyexghangeapp.repository.interactors

import com.example.currencyexghangeapp.repository.api.CurrencyExchangeApi
import com.example.currencyexghangeapp.repository.model.Exchange
import dagger.hilt.android.scopes.ActivityRetainedScoped
import io.reactivex.Flowable
import javax.inject.Inject

interface CurrencyExchangeUseCase {
    fun getCurrencyExchangeRates(): Flowable<Exchange>
}

@ActivityRetainedScoped
class CurrencyExchangeUseCaseImpl @Inject constructor(private val currencyExchangeApi: CurrencyExchangeApi) : CurrencyExchangeUseCase {

    override fun getCurrencyExchangeRates(): Flowable<Exchange> {
        return currencyExchangeApi.getCurrencyExchangeList()
    }
}