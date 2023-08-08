package com.example.gymapplication.activities.workouts

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapplication.R
import com.example.gymapplication.activities.HomeActivity
import com.example.gymapplication.adapters.ExerciseAdapter
import com.example.gymapplication.adapters.RecordAdapter
import com.example.gymapplication.models.ExerciseModel
import com.example.gymapplication.models.RecordModel
import com.example.gymapplication.models.WorkoutModel
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class RecordActivity:AppCompatActivity() {
    private lateinit var btnAddItem : Button
    private lateinit var etAddWeight:EditText
    private lateinit var etAddRepeat:EditText
    private lateinit var tvRepeat:TextView
    private lateinit var tvWeight:TextView
    private lateinit var btnNext:Button
    private lateinit var rvRecords:RecyclerView
    private lateinit var tvExerciseName:TextView
    private lateinit var newRecords: ArrayList<RecordModel>
    private lateinit var db:DatabaseReference
    private var i = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        i = intent.getIntExtra("currentIndex", 0)
        val exercisesArray = intent.getSerializableExtra("ExercisesList") as? ArrayList<ExerciseModel> ?: ArrayList()
        val workoutID = intent.getStringExtra("WorkoutList")
        initView()
        getValues(i)

        btnNext.setOnClickListener {
            for (record in newRecords) {
                val exercise = exercisesArray[i].exerciseName ?: ""
                val customUrl = "https://gymappfirebase-9f06f-default-rtdb.europe-west1.firebasedatabase.app"

                if (workoutID != null) {
                    val recordRef = FirebaseDatabase.getInstance(customUrl).getReference("SelectedWorkouts").child(workoutID).child("exercisesList").child(i.toString()).child("exerciseRecord")

                    recordRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val existingRecords = dataSnapshot.getValue<ArrayList<RecordModel>>() ?: ArrayList()
                            for (recordModel in newRecords) {
                                existingRecords.add(recordModel)
                            }
                            recordRef.setValue(existingRecords)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                }

            }
            i++
            if(i>=exercisesArray.size){
                val intent = Intent(this,HomeActivity::class.java)
                startActivity(intent)

            } else {
                refreshActivity()
            }

        }





    }
    private fun refreshActivity() {
        val intent = intent
        val exercisesArray = intent.getSerializableExtra("ExercisesList") as? ArrayList<ExerciseModel> ?: ArrayList()
        val workoutID = intent.getStringExtra("WorkoutList")
        intent.putExtra("ExercisesList", exercisesArray)
        intent.putExtra("WorkoutList", workoutID)
        intent.putExtra("currentIndex", i)

        finish()
        startActivity(intent)
    }



    private fun initView(){
        btnAddItem = findViewById(R.id.btnAddItem)
        etAddRepeat = findViewById(R.id.etAddRepeats)
        etAddWeight = findViewById(R.id.etAddWeight)
        tvRepeat = findViewById(R.id.tvRepeat)
        tvWeight = findViewById(R.id.tvWeight)
        btnNext = findViewById(R.id.btnNextPage)
        rvRecords = findViewById(R.id.rvRecords)
        tvExerciseName = findViewById(R.id.tvExerciseName)
        newRecords = ArrayList<RecordModel>()
        rvRecords.layoutManager=LinearLayoutManager(this)
        rvRecords.setHasFixedSize(true)
    }

    private fun getValues(i: Int){
        val exercisesArray = intent.getSerializableExtra("ExercisesList") as? ArrayList<ExerciseModel> ?: ArrayList()
        val mAdapter = RecordAdapter(exercisesArray[i].exerciseRecord)
        if (exercisesArray.isNotEmpty()) {
            tvExerciseName.text = exercisesArray[i].exerciseName ?: ""

        }
        rvRecords.adapter = mAdapter
        btnAddItem.setOnClickListener {
            val newRecord = RecordModel()
            newRecord.repeats = etAddRepeat.text.toString()
            newRecord.weight = etAddWeight.text.toString()
            newRecords.add(newRecord)
            mAdapter.notifyDataSetChanged()
        }


    }
}