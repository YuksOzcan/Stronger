package com.example.gymapplication.activities.workouts

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapplication.R
import com.example.gymapplication.adapters.ExerciseAdapter
import com.example.gymapplication.adapters.WorkoutAdapter
import com.example.gymapplication.models.ExerciseModel
import com.example.gymapplication.models.WorkoutModel
import com.google.firebase.database.*

private lateinit var btnCreateRoutine: Button
private lateinit var tvDate : TextView
private lateinit var rvWorkout:RecyclerView
private lateinit var dbRef:DatabaseReference
private lateinit var workoutList:ArrayList<WorkoutModel>

class SavedWorkoutActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_workouts)

        val date = intent.getStringExtra("Date")
        btnCreateRoutine=findViewById(R.id.btnCreateRoutine)
        tvDate = findViewById(R.id.tvSelectedDate)
        tvDate.text= date

        rvWorkout=findViewById(R.id.rvSavedWorkouts)
        rvWorkout.layoutManager=LinearLayoutManager(this)
        rvWorkout.setHasFixedSize(true)
        workoutList= arrayListOf()
        getWorkouts()

        btnCreateRoutine.setOnClickListener{
            val intent = Intent(this, CreateWorkoutActivity::class.java )
            startActivity(intent)
        }
    }
    private fun getWorkouts(){
        val customUrl = "https://gymappfirebase-9f06f-default-rtdb.europe-west1.firebasedatabase.app"
        dbRef = FirebaseDatabase.getInstance(customUrl).getReference("Workouts")
        dbRef.addValueEventListener(object : ValueEventListener {
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