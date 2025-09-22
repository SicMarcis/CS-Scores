package cz.sic.domain.repository

import cz.sic.domain.model.Score
import cz.sic.domain.model.Store
import cz.sic.utils.Result
import kotlinx.coroutines.flow.Flow

interface ScoreRepository {

    fun observeScoresByStore(store: Store) : Flow<Result<List<Score>>>

    suspend fun saveScore(score: Score, store: Store)

    suspend fun deleteScore(id: Long, store: Store)

    suspend fun deleteAllScores()

    suspend fun getScore(id: Long, store: Store): Result<Score>
}