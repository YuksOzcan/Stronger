package com.example.gymapplication.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapplication.R
import com.example.gymapplication.adapters.EmpAdapter
import com.example.gymapplication.models.UserModel
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class FetchingActivity :AppCompatActivity(){

    private lateinit var empRecyclerView: RecyclerView
    private lateinit var tvLoadingdata:TextView
    private lateinit var empList:ArrayList<UserModel>
    private lateinit var dbRef : DatabaseReference






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetch)

        empRecyclerView=findViewById(R.id.rvEmp)
        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)
        tvLoadingdata=findViewById(R.id.tvLoadingData)

        empList= arrayListOf<UserModel>()

        getEmployeesData()

    }

    private fun getEmployeesData(){
        empRecyclerView.visibility=View.GONE
        tvLoadingdata.visibility= View.VISIBLE


        val customUrl = "https://gymappfirebase-9f06f-default-rtdb.europe-west1.firebasedatabase.app"
        dbRef = FirebaseDatabase.getInstance(customUrl).getReference("Users")

        dbRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                   empList.clear()
                    if (snapshot.exists()){
                        for( empSnap in snapshot.children){
                            val empData = empSnap.getValue(UserModel::class.java)
                            empList.add(empData!!)
                        }
                        val mAdapter = EmpAdapter(empList)
                        empRecyclerView.adapter=mAdapter

                        mAdapter.setOnItemClickListener(object : EmpAdapter.onItemClickListener {
                            override fun onItemClick(position: Int) {
                                val intent = Intent(this@FetchingActivity, UserDetailsActivity::class.java)

                                intent.putExtra("userId",empList[position].userId)
                                intent.putExtra("userName",empList[position].userName)
                                startActivity(intent)

                            }
                        })
                        empRecyclerView.visibility= View.VISIBLE
                        tvLoadingdata.visibility = View.GONE
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })


    }
}