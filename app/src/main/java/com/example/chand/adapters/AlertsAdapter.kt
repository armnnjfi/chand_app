package com.example.chand

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.chand.DataBase.AlertEntity
import com.example.chand.DataBase.ChandDatabase
import com.example.chand.databinding.ItemAlertsRecyclerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlertsAdapter(
    private val scope: CoroutineScope,
    private val onDeleteClick: (AlertEntity) -> Unit
) : RecyclerView.Adapter<AlertsAdapter.ViewHolder>() {

    private lateinit var binding: ItemAlertsRecyclerBinding

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AlertEntity) {
            binding.apply {
                nameCurrency.text = item.symbol
                priceRange.text = "Ceiling: ${item.upperLimit} | Floor: ${item.lowerLimit}"
                alertSwitch.isChecked = item.isActive

                deleteButton.setOnClickListener {
                    onDeleteClick(item)
                }

                // تغییر وضعیت سوییچ و ذخیره در دیتابیس
                alertSwitch.setOnCheckedChangeListener { _, isChecked ->
                    val dao = ChandDatabase.getDatabase(binding.root.context).dao()
                    scope.launch(Dispatchers.IO) {
                        dao.updateAlertStatus(item.id, isChecked)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemAlertsRecyclerBinding.inflate(inflater, parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    private val diffCallback = object : DiffUtil.ItemCallback<AlertEntity>() {
        override fun areItemsTheSame(oldItem: AlertEntity, newItem: AlertEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: AlertEntity, newItem: AlertEntity) =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, diffCallback)
}
