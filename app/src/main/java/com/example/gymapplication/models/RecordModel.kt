package com.example.gymapplication.models

import java.io.Serializable

data class RecordModel (
        var repeats: String? = null,
        var weight: String? = null
    ) : Serializable
