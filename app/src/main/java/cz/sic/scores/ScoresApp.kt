package cz.sic.scores

import android.app.Application
import cz.sic.data.di.dataModule
import cz.sic.detail.domain.di.detailDomainModule
import cz.sic.detail.presentation.di.detailPresentationModule
import cz.sic.domain.di.domainModule
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
                domainModule,
                dataModule,
                listPresentationModule,
                listDomainModule,
                listPresentationModule,
                detailDomainModule,
                detailPresentationModule
            )
        }
    }
}