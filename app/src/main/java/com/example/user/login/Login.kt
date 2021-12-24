package com.example.user.login

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.user.activity.MainActivity
import com.example.user.databinding.ActivityLoginBinding
import com.example.user.entity.Roomentity
import com.example.user.room.UserViewModel
import com.example.user.viewModel.LoginViewModel

class Login : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private val viewModel:LoginViewModel by lazy { ViewModelProviders.of(this).get(LoginViewModel::class.java) }

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.readUser()
        chackUser()
        chackCameraPerimition()
    }



    private fun chackUser() {
        binding.signin.setOnClickListener {
            if(binding.addLogin.text.toString()!="" && binding.parol.text.toString()!="")
            {  viewModel.users.observe(this, Observer {
                it.forEach {
                    if(it.login==binding.addLogin.text.toString() && it.password==binding.parol.text.toString())
                    {
                        startActivity(Intent(this,MainActivity::class.java))
                        userViewModel.insertUser(Roomentity(userName = it.login.toString()))
                        finish()
                    }
                }
                Toast.makeText(this, "Login yoki parol xato!!", Toast.LENGTH_SHORT).show()
            })
            }
        }

    }
    private fun chackCameraPerimition() {

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                123
            )
        }
    }
}