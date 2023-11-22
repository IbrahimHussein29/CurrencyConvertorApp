package com.sec.currencyconverterappex.data.dto

import java.io.Serializable

data class CurrencyResponse(
    val base: String,
    val date: String,
    val rates: Rates,
    val success: Boolean,
    val timestamp: Int
): Serializable