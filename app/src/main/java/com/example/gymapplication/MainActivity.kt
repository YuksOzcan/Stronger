package com.example.gymapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase




class MainActivity : AppCompatActivity() {

    private lateinit var btnInsertData :Button
    private lateinit var btnFetchData: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firebase: DatabaseReference =  FirebaseDatabase.getInstance().getReference()

        btnInsertData= findViewById(R.id.btnInsert)
        btnFetchData=findViewById(R.id.btnFetch)

        btnInsertData.setOnClickListener{
            val intent = Intent(this , InsertionActivity::class.java)
            startActivity(intent)
        }

    }
}