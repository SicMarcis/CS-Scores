package cz.sic.list.domain.usecase

import cz.sic.list.domain.ScoreRepository
import cz.sic.list.domain.model.Score
import cz.sic.list.domain.model.ScoreWithStore
import cz.sic.list.domain.model.Store
import cz.sic.list.domain.model.toScoreWithStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

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