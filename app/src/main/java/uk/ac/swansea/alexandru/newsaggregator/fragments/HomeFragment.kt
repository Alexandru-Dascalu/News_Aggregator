package uk.ac.swansea.alexandru.newsaggregator.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import uk.ac.swansea.alexandru.newsaggregator.Database
import uk.ac.swansea.alexandru.newsaggregator.R
import uk.ac.swansea.alexandru.newsaggregator.adapters.NewsStreamTabAdapter

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val homeRootView = inflater.inflate(R.layout.home_fragment, container, false)
        Log.i("homefragment", "Home fragment on create view")

        return homeRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        val viewPager = view.findViewById<ViewPager2>(R.id.stream_pager)

        val newsStreams = Database.instance.getUserCustomStreamNames()
        //newsStreams.add(context!!.resources.getString(R.string.recommended))

        viewPager.adapter = NewsStreamTabAdapter(activity as AppCompatActivity, newsStreams)
        TabLayoutMediator(tabLayout, viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when (position) {
                    0 -> tab.text = newsStreams[0]
                    1 -> if (newsStreams.size > 1) tab.text = newsStreams[1]
                    2 -> if (newsStreams.size > 2) tab.text = newsStreams[2]
                }
            }).attach()

        Log.i("homefragment", "Home fragment on view created")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context )
        Log.i("homefragment", "Home fragment on attach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("homefragment", "Home fragment on create")
    }

    override fun onStart() {
        super.onStart()
        Log.i("homefragment", "Home fragment on start")
    }

    override fun onResume() {
        super.onResume()
        Log.i("homefragment", "Home fragment on resume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("homefragment", "Home fragment on pause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("homefragment", "Home fragment on stop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("homefragment", "Home fragment on destroy view")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("homefragment", "Home fragment on destroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.i("homefragment", "Home fragment on detach")
    }
}