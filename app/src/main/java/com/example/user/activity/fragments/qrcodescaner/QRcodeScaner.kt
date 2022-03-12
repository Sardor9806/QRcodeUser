package com.example.user.activity.fragments.qrcodescaner


import android.Manifest
import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.user.R
import com.example.user.activity.UserChatting
import com.example.user.activity.fragments.splash.SplashscreenFragment
import com.example.user.constants.Constants
import com.example.user.databinding.FragmentQRcodeScanerBinding
import com.example.user.entity.UserEntity
import com.example.user.log.D
import com.example.user.room.UserViewModel
import com.example.user.viewModel.DomenViewModel
import com.example.user.viewModel.LoginViewModel
import com.example.user.viewModel.UserChattingViewModel
import com.example.user.webView.OpenWebView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.FirebaseDatabase



class QRcodeScaner : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        loginViewModel.readUser()
        xabarSoni()
        foydalanuvchiniTekshirish()
        try {
            viewModel.readDomen()
            startScaneer()
            adminChatting()
        }
        catch (e:Exception)
        {
          D.d(e.message.toString())
        }

    }



    private val userChatViewModel: UserChattingViewModel by lazy {
        ViewModelProviders.of(this).get(UserChattingViewModel::class.java)
    }
    val usersDb = FirebaseDatabase.getInstance().getReference(Constants.USERS)

    private val codeScanner: CodeScanner by lazy { CodeScanner(requireContext(), binding.qrScaneer) }

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private val loginViewModel: LoginViewModel by lazy { ViewModelProviders.of(this).get(LoginViewModel::class.java) }

    private val viewModel: DomenViewModel by lazy {
        ViewModelProviders.of(this).get(DomenViewModel::class.java)
    }
    private val userViewModel: UserViewModel by viewModels()




    private fun foydalanuvchiniTekshirish() {
        userViewModel.readNotes.observe(viewLifecycleOwner, Observer { room ->
            loginViewModel.users.observe(viewLifecycleOwner, Observer { user ->

                    user.forEach {
                    }
                try {
                    if (!user.contains(UserEntity(room[0].userName, room[0].passwor, "online")) &&
                        !user.contains(UserEntity(room[0].userName, room[0].passwor, "offline"))
                    )
                    {
                        userViewModel.deleteAllUser()
                        activity?.finish()
                    }
                }
                catch (e:Exception)
                    {
                        D.d("${e.message}")
                    activity?.finish()
                }



            })
        })

    }



    private fun xabarSoni() {
        userViewModel.readNotes.observe(requireActivity(), Observer {
            try {
                userChatViewModel.readMessage(it[0].userName)
                userChatViewModel.xabarlar_soni.observe(requireActivity(), Observer {
                    if(it.size==0)
                    binding.xabarSoni.text= it[it.size].toString()
                    else
                        binding.xabarSoni.text= it[it.size-1].toString()
                })
            }catch (e:Exception)
            {
               D.d(e.message.toString())
            }
        })


    }

    private fun adminChatting() {
        binding.chat.setOnClickListener {

            userViewModel.readNotes.observe(viewLifecycleOwner, Observer {
                val intent= Intent(requireContext(), UserChatting::class.java)
                intent.putExtra("login",it[0].userName)
                startActivity(intent)
            })
        }

    }

    fun startScaneer() {

        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false
        codeScanner.decodeCallback = DecodeCallback { result ->
            activity?.runOnUiThread {
                val vibrator = activity?.getSystemService(VIBRATOR_SERVICE) as Vibrator
                if (Build.VERSION.SDK_INT >= 26) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            500,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                } else {
                    vibrator.vibrate(500)
                }
                var domen: Boolean = false
                viewModel.domens.observe(viewLifecycleOwner, Observer {
                    it.forEach {
                        if (result.text.contains(it.domenName.toString())) {
                            domen = true
                        }
                    }
                })
                var intent = Intent(requireContext(), OpenWebView::class.java)
                intent.putExtra("bool", domen)
                intent.putExtra("view", result.text)
                startActivity(intent)
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            activity?.runOnUiThread {

            }
        }

        binding.qrScaneer.setOnClickListener {
            codeScanner.startPreview()
        }
    }


    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
        userViewModel.readNotes.observe(this, Observer {
            if(it.size!=0){
                try {
                    val map = HashMap<String,Any>()
                    map["status"] = "online"
                    usersDb.child(it[0].userName!!).updateChildren(map)
                }catch (e:Exception)
                {
                    D.d(e.message.toString()+" onResume")
                }
            }
        }
        )
    }


    override fun onPause() {
        codeScanner.releaseResources()
        userViewModel.readNotes.observe(this, Observer {

                if(it.size!=0){
                    try {
                    val map = HashMap<String,Any>()
                    map["status"] = "offline"
                    usersDb.child(it[0].userName!!).updateChildren(map)
                    }catch (e:Exception)
                    {
                        D.d(e.message.toString()+" onPause")
                    }
            }


        }
        )
        super.onPause()

    }







    private var _binding: FragmentQRcodeScanerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQRcodeScanerBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}