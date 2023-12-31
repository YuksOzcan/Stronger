
    package com.example.gymapplication.activities.workouts

    import android.content.Intent
    import android.os.Bundle
    import android.widget.Button
    import android.widget.Toast
    import androidx.appcompat.app.AppCompatActivity
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.example.gymapplication.R
    import com.example.gymapplication.adapters.ExerciseAdapter
    import com.example.gymapplication.adapters.UserAdapter
    import com.example.gymapplication.models.ExerciseModel
    import com.example.gymapplication.models.UserModel
    import com.google.firebase.auth.FirebaseAuth
    import com.google.firebase.database.*

    class ChooseExercisesActivity : AppCompatActivity() {

        private lateinit var rvExercises: RecyclerView
        private lateinit var exerciseList: ArrayList<ExerciseModel>
        private var selectedExercisesList: ArrayList<ExerciseModel> = ArrayList()
        private lateinit var btnSave: Button
        private lateinit var dbRef:DatabaseReference

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_choose_exercises)

            rvExercises = findViewById(R.id.rvExercises)
            rvExercises.layoutManager = LinearLayoutManager(this)
            rvExercises.setHasFixedSize(true)
            exerciseList = arrayListOf()
            btnSave = findViewById(R.id.btnSendWorkoutList)

            getExercises()

            btnSave.setOnClickListener{
                val intent = Intent(this,CreateWorkoutActivity::class.java)
                intent.putExtra("Exercises_list", selectedExercisesList)
                startActivity(intent)
            }
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
                        mAdapter.setOnItemClickListener(object :
                            ExerciseAdapter.onItemClickListener {
                            override fun onItemClick(position: Int) {
                                selectedExercisesList.add(exerciseList[position])
                            }
                        })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

    }

