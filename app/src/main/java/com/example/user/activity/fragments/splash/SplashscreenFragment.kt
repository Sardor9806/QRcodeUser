package com.example.user.activity.fragments.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.user.R
import com.example.user.activity.fragments.login.LoginFragment
import com.example.user.activity.fragments.qrcodescaner.QRcodeScaner
import com.example.user.databinding.FragmentSplashscreenBinding
import com.example.user.room.UserViewModel
import com.example.user.viewModel.LoginViewModel


class SplashscreenFragment : Fragment() {


    private val userViewModel: UserViewModel by viewModels()
    private val viewModel: LoginViewModel by lazy {
        ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.readUser()

            userViewModel.readNotes.observe(viewLifecycleOwner, Observer { room ->
                if (room.size!=0) {
                    viewModel.users.observe(viewLifecycleOwner, Observer { user ->
                            if (user.isEmpty()) {
                                fragmentManager?.beginTransaction()
                                    ?.replace(R.id.main_fragmet_layout, LoginFragment())?.commit()

                            } else {
                                var s = 0
                                user.forEach {
                                    if (room[0].userName == it.login) {
                                        fragmentManager?.beginTransaction()
                                            ?.replace(R.id.main_fragmet_layout, QRcodeScaner())
                                            ?.commit()
                                    } else {
                                        s++
                                    }
                                    if (s == user.size) {
                                        userViewModel.deleteAllUser()
                                        fragmentManager?.beginTransaction()
                                            ?.replace(R.id.main_fragmet_layout, LoginFragment())
                                            ?.commit()
                                    }
                                }
                            }
                    })
                } else {
                    fragmentManager?.beginTransaction()
                        ?.replace(R.id.main_fragmet_layout, LoginFragment())?.commit()
                }
            })
    }


    private var _binding: FragmentSplashscreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashscreenBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}