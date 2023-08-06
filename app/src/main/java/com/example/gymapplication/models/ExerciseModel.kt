package com.example.gymapplication.models

import java.io.Serializable

data class ExerciseModel (
    var exerciseId:String? = null,
    var exerciseName:String? = null,
    var repeatAndWeightList: MutableList<RepeatAndWeight> = mutableListOf()
) : Serializable

data class RepeatAndWeight(
    var repeats: String? = null,
    var weight: String? = null
) : Serializable