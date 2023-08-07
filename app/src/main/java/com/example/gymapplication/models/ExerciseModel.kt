package com.example.gymapplication.models

import java.io.Serializable

data class ExerciseModel (
    var exerciseId:String? = null,
    var exerciseName:String? = null,
    var exerciseRecord: ArrayList<RecordModel> = arrayListOf()
) : Serializable