package com.example.quotesapp.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.quotesapp.Model.Quote
import com.google.gson.Gson

class MainViewModel(private val context: Context) : ViewModel() {

    private var quoteList: Array<Quote> = emptyArray()
    private var index = 0

    init {
        quoteList = loadQuoteFromAssets()
    }

    private fun loadQuoteFromAssets(): Array<Quote> {

        val inputStream = context.assets.open("quotes.json")
        val size: Int = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        val json = String(buffer, Charsets.UTF_8)
        val gson = Gson()
        return gson.fromJson(json, Array<Quote>::class.java)

    }

    fun getQuote() = quoteList[index]

    fun nextQuote(): Pair<Quote, Int> {
        return if (index < quoteList.size) {
            Pair(quoteList[++index], index)
        } else {
            Pair(getQuote(), index)
        }
    }

    fun prevQuote(): Pair<Quote, Int> {
        return if (index > 0) {
            Pair(quoteList[--index], index)
        } else {
            Pair(getQuote(), index)
        }
    }

}
