package uk.ac.swansea.alexandru.newsaggregator

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.*
import uk.ac.swansea.alexandru.newsaggregator.utils.hideKeyboard

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
            val task: Task<AuthResult> = authenticator.createUserWithEmailAndPassword(
                emailAddress, password)
            task.addOnCompleteListener(this) {signUpTask -> checkSignUp(view, signUpTask)}
        }
    }

    private fun checkSignUp(view: View, signUpTask: Task<AuthResult>) {
        if(signUpTask.isSuccessful) {
            this.finish()
        } else {
            try {
                throw signUpTask.exception!!
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                Snackbar.make(view, getString(R.string.invalid_email_msg), Snackbar.LENGTH_LONG).show()
                Log.e("error", "$e.message")
                hideKeyboard(view, this)
            }
            catch (e: FirebaseAuthWeakPasswordException) {
                Snackbar.make(view, "${getString(R.string.weak_password_msg)}!$e.getReason()", Snackbar.LENGTH_LONG).show()
                Log.e("error", "${e.reason}")
                hideKeyboard(view, this)
            }
            catch (e: FirebaseAuthUserCollisionException) {
                Snackbar.make(view, getString(R.string.existing_user_collision_msg), Snackbar.LENGTH_LONG).show()
                Log.e("error", "${e.message}")
                hideKeyboard(view, this)
            }
        }
    }
}