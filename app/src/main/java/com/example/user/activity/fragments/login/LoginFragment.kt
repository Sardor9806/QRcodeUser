package com.example.user.activity.fragments.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.user.R
import com.example.user.activity.fragments.qrcodescaner.QRcodeScaner
import com.example.user.databinding.FragmentLoginBinding
import com.example.user.entity.Roomentity
import com.example.user.log.D
import com.example.user.room.UserViewModel
import com.example.user.viewModel.LoginViewModel

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by lazy { ViewModelProviders.of(this).get(LoginViewModel::class.java) }
    private val userViewModel: UserViewModel by viewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        chackUser()


    }

    private fun chackUser() {
        viewModel.readUser()
        binding.signin.setOnClickListener {

            if(binding.addLogin.text.toString()!="" && binding.parol.text.toString()!="")
            viewModel.users.observe(viewLifecycleOwner, Observer {
                it.forEach {
                    if(it.login==binding.addLogin.text.toString() && it.password==binding.parol.text.toString())
                    {
                        userViewModel.insertUser(Roomentity(userName = it.login.toString(), passwor = it.password.toString()))
                        fragmentManager?.beginTransaction()?.replace(R.id.main_fragmet_layout,
                            QRcodeScaner()
                        )?.commit()

                    }
                }
            })



//            if(binding.addLogin.text.toString()!="" && binding.parol.text.toString()!="")
//            {
//                viewModel.users.observe(viewLifecycleOwner, Observer {
//                it.forEach {
//
//                    if(it.login==binding.addLogin.text.toString() && it.password==binding.parol.text.toString())
//                    {
//                        userViewModel.insertUser(Roomentity(userName = it.login.toString(), passwor = it.password.toString()))
//                        fragmentManager?.beginTransaction()?.replace(R.id.main_fragmet_layout,QRcodeScaner())?.commit()
//
//                    }
//                }
//            })
//            }
        }

    }

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}