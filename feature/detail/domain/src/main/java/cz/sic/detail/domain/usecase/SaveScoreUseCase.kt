package cz.sic.detail.domain.usecase

import cz.sic.domain.model.Score
import cz.sic.domain.model.Store
import cz.sic.domain.repository.ScoreRepository

interface SaveScoreUseCase {

    suspend fun saveScore(score: Score, store: Store)
}
internal class SaveScoreUseCaseImpl(
    private val repo: ScoreRepository
): SaveScoreUseCase {

    override suspend fun saveScore(score: Score, store: Store) {
        repo.saveScore(score = score, store = store)
    }
}