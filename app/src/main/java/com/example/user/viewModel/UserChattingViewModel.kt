package com.example.user.viewModel

import android.util.Log
import android.util.Log.d
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
    var t:String=""
    var _xabarlar_soni=MutableLiveData<List<Int>>()
    val xabarlar_soni:LiveData<List<Int>>
        get() =_xabarlar_soni

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

    fun readMessage(readUser:String){
        t=readUser
        val messageDb = FirebaseDatabase.getInstance().getReference("admin"+readUser)
        messageDb.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists())
                {
                    val items= mutableListOf<UserChatAddEntity>()
                    val xabar= mutableListOf<Int>()
                    var xabarsanagich=0
                    p0.children.forEach {
                        val item=it.getValue(UserChatAddEntity::class.java)
                        item?.login_chat=it.key
                        item?.let {
                            if(it.admin.isNotEmpty() && it.message_status!="seen")
                              xabarsanagich++
                            items.add(it)
                            xabar.add(xabarsanagich)
                        }
                    }
                    if(xabar.size==0)
                    _xabarlar_soni.value= listOf(0)
                    else
                        _xabarlar_soni.value=xabar
                    _messsage.value = items
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }



    fun deleteLocation(userChatAddEntity: String) {
        val messageDb = FirebaseDatabase.getInstance().getReference("admin"+t)
        messageDb.child(userChatAddEntity).setValue(null)
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