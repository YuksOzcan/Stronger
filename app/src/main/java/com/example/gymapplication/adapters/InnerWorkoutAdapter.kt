package com.example.gymapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapplication.R
import com.example.gymapplication.models.RecordModel

class InnerWorkoutAdapter ( private val recordList : ArrayList<RecordModel>)
    : RecyclerView.Adapter<InnerWorkoutAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.inner_workout_item, parent, false)
        return ViewHolder(itemView)
    }


    override fun getItemCount(): Int {
        return recordList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRepeats: TextView = itemView.findViewById(R.id.tvWorkoutInnerRepeats)
        val tvWeight: TextView = itemView.findViewById(R.id.tvWorkoutInnerWeight)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = recordList[position]

        holder.tvRepeats.text = currentItem.repeats
        holder.tvWeight.text= currentItem.weight


    }
}