package uk.ac.swansea.alexandru.newsaggregator

import com.dfl.newsapi.model.ArticleDto

interface NewsApiCallback {
    fun onGetArticles(articles: List<ArticleDto>)
}