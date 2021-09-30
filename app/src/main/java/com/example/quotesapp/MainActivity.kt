package com.example.quotesapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnSystemUiVisibilityChangeListener
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.quotesapp.Model.Quote
import com.example.quotesapp.ViewModel.MainViewModel
import com.example.quotesapp.ViewModel.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var mainViewModel: MainViewModel

    private val quoteText: TextView
        get() = findViewById(R.id.quoteText)

    private val currentApiVersion = Build.VERSION.SDK_INT


    private val quoteAuthor: TextView
        get() = findViewById(R.id.quoteAuthor)

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Fullscreen

        val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        // This work only for android 4.4+
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility = flags

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            val decorView = window.decorView
            decorView
                .setOnSystemUiVisibilityChangeListener { visibility ->
                    if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                        decorView.systemUiVisibility = flags
                    }
                }
        }


        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(applicationContext)
        ).get(MainViewModel::class.java)

        setQuote(mainViewModel.getQuote())
    }

    @SuppressLint("NewApi")
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    fun setQuote(quote: Quote) {
        quoteText.text = quote.text
        quoteAuthor.text = quote.author
    }

    fun onPrevious(view: View) {
        var (singleQuote, index) = mainViewModel.prevQuote()
        val prevBtn: TextView = view.findViewById(R.id.prevBtn)
        when {
            index == 0 -> {
                Log.i("Index", index.toString())
                setQuote(singleQuote)
                prevBtn.isEnabled = false
            }
            index > 0 -> {
                setQuote(singleQuote)
            }
            else -> {
                //
            }
        }
    }

    fun onNext(view: View) {
        var (singleQuote, index) = mainViewModel.nextQuote()
        val prevBtn: TextView = findViewById(R.id.prevBtn)
        prevBtn.isEnabled = true
        Log.i("Index", index.toString())
        setQuote(singleQuote)
    }

    fun onShare(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")
        intent.putExtra(Intent.EXTRA_TEXT, mainViewModel.getQuote().text)
        startActivity(intent)
    }

    public fun abc() {}
}