package com.example.gymapplication.activities.workouts

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapplication.R
import com.example.gymapplication.adapters.HistoryAdapter
import com.example.gymapplication.adapters.WorkoutAdapter
import com.example.gymapplication.models.UserModel
import com.example.gymapplication.models.WorkoutModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class WorkoutHistoryActivity:AppCompatActivity() {

    private lateinit var rvHistory:RecyclerView
    private lateinit var dbRef: DatabaseReference
    private lateinit var workoutList: ArrayList<WorkoutModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_history)

        rvHistory=findViewById(R.id.rvHistory)
        rvHistory.layoutManager=LinearLayoutManager(this)
        rvHistory.setHasFixedSize(true)
        getWorkouts()
        workoutList= arrayListOf()
    }

    private fun getWorkouts() {
        val customUrl =
            "https://gymappfirebase-9f06f-default-rtdb.europe-west1.firebasedatabase.app"
        dbRef = FirebaseDatabase.getInstance(customUrl).getReference("SelectedWorkouts")
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val currentUserId = mAuth.currentUser?.uid
        val  query = dbRef.orderByChild("userId").equalTo(currentUserId)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                workoutList.clear()
                if (snapshot.exists()) {
                    for (userSnap in snapshot.children) {
                        val workoutData = userSnap.getValue(WorkoutModel::class.java)
                        if(workoutData != null) {
                            workoutList.add(workoutData)
                            val mAdapter = HistoryAdapter(workoutList)

                            mAdapter.setOnWorkoutClickListener(object : HistoryAdapter.OnWorkoutItemClickListener {
                                override fun onWorkoutClick(workout: WorkoutModel) {
                                    val intent= Intent(this@WorkoutHistoryActivity,HistoryActivity::class.java)
                                    intent.putExtra("workout",workout)
                                    startActivity(intent)
                                }
                            })
                            rvHistory.adapter = mAdapter


                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}