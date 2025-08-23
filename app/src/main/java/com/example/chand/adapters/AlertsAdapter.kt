package com.example.chand

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chand.DataBase.AlertEntity

class AlertsAdapter(private val onCheckedChange: (AlertEntity, Boolean) -> Unit) :
    ListAdapter<AlertEntity, AlertsAdapter.AlertViewHolder>(DiffCallback) {

    class AlertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val symbolText: TextView = itemView.findViewById(android.R.id.text1)
        val limitsText: TextView = itemView.findViewById(android.R.id.text2)
        val activeCheck: CheckBox = itemView.findViewById(android.R.id.checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_multiple_choice, parent, false)
        return AlertViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val alert = getItem(position)
        holder.symbolText.text = alert.symbol
        holder.limitsText.text = "S: ${alert.upperLimit}, F: ${alert.lowerLimit}"
        holder.activeCheck.isChecked = alert.isActive
        holder.activeCheck.setOnCheckedChangeListener { _, isChecked ->
            onCheckedChange(alert, isChecked)
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<AlertEntity>() {
            override fun areItemsTheSame(oldItem: AlertEntity, newItem: AlertEntity) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: AlertEntity, newItem: AlertEntity) = oldItem == newItem
        }
    }
}