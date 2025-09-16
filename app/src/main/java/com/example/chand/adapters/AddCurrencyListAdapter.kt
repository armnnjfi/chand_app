package com.example.chand.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.chand.databinding.ItemAddCurrencyRecyclerBinding
import com.example.chand.model.PriceItem
import com.example.retrofit_exersice.utils.Constants

class AddCurrencyListAdapter(private val onItemClick: (PriceItem) -> Unit) :
    RecyclerView.Adapter<AddCurrencyListAdapter.ViewHolder>(
    ) {
    private lateinit var binding: ItemAddCurrencyRecyclerBinding
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemAddCurrencyRecyclerBinding.inflate(inflater, parent, false)
        return ViewHolder()
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PriceItem) {
            binding.apply {
            val code = Constants.CURRENCY_To_COUNTRY_FLAG["${item.symbol}"] ?: "un"
                val flagUrl = "https://flagcdn.com/h240/$code.png"
                root.setOnClickListener { onItemClick(item) }
                textItemAddList.text = item.name ?: "Known"
                subTextItemAddList.text = "${item.price} | ${item.changePercent} %"
                imgItemAddCurrencyList.load(flagUrl){
                    transformations(CircleCropTransformation())   // گرد کردن
                    crossfade(true)                                // افکت fade
                }


            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<PriceItem>() {
        override fun areItemsTheSame(
            oldItem: PriceItem,
            newItem: PriceItem
        ): Boolean {
            return newItem.symbol == oldItem.symbol
        }

        override fun areContentsTheSame(
            oldItem: PriceItem,
            newItem: PriceItem
        ): Boolean {
            return newItem == oldItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)
}