package uk.ac.swansea.alexandru.newsaggregator.adapters

import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.dfl.newsapi.model.ArticleDto
import com.squareup.picasso.Picasso
import uk.ac.swansea.alexandru.newsaggregator.NewsApiCallback
import uk.ac.swansea.alexandru.newsaggregator.R
import uk.ac.swansea.alexandru.newsaggregator.fragments.NewsStreamFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class NewsCardAdapter(private var articleList: List<ArticleDto>,
                      private val newsStreamFragment: NewsStreamFragment)
    : RecyclerView.Adapter<NewsCardAdapter.ViewHolder>(), NewsApiCallback {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.article_layout, parent, false)
        Log.i("news card adapter", "view holder created")

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articleList[position]

        holder.displayReason.text = "blah"
        holder.articleTitle.text = article.title
        holder.articleDescription.text = article.description
        holder.articleSource.text = article.source.name
        holder.publicationTime.text = "• ${getPublishTimeAgo(article.publishedAt)}"

        Picasso.get().load(article.urlToImage).into(holder.articleImage)

        Log.i("news card adapter", "view holder bound")
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val displayReason: AppCompatTextView = itemView.findViewById<AppCompatTextView>(R.id.displayReason)
        val articleImage: AppCompatImageView = itemView.findViewById<AppCompatImageView>(R.id.articleImage)
        val articleTitle: AppCompatTextView = itemView.findViewById<AppCompatTextView>(R.id.articleTitle)
        val articleDescription: AppCompatTextView = itemView.findViewById<AppCompatTextView>(R.id.articleDescription)
        val articleSource: AppCompatTextView = itemView.findViewById<AppCompatTextView>(R.id.articleSource)
        val publicationTime: AppCompatTextView = itemView.findViewById<AppCompatTextView>(R.id.publicationTime)
    }

    override fun onGetArticles(articles: List<ArticleDto>) {
        newsStreamFragment.activity!!.runOnUiThread {
            articleList = articles
            this.notifyDataSetChanged()
        }
    }

    /**
     * Gets a nicely string describing when the article was published. If it was published less
     * than 7 days ago, the string will be 'x days ago' or 'x minutes ago'. It might be in another
     * language, depending on your Locale. If the article is more than 7 days old, it will display
     * the date, such 24 nov, 2020 .
     * @param publishingTime The publishing time of the article, in the yyyy-MM-dd'T'HH:mm:ss'Z' format.
     * @return a string representing the publishing time relative to the current time.
     */
    private fun getPublishTimeAgo(publishingTime: String) : String {
        val utcDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val publishTimeDate: Date = utcDateFormat.parse(publishingTime)!!

        val timeAgo: String = DateUtils.getRelativeTimeSpanString(
            publishTimeDate.time,
            Calendar.getInstance().timeInMillis,
            DateUtils.MINUTE_IN_MILLIS
        ).toString()

        return timeAgo
    }
}