package uk.ac.swansea.dascalu.newsaggregator.adapters

import android.app.AlertDialog
import android.view.*
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import uk.ac.swansea.dascalu.newsaggregator.model.Database
import uk.ac.swansea.dascalu.newsaggregator.R
import java.util.Locale

class KeywordButtonAdapter (private val streamName: String, private val parentFragment: Fragment) :
    RecyclerView.Adapter<KeywordButtonAdapter.ViewHolder>() {

    private var actionMode: ActionMode? = null

    private val addClickListener = View.OnClickListener {view -> onAddCustomKeyword(
        view as MaterialButton)}

     private val keywordClickListener = View.OnClickListener { view ->
        val button = view as MaterialButton

        if(Database.getInstance().isKeywordSelectedInStream(button.text.toString(), streamName)) {
            unSelectKeywordButton(button)
            Database.getInstance().unSelectKeyword(streamName, button.text.toString())
        } else {
            selectKeywordButton(button)
            Database.getInstance().selectKeyword(streamName, button.text.toString())
        }
    }

    private val actionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            val inflater: MenuInflater = mode.menuInflater
            inflater.inflate(R.menu.keyword_delete_appbar_layout, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.remove_keyword -> {
                    Database.getInstance().removeCustomKeyword(actionMode!!.tag.toString().decapitalize())
                    notifyDataSetChanged()
                    mode.finish()
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            actionMode = null
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val rowView = layoutInflater.inflate(R.layout.keyword_button_layout, parent, false)

        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position == 0) {
            holder.keywordButton.text = "+"
            setAddButtonLook(holder.keywordButton)
            holder.keywordButton.textSize = 50F
            holder.keywordButton.setOnClickListener(addClickListener)
            holder.keywordButton.setOnLongClickListener(null)
        } else {
            val keyword: String = Database.getInstance().getKeywordList()[position - 1]

            holder.keywordButton.text = keyword

            if(Database.getInstance().isKeywordSelectedInStream(keyword, streamName)) {
                selectKeywordButton(holder.keywordButton)
            }
        }
    }

    override fun getItemCount(): Int {
        return Database.getInstance().getKeywordList().size + 1
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val keywordButton: MaterialButton = itemView.findViewById<MaterialButton>(R.id.keyword_button)

        init {
            keywordButton.setOnClickListener(keywordClickListener)
            keywordButton.setOnLongClickListener { view ->
                when (actionMode) {
                    null -> {
                        actionMode = parentFragment.requireActivity().startActionMode(actionModeCallback)
                        actionMode!!.title = "${parentFragment.requireContext().resources.getString(
                            R.string.remove_keyword_msg)} ${keywordButton.text}"
                        actionMode!!.tag = keywordButton.text.toString()

                        view.isSelected = true
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun selectKeywordButton(button: MaterialButton) {
        button.setBackgroundColor(ContextCompat.getColor(button.context, R.color.grey))
        button.setTextColor(ContextCompat.getColor(button.context, R.color.blue))
        button.strokeWidth = 10
        button.setStrokeColorResource(R.color.blue)
    }

    private fun unSelectKeywordButton(button: MaterialButton) {
        button.setBackgroundColor(ContextCompat.getColor(button.context, R.color.blue))
        button.setTextColor(ContextCompat.getColor(button.context, R.color.black))
        button.strokeWidth = 0
    }

    private fun setAddButtonLook(button: MaterialButton) {
        button.setBackgroundColor(ContextCompat.getColor(button.context, R.color.lightGreen))
        button.setTextColor(ContextCompat.getColor(button.context, R.color.grey))
        button.strokeWidth = 0
    }

    private fun onAddCustomKeyword(button: MaterialButton) {
        val keywordInput = EditText(button.context)

        val dialog = MaterialAlertDialogBuilder(button.context!!).setTitle(
            button.context.resources.getString(R.string.add_keyword_dialog_title))
            .setView(keywordInput)
            .setPositiveButton(button.context.resources.getString(R.string.add_msg), null)
            .setNeutralButton(button.context.resources.getString(R.string.cancel_msg)) { _, _ -> }
            .show()

        val addButton: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        addButton.setOnClickListener() {view ->
            if(keywordInput.text.toString() != "") {
                Database.getInstance().addCustomKeyword(keywordInput.text.toString().decapitalize(Locale.ROOT))
                notifyDataSetChanged()
                dialog.dismiss()
            } else {
                Snackbar.make(parentFragment.requireView(), view.context.getString(R.string.keyword_type_in_message),
                    Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}