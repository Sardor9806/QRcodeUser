package com.example.user.login

import android.Manifest
import android.content.Context
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


    private var PERMISSIONS: Array<String> = arrayOf(
        Manifest.permission.CAMERA
      //  Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.readUser()
        chackUser()
        chackPerimition()

    }



    private fun chackUser() {
        binding.signin.setOnClickListener {
            if(binding.addLogin.text.toString()!="" && binding.parol.text.toString()!="")
            {  viewModel.users.observe(this, Observer {
                it.forEach {
                    if(it.login==binding.addLogin.text.toString() && it.password==binding.parol.text.toString())
                    {
                        startActivity(Intent(this,MainActivity::class.java))
                        userViewModel.insertUser(Roomentity(userName = it.login.toString(), passwor = it.password.toString()))
                        finish()
                    }
                }
            })
            }
        }

    }

    /*---------------Ruxsat berish-------------------*/
    private fun chackPerimition() {
        if (!hasPermissions(this, *PERMISSIONS)) {

            ActivityCompat.requestPermissions(this,PERMISSIONS,1);
        }
    }
    private fun hasPermissions(context: Context?, vararg PERMISSIONS: String): Boolean {
        if (context != null && PERMISSIONS != null) {
            for (permission in PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera uchun ruxsat berildi", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Camera uchun ruxsat bering!!", Toast.LENGTH_SHORT).show()
            }
//            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//               Toast.makeText(this, "Joylashuvni olish uchun ruxsat berildi", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this, "Joylashuvni olish uchun ruxsat bering!!", Toast.LENGTH_SHORT).show()
//            }
        }
    }


}