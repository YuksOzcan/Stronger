package com.example.gymapplication.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gymapplication.R
import com.example.gymapplication.models.UserModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var btnSave : Button

    private lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert)

        etName =findViewById(R.id.edit_name)
        btnSave=findViewById(R.id.btnSaveData)

        val customUrl = "https://gymappfirebase-9f06f-default-rtdb.europe-west1.firebasedatabase.app"

        dbRef= FirebaseDatabase.getInstance(customUrl).getReference("Users")

        btnSave.setOnClickListener{
            saveNameData()
        }

    }

    private fun saveNameData() {
        val usersName = etName.text.toString()

        if (usersName.isEmpty()) {
            etName.error = "Please enter a name"
        } else {
            val usersID = dbRef.push().key!!
            val user = UserModel(usersID, usersName)

            dbRef.child(usersID).setValue(user)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data is inserted Successfully", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener { err ->
                    Toast.makeText(this, "Error: ${err.message}", Toast.LENGTH_LONG).show()
                }
        }
    }


}