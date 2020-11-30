package uk.ac.swansea.alexandru.newsaggregator.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import uk.ac.swansea.alexandru.newsaggregator.Database
import uk.ac.swansea.alexandru.newsaggregator.R
import uk.ac.swansea.alexandru.newsaggregator.model.User

class KeywordButtonAdapter (val streamName: String) : RecyclerView.Adapter<KeywordButtonAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val rowView = layoutInflater.inflate(R.layout.keyword_button_layout, parent, false)

        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val keyword: String = Database.instance.getKeywordList()[position]

        holder.keywordButton.text = keyword

        if(Database.instance.isKeywordSelectedInStream(keyword, streamName)) {
            selectKeywordButton(holder.keywordButton)
        }
    }

    override fun getItemCount(): Int {
        return Database.instance.getKeywordList().size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val keywordButton: MaterialButton = itemView.findViewById<MaterialButton>(R.id.keyword_button)

        init {
            keywordButton.setOnClickListener() {view ->
                val button = view as MaterialButton

                if(Database.instance.isKeywordSelectedInStream(button.text.toString(), streamName)) {
                    unSelectKeywordButton(button)
                    Database.instance.unSelectKeyword(streamName, button.text.toString())
                } else {
                    selectKeywordButton(button)
                    Database.instance.selectKeyword(streamName, button.text.toString())
                }
            }
        }
    }

    private fun selectKeywordButton(button: MaterialButton) {
        button.setBackgroundColor(ContextCompat.getColor(button.context, R.color.colorBackground))
        button.setTextColor(ContextCompat.getColor(button.context, R.color.colorPrimary))
        button.strokeWidth = 10
        button.setStrokeColorResource(R.color.colorPrimary)
    }

    private fun unSelectKeywordButton(button: MaterialButton) {
        button.setBackgroundColor(ContextCompat.getColor(button.context, R.color.colorPrimary))
        button.setTextColor(ContextCompat.getColor(button.context, R.color.onPrimary))
        button.strokeWidth = 0
    }
}