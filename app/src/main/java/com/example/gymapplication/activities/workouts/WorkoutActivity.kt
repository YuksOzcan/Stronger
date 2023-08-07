package com.example.gymapplication.activities.workouts

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapplication.R
import com.example.gymapplication.adapters.ExerciseAdapter
import com.example.gymapplication.models.ExerciseModel
import com.example.gymapplication.models.WorkoutModel
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

        rvExercises.layoutManager = LinearLayoutManager(this)
        rvExercises.setHasFixedSize(true)

        tvWorkoutName.text = intent.getStringExtra("WorkoutName")

        getExercises()
        val exercisesArray = intent.getSerializableExtra("ExercisesList") as? ArrayList<ExerciseModel> ?: ArrayList()
        val workoutArray = intent.getSerializableExtra("WorkoutList") as? ArrayList<WorkoutModel> ?: ArrayList()


        btnStart.setOnClickListener{
            val intent = Intent(this,RecordActivity::class.java)
            intent.putExtra("ExercisesList", exercisesArray)
            intent.putExtra("WorkoutList", workoutArray)
            startActivity(intent)
        }
    }

    private fun getExercises() {
        val exercisesArray = intent.getSerializableExtra("ExercisesList") as? ArrayList<ExerciseModel> ?: ArrayList()
        val mAdapter = ExerciseAdapter(exercisesArray)
        rvExercises.adapter = mAdapter

        mAdapter.setOnItemClickListener(object : ExerciseAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {

            }
        })
    }
}
