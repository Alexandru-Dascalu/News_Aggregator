package uk.ac.swansea.alexandru.newsaggregator

import android.os.Bundle
import android.os.PersistableBundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

class FullArticleActivity() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val articleWebView = WebView(this)
        articleWebView.isVerticalScrollBarEnabled = true
        articleWebView.settings.javaScriptEnabled = true
        setContentView(articleWebView)

        val webLink : String = intent.getStringExtra("LINK")!!
        articleWebView.loadUrl(webLink)
    }
}