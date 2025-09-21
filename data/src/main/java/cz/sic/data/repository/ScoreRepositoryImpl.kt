package cz.sic.data.repository

import cz.sic.data.source.LocalSource
import cz.sic.data.source.RemoteSource
import cz.sic.data.source.db.RoomSource
import cz.sic.data.source.firebase.FirebaseSource
import cz.sic.domain.model.Score
import cz.sic.domain.model.Store
import cz.sic.domain.repository.ScoreRepository
import kotlinx.coroutines.flow.Flow

class ScoreRepositoryImpl(
    private val localStore: LocalSource<Score>,
    private val remoteStore: RemoteSource<Score>
): ScoreRepository {

    override suspend fun getAllScores(): List<Score> {
        return localStore.getData()
    }

    override suspend fun getScoresByStore(store: Store): List<Score> {
        return when (store) {
            Store.Local -> localStore.getData()
            Store.Remote -> remoteStore.getData()
            Store.Any -> {
                val local = localStore.getData()
                val remote = remoteStore.getData()
                local + remote
            }
        }
    }

    override fun observeScoresByStore(store: Store): Flow<List<Score>> {
        return when (store) {
            Store.Local -> localStore.observe()
            Store.Remote -> remoteStore.observe()
            Store.Any -> throw RuntimeException("Invalid State")
        }
    }

    override suspend fun saveScore(
        score: Score,
        store: Store
    ) {
        when (store) {
            Store.Local -> localStore.save(score)
            Store.Remote -> remoteStore.save(score)
            Store.Any -> {
                localStore.save(score)
                remoteStore.save(score)
            }
        }
    }

    override suspend fun getScore(
        id: Long,
        store: Store
    ): Score? {
        return when (store) {
            Store.Local -> localStore.getItem(id)
            Store.Remote -> remoteStore.getItem(id)
            Store.Any -> {
                throw RuntimeException("Illegal store '$store' provided.")
            }
        }
    }

    override suspend fun deleteScore(id: Long, store: Store) {
        when (store) {
            Store.Local -> localStore.delete(id)
            Store.Remote -> remoteStore.delete(id)
            Store.Any -> throw RuntimeException("Illegal store '$store' provided.")
        }
    }

    override suspend fun deleAllLocalScores() {
        localStore.deleteAll()
        remoteStore.deleteAll()
    }
}