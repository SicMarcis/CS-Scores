package cz.sic.list.domain.usecase

import cz.sic.domain.model.Score
import cz.sic.domain.model.Store
import cz.sic.domain.repository.ScoreRepository

class TestDataUseCase(
    private val repo: ScoreRepository
) {
    suspend fun saveScore(score: Score, store: Store) {
        repo.saveScore(score = score, store = store)
    }

    suspend fun deleteAllScores() {
        repo.deleAllLocalScores()
    }

}