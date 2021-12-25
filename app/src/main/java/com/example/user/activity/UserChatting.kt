package com.example.user.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.user.adapter.MessageAdapter
import com.example.user.databinding.ActivityUserChattingBinding
import com.example.user.entity.UserChatAddEntity
import com.example.user.viewModel.UserChattingViewModel

class UserChatting : AppCompatActivity() {

    lateinit var binding: ActivityUserChattingBinding
    private val userChatViewModel: UserChattingViewModel by lazy {
        ViewModelProviders.of(this).get(UserChattingViewModel::class.java)
    }


    private var messageArray: ArrayList<UserChatAddEntity> = arrayListOf()

    lateinit var adapterNew:MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        title = "Admin bilan bog`lanish"
        binding = ActivityUserChattingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        adapterNew= MessageAdapter(context = this,messageArray)
        sendMessage()
        userChatViewModel.readLocation(intent.getStringExtra("login").toString())
        readMessage()
    }

    private fun readMessage() {
        binding.messageRecyclerView.adapter=adapterNew
        binding.messageRecyclerView.layoutManager=LinearLayoutManager(this)
            userChatViewModel.message.observe(this, Observer {
                messageArray.clear()
                it.forEach {
                    messageArray.add(it)
                }
                adapterNew.notifyDataSetChanged()
                binding.messageRecyclerView.scrollToPosition(messageArray.size-1)
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