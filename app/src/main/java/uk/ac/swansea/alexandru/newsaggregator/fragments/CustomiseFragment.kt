package uk.ac.swansea.alexandru.newsaggregator.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import uk.ac.swansea.alexandru.newsaggregator.R

class CustomiseFragment : Fragment() {
    private val database = Firebase.database
    private val reference = database.getReference("topics")

    private val dataListener =  object: ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            Log.i("Kav", dataSnapshot.getValue<ArrayList<String>>().toString())
            val textView = view!!.findViewById<TextView>(R.id.displayReason)
            textView.text = dataSnapshot.getValue<ArrayList<String>>().toString()
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.w("a", "loadPost:onCancelled", databaseError.toException())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reference.addValueEventListener(dataListener)
        Log.i("customisefragment", "Customise fragment on create")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val homeRootView = inflater.inflate(R.layout.customise_fragment, container, false)
        Log.i("customisefragment", "Customise fragment on create view")

        return homeRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
}