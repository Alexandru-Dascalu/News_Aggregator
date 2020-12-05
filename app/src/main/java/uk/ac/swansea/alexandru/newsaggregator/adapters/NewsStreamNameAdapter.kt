package uk.ac.swansea.alexandru.newsaggregator.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import uk.ac.swansea.alexandru.newsaggregator.Database
import uk.ac.swansea.alexandru.newsaggregator.R
import uk.ac.swansea.alexandru.newsaggregator.fragments.CustomiseKeywordFragment

class NewsStreamNameAdapter (private val activity: AppCompatActivity) : RecyclerView.Adapter<NewsStreamNameAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val rowView = layoutInflater.inflate(R.layout.stream_name_layout, parent, false)

        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.streamNameTextView.text = Database.instance.getUser().customStreams[position].name
    }

    override fun getItemCount(): Int {
        return Database.instance.getUser().customStreams.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val streamNameTextView: AppCompatTextView = itemView.findViewById<AppCompatTextView>(R.id.stream_name_textview)

        init {
            itemView.setOnClickListener() { item ->
                val fragmentTransaction = activity.supportFragmentManager.beginTransaction()

                val stream: String = streamNameTextView.text.toString()

                fragmentTransaction.replace(R.id.main_content_frame, CustomiseKeywordFragment(stream))
                fragmentTransaction.addToBackStack("key_word_fragment")
                fragmentTransaction.commit()
            }
        }
    }
}