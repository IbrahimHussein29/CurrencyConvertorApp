package com.sec.currencyconverterappex.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sec.currencyconverterappex.data.AppRepositoryImpl
import com.sec.currencyconverterappex.data.common.Resource
import com.sec.currencyconverterappex.data.common.onError
import com.sec.currencyconverterappex.data.common.onSuccess
import com.sec.currencyconverterappex.data.dto.Rates

import com.sec.currencyconverterappex.data.remote.Constants

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.Response

import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class MainViewModel @Inject constructor(private var appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {
    private var _currencyRates = MutableLiveData<Resource<String>>()
    val currencyRates: LiveData<Resource<String>> = _currencyRates
    private var _loaderStatus = MutableLiveData<Boolean>()
    val loaderStatus: LiveData<Boolean> = _loaderStatus

    fun convert(amountStr: String, fromCurrency: String, toCurrency: String) {
        val fromAmount = amountStr.toFloatOrNull()
        if (fromAmount == null) {
            _currencyRates.value = Resource.Error(message = "Not a valid amount")
            return
        }
        viewModelScope.launch {
            _loaderStatus.value = true

            appRepositoryImpl.fetchAllRates(fromCurrency, Constants.API_KEY).collect {
                it.onSuccess { _ ->
        val rates=it.data!!
        val rate= getRateForCurrency(toCurrency, rates)
                    if(rate==null) {
                        _currencyRates.value = Resource.Error(message = "Unexpected error")

                    }else{
                        val convertedCurrency= round(fromAmount*rate*100)/100
                        _currencyRates.value = Resource.Success("$fromAmount $fromCurrency= $convertedCurrency $toCurrency")
                    }





                }.onError { _, _ ->
                    _currencyRates.value = Resource.Error(message="Not a valid data")
                }


            }
        }
    }
private fun getRateForCurrency(currency: String, rates: Rates) = when (currency) {
    "CAD" -> rates.CAD
    "HKD" -> rates.HKD
    "ISK" -> rates.ISK
    "EUR" -> rates.EUR
    "PHP" -> rates.PHP
    "DKK" -> rates.DKK
    "HUF" -> rates.HUF
    "CZK" -> rates.CZK
    "AUD" -> rates.AUD
    "RON" -> rates.RON
    "SEK" -> rates.SEK
    "IDR" -> rates.IDR
    "INR" -> rates.INR
    "BRL" -> rates.BRL
    "RUB" -> rates.RUB
    "HRK" -> rates.HRK
    "JPY" -> rates.JPY
    "THB" -> rates.THB
    "CHF" -> rates.CHF
    "SGD" -> rates.SGD
    "PLN" -> rates.PLN
    "BGN" -> rates.BGN
    "CNY" -> rates.CNY
    "NOK" -> rates.NOK
    "NZD" -> rates.NZD
    "ZAR" -> rates.ZAR
    "USD" -> rates.USD
    "MXN" -> rates.MXN
    "ILS" -> rates.ILS
    "GBP" -> rates.GBP
    "KRW" -> rates.KRW
    "MYR" -> rates.MYR
    else -> null
}


}
