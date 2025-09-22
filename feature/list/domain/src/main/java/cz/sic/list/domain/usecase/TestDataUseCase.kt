package cz.sic.list.domain.usecase

import cz.sic.domain.model.Score
import cz.sic.domain.model.Store
import cz.sic.domain.repository.ScoreRepository

interface TestDataUseCase {

    suspend operator fun invoke(score: Score, store: Store)
}
class TestDataUseCaseImpl(
    private val repo: ScoreRepository
): TestDataUseCase {

    override suspend operator fun invoke(score: Score, store: Store) {
        repo.saveScore(score = score, store = store)
    }
}