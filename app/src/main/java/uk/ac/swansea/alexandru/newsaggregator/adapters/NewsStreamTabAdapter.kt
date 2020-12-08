package uk.ac.swansea.alexandru.newsaggregator.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import uk.ac.swansea.alexandru.newsaggregator.fragments.NewsStreamFragment
import uk.ac.swansea.alexandru.newsaggregator.fragments.RecommendedFragment
import uk.ac.swansea.alexandru.newsaggregator.fragments.WelshFragment

class NewsStreamTabAdapter (activity: AppCompatActivity, private val streams: List<String>) : FragmentStateAdapter(activity) {
    private val streamFragments: MutableList<NewsStreamFragment> = mutableListOf<NewsStreamFragment>()

    init {
        for(streamName in streams) {
            streamFragments.add(NewsStreamFragment(streamName))
        }
    }

    override fun createFragment(position: Int): Fragment {
        return NewsStreamFragment(streams[position])
    }

    override fun getItemCount(): Int {
        return 3
    }
}