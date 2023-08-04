package com.example.gymapplication.models

import java.text.SimpleDateFormat
import java.util.*
import java.util.AbstractMap.SimpleEntry

data class  CalendarModel(var data: Date, var isSelected: Boolean = true) {

    val calendarDay: String
        get() = SimpleDateFormat("EE", Locale.getDefault()).format(data)

    val calendarYear: String
        get() = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(data)

    val calendarDate: String
        get() {
            val cal = Calendar.getInstance()
            cal.time = data
            return cal[Calendar.DAY_OF_MONTH].toString()
        }
}