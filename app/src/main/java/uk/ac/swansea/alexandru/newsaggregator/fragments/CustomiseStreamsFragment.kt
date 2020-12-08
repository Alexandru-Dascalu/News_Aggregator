package uk.ac.swansea.alexandru.newsaggregator.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import uk.ac.swansea.alexandru.newsaggregator.Database
import uk.ac.swansea.alexandru.newsaggregator.R
import uk.ac.swansea.alexandru.newsaggregator.adapters.NewsStreamNameAdapter

class CustomiseStreamsFragment () : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("customisefragment", "Customise fragment on create")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val customiseRootView = inflater.inflate(R.layout.customise_streams_fragment, container, false)
        Log.i("customisefragment", "Customise fragment on create view")

        val addStreamButton = customiseRootView.findViewById<ImageButton>(R.id.add_stream_button)
        addStreamButton.setOnClickListener { view -> onAddCustomStream() }

        val removeStreamButton = customiseRootView.findViewById<ImageButton>(R.id.remove_stream_button)
        removeStreamButton.setOnClickListener {view -> onRemoveCustomStream(view)}

        return customiseRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       val recyclerView = view.findViewById<RecyclerView>(R.id.news_stream_names_recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = NewsStreamNameAdapter(activity as AppCompatActivity)

        Log.i("customisefragment", "Customise fragment on view created")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context )
        Log.i("customisefragment", "Customise fragment on attach")
    }

    override fun onStart() {
        super.onStart()
        Log.i("customisefragment", "Customise fragment on start")
    }

    override fun onResume() {
        super.onResume()
        Log.i("customisefragment", "Customise fragment on resume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("customisefragment", "Customise fragment on pause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("customisefragment", "Customise fragment on stop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("customisefragment", "Customise fragment on destroy view")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("customisefragment", "Customise fragment on destroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.i("customisefragment", "Customise fragment on detach")
    }

    fun onAddCustomStream() {
        val streamNameInput = EditText(context)

        val dialog = MaterialAlertDialogBuilder(context!!).setTitle(resources.getString(R.string.add_stream_dialog_title))
            .setView(streamNameInput)
            .setPositiveButton(resources.getString(R.string.add_msg), null)
            .setNeutralButton(resources.getString(R.string.cancel_msg)) { dialog, which -> }
            .show()

        val addButton: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        addButton.setOnClickListener() {view ->
            if(streamNameInput.text.toString() != "") {
                Database.instance.addCustomNewsStream(streamNameInput.text.toString())
                dialog.dismiss()
            } else {
                Snackbar.make(this.view!!, resources.getString(R.string.stream_name_type_in_message),
                    Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    fun onRemoveCustomStream(fragmentView: View) {
        val streamNameInput = EditText(context)

        val dialog = MaterialAlertDialogBuilder(context!!).setTitle(resources.getString(
            R.string.remove_stream_dialog_title))
            .setView(streamNameInput)
            .setPositiveButton(resources.getString(R.string.remove_msg), null)
            .setNeutralButton(resources.getString(R.string.cancel_msg)) { dialog, which -> }
            .show()

        val addButton: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        addButton.setOnClickListener() { view ->
            if (streamNameInput.text.toString() != "") {
                if (streamNameInput.text.toString() == context!!.resources.getString(R.string.all)) {
                    Snackbar.make(this.view!!, resources.getString(R.string.remove_all_stream_msg),
                        Snackbar.LENGTH_LONG).show()
                } else {
                    Database.instance.removeCustomNewsStream(streamNameInput.text.toString())
                    dialog.dismiss()
                }
            } else {
                Snackbar.make(this.view!!, resources.getString(R.string.stream_name_type_in_message),
                    Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}