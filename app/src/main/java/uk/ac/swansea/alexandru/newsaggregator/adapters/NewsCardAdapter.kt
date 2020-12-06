package uk.ac.swansea.alexandru.newsaggregator.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import uk.ac.swansea.alexandru.newsaggregator.model.Article
import uk.ac.swansea.alexandru.newsaggregator.R

class NewsCardAdapter (private val articleList: List<Article>) : RecyclerView.Adapter<NewsCardAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.article_layout, parent, false)
        Log.i("news card adapter", "view holder created")

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articleList[position]

        holder.displayReason.text = article.getReason()
        holder.articleImage.setImageResource(article.getImage())
        holder.articleTitle.text = article.getTitle()
        holder.articleDescription.text = article.getDescription()
        holder.articleSource.text = article.getSource()
        holder.publicationTime.text = article.getTime()

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
}