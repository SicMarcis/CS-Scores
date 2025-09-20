package cz.sic.domain.usecase

import cz.sic.domain.model.ScoreWithStore
import cz.sic.domain.model.Store
import cz.sic.domain.model.toScoreWithStore
import cz.sic.domain.repository.ScoreRepository

interface GetScoreItemUseCase {
    suspend operator fun invoke(id: Long, store: Store): ScoreWithStore?
}

class GetScoreItemUseCaseImpl(
    val repo: ScoreRepository
): GetScoreItemUseCase {

    override suspend fun invoke(id: Long, store: Store): ScoreWithStore? {
        return repo.getScore(id, store)
            ?.toScoreWithStore(store)
    }
}