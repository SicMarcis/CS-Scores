package cz.sic.domain.usecase

import cz.sic.domain.model.ScoreWithStore
import cz.sic.domain.model.Store
import cz.sic.domain.model.toScoreWithStore
import cz.sic.domain.repository.ScoreRepository
import cz.sic.utils.map
import cz.sic.utils.Result

interface GetScoreItemUseCase {
    suspend operator fun invoke(id: Long, store: Store): Result<ScoreWithStore>
}

internal class GetScoreItemUseCaseImpl(
    private val repo: ScoreRepository
): GetScoreItemUseCase {

    override suspend fun invoke(id: Long, store: Store): Result<ScoreWithStore> {
        return repo.getScore(id, store).map { it.toScoreWithStore(store) }
    }
}