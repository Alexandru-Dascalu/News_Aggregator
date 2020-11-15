package uk.ac.swansea.alexandru.newsaggregator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var authenticator : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        authenticator = FirebaseAuth.getInstance()
    }

    fun createAccount(view: View) {
        val emailAddress = findViewById<EditText>(R.id.email_input).text.toString()
        val password = findViewById<EditText>(R.id.password_input).text.toString()

        if(emailAddress != "" && password != "") {
            authenticator.createUserWithEmailAndPassword(emailAddress, password)
            //Snackbar.make(view, "Account succesfully created!", Snackbar.LENGTH_SHORT).show()

            authenticator.signInWithEmailAndPassword(emailAddress, password)
            val mainActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(mainActivityIntent)
            this.finish()
        }
    }
}