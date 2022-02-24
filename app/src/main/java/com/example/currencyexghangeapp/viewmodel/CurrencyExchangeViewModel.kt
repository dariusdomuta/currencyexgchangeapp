package com.example.currencyexghangeapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.currencyexghangeapp.repository.api.ErrorApiHandler
import com.example.currencyexghangeapp.repository.interactors.CurrencyExchangeUseCase
import com.example.currencyexghangeapp.repository.model.Exchange
import com.example.currencyexghangeapp.util.defaultSchedulers
import com.example.currencyexghangeapp.viewmodel.Event.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CurrencyExchangeViewModel @Inject constructor(private val currencyExchangeUseCase: CurrencyExchangeUseCase, private val errorApiHandler: ErrorApiHandler): RxViewModel() {

    private var currencyExchangeLiveData: MutableLiveData<Event<Exchange>> = MutableLiveData()

    fun observeForCurrencyExchange(): LiveData<Event<Exchange>> = currencyExchangeLiveData

    fun getCurrencyExchange() {
        loadCurrencyExchange()
    }

    fun clear() {
        currencyExchangeLiveData = MutableLiveData()
    }

    private fun loadCurrencyExchange() {
        subscribe(currencyExchangeUseCase.getCurrencyExchangeRates()
            .defaultSchedulers()
            .subscribe({
                sendHideLoadingEvent()
                currencyExchangeLiveData.value = Event(it)
            }, {
                sendHideLoadingEvent()
                sendErrorEvent(errorApiHandler.parseApiErrors(it).first().errorMessage)
            })
        )
    }
}