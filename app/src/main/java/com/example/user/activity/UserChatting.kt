package com.example.user.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.user.adapter.UserChattingAdapter
import com.example.user.databinding.ActivityUserChattingBinding
import com.example.user.entity.UserChatAddEntity
import com.example.user.viewModel.UserChattingViewModel

class UserChatting : AppCompatActivity() {

    lateinit var binding: ActivityUserChattingBinding
    private val userChatViewModel: UserChattingViewModel by lazy {
        ViewModelProviders.of(this).get(UserChattingViewModel::class.java)
    }

    private val adapter: UserChattingAdapter by lazy { UserChattingAdapter() }

    private var messageArray: MutableList<UserChatAddEntity> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        title = "Admin bilan bog`lanish"
        binding = ActivityUserChattingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sendMessage()
        userChatViewModel.readLocation(intent.getStringExtra("login").toString())
        readMessage()
    }

    private fun readMessage() {
        binding.messageRecyclerView.adapter=adapter
        binding.messageRecyclerView.layoutManager=LinearLayoutManager(this)
            userChatViewModel.message.observe(this, Observer {
                messageArray.clear()
                it.forEach {
                    messageArray.add(it)
                    d("sardor","keldiii ${it.admin}")
                }
                adapter.sendData(messageArray)
            })
    }

    private fun sendMessage() {
        binding.sendMessageButton.setOnClickListener {
            userChatViewModel.insertChatUser(
                UserChatAddEntity(
                    login_chat = intent.getStringExtra("login"),
                    user = binding.messageWriteEdt.text.toString()
                )
            )
            binding.messageWriteEdt.text.clear()
        }
    }
}