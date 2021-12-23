package com.example.user.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.user.constants.Constants.DOMEN
import com.example.user.entity.DomenEntity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DomenViewModel: ViewModel() {

    private  val domenDb = FirebaseDatabase.getInstance().getReference(DOMEN)
   // private  val location = FirebaseDatabase.getInstance().getReference(LOCATION)

    private val _domens = MutableLiveData<List<DomenEntity>>()
    val domens: LiveData<List<DomenEntity>>
        get() = _domens

    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?>
        get() = _result

//    fun insertLocation(locationEntity: LocationEntity) {
//        locationEntity.id = "Sardor"
//        locationEntity.x="sardoraaa"
//        locationEntity.y="zafareee"
//        location.child(locationEntity.id!!).setValue(locationEntity)
//            .addOnCompleteListener {
//                if(it.isSuccessful)
//                {
//                    _result.value=null
//                }else
//                {
//                    _result.value=it.exception
//                }
//            }
//    }

    fun readDomen(){
        domenDb.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists())
                {
                    val items= mutableListOf<DomenEntity>()
                    p0.children.forEach {
                        val item=it.getValue(DomenEntity::class.java)
                        item?.id=it.key
                        item?.let { items.add(it) }
                    }
                    _domens.value = items
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}