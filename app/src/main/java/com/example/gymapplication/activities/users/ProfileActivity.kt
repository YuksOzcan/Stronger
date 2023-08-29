package com.example.gymapplication.activities.users

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.gymapplication.R
import org.w3c.dom.Text

class ProfileActivity:AppCompatActivity() {
    private lateinit var tvProfile:TextView
    private lateinit var tvName:TextView
    private lateinit var tvEmail:TextView
    private lateinit var tvStatus:TextView
    private lateinit var tvPt:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        init()
    }
    private fun init(){
        tvProfile=findViewById(R.id.tvProfile)
        tvName=findViewById(R.id.tvUserName)
        tvPt=findViewById(R.id.tvUserPT)
        tvEmail=findViewById(R.id.tvUserEmail)
        tvStatus=findViewById(R.id.tvUserType)
    }

}