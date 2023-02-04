package ru.uspehovmax.cryptoapp.data.mapper

import com.google.gson.Gson
import ru.uspehovmax.cryptoapp.data.database.CoinInfoDbModel
import ru.uspehovmax.cryptoapp.data.network.model.CoinInfoDto
import ru.uspehovmax.cryptoapp.data.network.model.CoinInfoJsonContainerDto
import ru.uspehovmax.cryptoapp.data.network.model.CoinNamesListDto
import ru.uspehovmax.cryptoapp.domain.CoinInfo

class CoinMapper {
    // проеобразование объекты из ДТО в объекты БД
    fun mapDtoToDbModel(dto: CoinInfoDto) = CoinInfoDbModel(
        fromSymbol = dto.fromSymbol,
        toSymbol = dto.toSymbol,
        price = dto.price,
        lastUpdate = dto.lastUpdate,
        highDay = dto.highDay,
        lowDay = dto.lowDay,
        lastMarket = dto.lastMarket,
        imageUrl = dto.imageUrl
    )

    // проеобразование Json-контейнер в список объектов CoinInfoDto
    fun mapJsonContainerToListCoinInfo(jsonContainer: CoinInfoJsonContainerDto): List<CoinInfoDto> {
        val result = mutableListOf<CoinInfoDto>()
        val jsonObject = jsonContainer.json ?: return result
        val coinKeySet = jsonObject.keySet()
        for (coinKey in coinKeySet) {
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = currencyJson.keySet()
            for (currencyKey in currencyKeySet) {
                val priceInfo = Gson().fromJson(
                    currencyJson.getAsJsonObject(currencyKey),
                    CoinInfoDto::class.java
                )
                result.add(priceInfo)
            }
        }
        return result
    }

    // Список валют в строоку через запятую
    fun mapNamesListToString(namesListDto: CoinNamesListDto): String {
        return namesListDto.names?.map {
            it.coinName?.name
        }?.joinToString(",") ?: ""
    }

    // из Модель БД в сущности Домайн слоя
    fun mapDbModelToEntity(dbModel: CoinInfoDbModel) = CoinInfo(
        fromSymbol = dbModel.fromSymbol,
        toSymbol = dbModel.toSymbol,
        price = dbModel.price,
        lastUpdate = dbModel.lastUpdate,
        highDay = dbModel.highDay,
        lowDay = dbModel.lowDay,
        lastMarket = dbModel.lastMarket,
        imageUrl = dbModel.imageUrl
    )
}