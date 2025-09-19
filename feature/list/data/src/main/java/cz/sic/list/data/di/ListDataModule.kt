package cz.sic.list.data.di

import android.app.Application
import androidx.room.Room
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import cz.sic.list.data.repository.ScoreRepositoryImpl
import cz.sic.list.data.source.db.RoomSource
import cz.sic.list.data.source.db.ScoreDao
import cz.sic.list.data.source.db.ScoreDatabase
import cz.sic.list.data.source.firebase.FirebaseSource
import cz.sic.list.domain.ScoreRepository
import cz.sic.list.domain.di.listDomainModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val listDataModule = module {
    includes(listDomainModule)

    // Sources
    single { Firebase.firestore}
    singleOf(::RoomSource)
    singleOf(::FirebaseSource)

    // Database
    single {
        Room.databaseBuilder(
            get<Application>(),
            ScoreDatabase::class.java,
            "score.db"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }
    single<ScoreDao> { get<ScoreDatabase>().scoreDao() }

    // Repository
    factoryOf(::ScoreRepositoryImpl) bind ScoreRepository::class


}