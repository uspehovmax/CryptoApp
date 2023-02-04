package ru.uspehovmax.cryptoapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.uspehovmax.cryptoapp.data.repository.CoinRepositoryImpl
import ru.uspehovmax.cryptoapp.domain.GetCoinInfoListUseCase
import ru.uspehovmax.cryptoapp.domain.GetCoinInfoUseCase
import ru.uspehovmax.cryptoapp.domain.LoadDataUseCase

class CoinViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CoinRepositoryImpl(application)

    private val getCoinInfoListUseCase = GetCoinInfoListUseCase(repository)
    private val getCoinInfoUseCase = GetCoinInfoUseCase(repository)
    private val loadDataUseCase = LoadDataUseCase(repository)

    val coinInfoList = getCoinInfoListUseCase()

    fun getDetailInfo(fSym: String) = getCoinInfoUseCase(fSym)

    init {
        viewModelScope.launch {
            loadDataUseCase()
        }
    }
}