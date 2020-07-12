package com.example.topnewsmvvmkotlin.ui

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.createGraph
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.topnewsmvvmkotlin.R
import com.example.topnewsmvvmkotlin.util.show
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.navHostFragment)
        navBottomNavigation.setupWithNavController(navController)


    }
}