package uk.ac.swansea.alexandru.newsaggregator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import uk.ac.swansea.alexandru.newsaggregator.fragments.HomeFragment

class MainActivity : AppCompatActivity() {
    private val authenticator = FirebaseAuth.getInstance()

    private val navigationBarItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.home_button -> {
                addHomeFragment()
                return@OnNavigationItemSelectedListener true
            }
            R.id.customise_button -> {
                val intent = Intent(this, CustomiseActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val homeFragment = HomeFragment()

        fragmentTransaction.add(R.id.main_content_fragment, homeFragment)
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