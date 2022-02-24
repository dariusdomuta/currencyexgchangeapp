package com.example.currencyexghangeapp.repository.model

data class Exchange(
    val rates: List<Rate>,
    val pairs: List<Pair>
)