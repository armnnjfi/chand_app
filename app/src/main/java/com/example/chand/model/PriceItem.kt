package com.example.chand.model

sealed class PriceItem {
    abstract val symbol: String?
    abstract val name: String?
    abstract val nameEn: String?
    abstract val price: String?
    abstract val changePercent: Double?
    abstract val unit: String?
    abstract val date: String?
    abstract val time: String?

    data class GoldItem(val gold: Response_Currency_Price.Gold) : PriceItem() {
        override val symbol: String? get() = gold.symbol
        override val name: String? get() = gold.name
        override val nameEn: String? get() = gold.nameEn
        override val price: String? get() = gold.price?.toString()
        override val changePercent: Double? get() = gold.changePercent
        override val unit: String? get() = gold.unit
        override val date: String? get() = gold.date
        override val time: String? get() = gold.time
    }

    data class CurrencyItem(val currency: Response_Currency_Price.Currency) : PriceItem() {
        override val symbol: String? get() = currency.symbol
        override val name: String? get() = currency.name
        override val nameEn: String? get() = currency.nameEn
        override val price: String? get() = currency.price?.toString()
        override val changePercent: Double? get() = currency.changePercent
        override val unit: String? get() = currency.unit
        override val date: String? get() = currency.date
        override val time: String? get() = currency.time
    }

    data class CryptocurrencyItem(val cryptocurrency: Response_Currency_Price.Cryptocurrency) : PriceItem() {
        override val symbol: String? get() = cryptocurrency.symbol
        override val name: String? get() = cryptocurrency.name
        override val nameEn: String? get() = cryptocurrency.nameEn
        override val price: String? get() = cryptocurrency.price
        override val changePercent: Double? get() = cryptocurrency.changePercent
        override val unit: String? get() = cryptocurrency.unit
        override val date: String? get() = cryptocurrency.date
        override val time: String? get() = cryptocurrency.time
    }
}
