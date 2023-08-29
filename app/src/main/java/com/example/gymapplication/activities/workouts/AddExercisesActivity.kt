package com.example.gymapplication.activities.workouts

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapplication.AuthHelper
import com.example.gymapplication.R
import com.example.gymapplication.activities.HomeActivity
import com.example.gymapplication.activities.MainActivity
import com.example.gymapplication.adapters.ExerciseAdapter
import com.example.gymapplication.models.ExerciseModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddExercisesActivity : AppCompatActivity(){

    private lateinit var dbRef:DatabaseReference
    private lateinit var tvExercise:TextView
    private lateinit var rvExercises:RecyclerView
    private lateinit var etExercise:TextView
    private lateinit var btnAddExercise:Button
    private lateinit var btnGoHome:Button
    private lateinit var btnGoExercise:Button
    private lateinit var btnGoProfile:Button
    private lateinit var btnGoSignOut:Button
    private lateinit var btnGoHistory:Button
    private lateinit var auth: FirebaseAuth
    private lateinit var exerciseList: ArrayList<ExerciseModel>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_exercises)


        init()
        getExercises()

        btnGoSignOut.setOnClickListener{

            AuthHelper.signOut(this,auth)
        }
        btnGoExercise.setOnClickListener {
            AuthHelper.exercise(this)
        }
        btnGoHome.setOnClickListener {
            AuthHelper.home(this)
        }
        btnAddExercise.setOnClickListener {
            saveExercises()
        }

    }
    private fun init(){
        auth = FirebaseAuth.getInstance()
        exerciseList = arrayListOf()
        etExercise=findViewById(R.id.etNewExerciseName)
        tvExercise=findViewById(R.id.tvExercises)
        rvExercises=findViewById(R.id.rvAllExercises)
        btnGoExercise=findViewById(R.id.btnGoToExerciseFromExercise)
        btnGoHome=findViewById(R.id.btnGoToHomeFromExercise)
        btnGoProfile=findViewById(R.id.btnGoToProfileFromExercise)
        btnGoSignOut=findViewById(R.id.btnGoToSignOutFromExercise)
        btnGoHistory=findViewById(R.id.btnGoToHistoryFromExercise)
        btnAddExercise=findViewById(R.id.btnAddExercise)
        rvExercises.layoutManager=LinearLayoutManager(this)
        rvExercises.setHasFixedSize(true)
    }

    private fun getExercises(){
        val customUrl = "https://gymappfirebase-9f06f-default-rtdb.europe-west1.firebasedatabase.app"
        dbRef = FirebaseDatabase.getInstance(customUrl).getReference("Exercises")
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val currentUserId = mAuth.currentUser?.uid
        val query = dbRef.orderByChild("userId").equalTo(currentUserId)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                exerciseList.clear()
                if (snapshot.exists()) {
                    for (userSnap in snapshot.children) {
                        val userData = userSnap.getValue(ExerciseModel::class.java)
                        exerciseList.add(userData!!)
                    }
                    val mAdapter = ExerciseAdapter(exerciseList)
                    rvExercises.adapter = mAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun saveExercises()
    {
        val customUrl = "https://gymappfirebase-9f06f-default-rtdb.europe-west1.firebasedatabase.app"
        dbRef= FirebaseDatabase.getInstance(customUrl).getReference("Exercises")
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val exerciseName = etExercise.text.toString()
        val currentUserId = mAuth.currentUser?.uid
            val exerciseId = dbRef.push().key!!
            val exercise = ExerciseModel(currentUserId,exerciseId, exerciseName)
            dbRef.child(exerciseId).setValue(exercise).addOnSuccessListener {
            }
        }
    }

