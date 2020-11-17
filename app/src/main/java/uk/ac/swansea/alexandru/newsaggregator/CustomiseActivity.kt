package uk.ac.swansea.alexandru.newsaggregator

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class CustomiseActivity : AppCompatActivity() {

    private var authenticator : FirebaseAuth = FirebaseAuth.getInstance()

    private val navigationBarItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.home_button -> {
                Log.i("mama", "BLAH!")
                this.finish()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customise)

        val appBar = findViewById<Toolbar>(R.id.customise_main_toolbar)
        setSupportActionBar(appBar)

        authenticator = FirebaseAuth.getInstance()

        findViewById<BottomNavigationView>(R.id.customise_bottom_navigation_bar).setOnNavigationItemSelectedListener(navigationBarItemSelectedListener)

        authenticator.addAuthStateListener {
            if(authenticator.currentUser == null) {
                this.finish()
            }
        }
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
}