package ru.uspehovmax.cryptoapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import ru.uspehovmax.cryptoapp.data.database.CoinInfoDao
import ru.uspehovmax.cryptoapp.data.mapper.CoinMapper
import ru.uspehovmax.cryptoapp.data.workers.RefreshDataWorker
import ru.uspehovmax.cryptoapp.domain.CoinInfo
import ru.uspehovmax.cryptoapp.domain.CoinRepository
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val application: Application,
    private val coinInfoDao: CoinInfoDao,
    private val mapper: CoinMapper
) : CoinRepository {


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

    override fun loadData() {
        val workManager = WorkManager.getInstance(application)
        workManager.enqueueUniqueWork(
            RefreshDataWorker.NAME,
            ExistingWorkPolicy.REPLACE,
            RefreshDataWorker.makeRequest()
        )
    }
}

/*
private val apiService = ApiFactory.apiService


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
}*/
