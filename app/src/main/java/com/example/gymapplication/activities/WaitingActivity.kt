package com.example.gymapplication.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.gymapplication.R

class WaitingActivity :AppCompatActivity(){

    private lateinit var tvWaiting : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting)

        tvWaiting=findViewById(R.id.tvWaiting)
    }

}