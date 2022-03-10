package com.example.user.room

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.user.entity.Roomentity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class UserViewModel(application: Application): AndroidViewModel(application) {

    private val notesRepository:Userrepository
    var readNotes: LiveData<List<Roomentity>>
    init {
        val notesDao=UserDatabase.getDatabase(application).userDao()
        notesRepository= Userrepository(notesDao)
        readNotes=notesRepository.readNotes
    }

    fun insertUser(notesEntity: Roomentity)
    {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                notesRepository.insertNotes(notesEntity)
            }catch (e: Exception)
            {
                Log.d("sardor", "UserViewModel insertNotes")
            }
        }
    }


    fun deleteAllUser()
    {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                notesRepository.deleteAllNotes()
            }catch (e: Exception)
            {
                Log.d("sardor", "UserViewModel deleteAllNotes")
            }

        }
    }


}