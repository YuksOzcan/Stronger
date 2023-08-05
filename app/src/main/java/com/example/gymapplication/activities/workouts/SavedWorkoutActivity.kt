package com.example.gymapplication.activities.workouts

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.gymapplication.R

private lateinit var btnCreateRoutine: Button
private lateinit var tvDate : TextView

class SavedWorkoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_workouts)

        val date = intent.getStringExtra("Date")
        btnCreateRoutine=findViewById(R.id.btnCreateRoutine)
        tvDate = findViewById(R.id.tvSelectedDate)
        tvDate.text= date

        btnCreateRoutine.setOnClickListener{
            val intent = Intent(this, CreateWorkoutActivity::class.java )
            startActivity(intent)
        }
    }
}