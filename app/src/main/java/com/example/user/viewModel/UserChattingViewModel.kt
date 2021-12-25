package com.example.user.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.user.entity.UserChatAddEntity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class UserChattingViewModel: ViewModel() {

    private val _messsage = MutableLiveData<List<UserChatAddEntity>>()
    val message: LiveData<List<UserChatAddEntity>>
        get() = _messsage

    fun insertChatUser(userChatAddEntity: UserChatAddEntity) {
        val messageDb = FirebaseDatabase.getInstance().getReference("admin"+userChatAddEntity.login_chat.toString())
        userChatAddEntity.login_chat=messageDb.push().key
        messageDb.child(userChatAddEntity.login_chat!!).setValue(userChatAddEntity)
            .addOnCompleteListener {
                if(it.isSuccessful)
                {
                    Log.d("sardor", "qo`shildi")
                }else
                {
                    Log.d("sardor", "nimadir bo`ldiii")
                }
            }
    }

    fun readLocation(readUser:String){

        val messageDb = FirebaseDatabase.getInstance().getReference("admin"+readUser)

        messageDb.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists())
                {
                    val items= mutableListOf<UserChatAddEntity>()
                    p0.children.forEach {
                        val item=it.getValue(UserChatAddEntity::class.java)
                        item?.login_chat=it.key
                        item?.let { items.add(it) }
                    }
                    _messsage.value = items
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }



    fun deleteLocation(userChatAddEntity: UserChatAddEntity) {
        val messageDb = FirebaseDatabase.getInstance().getReference("admin"+userChatAddEntity.login_chat.toString())
        messageDb.child(userChatAddEntity.login_chat!!).setValue(null)
            .addOnCompleteListener {
                if(it.isSuccessful)
                {
                    Log.d("sardor", "qo`shildi")
                }else
                {
                    Log.d("sardor", "nimadir bo`ldiii")
                }
            }
    }

}