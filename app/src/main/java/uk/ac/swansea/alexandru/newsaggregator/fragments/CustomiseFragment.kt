package uk.ac.swansea.alexandru.newsaggregator.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uk.ac.swansea.alexandru.newsaggregator.R
import uk.ac.swansea.alexandru.newsaggregator.adapters.NewsStreamNameAdapter
import uk.ac.swansea.alexandru.newsaggregator.model.User

class CustomiseFragment (private val user: User) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("customisefragment", "Customise fragment on create")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val homeRootView = inflater.inflate(R.layout.customise_fragment, container, false)
        Log.i("customisefragment", "Customise fragment on create view")

        return homeRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       val recyclerView = view.findViewById<RecyclerView>(R.id.news_stream_names_recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = NewsStreamNameAdapter(user.customStreams)

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