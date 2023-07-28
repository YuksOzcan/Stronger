package com.example.gymapplication.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.gymapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdminHomeActivity : AppCompatActivity() {

    private lateinit var btnInsertData : Button
    private lateinit var btnFetchData: Button
    private lateinit var btnSignOut:Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        val firebase: DatabaseReference =  FirebaseDatabase.getInstance().getReference()
        auth = FirebaseAuth.getInstance()

        btnInsertData= findViewById(R.id.btnInsert)
        btnFetchData=findViewById(R.id.btnFetch)
        btnSignOut=findViewById(R.id.btnAdminSignOut)

        btnSignOut.setOnClickListener{
            auth.signOut()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(Intent(this,MainActivity::class.java))
        }


        btnInsertData.setOnClickListener{
            val intent = Intent(this , InsertionActivity::class.java)
            startActivity(intent)
        }
        btnFetchData.setOnClickListener{
            val intent = Intent(this , FetchingActivity::class.java)
            startActivity(intent)
        }

    }
}