package com.example.fithub

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(val context: Context, val list: List<UserHistoryData>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var age: TextView
        var height: TextView
        var weight: TextView
        var bmi: TextView
        var category: TextView
        var type: TextView

        init {
            age = itemView.findViewById(R.id.age_value)
            height = itemView.findViewById(R.id.height_value)
            weight = itemView.findViewById(R.id.weight_value)
            bmi = itemView.findViewById(R.id.bmi_value)
            category = itemView.findViewById(R.id.category_value)
            type = itemView.findViewById(R.id.type_value)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.row_items, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.age.text = " ${list[position].age}"
        holder.height.text = " ${list[position].height} cm"
        holder.weight.text = " ${list[position].weight} Kg"
        holder.bmi.text = " ${list[position].bmi.toString()}"
        holder.category.text = " ${list[position].category}"
        if (list[position].type.equals("bmi")) {
            holder.type.text = context.resources.getString(R.string.bmi_title)
        } else {
            holder.type.text = context.resources.getString(R.string.body_fat_title)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}