package com.example.gymapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapplication.R
import com.example.gymapplication.models.WorkoutModel

class WorkoutAdapter ( private val workoutList: ArrayList<WorkoutModel>)
    :RecyclerView.Adapter<WorkoutAdapter.ViewHolder>(){

    private var mListener : onItemClickListener?= null

    interface onItemClickListener{
        fun onItemClick(position:Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener=clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup,position: Int):ViewHolder{
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.saved_workout_items,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder:ViewHolder , position:Int){
        val selectedWorkout = workoutList[position]
        holder.tv_workout_name.text=selectedWorkout.workoutName
        holder.itemView.setOnClickListener{
            mListener?.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return workoutList.size
    }

    class ViewHolder(itemview:View): RecyclerView.ViewHolder(itemview){
        val tv_workout_name:TextView= itemview.findViewById(R.id.tv_workout_name)
    }



}