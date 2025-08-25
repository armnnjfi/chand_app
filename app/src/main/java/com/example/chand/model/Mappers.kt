package com.example.chand.DataBase

import com.example.chand.DataBase.watchlist.WatchlistItemEntity
import com.example.chand.model.*
import com.example.chand.model.Response_Currency_Price.Cryptocurrency
import com.example.chand.model.Response_Currency_Price.Currency
import com.example.chand.model.Response_Currency_Price.Gold

fun PriceItem.toEntity(): WatchlistItemEntity {
    return when (this) {
        is PriceItem.CurrencyItem -> WatchlistItemEntity(
            symbol = currency.symbol ?: "",
            name = currency.name,
            nameEn = currency.nameEn,
            price = currency.price.toString(),
            changePercent = currency.changePercent,
            unit = currency.unit,
            date = currency.date,
            time = currency.time,
            type = "currency"
        )

        is PriceItem.GoldItem -> WatchlistItemEntity(
            symbol = gold.symbol ?: "",
            name = gold.name,
            nameEn = gold.name,
            price = gold.price.toString(),
            changePercent = gold.changePercent,
            unit = gold.unit,
            date = gold.date,
            time = gold.time,
            type = "gold"
        )

        is PriceItem.CryptocurrencyItem -> WatchlistItemEntity(
            symbol = cryptocurrency.symbol ?: "",
            name = cryptocurrency.nameEn,
            nameEn = cryptocurrency.nameEn,
            price = cryptocurrency.price,
            changePercent = cryptocurrency.changePercent,
            unit = cryptocurrency.unit,
            date = cryptocurrency.date,
            time = cryptocurrency.time,
            type = "crypto"
        )

        PriceItem.TomanItem -> TODO()
    }
}

fun PriceItem.toConverterEntity(): ConverterPriceEntity {
    return when (this) {
        is PriceItem.CurrencyItem -> ConverterPriceEntity(
            symbol = currency.symbol ?: "",
            name = currency.name,
            nameEn = currency.nameEn,
            price = currency.price?.toString(),
            changePercent = currency.changePercent,
            unit = currency.unit,
            date = currency.date,
            time = currency.time,
            type = "currency"
        )

        is PriceItem.GoldItem -> ConverterPriceEntity(
            symbol = gold.symbol ?: "",
            name = gold.name,
            nameEn = gold.nameEn,
            price = gold.price?.toString(),
            changePercent = gold.changePercent,
            unit = gold.unit,
            date = gold.date,
            time = gold.time,
            type = "gold"
        )

        is PriceItem.CryptocurrencyItem -> ConverterPriceEntity(
            symbol = cryptocurrency.symbol ?: "",
            name = cryptocurrency.name,
            nameEn = cryptocurrency.nameEn,
            price = cryptocurrency.price,
            changePercent = cryptocurrency.changePercent,
            unit = cryptocurrency.unit,
            date = cryptocurrency.date,
            time = cryptocurrency.time,
            type = "crypto"
        )

        is PriceItem.TomanItem -> ConverterPriceEntity(
            symbol = "IRT",
            name = "تومان",
            nameEn = "Toman",
            price = "1",
            changePercent = 0.0,
            unit = "IRT",
            date = null,
            time = null,
            type = "toman"
        )
    }
}

fun ConverterPriceEntity.toPriceItem(): PriceItem {
    return when (type) {
        "currency" -> PriceItem.CurrencyItem(
            Response_Currency_Price.Currency(
                symbol = symbol,
                name = name,
                nameEn = nameEn,
                price = price,
                changePercent = changePercent,
                unit = unit,
                date = date,
                time = time,
                timeUnix = null,
                changeValue = null
            )
        )

        "gold" -> PriceItem.GoldItem(
            Response_Currency_Price.Gold(
                symbol = symbol,
                name = name,
                nameEn = nameEn,
                price = price,
                changePercent = changePercent,
                unit = unit,
                date = date,
                time = time,
                timeUnix = null,
                changeValue = null
            )
        )

        "crypto" -> PriceItem.CryptocurrencyItem(
            Response_Currency_Price.Cryptocurrency(
                symbol = symbol,
                name = name,
                nameEn = nameEn,
                price = price,
                changePercent = changePercent,
                unit = unit,
                date = date,
                time = time,
                timeUnix = null,
                description = null,
                marketCap = null
            )
        )

        "toman" -> PriceItem.TomanItem

        else -> throw IllegalArgumentException("Unknown type: $type")
    }
}

fun WatchlistItemEntity.toPriceItem(): PriceItem {
    return when (type) {
        "currency" -> PriceItem.CurrencyItem(
            Currency(
                symbol = symbol,
                name = name,
                nameEn = nameEn,
                price = price,
                changePercent = changePercent,
                unit = unit,
                date = date,
                time = time,
                timeUnix = null,
                changeValue = null
            )
        )

        "gold" -> PriceItem.GoldItem(
            Gold(
                symbol = symbol,
                name = name,
                nameEn = nameEn,
                price = price,
                changePercent = changePercent,
                unit = unit,
                date = date,
                time = time,
                timeUnix = null,
                changeValue = null
            )
        )

        "crypto" -> PriceItem.CryptocurrencyItem(
            Cryptocurrency(
                symbol = symbol,
                name = name,
                nameEn = nameEn,
                price = price,
                changePercent = changePercent,
                unit = unit,
                date = date,
                time = time,
                timeUnix = null,
                description = null,
                marketCap = null
            )
        )

        else -> throw IllegalArgumentException("Unknown type: $type")
    }
}

fun AlertEntity.toPriceItem(): PriceItem {
    return PriceItem.CurrencyItem(
        Currency(
            symbol = this.symbol,
            name = null,
            nameEn = null,
            price = null,
            changePercent = null,
            unit = null,
            date = null,
            time = null,
            timeUnix = null,
            changeValue = null
        )
    )
}
