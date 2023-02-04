package ru.uspehovmax.cryptoapp.domain

import javax.inject.Inject

class LoadDataUseCase @Inject constructor(
    private val repository: CoinRepository
) {

    //загрузка из сети - suspend
    operator fun invoke() = repository.loadData()
}
