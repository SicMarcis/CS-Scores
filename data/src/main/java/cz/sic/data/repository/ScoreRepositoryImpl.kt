package cz.sic.data.repository

import cz.sic.data.source.LocalSource
import cz.sic.data.source.RemoteSource
import cz.sic.domain.model.Score
import cz.sic.domain.model.Store
import cz.sic.domain.repository.ScoreRepository
import cz.sic.utils.ErrorResult
import cz.sic.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class ScoreRepositoryImpl(
    private val localStore: LocalSource<Score>,
    private val remoteStore: RemoteSource<Score>
): ScoreRepository {

    override fun observeScoresByStore(store: Store): Flow<Result<List<Score>>> {
        return when (store) {
            Store.Local -> localStore.observe().map { Result.Success(it) }
            Store.Remote -> remoteStore.observe().map { Result.Success(it) }
            Store.Any -> flow {
                Result.Error(
                    error = ErrorResult.General(RuntimeException("Invalid State")),
                    data = emptyList<Score>()
                )
            }
        }
    }

    override suspend fun saveScore(score: Score, store: Store) {
        when (store) {
            Store.Local -> localStore.save(score)
            Store.Remote -> remoteStore.save(score)
            Store.Any -> throw IllegalStateException("Illegal store '$store' provided.")
        }
    }

    override suspend fun getScore(id: Long, store: Store): Result<Score> {
        return when (store) {
            Store.Local -> localStore.getItem(id)
            Store.Remote -> remoteStore.getItem(id)
            Store.Any -> {
                throw RuntimeException("Illegal store '$store' provided.")
            }
        }.let { data ->
            data?.let {
                Result.Success(it)
            } ?: Result.Error(
                error = ErrorResult.General(
                    RuntimeException("Score with id '$id' not found in store '$store'.")
                )
            )
        }
    }

    override suspend fun deleteScore(id: Long, store: Store) {
        when (store) {
            Store.Local -> localStore.delete(id)
            Store.Remote -> remoteStore.delete(id)
            Store.Any -> throw RuntimeException("Illegal store '$store' provided.")
        }
    }

    override suspend fun deleteAllScores() {
        localStore.deleteAll()
        remoteStore.deleteAll()
    }
}