package com.example.user.splashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.user.R
import com.example.user.activity.MainActivity
import com.example.user.login.Login
import com.example.user.room.UserViewModel

class SplashScreen : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler().postDelayed({
            userViewModel.readNotes.observe(this, Observer {
                if(it.isNotEmpty())
                {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }else
                {
                    startActivity(Intent(this, Login::class.java))
                    finish()
                }
            })
        }, 1300)
    }
}