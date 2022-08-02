package com.yuliamelnyk.app.login

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yuliamelnyk.app.entity.User
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.yuliamelnyk.app.R
import com.yuliamelnyk.app.splash.SplashActivity
import com.yuliamelnyk.app.utils.ValidateUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    // Firebase Auth
    private lateinit var auth: FirebaseAuth

    // Static currentUser
    companion object {
        lateinit var currentUser: User
        val EMAIL = "email"
        val TAG = "LoginActivity"
    }

    private var validateUser: ValidateUser? = null

    //Google signIn
    private var googleSignIn: ImageView? = null

    //    Request code used to invoke sign in user interactions for Google+
    val RC_GOOGLE_LOGIN = 1

    private lateinit var act: Activity


    /* The callback manager for Facebook */
    lateinit var callbackManager: CallbackManager

    /* Used to track user logging in/out off Facebook */
    private val mFacebookAccessTokenTracker: AccessTokenTracker? = null

    var personName: String? = null
    var gmail: String? = null
    var gPhoto: Uri? = null
    private var idToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Sign In with Facebook
        loginFacebookImg?.setOnClickListener {

            LoginManager.getInstance().logInWithReadPermissions(
                this@LoginActivity,
                listOf("EMAIL")
            )
            btn_fb_login?.setPermissions(listOf(EMAIL))
            callbackManager = CallbackManager.Factory.create()

            // Callback registration
            btn_fb_login?.registerCallback(
                callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        // App code
                        Log.d(TAG, "facebook:onSuccess:$loginResult")
                        Toast.makeText(
                            this@LoginActivity, "FB success.",
                            Toast.LENGTH_SHORT
                        ).show()
                        val request = GraphRequest.newMeRequest(
                            loginResult.accessToken
                        ) { `object`, response ->
                            // Application code
                        }
                        val parameters = Bundle()
                        parameters.putString("fields", "id, name, email, gender, birthday, picture")
                        request.parameters = parameters
                        request.executeAsync()
                        goToHomePage()
                    }

                    override fun onCancel() {
                        Log.d(TAG, "facebook:onCancel")
                        Toast.makeText(
                            this@LoginActivity, "FB Cancelled.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onError(exception: FacebookException) {
                        Log.d(TAG, "facebook:onError", exception)
                        Toast.makeText(
                            this@LoginActivity,
                            "Error on Login, check your facebook app_id",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }

        //Initialize firebaseAuth
        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            startActivity(Intent(this@LoginActivity, SplashActivity::class.java))
            finish()
        }

        //if user forget his password
//        reset_password!!.setOnClickListener {
//            startActivity(Intent(this, ResetPassword::class.java))
//        }
        //sign Up button
        signup_label?.setOnClickListener {
            var intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
        //Click on Login Button (validate)
        btn_login?.setOnClickListener(View.OnClickListener {
            val email = log_email!!.text.toString().trim()
            val password = log_password!!.text.toString().trim()

            validateUser = ValidateUser()
            if (TextUtils.isEmpty(email)) {
                log_email?.error = getString(R.string.error_field_required)
                log_email?.requestFocus()
                Toast.makeText(applicationContext, "Please enter your email.", Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            } else if (!validateUser!!.isEmailValid(email)) {
                log_email?.error = getString(R.string.error_invalid_email)
                log_email?.requestFocus()
            } else if (TextUtils.isEmpty(password)) {
                log_password?.error = getString(R.string.error_incorrect_password)
                log_password?.requestFocus()
                Toast.makeText(
                    applicationContext,
                    "Please enter your password.",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            } else if (!validateUser!!.isPasswordValid(password)) {
                log_password?.error = getString(R.string.error_invalid_password)
                log_password?.requestFocus()
            }

            progressBar!!.visibility = View.VISIBLE
            // authenticate the user
            auth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    progressBar!!.visibility = View.VISIBLE
                    if (!task.isSuccessful) {
                        Toast.makeText(
                            this@LoginActivity,
                            getString(R.string.auth_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        goToHomePage()
                    }
                }
        })
        googleSignIn = findViewById(R.id.login_google)
        googleSignIn?.setOnClickListener(View.OnClickListener {
            startActivityForResult(Intent(signInWithGoogle().signInIntent), RC_GOOGLE_LOGIN)
        })
    }

    private fun goToHomePage() {
        startActivity(Intent(this@LoginActivity, SplashActivity::class.java))
        finish()
    }

    fun signInWithGoogle(): GoogleSignInClient {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        return googleSignInClient
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_LOGIN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result!!.isSuccess) {
                // Google Sign In was successful, authenticate with Firebase
                handleSignInResult(result)
            } else {
                Toast.makeText(
                    this@LoginActivity, "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            //Facebook
            callbackManager?.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleSignInResult(result: GoogleSignInResult?) {
        if (result!!.isSuccess) {
            val account = result.signInAccount
            idToken = account!!.idToken
            personName = account.displayName
            gmail = account.email
            gPhoto = account.photoUrl

            // you can store user data to SharedPreference
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuthWithGoogle(credential)
        } else {
            // Google Sign In failed, update UI appropriately
            Log.e(TAG, "Login Unsuccessful. $result")
            Toast.makeText(this, "Login Unsuccessful", Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseAuthWithGoogle(credential: AuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(
                this
            ) { task ->
                Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful)
                if (task.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT)
                        .show()
                    goToHomePage()
                } else {
                    Log.w(TAG, "signInWithCredential" + task.exception!!.message)
                    task.exception!!.printStackTrace()
                    Toast.makeText(
                        this@LoginActivity, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}