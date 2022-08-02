package com.yuliamelnyk.app.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.yuliamelnyk.app.R
import com.yuliamelnyk.app.home.HomeActivity


class SplashActivity : AppCompatActivity() {
    //Duration of wait
    val SPLASH_LENGHT: Int = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        Handler().postDelayed({ /* Create an Intent that will start the Menu-Activity. */
            val mainIntent = Intent(this@SplashActivity, HomeActivity::class.java)
            this@SplashActivity.startActivity(mainIntent)
            finish()
        }, SPLASH_LENGHT.toLong())
    }
}