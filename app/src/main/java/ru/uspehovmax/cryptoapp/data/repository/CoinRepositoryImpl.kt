package ru.uspehovmax.cryptoapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.delay
import ru.uspehovmax.cryptoapp.data.database.AppDatabase
import ru.uspehovmax.cryptoapp.data.mapper.CoinMapper
import ru.uspehovmax.cryptoapp.data.network.ApiFactory
import ru.uspehovmax.cryptoapp.domain.CoinInfo
import ru.uspehovmax.cryptoapp.domain.CoinRepository

class CoinRepositoryImpl(
    private val application: Application
) : CoinRepository {

    private val coinInfoDao = AppDatabase.getInstance(application).coinPriceInfoDao()
    private val apiService = ApiFactory.apiService
    private val mapper = CoinMapper()

    override fun getCoinInfoList(): LiveData<List<CoinInfo>> {
        return Transformations.map(coinInfoDao.getPriceList()) {
            it.map {
                mapper.mapDbModelToEntity(it)
            }
        }
    }

    override fun getCoinInfo(fromSymbol: String): LiveData<CoinInfo> {
        return Transformations.map(coinInfoDao.getPriceInfoAboutCoin(fromSymbol)) {
            mapper.mapDbModelToEntity(it)
        }
    }

    // загрузка данных
    override suspend fun loadData() {
        // бесконечный цикл загрузки
        while (true) {
            try {// загрузка 50 топ-валют
                val topCoins = apiService.getTopCoinsInfo(limit = 50)
                // все валюты в 1 строку
                val fSyms = mapper.mapNamesListToString(topCoins)
                // загрузка данных из сети по валютам
                val jsonContainer = apiService.getFullPriceList(fSyms = fSyms)
                // в коллекцию объектов ДТО из контейнера jsonContainer
                val coinInfoDtoList = mapper.mapJsonContainerToListCoinInfo(jsonContainer)
                // создание коллекции для записи в БД
                val dbModelList = coinInfoDtoList.map { mapper.mapDtoToDbModel(it) }
                // загрузка в БД
                coinInfoDao.insertPriceList(dbModelList)
                // задержка 10 сек - интервал
            } catch (e: Exception) {
            }
            delay(10_000)
        }
    }
}
