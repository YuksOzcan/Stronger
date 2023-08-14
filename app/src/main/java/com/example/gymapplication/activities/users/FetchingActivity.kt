package com.example.gymapplication.activities.users

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapplication.R
import com.example.gymapplication.adapters.NestedWorkoutAdapter
import com.example.gymapplication.adapters.UserAdapter
import com.example.gymapplication.models.UserModel
import com.example.gymapplication.models.WorkoutModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FetchingActivity :AppCompatActivity(){

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var tvLoadingdata:TextView
    private lateinit var userList:ArrayList<UserModel>
    private lateinit var dbRef : DatabaseReference
    private lateinit var trainerList:ArrayList<UserModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetch)

        userRecyclerView=findViewById(R.id.rvUser)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)
        tvLoadingdata=findViewById(R.id.tvLoadingData)

        userList= arrayListOf()
        trainerList = arrayListOf()
        getUsersData()

    }

    private fun getUsersData(){
        userRecyclerView.visibility=View.GONE
        tvLoadingdata.visibility= View.VISIBLE

        val customUrl = "https://gymappfirebase-9f06f-default-rtdb.europe-west1.firebasedatabase.app"
        dbRef = FirebaseDatabase.getInstance(customUrl).getReference("Users")
        dbRef.orderByChild("userType").equalTo("PT").addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(UserModel::class.java)
                    if (user != null) {
                        trainerList.add(user)
                    }
                }

            dbRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                   userList.clear()
                    if (snapshot.exists()){
                        for( userSnap in snapshot.children){
                            val userData = userSnap.getValue(UserModel::class.java)
                            userList.add(userData!!)
                        }
                        val mAdapter = UserAdapter(userList)
                        userRecyclerView.adapter=mAdapter

                        mAdapter.setOnItemClickListener(object : UserAdapter.onItemClickListener {
                            override fun onItemClick(position: Int) {
                                val intent = Intent(this@FetchingActivity, UserDetailsActivity::class.java)

                                intent.putExtra("user",userList[position])
                                intent.putExtra("trainerList",trainerList)
                                startActivity(intent)

                            }
                        })
                        userRecyclerView.visibility= View.VISIBLE
                        tvLoadingdata.visibility = View.GONE
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })


    }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        )
    }
}