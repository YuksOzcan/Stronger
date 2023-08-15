package com.example.gymapplication.activities.users

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapplication.R
import com.example.gymapplication.adapters.UserAdapter
import com.example.gymapplication.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class PersonalTrainerActivity : AppCompatActivity() {

    private lateinit var rvClient:RecyclerView
    private lateinit var tvClientName:TextView
    private lateinit var dbRef:DatabaseReference
    private lateinit var clientList: ArrayList<UserModel>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pt)

        rvClient=findViewById(R.id.rvClients)
        tvClientName=findViewById(R.id.tvPT_clientsName)
        rvClient.layoutManager=LinearLayoutManager(this)
        rvClient.setHasFixedSize(true)
        clientList= arrayListOf()
        getVIPusers()

    }

    private fun getVIPusers(){
        val customUrl = "https://gymappfirebase-9f06f-default-rtdb.europe-west1.firebasedatabase.app"
        dbRef = FirebaseDatabase.getInstance(customUrl).getReference("Users")
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val currentUserId = mAuth.currentUser?.uid
        val query = dbRef.orderByChild("userPTid").equalTo(currentUserId)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                clientList.clear()
                if (dataSnapshot.exists()) {
                    for (workoutSnapshot in dataSnapshot.children) {
                        val user = workoutSnapshot.getValue(UserModel::class.java)
                        if (user != null) {
                            clientList.add(user!!)
                            val mAdapter = UserAdapter(clientList)
                            rvClient.adapter=mAdapter

                            mAdapter.setOnItemClickListener(object :UserAdapter.onItemClickListener{
                                override fun onItemClick(position: Int) {
                                    val intent = Intent(this@PersonalTrainerActivity,UserDetailsActivity::class.java)
                                    intent.putExtra("user",clientList[position])
                                    startActivity(intent)

                                }
                            })

                        }
                    }
                } else {
                    Log.d("DEBUG", "No data found for the given userId")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("DB_ERROR", "Database error: ${databaseError.message}")
            }
        })



    }
}