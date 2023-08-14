package com.example.gymapplication.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.gymapplication.R
import com.example.gymapplication.activities.users.AdminHomeActivity
import com.example.gymapplication.activities.users.FetchingActivity
import com.example.gymapplication.activities.users.InsertionActivity
import com.example.gymapplication.activities.workouts.ChooseExercisesActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

            googleSignInClient = GoogleSignIn.getClient(this, gso)

        findViewById<Button>(R.id.btnGoogleSignIn).setOnClickListener {
            googleSignInClient.signOut().addOnCompleteListener {
                signInGoogle()
            }
        }
    }

    private fun signInGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)

    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result->(result.resultCode == Activity.RESULT_OK)

            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)

    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                updateUI(account)
            }
        } else {
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val firebaseAuthEmail = auth.currentUser?.email
                val dbRef = FirebaseDatabase.getInstance().getReference("Users")

                dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var foundMatch = false

                        for (userSnapshot in snapshot.children) {
                            val emailFromDatabase =
                                userSnapshot.child("userEmail").getValue(String::class.java)
                            if (emailFromDatabase == account.email) {
                                foundMatch = true
                                break
                            }
                        }
                        // burayı admin activity yapıcakasın






                        if (foundMatch) {
                            val intent = Intent(this@MainActivity, HomeActivity::class.java)

                            val dbRef = FirebaseDatabase.getInstance().getReference("Users")
                            dbRef.orderByChild("userEmail").equalTo(account.email)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()) {
                                            for (userSnapshot in snapshot.children) {
                                                val userStatusFromDatabase = userSnapshot.child("userStatus").getValue(String::class.java)
                                                if (userStatusFromDatabase == "Passive") {
                                                    val intent = Intent(this@MainActivity, WaitingActivity::class.java)
                                                    startActivity(intent)
                                                    break
                                                } else if (userStatusFromDatabase == "Active") {
                                                    //bu satır for fast access
                                                    val intent = Intent(this@MainActivity,AdminHomeActivity::class.java)
                                                    startActivity(intent)
                                                    break
                                                }
                                            }
                                        } else {
                                            Toast.makeText(this@MainActivity, "User not found", Toast.LENGTH_LONG).show()
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        Toast.makeText(this@MainActivity, "Error: ${error.message}", Toast.LENGTH_LONG).show()
                                    }
                                })
                        }else {

                            val intent = Intent(this@MainActivity, InsertionActivity::class.java)
                            intent.putExtra("userEmail", account.email)
                            intent.putExtra("name", account.displayName)
                            startActivity(intent)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@MainActivity, "Database Error: ${error.message}", Toast.LENGTH_LONG).show()
                    }
                })

            } else {
                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

}