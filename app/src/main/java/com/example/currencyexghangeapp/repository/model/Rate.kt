package com.example.currencyexghangeapp.repository.model

data class Rate(
    val from: Currency,
    val to: Currency,
    val rate: Float
)

enum class Currency { AUD, EUR, USD, CAD, RON }