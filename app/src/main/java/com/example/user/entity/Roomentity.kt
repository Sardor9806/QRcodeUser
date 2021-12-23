package com.example.user.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")

data class Roomentity (
        @PrimaryKey(autoGenerate = true)
        val id:Int=0,
        val userName:String
        )