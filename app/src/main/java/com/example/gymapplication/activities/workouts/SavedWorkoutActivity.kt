package com.example.gymapplication.activities.workouts

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapplication.R
import com.example.gymapplication.adapters.WorkoutAdapter
import com.example.gymapplication.models.UserModel
import com.example.gymapplication.models.WorkoutModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

private lateinit var btnCreateRoutine: Button
private lateinit var tvDate : TextView
private lateinit var rvWorkout:RecyclerView
private lateinit var dbRef:DatabaseReference
private lateinit var workoutList:ArrayList<WorkoutModel>
var boolAssing: Boolean = false
var date: String? =null
var ptBooleanReceived: Boolean = false
var savedDate:String?=null

class SavedWorkoutActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_workouts)

        tvDate = findViewById(R.id.tvSelectedDate)
        date = intent.getStringExtra("Date")
        tvDate.text = date.toString()
        var sharedPref = getSharedPreferences("MyApp", MODE_PRIVATE)
        savedDate = sharedPref.getString("selectedDate", null)

        ptBooleanReceived = intent.getBooleanExtra("ptBoolean", false)
        if (date != null) {
            tvDate.text = date
            getWorkouts(date!!)

        } else {
           if (savedDate!=null) {
               date = tvDate.text.toString()
               tvDate.text = savedDate
               getWorkouts(date!!)
               with(sharedPref.edit()) {
                   remove("selectedDate")
                   commit()
               }

           }
            else{
               openCalendarDialog()

           }


        }


        btnCreateRoutine = findViewById(R.id.btnCreateRoutine)
        rvWorkout = findViewById(R.id.rvSavedWorkouts)
        rvWorkout.layoutManager = LinearLayoutManager(this)
        rvWorkout.setHasFixedSize(true)
        workoutList = arrayListOf()

        btnCreateRoutine.setOnClickListener {
            val sharedPref = getSharedPreferences("MyApp", MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("selectedDate", date)
                commit()
            }
            val intent = Intent(this, CreateWorkoutActivity::class.java)
            intent.putExtra("Date", date)
            startActivity(intent)
        }


    }

    private fun openCalendarDialog() {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.calendar_dialog, null)
        val calendarDate = mDialogView.findViewById<CalendarView>(R.id.calendarWorkout)
        var selectedDate: String? = null

        calendarDate.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val months = arrayOf(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
            )

            selectedDate = "$dayOfMonth ${months[month]} $year"
            Toast.makeText(this, selectedDate, Toast.LENGTH_SHORT).show()
        }

        mDialog.setView(mDialogView)
        mDialog.setPositiveButton("Select") { dialog, _ ->
            if (selectedDate != null) {
                tvDate.text = selectedDate.toString()
                boolAssing = true
                dialog.dismiss()
                if (ptBooleanReceived) {
                    getWorkouts(selectedDate!!)
                }
            } else {
                Toast.makeText(this, "Please choose a Date", Toast.LENGTH_SHORT).show()
            }
        }

        mDialog.show()
    }


    private fun setSelectedWorkout(date: String, position: Int) {
        val customUrl =
            "https://gymappfirebase-9f06f-default-rtdb.europe-west1.firebasedatabase.app"
        dbRef = FirebaseDatabase.getInstance(customUrl).getReference("Workouts")
        dbRef = FirebaseDatabase.getInstance(customUrl).getReference("SelectedWorkouts")
        val client = intent.getSerializableExtra("client") as? UserModel
        val clientId = client?.userId.toString()
        val workoutID = dbRef.push().key!!
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val currentUserId = mAuth.currentUser?.uid
        var workout = WorkoutModel()
        if (!boolAssing) {
            workout = WorkoutModel(
                workoutID, workoutList[position].workoutName,
                workoutList[position].exercisesList, date + currentUserId)

        } else {
            workout = WorkoutModel(
                workoutID, workoutList[position].workoutName,
                workoutList[position].exercisesList, date + clientId)

        }

        dbRef.child(workoutID).setValue(workout)
        val intent = Intent(this@SavedWorkoutActivity, WorkoutActivity::class.java)
        intent.putExtra("WorkoutName", workoutList[position].workoutName)
        intent.putExtra("ExercisesList", workoutList[position].exercisesList)
        intent.putExtra("WorkoutList", workoutID)
        intent.putExtra("Date", date)
        startActivity(intent)
    }


    private fun getWorkouts(date: String) {
        val customUrl =
            "https://gymappfirebase-9f06f-default-rtdb.europe-west1.firebasedatabase.app"
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val currentUserId = mAuth.currentUser?.uid
        dbRef = FirebaseDatabase.getInstance(customUrl).getReference("Workouts")
        val  query = dbRef.orderByChild("userId").equalTo(currentUserId)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                workoutList.clear()
                if (snapshot.exists()) {
                    for (userSnap in snapshot.children) {
                        val workoutData = userSnap.getValue(WorkoutModel::class.java)
                        workoutList.add(workoutData!!)
                        val mAdapter = WorkoutAdapter(workoutList)
                        rvWorkout.adapter = mAdapter
                        mAdapter.setOnItemClickListener(object :
                            WorkoutAdapter.onItemClickListener {
                            override fun onItemClick(position: Int) {

                                Toast.makeText(
                                    this@SavedWorkoutActivity,
                                    "You clicked on ${workoutList[position].workoutName}",
                                    Toast.LENGTH_LONG
                                ).show()

                                setSelectedWorkout(date, position)


                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}