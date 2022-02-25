package com.example.currencyexghangeapp

import androidx.lifecycle.Observer
import com.example.currencyexghangeapp.repository.api.ErrorApiHandler
import com.example.currencyexghangeapp.repository.interactors.CurrencyExchangeUseCase
import com.example.currencyexghangeapp.repository.model.Currency
import com.example.currencyexghangeapp.repository.model.Exchange
import com.example.currencyexghangeapp.repository.model.Pair
import com.example.currencyexghangeapp.repository.model.Rate
import com.example.currencyexghangeapp.viewmodel.CurrencyExchangeViewModel
import com.example.currencyexghangeapp.viewmodel.ExchangeDisplayableItem
import com.example.currencyexghangeapp.viewmodel.event.Event
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*

class CurrencyExchangeViewModelTest: BaseViewModelTest() {

    @Mock
    private lateinit var currencyExchangeUseCase: CurrencyExchangeUseCase

    @Mock
    private lateinit var errorApiHandler: ErrorApiHandler

    private val displayableExchangeItemsObserver: Observer<Event<List<ExchangeDisplayableItem>>> = mock()

    private lateinit var viewModel: CurrencyExchangeViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        viewModel = CurrencyExchangeViewModel(currencyExchangeUseCase, errorApiHandler)

        viewModel.observeForCurrencyExchange().observeForever(displayableExchangeItemsObserver)
    }


    @Test
    fun getCurrencyExchange_success_shouldComputeCorrectRates() {
        initMocks()

        viewModel.getCurrencyExchange()

        val displayableCurrencyExchangeCaptor = argumentCaptor<Event<List<ExchangeDisplayableItem>>>()
        verify(displayableExchangeItemsObserver).onChanged(displayableCurrencyExchangeCaptor.capture())

        val expectedResult = listOf(ExchangeDisplayableItem("EUR", "USD", 1.1385f))
        assert(displayableCurrencyExchangeCaptor.firstValue.peekContent() == expectedResult)
    }

    private fun initMocks() {
        val defaultExchange = Exchange(listOf(
            Rate(Currency.EUR, Currency.RON, 4.95f),
            Rate(Currency.RON, Currency.USD, 0.23f)
        ), listOf(
            Pair(Currency.EUR, Currency.USD)
        ))

        whenever(currencyExchangeUseCase.getCurrencyExchangeRates()).thenReturn(Flowable.just(defaultExchange))
        whenever(errorApiHandler.parseApiErrors(any())).thenReturn(listOf())
    }
}