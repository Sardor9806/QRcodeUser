package com.example.user.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.user.constants.Constants
import com.example.user.entity.DomenEntity
import com.example.user.entity.UserEntity
import com.example.user.firebaseServis.FireBaseService
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging

class LoginViewModel: ViewModel() {




    private  val usersDb = FirebaseDatabase.getInstance().getReference(Constants.USERS)

    private val _users = MutableLiveData<List<UserEntity>>()
    val users: LiveData<List<UserEntity>>
        get() = _users

    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?>
        get() = _result


    fun readUser(){
        usersDb.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists())
                {
                    val items= mutableListOf<UserEntity>()
                    p0.children.forEach {
                        val item=it.getValue(UserEntity::class.java)
                        item?.login=it.key
                        item?.let { items.add(it) }
                        FirebaseMessaging.getInstance().subscribeToTopic("/topics/${it.key}")
                    }
                    _users.value = items
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

        fun updataStatus(userEntity: UserEntity)
        {
            val map = HashMap<String,Any>()
            map["status"] = userEntity.status.toString()
            usersDb.child(userEntity.login!!).updateChildren(map)
        }


}