package com.example.gymapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapplication.R
import com.example.gymapplication.models.WorkoutModel

class WorkoutHistoryAdapter (   private val workoutList: ArrayList<WorkoutModel>,
                                private val IdFilter: String?
) : RecyclerView.Adapter<WorkoutHistoryAdapter.ViewHolder>(){

    private val filteredWorkoutList = workoutList.filter { it.workoutID == IdFilter }

    private var mListener : onItemClickListener?= null

    interface onItemClickListener {
        fun onItemClick(workout: WorkoutModel)
    }
        fun setOnItemClickListener(clickListener: onItemClickListener){
            mListener=clickListener
        }

        override fun onCreateViewHolder(parent: ViewGroup, position: Int):ViewHolder{
            val itemView= LayoutInflater.from(parent.context).inflate(R.layout.saved_workout_items,parent,false)
            return ViewHolder(itemView)
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val selectedWorkout = filteredWorkoutList[position]
        holder.tv_workout_name.text = selectedWorkout.workoutName
        holder.itemView.setOnClickListener{
                mListener?.onItemClick(selectedWorkout)
            }
        }

    override fun getItemCount(): Int {
        return filteredWorkoutList.size
    }

        class ViewHolder(itemview: View): RecyclerView.ViewHolder(itemview){
            val tv_workout_name: TextView = itemview.findViewById(R.id.tv_workout_name)
        }
    }

