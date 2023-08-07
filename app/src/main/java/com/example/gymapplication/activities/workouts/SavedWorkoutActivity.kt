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
import com.example.gymapplication.adapters.WorkoutAdapter
import com.example.gymapplication.models.WorkoutModel
import com.google.firebase.auth.FirebaseAuth
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

        tvDate = findViewById(R.id.tvSelectedDate)
        var date = intent.getStringExtra("Date")
        var sharedPref = getSharedPreferences("MyApp", MODE_PRIVATE)
        if(date != null){
            tvDate.text = date
        } else {
            date = sharedPref.getString("selectedDate", "No Date Available")
            tvDate.text = date
        }

        btnCreateRoutine=findViewById(R.id.btnCreateRoutine)
        rvWorkout=findViewById(R.id.rvSavedWorkouts)
        rvWorkout.layoutManager=LinearLayoutManager(this)
        rvWorkout.setHasFixedSize(true)
        workoutList= arrayListOf()
        if(date!=null) {
            getWorkouts(date)

        }
        btnCreateRoutine.setOnClickListener {
            val sharedPref = getSharedPreferences("MyApp", MODE_PRIVATE)
            with (sharedPref.edit()) {
                putString("selectedDate", date)
                commit()
            }
            val intent = Intent(this, CreateWorkoutActivity::class.java )
            intent.putExtra("Date", date)
            startActivity(intent)
        }


    }

    private fun getWorkouts(date:String){
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
                                dbRef = FirebaseDatabase.getInstance(customUrl).getReference("SelectedWorkouts")
                                val workoutID =dbRef.push().key!!
                                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                                val currentUserId = mAuth.currentUser?.uid
                                val workout = WorkoutModel(workoutID,workoutList[position].workoutName,
                                    workoutList[position].exercisesList,date,currentUserId)
                                dbRef.child(workoutID).setValue(workout)
                                val intent = Intent(this@SavedWorkoutActivity,WorkoutActivity::class.java)
                                intent.putExtra("WorkoutName", workoutList[position].workoutName)
                                intent.putExtra("ExercisesList", workoutList[position].exercisesList)
                                intent.putExtra("WorkoutList", workoutList[position])
                                intent.putExtra("Date",date)
                                startActivity(intent)




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