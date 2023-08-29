package com.example.gymapplication

import android.app.Activity
import android.content.Intent
import com.example.gymapplication.activities.HomeActivity
import com.example.gymapplication.activities.MainActivity
import com.example.gymapplication.activities.users.ProfileActivity
import com.example.gymapplication.activities.workouts.AddExercisesActivity
import com.example.gymapplication.activities.workouts.WorkoutHistoryActivity
import com.google.firebase.auth.FirebaseAuth

object AuthHelper {
    fun signOut(activity: Activity, auth: FirebaseAuth) {
        auth.signOut()
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity.startActivity(intent)
    }
    fun exercise(activity: Activity){
        val intent = Intent(activity, AddExercisesActivity::class.java)
        activity.startActivity(intent)
    }
    fun home(activity: Activity){
        val intent = Intent(activity, HomeActivity::class.java)
        activity.startActivity(intent)
    }
    fun history(activity: Activity){
        val intent = Intent(activity, WorkoutHistoryActivity::class.java)
        activity.startActivity(intent)
    }
    fun profile(activity: Activity){
        val intent = Intent(activity, ProfileActivity::class.java)
        activity.startActivity(intent)
    }
}