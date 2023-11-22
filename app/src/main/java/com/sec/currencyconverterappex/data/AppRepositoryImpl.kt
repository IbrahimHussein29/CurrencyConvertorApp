package com.sec.currencyconverterappex.data

import com.sec.currencyconverterappex.data.common.Resource
import com.sec.currencyconverterappex.data.common.onError
import com.sec.currencyconverterappex.data.common.onSuccess
import com.sec.currencyconverterappex.data.dto.CurrencyResponse
import com.sec.currencyconverterappex.data.dto.Rates
import com.sec.currencyconverterappex.data.remote.ApiService
import com.sec.currencyconverterappex.data.remote.dataSource.BaseRemoteDataSource
import com.sec.currencyconverterappex.data.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class AppRepositoryImpl(var apiService: ApiService): AppRepository, BaseRemoteDataSource() {
    override fun fetchAllRates(base: String, apiKey: String): Flow<Resource<Rates>> {

          return flow {

               val remoteData= handleApiCall { apiService.fetchAllRates(base, apiKey) }
               remoteData.onSuccess {
                   emit(Resource.Success(it.rates))
               }.onError{ message, result ->
                   emit(Resource.Error(message=message, data = result?.rates))
               }
          }.flowOn(Dispatchers.IO)


    }
}