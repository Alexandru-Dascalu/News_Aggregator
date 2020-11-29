package uk.ac.swansea.alexandru.newsaggregator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import uk.ac.swansea.alexandru.newsaggregator.fragments.CustomiseFragment
import uk.ac.swansea.alexandru.newsaggregator.fragments.HomeFragment
import uk.ac.swansea.alexandru.newsaggregator.model.NewsStream
import uk.ac.swansea.alexandru.newsaggregator.model.User

class MainActivity : AppCompatActivity() {
    private val authenticator = FirebaseAuth.getInstance()
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

    private val navigationBarItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.home_button -> {
                addHomeFragment()
                return@OnNavigationItemSelectedListener true
            }
            R.id.customise_button -> {
                addCustomiseFragment()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        newsTopicsReference.addValueEventListener(newsTopicsListener)
        userReference.addValueEventListener(userListener)

        val appBar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(appBar)

        findViewById<BottomNavigationView>(R.id.bottom_navigation_bar).
            setOnNavigationItemSelectedListener(navigationBarItemSelectedListener)

        authenticator.addAuthStateListener {
            if(authenticator.currentUser == null) {
                this.finish()
            }
        }

        addHomeFragment()
        Log.i("me", "activity is created")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_layout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.log_out_button) {
            authenticator.signOut()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun addHomeFragment() {
        replaceFragment(HomeFragment())
    }

    private fun addCustomiseFragment() {
        replaceFragment(CustomiseFragment(user))
    }

    private fun replaceFragment(newFragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.main_content_frame, newFragment)
        fragmentTransaction.commit()
    }

    override fun onStart() {
        super.onStart()
        Log.i("me", "Main start")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("me", "Main restart")
    }

    override fun onResume() {
        super.onResume()
        Log.i("me","Main resume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("me", "Main pause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("me", "Main stop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("me", "Main destroy")
    }
}