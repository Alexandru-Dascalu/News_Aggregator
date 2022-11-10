package uk.ac.swansea.dascalu.newsaggregator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

import uk.ac.swansea.dascalu.newsaggregator.utils.hideKeyboard

val DEBUG : Boolean = true

class LogInActivity : AppCompatActivity() {

    private lateinit var authenticator : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        authenticator = FirebaseAuth.getInstance()
    }

    fun logIn(view: View) {
        var emailAddress = findViewById<EditText>(R.id.email_input).text.toString()
        var password = findViewById<EditText>(R.id.password_input).text.toString()
        if(DEBUG) {
            emailAddress = "alexandru.dascalu100@gmail.com"
            password = "123456"
        }

        if(emailAddress != "" && password != "") {
            val task = authenticator.signInWithEmailAndPassword(emailAddress, password)

            task.addOnCompleteListener(this) {logInTask -> checkLogIn(logInTask, view)}
        } else {
            Snackbar.make(view, getString(R.string.log_in_credentials_msg), Snackbar.LENGTH_LONG).show()
            hideKeyboard(view, this)
        }
    }

    fun signUp(view: View) {
        val signUpIntent = Intent(this, SignUpActivity::class.java)
        startActivity(signUpIntent)
    }

    private fun checkLogIn(task: Task<AuthResult>, view: View) {
        if (task.isSuccessful) {
            val mainActivityIntent = Intent(this, LoadingActivity::class.java)
            startActivity(mainActivityIntent)
        } else {
            Snackbar.make(view, getString(R.string.wrong_login_msg), Snackbar.LENGTH_LONG).show()
            val currentView = this.currentFocus

            if(currentView != null) {
                hideKeyboard(currentView, this)
            }
        }
    }

    override fun onStop() {
        findViewById<EditText>(R.id.email_input).text.clear()
        findViewById<EditText>(R.id.password_input).text.clear()
        super.onStop()
    }
}