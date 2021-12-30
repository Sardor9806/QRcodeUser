package com.example.user.splashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        checkUserBase()
    }
    private fun checkUserBase()
    {
        var list= arrayListOf<String>()
        userViewModel.readNotes.observe(this, Observer { room->
            if(room.size==0){
                startActivity(Intent(this,Login::class.java))
            }else{
            viewModel.users.observe(this, Observer {
                it.forEach {
                    list.add(it.login.toString())
                }
            })
            if(!list.contains(room[0].userName))
            {
                userViewModel.deleteAllUser()
                startActivity(Intent(this,Login::class.java))
                finish()
            }
            else
            {
                startActivity(Intent(this, MainActivity::class.java))
            }
            }
        })

    }
}