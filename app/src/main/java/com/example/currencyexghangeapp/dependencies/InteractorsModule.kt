package com.example.currencyexghangeapp.dependencies

import com.example.currencyexghangeapp.repository.api.ErrorApiHandler
import com.example.currencyexghangeapp.repository.api.ErrorApiHandlerImpl
import com.example.currencyexghangeapp.repository.interactors.CurrencyExchangeUseCase
import com.example.currencyexghangeapp.repository.interactors.CurrencyExchangeUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class InteractorsModule {

    @Binds
    abstract fun bindsErrorAPiHandler(errorApiHandlerImpl: ErrorApiHandlerImpl): ErrorApiHandler

    @Binds
    abstract fun bindsCurrencyExchangeUseCase(currencyExchangeUseCaseImpl: CurrencyExchangeUseCaseImpl): CurrencyExchangeUseCase
}