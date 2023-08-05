package com.example.gymapplication.activities.workouts

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapplication.R
import com.example.gymapplication.activities.WaitingActivity
import com.example.gymapplication.adapters.ExerciseAdapter
import com.example.gymapplication.models.ExerciseModel
import com.example.gymapplication.models.WorkoutModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class CreateWorkoutActivity:AppCompatActivity() {

    private lateinit var btnExercises: Button
    private lateinit var etWorkoutName: EditText
    private var selectedExercisesList: ArrayList<ExerciseModel> = ArrayList()
    private lateinit var rvExercises: RecyclerView
    private lateinit var btnSave:Button
    private lateinit var dbRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_workout)

        btnExercises= findViewById(R.id.btnExercises)
        etWorkoutName=findViewById(R.id.etWorkoutName)
        rvExercises=findViewById(R.id.rvSelectedExercises)
        btnSave=findViewById(R.id.btnSaveWorkout)



        getExercises()

        btnExercises.setOnClickListener{
            val intent = Intent(this,ChooseExercisesActivity::class.java)
            startActivity(intent)
        }
        btnSave.setOnClickListener{
            saveWorkout()
        }


    }
    private fun saveWorkout(){
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val currentUserId = mAuth.currentUser?.uid
        val customUrl = "https://gymappfirebase-9f06f-default-rtdb.europe-west1.firebasedatabase.app"

        val userRef = FirebaseDatabase.getInstance(customUrl).getReference("Users").child(currentUserId!!)

        dbRef= FirebaseDatabase.getInstance(customUrl).getReference("Workouts")
        val workoutName = etWorkoutName.text.toString()
        if (workoutName.isEmpty()){
            Toast.makeText(this,"Please enter a workout name",Toast.LENGTH_LONG).show()
        }
        else{
            val workoutID =dbRef.push().key!!
            val workout= WorkoutModel(workoutID,workoutName,selectedExercisesList)

            dbRef.child(workoutID).setValue(workout)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data is inserted Successfully", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, SavedWorkoutActivity::class.java)
                    startActivity(intent)
                    userRef.child("workouts").addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val currentWorkouts = dataSnapshot.getValue<ArrayList<String>>() ?: ArrayList()
                            currentWorkouts.add(workoutID)
                            userRef.child("workouts").setValue(currentWorkouts)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
        }

    }
    }


    private fun getExercises(){
        selectedExercisesList = intent.getSerializableExtra("Exercises_list") as? ArrayList<ExerciseModel> ?: ArrayList()
        rvExercises.layoutManager = LinearLayoutManager(this)
        val mAdapter = ExerciseAdapter(selectedExercisesList)
        rvExercises.adapter = mAdapter
    }
}
