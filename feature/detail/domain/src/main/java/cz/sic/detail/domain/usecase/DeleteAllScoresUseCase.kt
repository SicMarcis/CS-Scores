package cz.sic.detail.domain.usecase

import cz.sic.domain.repository.ScoreRepository

internal interface DeleteAllScoresUseCase {
    suspend operator fun invoke()
}

internal class DeleteAllScoresUseCaseImpl(
    private val repo: ScoreRepository
): DeleteAllScoresUseCase {
    override suspend fun invoke() {
        repo.deleteAllScores()
    }
}