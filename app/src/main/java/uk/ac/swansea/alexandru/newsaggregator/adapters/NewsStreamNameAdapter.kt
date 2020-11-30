package uk.ac.swansea.alexandru.newsaggregator.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import uk.ac.swansea.alexandru.newsaggregator.R
import uk.ac.swansea.alexandru.newsaggregator.fragments.CustomiseKeywordFragment
import uk.ac.swansea.alexandru.newsaggregator.model.NewsStream
import uk.ac.swansea.alexandru.newsaggregator.model.User

class NewsStreamNameAdapter (private val user: User, private val topics: List<String>, private val activity: AppCompatActivity) : RecyclerView.Adapter<NewsStreamNameAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val rowView = layoutInflater.inflate(R.layout.stream_name_layout, parent, false)

        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.streamNameTextView.text = user.customStreams[position].name
    }

    override fun getItemCount(): Int {
        return user.customStreams.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val streamNameTextView: AppCompatTextView = itemView.findViewById<AppCompatTextView>(R.id.stream_name_textview)

        init {
            itemView.setOnClickListener() { item ->
                val fragmentTransaction = activity.supportFragmentManager.beginTransaction()

                val stream: NewsStream = user.customStreams.first { stream -> stream.name ==
                        streamNameTextView.text.toString() }

                fragmentTransaction.replace(R.id.main_content_frame, CustomiseKeywordFragment(getKeywords()))
                fragmentTransaction.addToBackStack("key_word_fragment")
                fragmentTransaction.commit()
            }
        }
    }

    private fun getKeywords() : List<String> {
        val keywords = mutableListOf<String>()
        keywords.addAll(topics)
        keywords.addAll(user.customKeywords)

        return keywords
    }
}