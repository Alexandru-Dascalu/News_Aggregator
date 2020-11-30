package uk.ac.swansea.alexandru.newsaggregator.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import uk.ac.swansea.alexandru.newsaggregator.R

class KeywordButtonAdapter (private val keywords: List<String>, private val selectedKeywords: Set<String>) : RecyclerView.Adapter<KeywordButtonAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val rowView = layoutInflater.inflate(R.layout.keyword_button_layout, parent, false)

        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.keywordButton.text = keywords[position]
    }

    override fun getItemCount(): Int {
        return keywords.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val keywordButton = itemView.findViewById<Button>(R.id.keyword_button)

        init {
            keywordButton.setOnClickListener() {button ->

            }
        }
    }
}