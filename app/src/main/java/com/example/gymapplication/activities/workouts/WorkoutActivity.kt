package com.example.gymapplication.activities.workouts

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapplication.R
import com.example.gymapplication.adapters.ExerciseAdapter
import com.example.gymapplication.models.ExerciseModel
import com.google.firebase.database.*

class WorkoutActivity : AppCompatActivity() {

    private lateinit var btnStart: Button
    private lateinit var tvWorkoutName: TextView
    private lateinit var rvExercises: RecyclerView
    private lateinit var exerciseList: ArrayList<ExerciseModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)

        btnStart = findViewById(R.id.btnStartSelectedWorkout)
        tvWorkoutName = findViewById(R.id.tvSelectedWorkout)
        rvExercises = findViewById(R.id.rvWorkoutExercises)

        // Setup RecyclerView
        rvExercises.layoutManager = LinearLayoutManager(this)
        rvExercises.setHasFixedSize(true)

        tvWorkoutName.text = intent.getStringExtra("WorkoutName")

        getExercises()
    }

    private fun getExercises() {
        // Get the exercises from the intent
        val exercisesArray = intent.getSerializableExtra("ExercisesList") as? ArrayList<ExerciseModel> ?: ArrayList()

        // Create and set the adapter for the exercises RecyclerView
        val mAdapter = ExerciseAdapter(exercisesArray)
        rvExercises.adapter = mAdapter

        mAdapter.setOnItemClickListener(object : ExerciseAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                // Handle the item click if needed
            }
        })
    }
}
