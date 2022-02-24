package com.example.currencyexghangeapp.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencyexghangeapp.R
import com.example.currencyexghangeapp.databinding.FragmentCurrencyExchangeBinding
import com.example.currencyexghangeapp.view.adapter.CurrencyExchangesAdapter
import com.example.currencyexghangeapp.viewmodel.CurrencyExchangeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencyExchangeFragment: BaseFragment<FragmentCurrencyExchangeBinding>() {

    companion object {
        fun newInstance() = CurrencyExchangeFragment()
    }

    private val viewModel: CurrencyExchangeViewModel by viewModels()
    private var adapter: CurrencyExchangesAdapter? = null

    override fun getLayoutId(): Int = R.layout.fragment_currency_exchange

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.view = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        adapter = context?.let { CurrencyExchangesAdapter(it, listOf()) }
        binding.currencyExchangeRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.currencyExchangeRecyclerView.adapter = adapter

        observeForCurrencyExchangeRates()
        viewModel.getCurrencyExchange()
    }

    private fun observeForCurrencyExchangeRates() {
        viewModel.observeForCurrencyExchange().observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { exchange ->
                adapter?.submitList(exchange)
            }
        })
    }
}