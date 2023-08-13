package com.example.gymapplication.models

import java.io.Serializable

data class WorkoutModel(
    var workoutID: String? = null,
    var workoutName: String? = null,
    var exercisesList: ArrayList<ExerciseModel>? = null,
    var date: String?=null,
    var userId:String?=null
): Serializable {

    fun getCombinedKey(): String {
        return "${userId}_$date"
    }
}