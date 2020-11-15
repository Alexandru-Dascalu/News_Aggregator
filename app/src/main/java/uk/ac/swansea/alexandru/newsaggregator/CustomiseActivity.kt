package uk.ac.swansea.alexandru.newsaggregator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth

class CustomiseActivity : AppCompatActivity() {

    private lateinit var authenticator : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customise)

        val appBar = findViewById<Toolbar>(R.id.customise_main_toolbar)
        setSupportActionBar(appBar)

        authenticator = FirebaseAuth.getInstance()

    }
}