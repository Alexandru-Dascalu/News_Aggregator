package uk.ac.swansea.alexandru.newsaggregator.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import uk.ac.swansea.alexandru.newsaggregator.fragments.AllFragment
import uk.ac.swansea.alexandru.newsaggregator.fragments.RecommendedFragment
import uk.ac.swansea.alexandru.newsaggregator.fragments.WelshFragment

class NewsStreamTabAdapter (activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return AllFragment()
            1 -> return RecommendedFragment()
            2 -> return WelshFragment()
        }

        return AllFragment()
    }

    override fun getItemCount(): Int {
        return 3
    }
}