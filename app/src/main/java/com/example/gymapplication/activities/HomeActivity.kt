package com.example.gymapplication.activities


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.gymapplication.AuthHelper
import com.example.gymapplication.R
import com.example.gymapplication.activities.workouts.SavedWorkoutActivity
import com.example.gymapplication.activities.workouts.WorkoutActivity
import com.example.gymapplication.activities.workouts.date
import com.example.gymapplication.adapters.CalendarAdapter
import com.example.gymapplication.adapters.NestedWorkoutAdapter
import com.example.gymapplication.models.CalendarModel
import com.example.gymapplication.models.WorkoutModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.collections.ArrayList
import com.google.firebase.database.ValueEventListener


class HomeActivity : AppCompatActivity() , CalendarAdapter.onItemClickListener {

    private lateinit var rvCalendar: RecyclerView
    private val cal = Calendar.getInstance(Locale.ENGLISH)
    private val dates = ArrayList<Date>()
    private lateinit var adapter: CalendarAdapter
    private val calendarList2 = ArrayList<CalendarModel>()
    private lateinit var btnWorkoutRoutine : Button
    private var selectedDate:String? = null
    private lateinit var tvDate: TextView
    private lateinit var dbRef:DatabaseReference
    private lateinit var rvOuter:RecyclerView
    private lateinit var workoutList:ArrayList<WorkoutModel>
    private lateinit var btnGoHistory:ImageButton
    private lateinit var btnGoHome:ImageButton
    private lateinit var btnGoExercise:ImageButton
    private lateinit var btnGoSignOut:ImageButton
    private lateinit var btnGoProfile:ImageButton
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnWorkoutRoutine= findViewById(R.id.btnAddWorkoutToday)
        tvDate = findViewById(R.id.tvHomeDate)
        rvOuter = findViewById(R.id.rvWorkoutOuter)
        workoutList = arrayListOf()
        rvCalendar = findViewById(R.id.rvCalendar)
        rvOuter.layoutManager=LinearLayoutManager(this)
        rvOuter.setHasFixedSize(true)
        btnGoHistory=findViewById(R.id.btnGoToHistory)
        btnGoHome = findViewById(R.id.btnGoToHome)
        btnGoExercise= findViewById(R.id.btnGoToExercise)
        btnGoProfile = findViewById(R.id.btnGoToProfile)
        btnGoSignOut=findViewById(R.id.btnGoToSignOut)
        auth = FirebaseAuth.getInstance()

        btnGoExercise.setOnClickListener{
            AuthHelper.exercise(this)
        }

        btnGoSignOut.setOnClickListener {
            AuthHelper.signOut(this,auth)
        }
        btnGoHome.setOnClickListener {
            AuthHelper.home(this)
        }
        btnGoProfile.setOnClickListener {
            AuthHelper.profile(this)
        }
        btnGoHistory.setOnClickListener {
            AuthHelper.history(this)
        }


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
        setUpAdapter()
        setUpCalendar()
    }

    override fun onItemClick(text: String, date: String, day: String) {
        selectedDate=text
        tvDate.text= selectedDate.toString()
        getWorkouts()

    }
    private fun getWorkouts() {
        val customUrl = "https://gymappfirebase-9f06f-default-rtdb.europe-west1.firebasedatabase.app"
        dbRef = FirebaseDatabase.getInstance(customUrl).getReference("SelectedWorkouts")
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val currentUserId = mAuth.currentUser?.uid
        val combinedKey = "$selectedDate${currentUserId}"
        val query = dbRef.orderByChild("combinedKey").equalTo(combinedKey)
        workoutList.clear()
        rvOuter.adapter=null
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (workoutSnapshot in dataSnapshot.children) {
                        val workout = workoutSnapshot.getValue(WorkoutModel::class.java)
                        if (workout != null) {
                            workoutList.add(workout!!)
                            val mAdapter = NestedWorkoutAdapter(workoutList)
                            rvOuter.adapter=mAdapter


                            mAdapter.setOnItemClickListener(object : NestedWorkoutAdapter.onItemClickListener {
                                override fun onItemClick(position: Int) {
                                    val intent = Intent(this@HomeActivity, WorkoutActivity::class.java)
                                    intent.putExtra("Date", date)
                                    intent.putExtra("WorkoutName", workoutList[position].workoutName)
                                    intent.putExtra("ExercisesList", workoutList[position].exercisesList)
                                    intent.putExtra("WorkoutList", workoutList[position].workoutID)
                                    startActivity(intent)

                                }
                            })
                        }
                    }
                } else {
                    Log.d("DEBUG", "No data found for the given userId")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("DB_ERROR", "Database error: ${databaseError.message}")
            }
        })
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



