package com.example.gymapplication.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapplication.R
import com.example.gymapplication.adapters.UserAdapter
import com.example.gymapplication.models.UserModel
import com.google.firebase.database.*

class FetchingActivity :AppCompatActivity(){

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var tvLoadingdata:TextView
    private lateinit var userList:ArrayList<UserModel>
    private lateinit var dbRef : DatabaseReference






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetch)

        userRecyclerView=findViewById(R.id.rvUser)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)
        tvLoadingdata=findViewById(R.id.tvLoadingData)

        userList= arrayListOf<UserModel>()

        getUsersData()

    }

    private fun getUsersData(){
        userRecyclerView.visibility=View.GONE
        tvLoadingdata.visibility= View.VISIBLE


        val customUrl = "https://gymappfirebase-9f06f-default-rtdb.europe-west1.firebasedatabase.app"
        dbRef = FirebaseDatabase.getInstance(customUrl).getReference("Users")

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

                                intent.putExtra("userId",userList[position].userId)
                                intent.putExtra("userName",userList[position].userName)
                                intent.putExtra("userEmail",userList[position].userEmail)
                                intent.putExtra("userPT",userList[position].userPT)
                                intent.putExtra("userType",userList[position].userType)
                                intent.putExtra("userStatus",userList[position].userStatus)
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
}