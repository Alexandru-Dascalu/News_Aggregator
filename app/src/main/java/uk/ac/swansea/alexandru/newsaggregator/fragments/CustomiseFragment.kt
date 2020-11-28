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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import uk.ac.swansea.alexandru.newsaggregator.R
import uk.ac.swansea.alexandru.newsaggregator.adapters.NewsStreamTabAdapter
import uk.ac.swansea.alexandru.newsaggregator.model.User

class CustomiseFragment : Fragment() {
    private val database : FirebaseDatabase = Firebase.database
    private val newsTopicsReference : DatabaseReference = database.getReference("topics")
    private  val userReference: DatabaseReference

    init {
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        userReference = database.getReference("users").child(userID)
    }

    private lateinit var newsTopics : Map<String, Int>
    private lateinit var user : User

    private val newsTopicsListener =  object: ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val topics = dataSnapshot.getValue<Map<String, Int>>()
            if(topics != null) {
                newsTopics = topics
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.w("a", "loadPost:onCancelled", databaseError.toException())
        }
    }

    private val userListener =  object: ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val remoteUser = dataSnapshot.getValue<User>()
            if(remoteUser != null) {
                user = remoteUser
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.w("a", "loadPost:onCancelled", databaseError.toException())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newsTopicsReference.addValueEventListener(newsTopicsListener)
        userReference.addValueEventListener(userListener)
        Log.i("customisefragment", "Customise fragment on create")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val homeRootView = inflater.inflate(R.layout.customise_fragment, container, false)
        Log.i("customisefragment", "Customise fragment on create view")

        return homeRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val tabLayout = view.findViewById<TabLayout>(R.id.customisation_tab_layout)
        val viewPager = view.findViewById<ViewPager2>(R.id.customisation_stream_pager)
        val tabTitles = resources.getStringArray(R.array.tab_titles)

        viewPager.adapter = NewsStreamTabAdapter(activity as AppCompatActivity)
        TabLayoutMediator(tabLayout, viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when (position) {
                    0 -> tab.text = tabTitles[0]
                    1 -> tab.text = tabTitles[1]
                    2 -> tab.text = tabTitles[2]
                }
            }).attach()

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