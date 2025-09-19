package cz.sic.list.domain

import cz.sic.list.domain.model.Score
import cz.sic.list.domain.model.Store
import kotlinx.coroutines.flow.Flow

interface ScoreRepository {

    suspend fun getAllScores() : List<Score>

    suspend fun getScoresByStore(store: Store) : List<Score>

    fun observeScoresByStore(store: Store) : Flow<List<Score>>

    fun observeScores() : Flow<List<Score>>

    suspend fun saveScore(score: Score, store: Store)

    suspend fun deleteScore(id: Int)

    suspend fun deleAllLocalScores()
}