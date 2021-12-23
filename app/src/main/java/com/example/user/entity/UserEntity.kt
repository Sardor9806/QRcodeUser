package com.example.user.entity

import com.google.firebase.database.Exclude


data class UserEntity (
    @get:Exclude
    var login:String?=null,
    var password:String?=null
)