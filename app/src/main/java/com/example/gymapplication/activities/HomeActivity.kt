package com.example.gymapplication.activities


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.gymapplication.R
import com.example.gymapplication.activities.workouts.SavedWorkoutActivity
import com.example.gymapplication.adapters.CalendarAdapter
import com.example.gymapplication.models.CalendarModel
import java.util.*
import kotlin.collections.ArrayList

class HomeActivity : AppCompatActivity() , CalendarAdapter.onItemClickListener {

    private lateinit var rvCalendar: RecyclerView

    private val cal = Calendar.getInstance(Locale.ENGLISH)
    private val dates = ArrayList<Date>()
    private lateinit var adapter: CalendarAdapter
    private val calendarList2 = ArrayList<CalendarModel>()
    private lateinit var btnWorkoutRoutine : Button
    private var selectedDate:String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnWorkoutRoutine= findViewById(R.id.btnAddWorkoutToday)

        btnWorkoutRoutine.setOnClickListener{
            if (selectedDate != null) {
                val intent = Intent(this, SavedWorkoutActivity::class.java)
                intent.putExtra("Date", selectedDate)
                startActivity(intent)
             }
            else {
                Toast.makeText(this,"Select a Date",Toast.LENGTH_LONG).show()

            }
        }


        rvCalendar = findViewById(R.id.rvCalendar)
        setUpAdapter()
        setUpCalendar()
    }
    override fun onItemClick(text: String, date: String, day: String) {
        selectedDate=text
        val message = "Selected date: $date ($day)"
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    private fun setUpAdapter() {
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(rvCalendar)
        adapter = CalendarAdapter { calendarModel: CalendarModel, position: Int ->
            calendarList2.forEachIndexed { index, calendarModel ->
                calendarModel.isSelected = index == position
            }

            adapter.setData()
            adapter.setOnItemClickListener(this@HomeActivity)
        }
        rvCalendar.adapter = adapter
    }

    private fun setUpCalendar() {
        val calendarList = ArrayList<CalendarModel>()
        val monthCalendar = cal.clone() as Calendar
        monthCalendar.add(Calendar.YEAR, -1) // Here we set the year to one year earlier.
        val maxDaysInYear = 365 // Approximately number of days in a year.
        dates.clear()
        while (dates.size < maxDaysInYear) {
            dates.add(monthCalendar.time)
            calendarList.add(CalendarModel(monthCalendar.time))
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        calendarList2.clear()
        calendarList2.addAll(calendarList)
        adapter.setOnItemClickListener(this@HomeActivity)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.scrollToPositionWithOffset(10, 0)
        rvCalendar.layoutManager = linearLayoutManager

        adapter.setData()
    }


}



