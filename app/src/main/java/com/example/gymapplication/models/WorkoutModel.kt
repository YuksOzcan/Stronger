package com.example.gymapplication.models

data class WorkoutModel(
    var workoutID: String? = null,
    var workoutName: String? = null,
    var exercisesList: ArrayList<ExerciseModel>? = null,
    var date: String?=null
)