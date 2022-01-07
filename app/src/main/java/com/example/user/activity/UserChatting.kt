package com.example.user.activity

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.user.adapter.MessageAdapter
import com.example.user.constants.Constants
import com.example.user.databinding.ActivityUserChattingBinding
import com.example.user.entity.UserChatAddEntity
import com.example.user.entity.UserEntity
import com.example.user.login.Login
import com.example.user.notification.PushNotification
import com.example.user.retrofit.RetrofitInstance
import com.example.user.room.UserViewModel
import com.example.user.viewModel.LoginViewModel
import com.example.user.viewModel.UserChattingViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserChatting : AppCompatActivity(),MessageAdapter.MessageSetOnClickListener {

    val topic=""
    private val loginViewModel: LoginViewModel by lazy { ViewModelProviders.of(this).get(
        LoginViewModel::class.java) }

    val usersDb = FirebaseDatabase.getInstance().getReference(Constants.USERS)

    private val userViewModel: UserViewModel by viewModels()

    lateinit var binding: ActivityUserChattingBinding

    private val userChatViewModel: UserChattingViewModel by lazy {
        ViewModelProviders.of(this).get(UserChattingViewModel::class.java)
    }

    private var messageArray: ArrayList<UserChatAddEntity> = arrayListOf()

    lateinit var adapterNew:MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUserChattingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        adapterNew= MessageAdapter(context = this,messageArray,this)
        sendMessage()
        userChatViewModel.readMessage(intent.getStringExtra("login").toString())
        readMessage()
        loginViewModel.readUser()
        foydalanuvchiniTekshirish()
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
                    user = binding.messageWriteEdt.text.toString(),
                    message_status = "not seen"
                )
            )
            binding.messageWriteEdt.text.clear()
        }
    }
    private fun foydalanuvchiniTekshirish() {
        userViewModel.readNotes.observe(this, Observer {room->
            loginViewModel.users.observe(this, Observer { user->
                if(!user.contains(UserEntity(room[0].userName,room[0].passwor,"online")))
                {
                    userViewModel.deleteAllUser()
                    startActivity(Intent(this, Login::class.java))
                    finish()
                }
            })
        })


    }
    override fun listener(userChatAddEntity: String) {
        val alertDialog= AlertDialog.Builder(this)
        alertDialog.setMessage("Xabarni o`chirmoqchimisiz?")
        alertDialog.setPositiveButton("Ha"){ dialogInterface: DialogInterface, i: Int ->
            userChatViewModel.deleteLocation(userChatAddEntity)
        }
        alertDialog.setNegativeButton("Yo`q"){ dialogInterface: DialogInterface, i: Int -> }
        alertDialog.show()
    }
    override fun onResume() {
        super.onResume()
        seenMessage()
        userViewModel.readNotes.observe(this, Observer {
            val map = HashMap<String,Any>()
            map["status"] = "online"
            usersDb.child(it[0].userName!!).updateChildren(map)
        }
        )
    }

    override fun onPause() {
        super.onPause()
        userViewModel.readNotes.observe(this, Observer {
            val map = HashMap<String,Any>()
            map["status"] = "offline"
            usersDb.child(it[0].userName!!).updateChildren(map)
        }
        )
    }
    fun seenMessage(){
        val messageDb = FirebaseDatabase.getInstance().getReference("admin"+intent.getStringExtra("login"))
        messageDb.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists())
                {
                    p0.children.forEach {
                        val item=it.getValue(UserChatAddEntity::class.java)
                        if(item!!.user=="")
                        {
                            var hashMap = HashMap<String,Any>()
                            hashMap.put("message_status","seen")
                            it.ref.updateChildren(hashMap)
                        }
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}