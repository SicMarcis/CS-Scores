package cz.sic.data.repository

import cz.sic.data.source.db.RoomSource
import cz.sic.data.source.firebase.FirebaseSource
import cz.sic.domain.model.Score
import cz.sic.domain.model.Store
import cz.sic.domain.repository.ScoreRepository
import kotlinx.coroutines.flow.Flow

class ScoreRepositoryImpl(
    private val localStore: RoomSource,
    private val remoteStore: FirebaseSource
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
            Store.Local -> localStore.observeScores()
            Store.Remote -> remoteStore.observeScores()
            Store.Any -> throw RuntimeException("Invalid State")
        }
    }

    override fun observeScores(): Flow<List<Score>> =
        localStore.observeScores()

    override suspend fun saveScore(
        score: Score,
        store: Store
    ) {
        when (store) {
            Store.Local -> localStore.saveScore(score)
            Store.Remote -> remoteStore.saveScore(score)
            Store.Any -> {
                localStore.saveScore(score)
                remoteStore.saveScore(score)
            }
        }


    }

    override suspend fun deleteScore(id: Int) {
        localStore.deleteScore(id)
    }

    override suspend fun deleAllLocalScores() {
        localStore.deleteAllScores()
    }
}