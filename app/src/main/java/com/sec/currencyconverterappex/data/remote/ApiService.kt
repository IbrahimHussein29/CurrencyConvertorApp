package com.sec.currencyconverterappex.data.remote

import com.sec.currencyconverterappex.data.dto.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/v1/latest")
    suspend fun fetchAllRates(
        @Query("base") base:String,
        @Query("api_key") apiKey: String

    ): Response<CurrencyResponse>
}