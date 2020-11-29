package uk.ac.swansea.alexandru.newsaggregator.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import uk.ac.swansea.alexandru.newsaggregator.R
import uk.ac.swansea.alexandru.newsaggregator.model.NewsStream

class NewsStreamNameAdapter (private val streamList: List<NewsStream>) : RecyclerView.Adapter<NewsStreamNameAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val rowView = layoutInflater.inflate(R.layout.stream_name_layout, parent, false)

        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.streamNameTextView.text = streamList[position].name
    }

    override fun getItemCount(): Int {
        return streamList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val streamNameTextView: AppCompatTextView = itemView.findViewById<AppCompatTextView>(R.id.stream_name_textview)
    }
}