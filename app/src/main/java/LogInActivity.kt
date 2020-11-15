package uk.ac.swansea.alexandru.newsaggregator

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LogInActivity : AppCompatActivity() {

    private lateinit var authenticator : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in)
        authenticator = FirebaseAuth.getInstance()
    }

    fun signIn(view: View) {
        val emailInput = findViewById<EditText>(R.id.email_input)
        val passwordInput = findViewById<EditText>(R.id.password_input)

        val task = authenticator.signInWithEmailAndPassword(emailInput.text.toString(),
            passwordInput.text.toString())

        task.addOnCompleteListener(this) {logInTask -> checkLogIn(logInTask, view)}
    }

    private fun checkLogIn(task: Task<AuthResult>, view: View) {
        if (task.isSuccessful) {
            val user = authenticator.currentUser
            val mainActivityIntent = Intent(this, MainActivity::class.java)
            mainActivityIntent.putExtra("userEmail", user?.email)
            startActivity(mainActivityIntent)
        } else {
            Snackbar.make(view, "WRONG!", Snackbar.LENGTH_LONG).show()
            val currentView = this.currentFocus

            if(currentView != null) {
                val keyboardManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                keyboardManager.hideSoftInputFromWindow(currentView.windowToken, 0)
            }
        }
    }
}