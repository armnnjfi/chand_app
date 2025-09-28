package com.example.chand.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import coil.load
import coil.transform.CircleCropTransformation
import com.example.chand.databinding.ItemSpinnerConverterBinding
import com.example.chand.model.PriceItem
import com.example.retrofit_exersice.utils.Constants

class CurrencySpinnerAdapter(
    private val items: List<PriceItem>,
    private val context: Context
) : ArrayAdapter<PriceItem>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        return createItemView(position, convertView, parent, isSelectedView = true)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent, isSelectedView = false)
    }

    private fun createItemView(
        position: Int,
        convertView: View?,
        parent: ViewGroup,
        isSelectedView: Boolean
    ): View {
        val binding = if (convertView == null) {
            ItemSpinnerConverterBinding.inflate(LayoutInflater.from(context), parent, false)
        } else {
            ItemSpinnerConverterBinding.bind(convertView)
        }

        val item = items[position]
        binding.tvCurrencyName.text = item.name ?: item.nameEn ?: item.symbol ?: "Unknown"

        val code = Constants.CURRENCY_To_COUNTRY_FLAG[item.symbol] ?: "un"
        val flagUrl = "https://flagcdn.com/h40/$code.png"
        binding.imgFlag.load(flagUrl) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }

        // 🎯 تغییر اندازه‌ها بر اساس حالت انتخاب‌شده یا لیست
        if (isSelectedView) {
            // وقتی Spinner بسته است و آیتم انتخاب‌شده نمایش داده می‌شود
            binding.tvCurrencyName.textSize = 18f      // متن بزرگ‌تر
            binding.imgFlag.layoutParams.width  = dpToPx(40)
            binding.imgFlag.layoutParams.height = dpToPx(40)
        } else {
            // آیتم‌های لیست Dropdown
            binding.tvCurrencyName.textSize = 16f
            binding.imgFlag.layoutParams.width  = dpToPx(18)
            binding.imgFlag.layoutParams.height = dpToPx(18)
        }

        return binding.root
    }

    private fun dpToPx(dp: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }
}
