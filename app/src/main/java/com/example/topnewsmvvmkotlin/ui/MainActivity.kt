package com.example.topnewsmvvmkotlin.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.topnewsmvvmkotlin.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolBar)
        setContentView(R.layout.activity_main)
        toolBar.inflateMenu(R.menu.overflow_menu)
        navController = findNavController(R.id.navHostFragment)
        navBottomNavigation.setupWithNavController(navController)

    }
}