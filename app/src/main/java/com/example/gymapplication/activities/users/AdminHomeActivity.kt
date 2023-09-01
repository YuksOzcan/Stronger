package com.example.gymapplication.activities.users

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gymapplication.R
import com.example.gymapplication.activities.HomeActivity
import com.example.gymapplication.activities.MainActivity
import com.example.gymapplication.models.ExerciseModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdminHomeActivity : AppCompatActivity() {

    private lateinit var btnFetchData: Button
    private lateinit var btnSignOut:Button
    private lateinit var auth: FirebaseAuth
    private lateinit var btnHomePage:Button
    private lateinit var dbRef:DatabaseReference
    private lateinit var btnPT:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        val customUrl = "https://gymappfirebase-9f06f-default-rtdb.europe-west1.firebasedatabase.app"
        dbRef= FirebaseDatabase.getInstance(customUrl).getReference("Exercises")

        auth = FirebaseAuth.getInstance()

        btnFetchData=findViewById(R.id.btnFetch)
        btnSignOut=findViewById(R.id.btnAdminSignOut)
        btnHomePage=findViewById(R.id.btnHome)
        btnPT=findViewById(R.id.btnGoToPT_Section)


        btnSignOut.setOnClickListener{
            auth.signOut()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(Intent(this, MainActivity::class.java))
        }
        btnPT.setOnClickListener{
            val intent = Intent(this,PersonalTrainerActivity::class.java)
            startActivity(intent)
        }

        btnFetchData.setOnClickListener{
            val intent = Intent(this , FetchingActivity::class.java)
            startActivity(intent)
        }
        btnHomePage.setOnClickListener{
            val intent = Intent(this , HomeActivity::class.java)
            startActivity(intent)
        }

    }
}