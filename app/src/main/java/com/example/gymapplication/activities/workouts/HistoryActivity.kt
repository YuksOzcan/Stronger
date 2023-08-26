package com.example.gymapplication.activities.workouts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapplication.R
import com.example.gymapplication.activities.users.UserDetailsActivity
import com.example.gymapplication.adapters.NestedWorkoutAdapter
import com.example.gymapplication.models.UserModel
import com.example.gymapplication.models.WorkoutModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HistoryActivity:AppCompatActivity() {

    private lateinit var tvDate: TextView
    private lateinit var btnPrevious:Button
    private lateinit var rvWorkout:RecyclerView
    private lateinit var dbRef: DatabaseReference
    private lateinit var workoutList:ArrayList<WorkoutModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        val workout = intent.getSerializableExtra("workout") as? WorkoutModel
        tvDate= findViewById(R.id.tvPastDate)
        btnPrevious=findViewById(R.id.btnPastWorkoutPrevious)
        rvWorkout=findViewById(R.id.rvFinishedWorkout)
        workoutList= arrayListOf()
        rvWorkout.layoutManager=LinearLayoutManager(this)
        rvWorkout.setHasFixedSize(true)
        tvDate.text=workout?.workoutDate.toString()
        val user = intent.getSerializableExtra("user") as? UserModel

        getWorkouts()

        btnPrevious.setOnClickListener{
            val intent= Intent(this,UserDetailsActivity::class.java)
            intent.putExtra("user",user)
            startActivity(intent)

        }
    }
    private fun getWorkouts() {
        val customUrl = "https://gymappfirebase-9f06f-default-rtdb.europe-west1.firebasedatabase.app"
        dbRef = FirebaseDatabase.getInstance(customUrl).getReference("SelectedWorkouts")
        val workoutRoutine = intent.getSerializableExtra("workout") as? WorkoutModel
        val  query = dbRef.orderByChild("combinedKey").equalTo(workoutRoutine?.combinedKey)
        workoutList.clear()
        rvWorkout.adapter=null
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (workoutSnapshot in dataSnapshot.children) {
                        val workout = workoutSnapshot.getValue(WorkoutModel::class.java)
                        if (workout != null) {
                            workoutList.add(workout!!)
                            val mAdapter = NestedWorkoutAdapter(workoutList)
                            rvWorkout.adapter=mAdapter

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
}