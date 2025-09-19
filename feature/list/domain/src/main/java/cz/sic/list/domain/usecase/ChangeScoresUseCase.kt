package cz.sic.list.domain.usecase

import cz.sic.list.domain.ScoreRepository
import cz.sic.list.domain.model.Score
import cz.sic.list.domain.model.Store

class ChangeScoresUseCase(
    private val repo: ScoreRepository
) {
    suspend fun saveScore(score: Score, store: Store) {
        repo.saveScore(score = score, store = store)
    }

    suspend fun deleteAllScores() {
        repo.deleAllLocalScores()
    }

}