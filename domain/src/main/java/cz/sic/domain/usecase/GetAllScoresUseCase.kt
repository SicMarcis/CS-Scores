package cz.sic.domain.usecase

import cz.sic.domain.model.ScoreWithStore
import cz.sic.domain.model.Store
import cz.sic.domain.model.toScoreWithStore
import cz.sic.domain.repository.ScoreRepository
import cz.sic.utils.ErrorResult
import cz.sic.utils.Result
import cz.sic.utils.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

interface GetAllScoresUseCase {
    fun observeScoresByStore(store: Store): Flow<Result<List<ScoreWithStore>>>
}
internal class GetAllScoresUseCaseImpl(
    private val repo: ScoreRepository
): GetAllScoresUseCase {

    override fun observeScoresByStore(store: Store): Flow<Result<List<ScoreWithStore>>> =
        when (store) {
            Store.Local -> repo.observeScoresByStore(store)
                .map { it.map { it.map { it.toScoreWithStore(store) } } }
            Store.Remote -> repo.observeScoresByStore(store)
                .map { it.map { it.map { it.toScoreWithStore(store) } } }
            Store.Any -> {
                combine(
                    repo.observeScoresByStore(Store.Local),
                    repo.observeScoresByStore(Store.Remote)
                ) { local, remote ->
                    val localData = local.map { it.map { it.toScoreWithStore(Store.Local) } }
                    val remoteData = remote.map { it.map { it.toScoreWithStore(Store.Remote) } }

                    if (localData is Result.Success && remoteData is Result.Success) {
                        Result.Success(localData.data + remoteData.data)
                    } else {
                        Result.Error(
                            error = ErrorResult.General(IllegalStateException("One os source not loaded"))
                        )
                    }
                }
            }
        }
}