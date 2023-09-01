package com.example.gymapplication.activities.workouts

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapplication.R
import com.example.gymapplication.activities.users.PersonalTrainerActivity
import com.example.gymapplication.adapters.WorkoutAdapter
import com.example.gymapplication.models.UserModel
import com.example.gymapplication.models.WorkoutModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

private lateinit var btnCreateRoutine: Button
private lateinit var rvWorkout: RecyclerView
private lateinit var dbRef: DatabaseReference
private lateinit var workoutList: ArrayList<WorkoutModel>
private lateinit var professionalWorkoutList: ArrayList<WorkoutModel>

private lateinit var tvPersonal: TextView
private lateinit var tvProfessional: TextView
private lateinit var rvProfessional: RecyclerView
var boolAssing: Boolean = false
var date: String? = null
var ptBooleanReceived: Boolean = false
var ptBooleanShare: Boolean = false
var savedDate: String? = null

class SavedWorkoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_workouts)

        date = intent.getStringExtra("Date")
        var sharedPref = getSharedPreferences("MyApp", MODE_PRIVATE)
        savedDate = sharedPref.getString("selectedDate", null)

        ptBooleanReceived = intent.getBooleanExtra("ptBoolean", false)
        ptBooleanShare = intent.getBooleanExtra("share", false)

        if (date != null) {
            getWorkouts(date!!)
            checkUser(date!!)

        } else {
            if (savedDate != null) {
                date = savedDate
                getWorkouts(date!!)
                checkUser(date!!)

                with(sharedPref.edit()) {
                    remove("selectedDate")
                    commit()
                }
            } else {
                if (!ptBooleanShare) {
                    openCalendarDialog()
                } else {
                    getWorkouts("Share a Workout")


//belki buraya         getProfessionalWorkoouts(date) koyman gerekebilir
                }
            }
        }



btnCreateRoutine = findViewById(R.id.btnCreateRoutine)
        rvWorkout = findViewById(R.id.rvSavedWorkouts)
        rvWorkout.layoutManager = LinearLayoutManager(this)
        rvWorkout.setHasFixedSize(true)
        workoutList = arrayListOf()
        professionalWorkoutList= arrayListOf()
        tvPersonal=findViewById(R.id.tvPersonal)
        tvProfessional=findViewById(R.id.tvProfessional)
        rvProfessional=findViewById(R.id.rvProfessionalWorkouts)
        rvProfessional.layoutManager=LinearLayoutManager(this)
        rvProfessional.setHasFixedSize(true)

        btnCreateRoutine.setOnClickListener {
            val sharedPref = getSharedPreferences("MyApp", MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("selectedDate", date)
                commit()
            }
            val intent = Intent(this, CreateWorkoutActivity::class.java)
            intent.putExtra("Date", date)
            startActivity(intent)
        }


    }
    private fun shareWorkout(position: Int){
        val customUrl =
            "https://gymappfirebase-9f06f-default-rtdb.europe-west1.firebasedatabase.app"
        dbRef = FirebaseDatabase.getInstance(customUrl).getReference("ProfessionalWorkouts")
        val workoutID = dbRef.push().key!!
        val workout = WorkoutModel(
                workoutID, workoutList[position].workoutName,
                workoutList[position].exercisesList)
        dbRef.child(workoutID).setValue(workout)
        val intent = Intent(this@SavedWorkoutActivity, PersonalTrainerActivity::class.java)
        startActivity(intent)
    }
    private fun checkUser(date: String) {
        val customUrl =
            "https://gymappfirebase-9f06f-default-rtdb.europe-west1.firebasedatabase.app"
        val dbRef = FirebaseDatabase.getInstance(customUrl).getReference("Users")
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val currentUserId = mAuth.currentUser?.uid
        dbRef.orderByChild("userId").equalTo(currentUserId).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(UserModel::class.java)
                    if (user?.userStatus == "Active") {
                        getProfessionalWorkoouts(date)
                    }
                    tvProfessional.visibility=View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        )
    }


    private fun openCalendarDialog() {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.calendar_dialog, null)
        val calendarDate = mDialogView.findViewById<CalendarView>(R.id.calendarWorkout)
        var selectedDate: String? = null

        calendarDate.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val months = arrayOf(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
            )

            selectedDate = "$dayOfMonth ${months[month]} $year"
            Toast.makeText(this, selectedDate, Toast.LENGTH_SHORT).show()
        }

        mDialog.setView(mDialogView)
        mDialog.setPositiveButton("Select") { dialog, _ ->
            if (selectedDate != null) {
                boolAssing = true
                dialog.dismiss()
                if (ptBooleanReceived) {
                    getWorkouts(selectedDate!!)
                }
            } else {
                Toast.makeText(this, "Please choose a Date", Toast.LENGTH_SHORT).show()
            }
        }

        mDialog.show()

    }


    private fun setSelectedWorkout(date: String, position: Int) {
        val customUrl =
            "https://gymappfirebase-9f06f-default-rtdb.europe-west1.firebasedatabase.app"
        dbRef = FirebaseDatabase.getInstance(customUrl).getReference("Workouts")
        dbRef = FirebaseDatabase.getInstance(customUrl).getReference("SelectedWorkouts")
        val client = intent.getSerializableExtra("client") as? UserModel
        val clientId = client?.userId.toString()
        val workoutID = dbRef.push().key!!
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val currentUserId = mAuth.currentUser?.uid
        var workout = WorkoutModel()
        if (!boolAssing) {
            workout = WorkoutModel(
                workoutID, workoutList[position].workoutName,
                workoutList[position].exercisesList, date + currentUserId,currentUserId,date)

        } else {
            workout = WorkoutModel(
                workoutID, workoutList[position].workoutName,
                workoutList[position].exercisesList, date + clientId,clientId,date)

        }

        dbRef.child(workoutID).setValue(workout)
        val intent = Intent(this@SavedWorkoutActivity, WorkoutActivity::class.java)
        intent.putExtra("WorkoutName", workoutList[position].workoutName)
        intent.putExtra("ExercisesList", workoutList[position].exercisesList)
        intent.putExtra("WorkoutList", workoutID)
        intent.putExtra("Date", date)
        startActivity(intent)
    }


    private fun getWorkouts(date: String) {
       // getProfessionalWorkoouts(date)
        val customUrl =
            "https://gymappfirebase-9f06f-default-rtdb.europe-west1.firebasedatabase.app"
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val currentUserId = mAuth.currentUser?.uid
        dbRef = FirebaseDatabase.getInstance(customUrl).getReference("Workouts")
        val  query = dbRef.orderByChild("userId").equalTo(currentUserId)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                workoutList.clear()
                if (snapshot.exists()) {
                    for (userSnap in snapshot.children) {
                        val workoutData = userSnap.getValue(WorkoutModel::class.java)
                        workoutList.add(workoutData!!)
                        val mAdapter = WorkoutAdapter(workoutList)
                        rvWorkout.adapter = mAdapter
                        mAdapter.setOnItemClickListener(object :
                            WorkoutAdapter.onItemClickListener {
                            override fun onItemClick(position: Int) {

                                Toast.makeText(
                                    this@SavedWorkoutActivity,
                                    "You clicked on ${workoutList[position].workoutName}",
                                    Toast.LENGTH_LONG
                                ).show()

                                if (!ptBooleanShare) {
                                    setSelectedWorkout(date, position)
                                }
                                else{
                                    shareWorkout(position)
                                }
                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    private fun getProfessionalWorkoouts(date: String) {
        val customUrl =
            "https://gymappfirebase-9f06f-default-rtdb.europe-west1.firebasedatabase.app"
        dbRef = FirebaseDatabase.getInstance(customUrl).getReference("ProfessionalWorkouts")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                professionalWorkoutList.clear()
                if (snapshot.exists()) {
                    for (userSnap in snapshot.children) {
                        val workoutData = userSnap.getValue(WorkoutModel::class.java)
                        professionalWorkoutList.add(workoutData!!)
                        val mAdapter = WorkoutAdapter(professionalWorkoutList)
                        rvProfessional.adapter = mAdapter
                        mAdapter.setOnItemClickListener(object :
                            WorkoutAdapter.onItemClickListener {
                            override fun onItemClick(position: Int) {


                                if (!ptBooleanShare) {
                                    setSelectedWorkout(date, position)
                                }
                                else{
                                    shareWorkout(position)
                                }
                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}