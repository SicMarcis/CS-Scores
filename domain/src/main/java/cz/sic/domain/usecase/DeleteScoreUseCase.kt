package cz.sic.domain.usecase

import cz.sic.domain.model.Store
import cz.sic.domain.repository.ScoreRepository

interface DeleteScoreUseCase {
    suspend operator fun invoke(id: Long, store: Store)
}

internal class DeleteScoreUseCaseImpl(
    private val repo: ScoreRepository
): DeleteScoreUseCase {
    override suspend fun invoke(id: Long, store: Store) {
        repo.deleteScore(id, store)
    }
}