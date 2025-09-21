package cz.sic.detail.domain.usecase

import cz.sic.domain.model.Score
import cz.sic.domain.model.Store
import cz.sic.domain.repository.ScoreRepository

interface ChangeScoresUseCase {

    suspend fun saveScore(score: Score, store: Store)

    suspend fun deleteAllScores()
}
internal class ChangeScoresUseCaseImpl(
    private val repo: ScoreRepository
): ChangeScoresUseCase {
    override suspend fun saveScore(score: Score, store: Store) {
        repo.saveScore(score = score, store = store)
    }

    override suspend fun deleteAllScores() {
        repo.deleAllLocalScores()
    }

}