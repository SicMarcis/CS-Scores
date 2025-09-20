package cz.sic.domain.usecase

import cz.sic.domain.model.ScoreWithStore
import cz.sic.domain.model.Store
import cz.sic.domain.model.toScoreWithStore
import cz.sic.domain.repository.ScoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class GetAllScoresUseCase(
    private val repo: ScoreRepository
) {

    suspend fun getScoresByStore(store: Store): List<ScoreWithStore> {
        return repo.getScoresByStore(store)
            .map {
                it.toScoreWithStore(store)
            }
    }

    fun observeScoresByStore(store: Store): Flow<List<ScoreWithStore>> =
        when (store) {
            Store.Local -> repo.observeScoresByStore(store)
                .map { it.map { it.toScoreWithStore(store) } }
            Store.Remote -> repo.observeScoresByStore(store)
                .map { it.map { it.toScoreWithStore(store) } }
            Store.Any -> {
                combine(
                    repo.observeScoresByStore(Store.Local),
                    repo.observeScoresByStore(Store.Remote)
                ) { local, remote ->
                    local.map { it.toScoreWithStore(Store.Local) } + remote.map { it.toScoreWithStore(Store.Remote) }
                }
            }
        }
}