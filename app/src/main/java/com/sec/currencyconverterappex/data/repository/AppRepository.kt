package com.sec.currencyconverterappex.data.repository


import com.sec.currencyconverterappex.data.common.Resource

import com.sec.currencyconverterappex.data.dto.Rates
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    fun fetchAllRates(base:String, apiKey:String): Flow<Resource<Rates>>
}