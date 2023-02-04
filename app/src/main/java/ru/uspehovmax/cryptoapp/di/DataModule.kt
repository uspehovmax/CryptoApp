package ru.uspehovmax.cryptoapp.di

import android.app.Application
import ru.uspehovmax.cryptoapp.data.database.AppDatabase
import ru.uspehovmax.cryptoapp.data.database.CoinInfoDao
import ru.uspehovmax.cryptoapp.data.network.ApiFactory
import ru.uspehovmax.cryptoapp.data.network.ApiService
import ru.uspehovmax.cryptoapp.data.repository.CoinRepositoryImpl
import ru.uspehovmax.cryptoapp.domain.CoinRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindCoinRepository(impl: CoinRepositoryImpl): CoinRepository

    companion object {

        @Provides
        @ApplicationScope
        fun provideCoinInfoDao(
            application: Application
        ): CoinInfoDao {
            return AppDatabase.getInstance(application).coinPriceInfoDao()
        }

        @Provides
        @ApplicationScope
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }
    }
}
