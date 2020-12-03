package uk.ac.swansea.alexandru.newsaggregator.adapters

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import uk.ac.swansea.alexandru.newsaggregator.Database
import uk.ac.swansea.alexandru.newsaggregator.R

class KeywordButtonAdapter (val streamName: String) : RecyclerView.Adapter<KeywordButtonAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val rowView = layoutInflater.inflate(R.layout.keyword_button_layout, parent, false)

        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position == 0) {
            holder.keywordButton.text = "+"
            selectKeywordButton(holder.keywordButton)
            holder.keywordButton.textSize = 50F
        } else {
            val keyword: String = Database.instance.getKeywordList()[position - 1]

            holder.keywordButton.text = keyword

            if(Database.instance.isKeywordSelectedInStream(keyword, streamName)) {
                selectKeywordButton(holder.keywordButton)
            }
        }
    }

    override fun getItemCount(): Int {
        return Database.instance.getKeywordList().size + 1
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val keywordButton: MaterialButton = itemView.findViewById<MaterialButton>(R.id.keyword_button)

        init {
            if(layoutPosition > 0) {
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
            } else {
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

    private fun onAddCustomStream(button: MaterialButton) {
        val keywordInput = EditText(button.context)

        val dialog = MaterialAlertDialogBuilder(button.context!!).setTitle(
            button.context.resources.getString(R.string.add_stream_dialog_title))
            .setView(keywordInput)
            .setPositiveButton(button.context.resources.getString(R.string.add_msg), null)
            .setNeutralButton(button.context.resources.getString(R.string.cancel_msg)) { dialog, which -> }
            .show()

        val addButton: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        addButton.setOnClickListener() {view ->
            if(keywordInput.text.toString() != "") {
                Database.instance.addCustomNewsStream(keywordInput.text.toString())
                dialog.dismiss()
            } else {
                Snackbar.make(this.view!!, resources.getString(R.string.stream_name_type_in_message),
                    Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}