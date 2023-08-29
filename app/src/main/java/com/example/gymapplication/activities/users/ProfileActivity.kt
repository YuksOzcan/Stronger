package com.example.gymapplication.activities.users

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.gymapplication.R
import com.example.gymapplication.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.w3c.dom.Text

class ProfileActivity:AppCompatActivity() {
    private lateinit var tvProfile:TextView
    private lateinit var tvName:TextView
    private lateinit var tvEmail:TextView
    private lateinit var tvStatus:TextView
    private lateinit var tvPt:TextView
    private lateinit var tvType: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        init()
        getUser()
    }
    private fun init(){
        tvProfile=findViewById(R.id.tvProfile)
        tvName=findViewById(R.id.tvUserName)
        tvPt=findViewById(R.id.tvUserPT)
        tvEmail=findViewById(R.id.tvUserEmail)
        tvType=findViewById(R.id.tvUserType)
        tvStatus=findViewById(R.id.tvUserStatus)
    }

    private fun getUser(){
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
                    tvName.text=user?.userName.toString()
                    tvType.text=user?.userType.toString()
                    tvPt.text=user?.userPT.toString()
                    tvEmail.text=user?.userEmail.toString()
                    tvStatus.text=user?.userStatus.toString()
                    }
                }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        }



}