package cz.sic.data.di

import android.app.Application
import androidx.room.Room
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import cz.sic.data.repository.ScoreRepositoryImpl
import cz.sic.data.source.LocalSource
import cz.sic.data.source.RemoteSource
import cz.sic.data.source.db.RoomSource
import cz.sic.data.source.db.ScoreDao
import cz.sic.data.source.db.ScoreDatabase
import cz.sic.data.source.firebase.FirebaseSource
import cz.sic.domain.model.Score
import cz.sic.domain.repository.ScoreRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    // Sources
    single { Firebase.firestore}
    singleOf(::RoomSource) bind LocalSource::class
    singleOf(::FirebaseSource) bind RemoteSource::class

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