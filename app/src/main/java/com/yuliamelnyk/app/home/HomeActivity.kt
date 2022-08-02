package com.yuliamelnyk.app.home

import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import com.yuliamelnyk.app.R

class HomeActivity :AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_home)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }
}