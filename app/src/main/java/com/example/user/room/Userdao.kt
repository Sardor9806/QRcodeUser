package com.example.user.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.user.entity.Roomentity

@Dao
interface Userdao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(notesEntity: Roomentity)

    @Query("select * from user")
    fun readUser(): LiveData<List<Roomentity>>

    @Query("delete from user")
    suspend fun deleteAllUser()
}