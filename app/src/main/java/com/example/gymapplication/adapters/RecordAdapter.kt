package com.example.gymapplication.adapters

import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapplication.R
import com.example.gymapplication.models.ExerciseModel
import com.example.gymapplication.models.RecordModel

class RecordAdapter ( private val recordList : ArrayList<RecordModel>)
    : RecyclerView.Adapter<RecordAdapter.ViewHolder>() {

    private var mListener: onItemClickListener? = null

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener) {
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.record_item, parent, false)
        return ViewHolder(itemView)
    }


    override fun getItemCount(): Int {
        return recordList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val etRepeats: EditText = itemView.findViewById(R.id.et_repeats)
        val etWeight:EditText = itemView.findViewById(R.id.et_weight)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = recordList[position]

        holder.etRepeats.setText(currentItem.repeats ?: "")
        holder.etWeight.setText(currentItem.weight ?: "")

        holder.itemView.setOnClickListener {
            mListener?.onItemClick(position)
        }
    }
    fun addItem(item: RecordModel) {
        recordList.add(item)
        notifyDataSetChanged()
    }


}
