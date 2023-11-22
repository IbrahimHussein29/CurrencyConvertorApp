package com.sec.currencyconverterappex.ui

import android.content.ContentValues.TAG
import android.graphics.Color

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope

import com.sec.currencyconverterappex.data.common.onError
import com.sec.currencyconverterappex.data.common.onLoading
import com.sec.currencyconverterappex.data.common.onSuccess
import com.sec.currencyconverterappex.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnConvert.setOnClickListener {
          callData()

        }
        observeData()

    }

    private fun callData() {
        viewModel.convert(
            binding.etFrom.text.toString(),
            binding.spFromCurrency.selectedItem.toString(),
            binding.spToCurrency.selectedItem.toString()
        )
    }

    private fun observeData() {
        viewModel.currencyRates.observe(this) { Resource ->
            lifecycleScope.launch {

                Resource.onSuccess {
                    binding.progressbar.isVisible = false
                    binding.tvResult.setTextColor(Color.BLACK)
                    binding.tvResult.text = Resource.data
                }
                Resource.onLoading { binding.progressbar.isVisible = true }
                Resource.onError { message, _ ->
                    binding.progressbar.isVisible = false
                    binding.tvResult.setTextColor(Color.RED)
                    binding.tvResult.text = message
                }

            }
        }


    }
}