package com.example.gymapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapplication.R
import com.example.gymapplication.models.WorkoutModel


    class NestedWorkoutAdapter ( private val workoutList: ArrayList<WorkoutModel>)
        : RecyclerView.Adapter<NestedWorkoutAdapter.ViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, position: Int):ViewHolder{
            val itemView= LayoutInflater.from(parent.context).inflate(R.layout.outer_workout_item,parent,false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder:ViewHolder , position:Int){
            val selectedWorkout = workoutList[position]
            holder.tvWorkoutName.text=selectedWorkout.workoutName
            val exercises = selectedWorkout.exercisesList
            if (exercises!=null) {
                val middleAdapter = MiddleWorkoutAdapter(exercises)
                holder.rvWorkoutMiddle.layoutManager = LinearLayoutManager(holder.itemView.context)
                holder.rvWorkoutMiddle.adapter = middleAdapter
            }

        }

        override fun getItemCount(): Int {
            return workoutList.size
        }

        class ViewHolder(itemview: View): RecyclerView.ViewHolder(itemview){
            val tvWorkoutName: TextView = itemview.findViewById(R.id.tvWorkoutName)
            val rvWorkoutMiddle : RecyclerView = itemview.findViewById(R.id.rvWorkoutMiddle)

        }



    }
