package uk.ac.swansea.alexandru.newsaggregator.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.dfl.newsapi.NewsApiRepository
import com.dfl.newsapi.enums.Language
import com.dfl.newsapi.enums.SortBy
import com.dfl.newsapi.model.ArticleDto
import io.reactivex.schedulers.Schedulers

import uk.ac.swansea.alexandru.newsaggregator.Database
import uk.ac.swansea.alexandru.newsaggregator.adapters.NewsCardAdapter
import uk.ac.swansea.alexandru.newsaggregator.R

import java.lang.StringBuilder

class NewsStreamFragment(private val newsStreamName: String) : Fragment() {

    private lateinit var newsApi: NewsApiRepository
    private lateinit var newsCardAdapter : NewsCardAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        newsApi = NewsApiRepository(context.getString(R.string.news_api_key))
        newsCardAdapter = NewsCardAdapter(listOf<ArticleDto>(), this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val newsStreamRootView = inflater.inflate(R.layout.news_stream_fragment, container, false)

        return newsStreamRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        subscribeForArticles()

        val recyclerView = view.findViewById<RecyclerView>(R.id.news_stream_recycler_view)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = newsCardAdapter

        val swipeContainer = view.findViewById<SwipeRefreshLayout>(R.id.swipeLayout)
        swipeContainer.setOnRefreshListener {
            subscribeForArticles(swipeContainer)
        }
    }

     fun subscribeForArticles(swipeLayout: SwipeRefreshLayout? = null) {
        val articles = mutableListOf<ArticleDto>()

         if(newsStreamName != context!!.getString(R.string.recommended)) {
             newsApi.getEverything(q = getKeywordQuery(), domains = null, language = Language.EN, sortBy = SortBy.RELEVANCY, pageSize = 20, page = 1)
                 .subscribeOn(Schedulers.io())
                 .toFlowable()
                 .flatMapIterable { articlesDto -> articlesDto.articles }
                 .subscribe(
                     //onNext
                     {
                             article -> articles.add(article)
                     },
                     //onError
                     {
                             t -> Log.d("news api error", t.message!!)
                     },
                     //onComplete
                     {
                         articles.shuffle()
                         if(swipeLayout != null) {
                             activity!!.runOnUiThread {
                                 swipeLayout.isRefreshing = false
                             }
                         }

                         newsCardAdapter.onGetArticles(articles)
                     }
                 )
         } else {
             newsApi.getTopHeadlines(q = getKeywordQuery(), pageSize = 20, page = 1)
                 .subscribeOn(Schedulers.io())
                 .toFlowable()
                 .flatMapIterable { articlesDto -> articlesDto.articles }
                 .subscribe(
                     //onNext
                     { article -> articles.add(article) },
                     //onError
                     {
                             t -> Log.d("news api error", t.message!!)
                     },
                     //onComplete
                     {
                         articles.shuffle()
                         if(swipeLayout != null) {
                             activity!!.runOnUiThread {
                                 swipeLayout.isRefreshing = false
                             }
                         }
                         newsCardAdapter.onGetArticles(articles)
                     }
                 )
         }
    }

    private fun getKeywordQuery(): String {
        val streamKeywords = Database.instance.getKeywordsForStream(newsStreamName)

        if(streamKeywords.isEmpty()) {
            return ""
        }

        val builder = StringBuilder()
        builder.append("\"")
        builder.append(streamKeywords[0])
        builder.append("\"")

        for(i in 1 until streamKeywords.size) {
            builder.append(" OR ")
            builder.append("\"")
            builder.append(streamKeywords[i])
            builder.append("\"")
        }

        return builder.toString()
    }
}