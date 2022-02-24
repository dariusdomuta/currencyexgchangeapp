package com.example.currencyexghangeapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyexghangeapp.R
import com.example.currencyexghangeapp.viewmodel.ExchangeDisplayableItem
import kotlinx.android.synthetic.main.item_currency_exchange.view.*

class CurrencyExchangesAdapter(val context: Context, private var exchangesList: List<ExchangeDisplayableItem>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = inflater.inflate(R.layout.item_currency_exchange, parent, false)
        return ExchangeViewHolder(view)
    }

    override fun getItemCount(): Int = exchangesList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ExchangeViewHolder)?.bind(exchangesList[position])
    }

    fun submitList(exchangesList: List<ExchangeDisplayableItem>) {
        this.exchangesList = exchangesList
        notifyDataSetChanged()
    }

    inner class ExchangeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(currentExchange: ExchangeDisplayableItem) {
            itemView.baseCurrencyTextView.text = currentExchange.baseCurrency
            itemView.destinationCurrencyTextView.text = currentExchange.destinationCurrency
            itemView.exchangeRateTextView.text = String.format(context.resources.getString(R.string.exchange_rate_text), currentExchange.rate)
        }
    }
}
