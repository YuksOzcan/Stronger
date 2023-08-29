package com.example.gymapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapplication.R
import com.example.gymapplication.models.WorkoutModel

class HistoryAdapter(private val workoutList : ArrayList<WorkoutModel>)
    : RecyclerView.Adapter<HistoryAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int) : ViewHolder {
        val itemView=
            LayoutInflater.from(parent.context).inflate(R.layout.history_item,parent,false)
        return ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        val workout = workoutList[position]
        holder.tvHistoryDate.text = workout.workoutDate
        val innerAdapter = WorkoutAdapter(workoutList)
        holder.rvWorkoutInner.layoutManager = LinearLayoutManager(holder.itemView.context)

        holder.rvWorkoutInner.adapter=innerAdapter

    }
    override fun getItemCount(): Int {
        return workoutList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvHistoryDate : TextView =itemView.findViewById(R.id.tvHistoryDate)
        val rvWorkoutInner : RecyclerView = itemView.findViewById(R.id.rvWorkoutInner)

    }
}