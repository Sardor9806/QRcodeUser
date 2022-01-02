package com.example.user.splashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.user.R
import com.example.user.activity.MainActivity
import com.example.user.login.Login
import com.example.user.room.UserViewModel
import com.example.user.viewModel.LoginViewModel

class  SplashScreen : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()

    private val viewModel: LoginViewModel by lazy { ViewModelProviders.of(this).get(LoginViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
            supportActionBar?.hide()
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