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

    private var currencyExchangeLiveData: MutableLiveData<Event<List<ExchangeDisplayableItem>>> = MutableLiveData()

    fun observeForCurrencyExchange(): LiveData<Event<List<ExchangeDisplayableItem>>> = currencyExchangeLiveData

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
                currencyExchangeLiveData.value = Event(it.toDisplayableItemsList())
            }, {
                sendHideLoadingEvent()
                sendErrorEvent(errorApiHandler.parseApiErrors(it).first().errorMessage)
            })
        )
    }

    private fun Exchange.toDisplayableItemsList(): List<ExchangeDisplayableItem> {
        return this.rates.map {
            ExchangeDisplayableItem(it.from.name, it.to.name, it.rate)
        }
    }
}

data class ExchangeDisplayableItem(val baseCurrency: String, val destinationCurrency: String, val rate: Float)
