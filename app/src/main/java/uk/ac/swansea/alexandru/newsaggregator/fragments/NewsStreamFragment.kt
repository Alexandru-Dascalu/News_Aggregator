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
import com.dfl.newsapi.NewsApiRepository
import com.dfl.newsapi.enums.Language
import com.dfl.newsapi.enums.SortBy
import com.dfl.newsapi.model.ArticleDto
import io.reactivex.schedulers.Schedulers
import uk.ac.swansea.alexandru.newsaggregator.Database
import uk.ac.swansea.alexandru.newsaggregator.model.Article
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeForArticles()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val newsStreamRootView = inflater.inflate(R.layout.news_stream_fragment, container, false)

        return newsStreamRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val articleList = populateList()
        subscribeForArticles()

        val recyclerView = view.findViewById<RecyclerView>(R.id.news_stream_recycler_view)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = newsCardAdapter
    }

     fun subscribeForArticles() {
        val articles = mutableListOf<ArticleDto>()

        newsApi.getEverything(q = getKeywordQuery(), domains = null, language = Language.EN, sortBy = SortBy.RELEVANCY, pageSize = 20, page = 1)
            .subscribeOn(Schedulers.io())
            .toFlowable()
            .flatMapIterable { articles -> articles.articles }
            .subscribe(
                //onNext
                { article -> articles.add(article) },
                //onError
                { t -> Log.d("geLotEverything error", t.message!!) },
                //onComplete
                {
                    articles.shuffle()
                    newsCardAdapter.onGetArticles(articles)
                }
            )
    }

    private fun getKeywordQuery(): String {
        val streamKeywords = Database.instance.getKeywordsForStream(newsStreamName)
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

    private fun populateList(): ArrayList<Article> {
        val list = ArrayList<Article>()
        val reasonList = arrayOf("Welsh Politics", "Cats", "Tennis")
        val imageList = arrayOf(R.drawable.drakefore, R.drawable.cat, R.drawable.halep)
        val titleList = arrayOf(
            "Live coronavirus updates as First Minister Mark Drakeford to give " +
                    "briefing after first week of fire-break lockdown",
            "Here are the cutest cat videos " +
                    "to get you through quarantine",
            "Simona Halep ready for a New York challenge"
        )
        val descriptionList = arrayOf(
            "First Minister Mark Drakeford is to give the Welsh Government coronavirus briefing today (October 30) as the first week of Wales' fire-break lockdown comes to an end.\n" +
                    "\n" +
                    "You can follow live news and updates here all day. The First Minister's press conference starts from 12.15pm.\n" +
                    "\n",
            "Cats! Long worshipped, very independent, sometimes can love and hurt, but " +
                    "always there in the clutch when you need some kitty loving. Long revered by " +
                    "ancient cultures and the internet, cats have been at the forefront of entertainment " +
                    "whether through myths or internet videos. Cat videos are, perhaps, the purest thing on the World Wide Web.",
            "When Simona Halep finished her first match at the Rogers Cup in Toronto earlier in August, she was asked how it felt to be back on the court after not touching a racquet for two weeks.\n" +
                    "\n" +
                    "Halep smiled and said, in her deadpan way, “Well, it felt like I didn’t play for two weeks.”"
        )

        val sourceList = arrayOf("WalesOnline", "CutestCats.com", "UsOpen.org")
        val timeList = arrayOf("2 hr ago", "8 hr ago", "3 hr ago")

        for (i in 0..2) {
            val article = Article()
            article.setReason(reasonList[i])
            article.setImage(imageList[i])
            article.setTitle(titleList[i])
            article.setDescription(descriptionList[i])
            article.setSource(sourceList[i])
            article.setTime(timeList[i])

            list.add(article)
        }

        return list
    }
}