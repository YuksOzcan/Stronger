package com.example.gymapplication.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.gymapplication.R
import com.google.firebase.auth.FirebaseAuth

class WaitingActivity :AppCompatActivity(){

    private lateinit var tvWaiting : TextView
    private lateinit var btnSignOut: Button
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting)

        tvWaiting=findViewById(R.id.tvWaiting)
        btnSignOut=findViewById(R.id.btnWaitingSignOut)
        auth = FirebaseAuth.getInstance()


        btnSignOut.setOnClickListener{
            auth.signOut()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(Intent(this,MainActivity::class.java))
        }
    }

}