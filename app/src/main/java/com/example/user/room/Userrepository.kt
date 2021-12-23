package com.example.user.room

import androidx.lifecycle.LiveData
import com.example.user.entity.Roomentity


class Userrepository(private val notesDao: Userdao) {

    val readNotes: LiveData<List<Roomentity>> = notesDao.readUser()


    suspend fun insertNotes(notesEntity: Roomentity)
    {
        notesDao.insertUser(notesEntity)
    }


    suspend fun deleteAllNotes()
    {
        notesDao.deleteAllUser()
    }


}