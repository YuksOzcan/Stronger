package com.example.gymapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

        dbRef= FirebaseDatabase.getInstance().getReference("Users")

        btnSave.setOnClickListener{
            saveNameData()
        }

    }

    private fun saveNameData(){

        val usersName = etName.text.toString()

        if(usersName.isEmpty()) {
            etName.error = "Please enter a name"
        }
        val usersID = dbRef.push().key!!

        val user= UserModel(usersID,usersName)
        Log.d("InsertionActivity", "usersID: $usersID")
        Log.d("InsertionActivity", "user: $user")
        dbRef.child(usersID).setValue(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Başarılı
                    Toast.makeText(this, "Data is inserted Successfully", Toast.LENGTH_LONG).show()
                } else {
                    // Hata durumunu ele al
                    val exception = task.exception
                    Toast.makeText(this, "Error: ${exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

}