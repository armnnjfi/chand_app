package com.example.chand

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.chand.databinding.FragmentSettingsBinding
import androidx.appcompat.app.AppCompatDelegate

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPrefThemeMode= requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
        val isDark = sharedPrefThemeMode.getBoolean("dark_mode", false)


        // وضعیت سوییچ رو همون لحظه تنظیم کن
        binding.themeSwitch.isChecked = isDark

        // لیسنر برای تغییر تم
        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefThemeMode.edit().putBoolean("dark_mode", isChecked).apply()

            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        val sharedPrefAlerts = requireContext().getSharedPreferences("settingsAlert", Context.MODE_PRIVATE)
        val isAlertsOn = sharedPrefAlerts.getBoolean("alerts_enabled", true)

        // وضعیت ذخیره‌شده رو اعمال کن
        binding.switchAlert.isChecked = isAlertsOn

        binding.switchAlert.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefAlerts.edit().putBoolean("alerts_enabled", isChecked).apply()
        }
    }
}
