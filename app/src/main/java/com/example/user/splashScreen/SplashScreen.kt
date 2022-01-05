package com.example.user.splashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log.d
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.user.R
import com.example.user.activity.MainActivity
import com.example.user.login.Login
import com.example.user.room.UserViewModel
import com.example.user.viewModel.LoginViewModel

class SplashScreen : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()
    private val viewModel: LoginViewModel by lazy {
        ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        viewModel.readUser()
        userViewModel.readNotes.observe(this, Observer { room ->
            if (room.isNotEmpty()) {
                viewModel.users.observe(this, Observer { user ->
                    try {
                        if (user.isEmpty()) {
                            startActivity(Intent(this, Login::class.java))
                            finish()
                        } else {
                            var s = 0
                            user.forEach {
                                if (room[0].userName == it.login) {
                                    startActivity(Intent(this, MainActivity::class.java))
                                    finish()
                                } else {
                                    s++
                                }
                                if (s == user.size) {
                                    userViewModel.deleteAllUser()
                                    startActivity(Intent(this, Login::class.java))
                                    finish()
                                }
                            }

                        }
                    }catch (e:Exception)
                    {
                        d("sardor","keldii $e")
                    }


                })
            } else {
                startActivity(Intent(this, Login::class.java))
                finish()
            }
        })
    }
}