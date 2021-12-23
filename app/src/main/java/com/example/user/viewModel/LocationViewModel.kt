package com.example.user.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.user.constants.Constants
import com.example.user.entity.Locationentity
import com.google.firebase.database.FirebaseDatabase



class LocationViewModel: ViewModel() {

    private  val locationDb = FirebaseDatabase.getInstance().getReference(Constants.LOCATION)


    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?>
        get() = _result

    fun updataLocation(locationentity: Locationentity) {
        locationDb.child(locationentity.login!!).setValue(locationentity)
            .addOnCompleteListener {
                if(it.isSuccessful)
                {
                    _result.value=null
                }else
                {
                    _result.value=it.exception
                }
            }
    }

}