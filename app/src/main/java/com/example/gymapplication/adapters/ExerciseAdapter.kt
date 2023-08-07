    package com.example.gymapplication.adapters

    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.view.ViewParent
    import android.widget.TextView
    import androidx.recyclerview.widget.RecyclerView
    import com.example.gymapplication.R
    import com.example.gymapplication.models.ExerciseModel

    class ExerciseAdapter ( private val exerciseList : ArrayList<ExerciseModel>)
        :RecyclerView.Adapter<ExerciseAdapter.ViewHolder>(){


        private var mListener: onItemClickListener? = null

        interface onItemClickListener{
            fun onItemClick(position:Int)
        }

        fun setOnItemClickListener(clickListener: onItemClickListener) {
            mListener= clickListener
        }
        override fun onCreateViewHolder(parent: ViewGroup,viewType:Int) : ViewHolder {
            val itemView=LayoutInflater.from(parent.context).inflate(R.layout.exercises_item,parent,false)
            return ViewHolder(itemView)
        }
        override fun onBindViewHolder(holder:ViewHolder, position: Int) {
            val selectedExercise = exerciseList[position]
            holder.tv_exercise_name.text = selectedExercise.exerciseName
            holder.itemView.setOnClickListener{
                mListener?.onItemClick(position)
            }

        }
        override fun getItemCount(): Int {
            return exerciseList.size
        }

        class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
            val tv_exercise_name :TextView =itemView.findViewById(R.id.tv_exercises_name)

            }
        }