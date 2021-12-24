package com.example.user.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log.d
import android.view.Menu
import android.view.MenuItem

import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.user.R
import com.example.user.databinding.ActivityMainBinding
import com.example.user.entity.Locationentity
import com.example.user.login.Login
import com.example.user.room.UserViewModel
import com.example.user.viewModel.DomenViewModel
import com.example.user.viewModel.LocationViewModel
import com.example.user.webView.OpenWebView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val codeScanner: CodeScanner by lazy { CodeScanner(this, binding.qrScaneer) }

    private val currentlocation: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(
            this
        )
    }

    private val locationViewModel:LocationViewModel by lazy { ViewModelProviders.of(this).get(LocationViewModel::class.java) }

    private val viewModel: DomenViewModel by lazy {
        ViewModelProviders.of(this).get(DomenViewModel::class.java)
    }
    private val userViewModel:UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.readDomen()
        chackCameraPerimition()
        chackLocationPerimition()
    }

    private fun chackLocationPerimition() {
        val task = currentlocation.lastLocation
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }
        task.addOnSuccessListener { locati->
            userViewModel.readNotes.observe(this, Observer {
                try {
                    locationViewModel.updataLocation(Locationentity(login =  it[0].userName,x=locati.latitude.toString(),y=locati.longitude.toString()))
                }catch (e:Exception)
                {

                }

            })
            Toast.makeText(this, "${locati.longitude}  ${locati.latitude}", Toast.LENGTH_SHORT).show()

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
        } else
            startScaneer()
    }

    fun startScaneer() {

        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        codeScanner.decodeCallback = DecodeCallback { result ->
            runOnUiThread {
                val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
                if (Build.VERSION.SDK_INT >= 26) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            1000,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                } else {
                    vibrator.vibrate(1500)
                }
                var domen: Boolean = false
                viewModel.domens.observe(this, Observer {
                    it.forEach {

                        if (it.domenName?.uppercase() == result.text.uppercase()) {
                            domen = true
                        }
                    }
                })
                var intent = Intent(this, OpenWebView::class.java)
                intent.putExtra("bool", domen)
                intent.putExtra("view", result.text)
                startActivity(intent)
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(
                    this, "Error using ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.qrScaneer.setOnClickListener {
            codeScanner.startPreview()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
               userViewModel.deleteAllUser()
                startActivity(Intent(this,Login::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}