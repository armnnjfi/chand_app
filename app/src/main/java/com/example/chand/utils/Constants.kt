package com.example.retrofit_exersice.utils

object Constants {
    const val NETWORK_TIMEOUT = 30L
    const val BASE_URL = "https://brsapi.ir/Api/"
    const val TABLE_NAME = "watchlist"
    const val TABLE_CONVERTER = "converter_prices"
    const val TABLE_ALERT = "alerts"
    const val DB_NAME = "Chand_db"
    const val API_KEY = "FreejCpsMTnCQC5VM5mod6U35aNqCq5c"
    val CURRENCY_To_COUNTRY_FLAG = mapOf(
    "USD" to "us",   // United States
    "AED" to "ae",   // United Arab Emirates
    "TRY" to "tr",   // Turkey
    "EUR" to "eu",   // European Union
    "GBP" to "gb",   // United Kingdom
    "JPY" to "jp",
    "CAD" to "ca",
    "AUD" to "au",
    "CHF" to "ch",
    "KWD" to "kw",   // دینار کویت – Kuwait
    "SAR" to "sa",   // ریال سعودی – Saudi Arabia
    "INR" to "in",   // روپیه هند – India
    "PKR" to "pk",   // روپیه پاکستان – Pakistan
    "IQD" to "iq",   // دینار عراق – Iraq
    "SYP" to "sy",   // لیره سوریه – Syria
    "SEK" to "se",   // کرون سوئد – Sweden
    "QAP" to "qa",   // ریال قطر (کد صحیح ISO: QAR) – Qatar
    "OMR" to "om",   // ریال عمان – Oman
    "BHD" to "bh",   // دینار بحرین – Bahrain
    "AFN" to "af",   // افغانی افغانستان – Afghanistan
    "MYR" to "my",   // رینگیت مالزی – Malaysia
    "THB" to "th",   // بات تایلند – Thailand
    "RUB" to "ru",   // روبل روسیه – Russia
    "AZN" to "az",
    "AMD" to "am",   // درام ارمنستان – Armenia
    "GEL" to "ge",
    "CNY" to "cn")
}