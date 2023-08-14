package com.example.gymapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapplication.R
import com.example.gymapplication.models.ExerciseModel

class MiddleWorkoutAdapter (private val exerciseList : ArrayList<ExerciseModel>)
    : RecyclerView.Adapter<MiddleWorkoutAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int) : ViewHolder {
        val itemView=
            LayoutInflater.from(parent.context).inflate(R.layout.middle_workout_item,parent,false)
        return ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        val selectedExercise = exerciseList[position]
        holder.tvExerciseName.text = selectedExercise.exerciseName
        val innerAdapter = InnerWorkoutAdapter(selectedExercise.exerciseRecord)
        holder.rvWorkoutInner.layoutManager = LinearLayoutManager(holder.itemView.context)

        holder.rvWorkoutInner.adapter=innerAdapter

    }
    override fun getItemCount(): Int {
        return exerciseList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvExerciseName : TextView =itemView.findViewById(R.id.tvMiddleExerciseName)
        val rvWorkoutInner : RecyclerView= itemView.findViewById(R.id.rvWorkoutInner)

    }
}