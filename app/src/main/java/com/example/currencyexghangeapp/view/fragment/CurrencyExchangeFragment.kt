package com.example.currencyexghangeapp.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.currencyexghangeapp.R
import com.example.currencyexghangeapp.databinding.FragmentCurrencyExchangeBinding
import com.example.currencyexghangeapp.viewmodel.CurrencyExchangeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencyExchangeFragment: BaseFragment<FragmentCurrencyExchangeBinding>() {

    companion object {
        fun newInstance() = CurrencyExchangeFragment()
    }

    private val viewModel: CurrencyExchangeViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.fragment_currency_exchange

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.view = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        observeForCurrencyExchangeRates()
        viewModel.getCurrencyExchange()
    }

    private fun observeForCurrencyExchangeRates() {
        viewModel.observeForCurrencyExchange().observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { exchange ->
                binding.currencyExchangeTextView.text = exchange.rates.firstOrNull()?.toString()
            }
        })
    }
}