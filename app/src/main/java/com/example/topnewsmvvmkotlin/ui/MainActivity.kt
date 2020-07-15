@file:Suppress("UNREACHABLE_CODE")

package com.example.topnewsmvvmkotlin.ui

import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.topnewsmvvmkotlin.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolBar)
        setContentView(R.layout.activity_main)
        toolBar.inflateMenu(R.menu.overflow_menu)

        val navController = findNavController(R.id.navHostFragment)
        navBottomNavigation.setupWithNavController(navController)
    }
}