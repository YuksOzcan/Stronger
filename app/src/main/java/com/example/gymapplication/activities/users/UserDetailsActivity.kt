package com.example.gymapplication.activities.users

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.gymapplication.R
import com.example.gymapplication.models.UserModel
import com.google.firebase.database.FirebaseDatabase

class UserDetailsActivity :AppCompatActivity() {

    private lateinit var tvUserId: TextView
    private lateinit var tvUserName: TextView
    private lateinit var tvUserStatus: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var tvUserPT: TextView
    private lateinit var tvUserType: TextView


    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)


        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener{
            openUpdateDialog(
                intent.getStringExtra("userId").toString(),
                intent.getStringExtra("userName").toString(),

            )
        }
        btnDelete.setOnClickListener{
            deleteRecord(intent.getStringExtra("userId").toString(),
            )
        }

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
    }

    private fun setValuesToViews() {
        tvUserId.text = intent.getStringExtra("userId")
        tvUserName.text = intent.getStringExtra("userName")
        tvUserStatus.text=intent.getStringExtra("userStatus")
        tvUserEmail.text=intent.getStringExtra("userEmail")
        tvUserType.text=intent.getStringExtra("userType")
        tvUserPT.text=intent.getStringExtra("userPT")

    }

    private fun openUpdateDialog(userId: String, userName: String) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etUserName = mDialogView.findViewById<EditText>(R.id.etUserName)
        val etUserEmail = mDialogView.findViewById<EditText>(R.id.etUserEmail)
        val userStatusArray = resources.getStringArray(R.array.user_status)
        val userTypesArray = resources.getStringArray(R.array.user_types)

        val sUserType = mDialogView.findViewById<Spinner>(R.id.sUserType)
        val typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, userTypesArray)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sUserType.adapter = typeAdapter

        val sUserStatus = mDialogView.findViewById<Spinner>(R.id.sUserStatus)
        val statusAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,userStatusArray)
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sUserStatus.adapter=statusAdapter
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
                // Not implemented
            }
        }

        sUserType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tvUserType.text = userTypesArray[position]
            }


            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Not implemented
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

            if (updatedUserStatus== userStatusArray[0] && (updatedUserType != userTypesArray[3] )){
                Toast.makeText(this,"You can not make"+updatedUserType.toString()+"Passive",Toast.LENGTH_LONG).show()
            }
            else {

                updateUserData(
                    userId,
                    updatedUserName,
                    updatedUserEmail,
                    updatedUserStatus,
                    "none",
                    updatedUserType
                )

                tvUserName.text = etUserName.text.toString()
                tvUserEmail.text = etUserEmail.text.toString()


                alertDialog.dismiss()
            }
        }


    }
    private fun updateUserData(id: String, name: String,email:String, status: String, PT: String, type:String) {

        val customUrl = "https://gymappfirebase-9f06f-default-rtdb.europe-west1.firebasedatabase.app"

        val dbref = FirebaseDatabase.getInstance(customUrl).getReference("Users").child(id)
        val userInfo = UserModel(id, name, email,status,PT,type)
        dbref.setValue(userInfo)
    }
}