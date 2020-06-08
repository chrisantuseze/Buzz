package com.echrisantus.buzz.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.echrisantus.buzz.R
import com.echrisantus.buzz.database.AppSharedPref
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appSharedPref: AppSharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appSharedPref = AppSharedPref(this)

        val navView: BottomNavigationView = this.findViewById(R.id.nav_view)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        NavigationUI.setupWithNavController(navView, navHostFragment!!.navController)

        if (appSharedPref.isLogin()) {
            findNavController(R.id.nav_host_fragment).navigate(R.id.homeFragment)
        }

    }

}
