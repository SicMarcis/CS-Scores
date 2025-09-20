package cz.sic.domain.repository

import cz.sic.domain.model.Score
import cz.sic.domain.model.Store
import kotlinx.coroutines.flow.Flow

interface ScoreRepository {

    suspend fun getAllScores() : List<Score>

    suspend fun getScoresByStore(store: Store) : List<Score>

    fun observeScoresByStore(store: Store) : Flow<List<Score>>

    fun observeScores() : Flow<List<Score>>

    suspend fun saveScore(score: Score, store: Store)

    suspend fun deleteScore(id: Long, store: Store)

    suspend fun deleAllLocalScores()

    suspend fun getScore(id: Long, store: Store): Score?
}