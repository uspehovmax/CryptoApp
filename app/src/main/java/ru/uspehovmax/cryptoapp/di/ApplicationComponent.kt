package ru.uspehovmax.cryptoapp.di

import android.app.Application
import ru.uspehovmax.cryptoapp.presentation.CoinApp
import ru.uspehovmax.cryptoapp.presentation.CoinDetailFragment
import dagger.BindsInstance
import dagger.Component
import ru.uspehovmax.cryptoapp.presentation.CoinPriceListActivity

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class,
        WorkerModule::class
    ]
)
interface ApplicationComponent {

    fun inject(activity: CoinPriceListActivity)

    fun inject(fragment: CoinDetailFragment)

    fun inject(application: CoinApp)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}
