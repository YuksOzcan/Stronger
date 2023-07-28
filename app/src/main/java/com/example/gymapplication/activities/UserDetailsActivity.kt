package com.example.gymapplication.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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
    private lateinit var tvEmpSalary: TextView
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
        tvUserStatus = findViewById(R.id.tvUserStatus)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvUserId.text = intent.getStringExtra("userId")
        tvUserName.text = intent.getStringExtra("userName")
        tvUserStatus.text=intent.getStringExtra("userStatus")

    }

    private fun openUpdateDialog(userId: String, userName: String) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etUserName = mDialogView.findViewById<EditText>(R.id.etUserName)
        val etUserStatus = mDialogView.findViewById<EditText>(R.id.etUserStatus)
        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etUserName.setText(intent.getStringExtra("userName").toString())
        etUserStatus.setText(intent.getStringExtra("userStatus").toString())

        mDialog.setTitle("Updating $userName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            val updatedUserName = etUserName.text.toString()
            val updatedUserStatus = etUserStatus.text.toString()

            updateUserData(userId, updatedUserName, updatedUserStatus)

            tvUserName.text=etUserName.text.toString()
            tvUserStatus.text=etUserStatus.text.toString()


            alertDialog.dismiss()
        }


    }
    private fun updateUserData(id: String, name: String, status: String) {

        val customUrl = "https://gymappfirebase-9f06f-default-rtdb.europe-west1.firebasedatabase.app"

        val dbref = FirebaseDatabase.getInstance(customUrl).getReference("Users").child(id)
        val userInfo = UserModel(id, name, status)
        dbref.setValue(userInfo)
    }
}