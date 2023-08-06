package com.example.gymapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapplication.R
import com.example.gymapplication.models.ExerciseModel
import com.example.gymapplication.models.RepeatAndWeight

class RecordAdapter(private val exercises: MutableList<ExerciseModel>) : RecyclerView.Adapter<RecordAdapter.ExerciseViewHolder>() {

    inner class ExerciseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val etRepeats: EditText = view.findViewById(R.id.et_repeats)
        val etWeight: EditText = view.findViewById(R.id.et_weight)
        val btnAddItem: Button = view.findViewById(R.id.btnAddItem)
        val nestedRecyclerView: RecyclerView = view.findViewById(R.id.nestedRecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.record_item, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun getItemCount(): Int = exercises.size

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val currentExercise = exercises[position]

        // Burada nested bir RecyclerView oluşturuyoruz.
        // Bu RecyclerView, RepeatAndWeight objelerini gösterecek.
        val nestedAdapter = RepeatAndWeightAdapter(currentExercise.repeatAndWeightList)
        holder.nestedRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.nestedRecyclerView.adapter = nestedAdapter

        holder.btnAddItem.setOnClickListener {
            currentExercise.repeatAndWeightList.add(RepeatAndWeight("", ""))
            nestedAdapter.notifyItemInserted(currentExercise.repeatAndWeightList.size - 1)
        }
    }

    // Bu inner class, tekrarlar ve ağırlıklar için kullanılacak.
    inner class RepeatAndWeightAdapter(private val repeatAndWeightList: MutableList<RepeatAndWeight>) :
        RecyclerView.Adapter<RepeatAndWeightAdapter.RepeatAndWeightViewHolder>() {

        inner class RepeatAndWeightViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val etRepeats: EditText = view.findViewById(R.id.et_repeats)
            val etWeight: EditText = view.findViewById(R.id.et_weight)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepeatAndWeightViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.repeat_and_weight_item, parent, false)
            return RepeatAndWeightViewHolder(view)
        }

        override fun getItemCount(): Int = repeatAndWeightList.size

        override fun onBindViewHolder(holder: RepeatAndWeightViewHolder, position: Int) {
            val currentRepeatAndWeight = repeatAndWeightList[position]
            holder.etRepeats.setText(currentRepeatAndWeight.repeats)
            holder.etWeight.setText(currentRepeatAndWeight.weight)
        }
    }
}
