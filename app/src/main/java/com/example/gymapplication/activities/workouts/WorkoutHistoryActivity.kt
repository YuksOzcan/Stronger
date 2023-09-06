package com.example.gymapplication.activities.workouts

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapplication.AuthHelper
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
    private lateinit var btnGoHome: ImageButton
    private lateinit var btnGoExercise: ImageButton
    private lateinit var btnGoProfile: ImageButton
    private lateinit var btnGoSignOut: ImageButton
    private lateinit var btnGoHistory: ImageButton
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_history)

        rvHistory=findViewById(R.id.rvHistory)
        rvHistory.layoutManager=LinearLayoutManager(this)
        rvHistory.setHasFixedSize(true)
        getWorkouts()
        workoutList= arrayListOf()
        btnGoExercise=findViewById(R.id.btnGoToExercise)
        btnGoHome=findViewById(R.id.btnGoToHome)
        btnGoProfile=findViewById(R.id.btnGoToProfile)
        btnGoSignOut=findViewById(R.id.btnGoToSignOut)
        btnGoHistory=findViewById(R.id.btnGoToHistory)
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