package com.example.gymapplication.activities.users

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gymapplication.R
import com.example.gymapplication.activities.WaitingActivity
import com.example.gymapplication.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var btnSave : Button
    private lateinit var dbRef: DatabaseReference
    private lateinit var tvName: TextView
    private lateinit var tvAdv:TextView
    private lateinit var imgLogo: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert)

        etName =findViewById(R.id.edit_name)
        btnSave=findViewById(R.id.btnSaveData)
        tvName=findViewById(R.id.tvInsertLogo)
        tvAdv=findViewById(R.id.tvInsertAdv)
        imgLogo=findViewById(R.id.imgStronger)

        val customUrl = "https://gymappfirebase-9f06f-default-rtdb.europe-west1.firebasedatabase.app"
        dbRef= FirebaseDatabase.getInstance(customUrl).getReference("Users")

        btnSave.setOnClickListener{
            saveNameData()
        }

    }

    private fun saveNameData() {
        val usersName = etName.text.toString()
        val userTypesArray = resources.getStringArray(R.array.user_types)
        val userStatusArray = resources.getStringArray(R.array.user_status)

        if (usersName.isEmpty()) {
            etName.error = "Please enter a name"
        } else {
            val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
            val currentUserId = mAuth.currentUser?.uid
            //userTypesArray3 is regular user
            val type= userTypesArray[0]
            val PT= null
            val status=userStatusArray[1]
            val email = intent.getStringExtra("userEmail")
            val user = UserModel(currentUserId, usersName,email,status,PT,type)
            if (currentUserId == null) {
                Toast.makeText(this, "User is not authenticated!", Toast.LENGTH_LONG).show()
                return
            }
            else {

                dbRef.child(currentUserId).setValue(user)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Data is inserted Successfully", Toast.LENGTH_LONG)
                            .show()
                        val intent = Intent(this, WaitingActivity::class.java)
                        startActivity(intent)

                    }
                    .addOnFailureListener { err ->
                        Toast.makeText(this, "Error: ${err.message}", Toast.LENGTH_LONG).show()
                    }
            }
        }
    }


}