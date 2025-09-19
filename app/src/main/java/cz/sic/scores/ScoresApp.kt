package cz.sic.scores

import android.app.Application
import cz.sic.list.data.di.listDataModule
import cz.sic.list.domain.di.listDomainModule
import cz.sic.list.presentation.di.listPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ScoresApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ScoresApp)
            modules(
                listPresentationModule,
                listDomainModule,
                listDataModule,
                listPresentationModule
            )
        }
    }
}