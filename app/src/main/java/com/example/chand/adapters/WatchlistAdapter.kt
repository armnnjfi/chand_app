package com.example.chand.adapters

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.chand.DataBase.watchlist.WatchlistItemEntity
import com.example.chand.databinding.ItemWatchlistBinding
import com.example.retrofit_exersice.utils.Constants
import com.example.chand.R

class WatchlistAdapter(
    private val onDeleteClick: (WatchlistItemEntity) -> Unit
) : RecyclerView.Adapter<WatchlistAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemWatchlistBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<WatchlistItemEntity>() {
        override fun areItemsTheSame(
            oldItem: WatchlistItemEntity,
            newItem: WatchlistItemEntity
        ) = oldItem.symbol == newItem.symbol

        override fun areContentsTheSame(
            oldItem: WatchlistItemEntity,
            newItem: WatchlistItemEntity
        ) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWatchlistBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]

        holder.binding.apply {
            // بارگذاری پرچم گرد
            val code = Constants.CURRENCY_To_COUNTRY_FLAG[item.symbol] ?: "un"
            val flagUrl = "https://flagcdn.com/h240/$code.png"
            imgItemWatchList.load(flagUrl) {
                transformations(CircleCropTransformation())
                crossfade(true)
            }

            symbolTextView.text = item.symbol
            nameTextView.text = item.name ?: item.nameEn
            priceTextView.text = item.price
            unitTextView.text = item.unit

            item.changePercent?.let { percent ->
                changePercentTextView.text = "${percent}%"

                val arrowDrawable = when {
                    percent > 0 -> ContextCompat.getDrawable(
                        holder.itemView.context,
                        R.drawable.up_arrow_change_price
                    )
                    percent < 0 -> ContextCompat.getDrawable(
                        holder.itemView.context,
                        R.drawable.down_arrow_change_price
                    )
                    else -> null
                }

                val colorRes = when {
                    percent > 0 -> R.color.green
                    percent < 0 -> R.color.red
                    else -> R.color.gray
                }
                changePercentTextView.setTextColor(
                    ContextCompat.getColor(holder.itemView.context, colorRes)
                )
                changePercentTextView.setCompoundDrawablesWithIntrinsicBounds(
                    null, null, arrowDrawable, null
                )
                changePercentTextView.compoundDrawablePadding = 6  // فاصله متن و آیکون
            } ?: run {
                changePercentTextView.text = "N/A"
                changePercentTextView.setCompoundDrawablesWithIntrinsicBounds(
                    null, null, null, null
                )
            }

            // ✅ اینجا حذف دکمه قبلی انجام شده و فقط Long-Click داریم
            root.setOnLongClickListener {
                AlertDialog.Builder(holder.itemView.context)
                    .setTitle("حذف آیتم")
                    .setMessage("آیا می‌خواهید ${item.symbol} را حذف کنید؟")
                    .setPositiveButton("بله") { dialog, _ ->
                        onDeleteClick(item)    // فراخوانی حذف
                        dialog.dismiss()
                    }
                    .setNegativeButton("خیر") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
                true // نشان می‌دهد LongClick هندل شد
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}
