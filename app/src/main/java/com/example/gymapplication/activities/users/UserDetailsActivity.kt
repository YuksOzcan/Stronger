package com.example.gymapplication.activities.users

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapplication.R
import com.example.gymapplication.activities.workouts.HistoryActivity
import com.example.gymapplication.activities.workouts.SavedWorkoutActivity
import com.example.gymapplication.adapters.WorkoutAdapter
import com.example.gymapplication.models.UserModel
import com.example.gymapplication.models.WorkoutModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserDetailsActivity :AppCompatActivity() {

    private lateinit var tvUserId: TextView
    private lateinit var tvUserName: TextView
    private lateinit var tvUserStatus: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var tvUserPT: TextView
    private lateinit var tvUserType: TextView
    private lateinit var btnAssign:Button
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private lateinit var rvWorkouts:RecyclerView
    private lateinit var dbRef:DatabaseReference
    private lateinit var workoutList:ArrayList<WorkoutModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        var ptBoolean = true
        workoutList = ArrayList()

        initView()
        setValuesToViews()
        checkUser()
        getWorkouts()

        val user = intent.getSerializableExtra("user") as? UserModel
        val userName = user?.userName
        val userId = user?.userId

        btnUpdate.setOnClickListener{
            if (userId !=null && userName!=null) {
                openUpdateDialog(userId,userName)
            }
        }
        btnDelete.setOnClickListener{
            deleteRecord(intent.getStringExtra("userId").toString(),
            )
        }
        btnAssign.setOnClickListener{
            val user = intent.getSerializableExtra("user") as? UserModel
            val intent = Intent(this, SavedWorkoutActivity::class.java)
            intent.putExtra("ptBoolean",ptBoolean)
            intent.putExtra("client",user)
            startActivity(intent)

        }


    }
    private fun checkUser() {
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
                    if (user?.userType == "PT") {
                        btnDelete.visibility = View.GONE
                        btnUpdate.visibility = View.GONE
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        )
    }

    private fun deleteRecord(id:String){

        val customUrl = "https://gymappfirebase-9f06f-default-rtdb.europe-west1.firebasedatabase.app"
        val dbref = FirebaseDatabase.getInstance(customUrl).getReference("Users").child(id)
        val mTask = dbref.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "User Deleted Successfully", Toast.LENGTH_LONG).show()
            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
            }
            .addOnFailureListener{error->
                Toast.makeText(this, "Error $error.message", Toast.LENGTH_LONG).show()

            }

    }

    private fun initView() {
        tvUserId = findViewById(R.id.tvUserId)
        tvUserName = findViewById(R.id.tvUserName)
        tvUserEmail=findViewById(R.id.tvUserEmail)
        tvUserStatus = findViewById(R.id.tvUserStatus)
        tvUserPT = findViewById(R.id.tvUserPT)
        tvUserType = findViewById(R.id.tvUserType)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
        btnAssign = findViewById(R.id.btnAssignWorkout)
        rvWorkouts=findViewById(R.id.rvPastWorkouts)
        rvWorkouts.layoutManager=LinearLayoutManager(this)
        rvWorkouts.setHasFixedSize(true)
    }

    private fun setValuesToViews() {
        val user = intent.getSerializableExtra("user") as? UserModel
            tvUserId.text = user?.userId.toString()
            tvUserName.text = user?.userName.toString()
            tvUserStatus.text= user?.userStatus.toString()
            tvUserEmail.text= user?.userEmail.toString()
            tvUserType.text=user?.userType.toString()
            tvUserPT.text=user?.userPT.toString()
        }

    private fun getWorkouts() {
        val customUrl =
            "https://gymappfirebase-9f06f-default-rtdb.europe-west1.firebasedatabase.app"
        dbRef = FirebaseDatabase.getInstance(customUrl).getReference("SelectedWorkouts")
        val client = intent.getSerializableExtra("user") as? UserModel
        val  query = dbRef.orderByChild("userId").equalTo(client?.userId.toString())


        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                workoutList.clear()
                if (snapshot.exists()) {
                    for (userSnap in snapshot.children) {
                        val workoutData = userSnap.getValue(WorkoutModel::class.java)
                        if(workoutData != null) {
                            workoutList.add(workoutData)
                            val mAdapter = WorkoutAdapter(workoutList)
                            rvWorkouts.adapter = mAdapter
                            mAdapter.setOnItemClickListener(object :
                                WorkoutAdapter.onItemClickListener {
                                override fun onItemClick(position: Int) {
                                    val intent=Intent(this@UserDetailsActivity,HistoryActivity::class.java)
                                    intent.putExtra("workout",workoutList[position])
                                    intent.putExtra("user",client)
                                    startActivity(intent)

                                }
                            })
                    }
                }
            }
        }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun openUpdateDialog(userId: String, userName: String) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)
        var selectedUser: UserModel? = null


        mDialog.setView(mDialogView)

        val etUserName = mDialogView.findViewById<EditText>(R.id.etUserName)
        val etUserEmail = mDialogView.findViewById<EditText>(R.id.etUserEmail)
        val userStatusArray = resources.getStringArray(R.array.user_status)
        val userTypesArray = resources.getStringArray(R.array.user_types)

        val sUserType = mDialogView.findViewById<Spinner>(R.id.sUserType)
        val typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, userTypesArray)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sUserType.adapter = typeAdapter
        val trainerList = intent.getSerializableExtra("trainerList") as? ArrayList<UserModel> ?: ArrayList()
        val sUserStatus = mDialogView.findViewById<Spinner>(R.id.sUserStatus)
        val statusAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,userStatusArray)
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sUserStatus.adapter=statusAdapter


        val sPt = mDialogView.findViewById<Spinner>(R.id.sPersonalTrainer)

        val trainerAdapter = object : ArrayAdapter<UserModel>(this, android.R.layout.simple_spinner_item, trainerList) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val textView = view as TextView
                textView.text = getItem(position)?.userName
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val textView = view as TextView
                textView.text = getItem(position)?.userName
                return view
            }
        }
        sPt.adapter = trainerAdapter

        sPt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedUser = parent?.getItemAtPosition(position) as UserModel

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val userType = intent.getStringExtra("userType")
        val userTypeIndex = userTypesArray.indexOf(userType)

        val userStatus = intent.getStringExtra("userStatus")
        val userStatusIndex = userStatusArray.indexOf(userStatus)

        if(userStatusIndex != -1) {
            sUserStatus.setSelection(userStatusIndex)
        }

        if (userTypeIndex != -1) {
            sUserType.setSelection(userTypeIndex)
        }
        sUserStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tvUserStatus.text = userStatusArray[position]
            }


            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        sUserType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tvUserType.text = userTypesArray[position]
            }


            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etUserName.setText(tvUserName.text.toString())
        etUserEmail.setText(tvUserEmail.text.toString())
        etUserEmail.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(etUserEmail.text.toString()).matches()) {
                    btnUpdateData.isEnabled = true
                }
                else{
                    btnUpdateData.isEnabled=false
                }
            }

        })

        mDialog.setTitle("Updating $userName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            val updatedUserName = etUserName.text.toString()
            val updatedUserEmail = etUserEmail.text.toString()
            val updatedUserType = tvUserType.text.toString()
            val updatedUserStatus = tvUserStatus.text.toString()
            val updatedUserPt=selectedUser?.userName.toString()
            val updatedUserPtid=selectedUser?.userId.toString()

            if (updatedUserStatus== userStatusArray[0] && (updatedUserType != userTypesArray[3] )){
                Toast.makeText(this,"You can not make"+updatedUserType.toString()+"Passive",Toast.LENGTH_LONG).show()
            }
            else {

                updateUserData(userId, updatedUserName, updatedUserEmail, updatedUserStatus, updatedUserPt, updatedUserType, updatedUserPtid)

                tvUserName.text = etUserName.text.toString()
                tvUserEmail.text = etUserEmail.text.toString()
                tvUserPT.text=selectedUser?.userName.toString()


                alertDialog.dismiss()
            }
        }
    }
    private fun updateUserData(id: String, name: String,email:String, status: String, PT: String?, type:String, PTid: String?) {
        val customUrl = "https://gymappfirebase-9f06f-default-rtdb.europe-west1.firebasedatabase.app"
        val dbref = FirebaseDatabase.getInstance(customUrl).getReference("Users").child(id)
        val updates = hashMapOf<String,Any>(
            "userId" to id,
            "userName" to name,
            "userEmail" to email,
            "userStatus" to status,
            "userType" to type,
        )
        PT?.let {
            updates["userPT"] = it
        }

        PTid?.let {
            updates["userPTid"] = it
        }
        dbref.setValue(updates)
    }
}