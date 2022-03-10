package com.example.user.activity


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.user.R
import com.example.user.activity.fragments.qrcodescaner.QRcodeScaner
import com.example.user.activity.fragments.splash.SplashscreenFragment
import com.example.user.databinding.ActivityMainBinding
import com.example.user.entity.Roomentity
import com.example.user.log.D
import com.example.user.viewModel.LoginViewModel


class MainActivity : AppCompatActivity() {

       lateinit var binding: ActivityMainBinding
      private var PERMISSIONS: Array<String> = arrayOf(
        Manifest.permission.CAMERA
        //  Manifest.permission.ACCESS_COARSE_LOCATION,
    )
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
       supportFragmentManager.beginTransaction().replace(R.id.main_fragmet_layout,SplashscreenFragment()).commit()
            chackPerimition()
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
            }
        }
    }



//    lateinit var binding: ActivityMainBinding
//
//    private val userChatViewModel: UserChattingViewModel by lazy {
//        ViewModelProviders.of(this).get(UserChattingViewModel::class.java)
//    }
//    val usersDb = FirebaseDatabase.getInstance().getReference(Constants.USERS)
//
//    private val codeScanner: CodeScanner by lazy { CodeScanner(this, binding.qrScaneer) }
//
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//
//    private val locationViewModel:LocationViewModel by lazy { ViewModelProviders.of(this).get(LocationViewModel::class.java) }
//
//    private val loginViewModel:LoginViewModel by lazy { ViewModelProviders.of(this).get(LoginViewModel::class.java) }
//
//    private val viewModel: DomenViewModel by lazy {
//        ViewModelProviders.of(this).get(DomenViewModel::class.java)
//    }
//    private val userViewModel:UserViewModel by viewModels()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        loginViewModel.readUser()
//        xabarSoni()
//    foydalanuvchiniTekshirish()
//        try {
//           /// online()
//            viewModel.readDomen()
//            chackLocationPerimition()
//            startScaneer()
//            adminChatting()
//        }catch (e:Exception)
//        {
//            Toast.makeText(this, "Xatolik:  $e", Toast.LENGTH_LONG).show()
//        }
//        reklama()
//    }
//
//    private fun reklama() {
//        binding.imagess.setOnClickListener {
//            var alertDialog = AlertDialog.Builder(this)
//            alertDialog.setTitle("Muallif Sardor Safarov")
//            alertDialog.setMessage("Murojat uchun: +998995056698\nTg:@sardor1998_1\nGmail:zomin000s@gmail.com")
//            alertDialog.setPositiveButton("ok"){ dialogInterface: DialogInterface, i: Int -> }
//            alertDialog.show()
//        }
//    }
//
//    private fun foydalanuvchiniTekshirish() {
//        userViewModel.readNotes.observe(this, Observer {room->
//            loginViewModel.users.observe(this, Observer { user->
//                if(!user.contains(UserEntity(room[0].userName,room[0].passwor,"offline")))
//                {
//                    userViewModel.deleteAllUser()
//                   startActivity(Intent(this,Login::class.java))
//                    finish()
//                }
//            })
//        })
//
//
//    }
//
//    private fun xabarSoni() {
//        userViewModel.readNotes.observe(this, Observer {
//
//           userChatViewModel.readMessage(it[0].userName)
//        })
//        userChatViewModel.xabarlar_soni.observe(this, Observer {
//            binding.xabarSoni.text= it[it.size-1].toString()
//        })
//
//    }
//
//    private fun adminChatting() {
//        binding.chat.setOnClickListener {
//            userViewModel.readNotes.observe(this, Observer {
//                val intent=Intent(this,UserChatting::class.java)
//                intent.putExtra("login",it[0].userName)
//                startActivity(intent)
//            })
//        }
//
//    }
//    private fun chackLocationPerimition() {
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//
//            return
//        }
//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { location : Location? ->
//                userViewModel.readNotes.observe(this, Observer {
//                    try {
//                        locationViewModel.updataLocation(Locationentity(login =  it[0].userName,
//                            x=location?.latitude.toString(),
//                            y=location?.longitude.toString()))
//                    }catch (e:Exception)
//                    {
//
//                    }
//
//                })
//            }
//
//
//    }
//
//    fun startScaneer() {
//
//        codeScanner.camera = CodeScanner.CAMERA_BACK
//
//        codeScanner.formats = CodeScanner.ALL_FORMATS
//
//        codeScanner.autoFocusMode = AutoFocusMode.SAFE
//
//        codeScanner.scanMode = ScanMode.SINGLE
//
//        codeScanner.isAutoFocusEnabled = true
//
//        codeScanner.isFlashEnabled = false
//
//        codeScanner.decodeCallback = DecodeCallback { result ->
//            runOnUiThread {
//                val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
//                if (Build.VERSION.SDK_INT >= 26) {
//                    vibrator.vibrate(
//                        VibrationEffect.createOneShot(
//                            500,
//                            VibrationEffect.DEFAULT_AMPLITUDE
//                        )
//                    )
//                } else {
//                    vibrator.vibrate(500)
//                }
//                var domen: Boolean = false
//                viewModel.domens.observe(this, Observer {
//                    it.forEach {
//                        if (result.text.contains(it.domenName.toString())) {
//                            domen = true
//                        }
//                    }
//                })
//                var intent = Intent(this, OpenWebView::class.java)
//                intent.putExtra("bool", domen)
//                intent.putExtra("view", result.text)
//                startActivity(intent)
//            }
//        }
//        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
//            runOnUiThread {
//                Toast.makeText(
//                    this, "Error using ${it.message}",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//        }
//
//        binding.qrScaneer.setOnClickListener {
//            codeScanner.startPreview()
//        }
//    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.action_settings -> {
//                userViewModel.deleteAllUser()
//                startActivity(Intent(this,Login::class.java))
//                finish()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu, menu)
//        return true
//    }
//
//    override fun onResume() {
//        codeScanner.startPreview()
//        super.onResume()
//    }
//
//
//    override fun onPause() {
//        codeScanner.releaseResources()
//        super.onPause()
//
//    }

}