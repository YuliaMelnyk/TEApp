package com.yuliamelnyk.app.login

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.yuliamelnyk.app.entity.User

class SignUpActivity: AppCompatActivity()  {

    // Firebase Auth
    private lateinit var auth: FirebaseAuth

    // Static currentUser
    companion object {
        lateinit var currentUser: User
        val EMAIL = "email"
        val TAG = "LoginActivity"
    }
}