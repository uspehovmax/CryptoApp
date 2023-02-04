package ru.uspehovmax.cryptoapp.domain

class LoadDataUseCase(
    private val repository: CoinRepository
) {

    //загрузка из сети - suspend
    suspend operator fun invoke() = repository.loadData()
}
