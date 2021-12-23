package com.example.user.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.user.constants.Constants
import com.example.user.entity.DomenEntity
import com.example.user.entity.UserEntity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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
                    }
                    _users.value = items
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}