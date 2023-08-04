package com.example.gymapplication.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapplication.R
import com.example.gymapplication.models.CalendarModel
import java.util.*
import kotlin.collections.ArrayList

class CalendarAdapter(private val listener: (calendarDateModel: CalendarModel, position: Int) -> Unit):
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>(){

    private var list = ArrayList<CalendarModel>()
    var adapterPosition = -1


    interface onItemClickListener{
        fun onItemClick(text: String, date: String, day: String)
    }

    private var mListener: onItemClickListener? = null

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.calendar_item,parent,false)
        return CalendarViewHolder(view)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val itemList = list[position]
        holder.calendarDay.text = itemList.calendarDay
        holder.calendarDate.text = itemList.calendarDate


        holder.itemView.setOnClickListener {
            adapterPosition = position
            notifyItemRangeChanged(0, list.size)

            val text = itemList.calendarYear
            val date = itemList.calendarDate
            val day = itemList.calendarDay
            mListener?.onItemClick(text,date,day)
        }

    }
    override fun getItemCount(): Int {
        return list.size
    }

    class CalendarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val calendarDay = itemView.findViewById<TextView>(R.id.tv_calendar_day)
        val calendarDate = itemView.findViewById<TextView>(R.id.tv_calendar_date)

    }

    fun setData() {
        list.clear()
        val currentDate = Calendar.getInstance()
        currentDate.add(Calendar.DAY_OF_MONTH, -10)

        for (i in 0 until 10 + 30) {
            val dateToAdd = currentDate.time
            list.add(CalendarModel(dateToAdd))
            currentDate.add(Calendar.DAY_OF_MONTH, 1)

        }
        notifyDataSetChanged()
    }


}
