package com.example.gymapplication.models

import java.io.Serializable

data class UserModel(
    var userId: String? = null,
    var userName:String? = null,
    var userEmail:String? = null,
    var userStatus:String? = null,
    var userPT:String? = null,
    var userType:String? =  null,
    var workouts: List<String>? = null,
    var selectedWorkouts: List<String>? = null,
    var clients: List<String>? = null,
    var userPTid:String?=null
) : Serializable