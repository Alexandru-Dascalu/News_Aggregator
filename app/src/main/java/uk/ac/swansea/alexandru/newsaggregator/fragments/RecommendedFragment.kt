package uk.ac.swansea.alexandru.newsaggregator.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uk.ac.swansea.alexandru.newsaggregator.Article
import uk.ac.swansea.alexandru.newsaggregator.adapters.NewsCardAdapter
import uk.ac.swansea.alexandru.newsaggregator.R

class RecommendedFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val newsStreamRootView = inflater.inflate(R.layout.news_stream_fragment, container, false)

        return newsStreamRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val articleList = populateList()

        val recyclerView = view.findViewById<RecyclerView>(R.id.news_stream_recycler_view)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        val newsCardAdapter = NewsCardAdapter(articleList)
        recyclerView.adapter = newsCardAdapter
    }

    private fun populateList(): ArrayList<Article> {
        val list = ArrayList<Article>()
        val reasonList = arrayOf("Welsh Politics", "Cats", "Tennis")
        val imageList = arrayOf(R.drawable.drakefore, R.drawable.cat, R.drawable.halep)
        val titleList = arrayOf(
            "Live coronavirus updates as First Minister Mark Drakeford to give " +
                    "briefing after first week of fire-break lockdown",
            "Here are the cutest cat videos " +
                    "to get you through quarantine",
            "Simona Halep ready for a New York challenge"
        )
        val descriptionList = arrayOf(
            "First Minister Mark Drakeford is to give the Welsh Government coronavirus briefing today (October 30) as the first week of Wales' fire-break lockdown comes to an end.\n" +
                    "\n" +
                    "You can follow live news and updates here all day. The First Minister's press conference starts from 12.15pm.\n" +
                    "\n",
            "Cats! Long worshipped, very independent, sometimes can love and hurt, but " +
                    "always there in the clutch when you need some kitty loving. Long revered by " +
                    "ancient cultures and the internet, cats have been at the forefront of entertainment " +
                    "whether through myths or internet videos. Cat videos are, perhaps, the purest thing on the World Wide Web.",
            "When Simona Halep finished her first match at the Rogers Cup in Toronto earlier in August, she was asked how it felt to be back on the court after not touching a racquet for two weeks.\n" +
                    "\n" +
                    "Halep smiled and said, in her deadpan way, “Well, it felt like I didn’t play for two weeks.”"
        )

        val sourceList = arrayOf("WalesOnline", "CutestCats.com", "UsOpen.org")
        val timeList = arrayOf("2 hr ago", "8 hr ago", "3 hr ago")

        for (i in 0..2) {
            val article = Article()
            article.setReason(reasonList[i])
            article.setImage(imageList[i])
            article.setTitle(titleList[i])
            article.setDescription(descriptionList[i])
            article.setSource(sourceList[i])
            article.setTime(timeList[i])

            list.add(article)
        }

        return list
    }
}