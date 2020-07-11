package com.example.topnewsmvvmkotlin.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.topnewsmvvmkotlin.R
import com.example.topnewsmvvmkotlin.ui.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class SplashActivity : AppCompatActivity() {

    private var timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        scheduleTask()
    }
    private fun scheduleTask() {
        val intent = Intent(this, MainActivity::class.java)
        val task = object : TimerTask() {
            override fun run() {
                startActivity(intent)
                finish()
            }
        }
        timer.schedule(task, 2_000L)
    }

}