package cz.sic.list.presentation.vm

import androidx.lifecycle.viewModelScope
import cz.sic.domain.model.Store
import cz.sic.domain.usecase.DeleteScoreUseCase
import cz.sic.domain.usecase.GetAllScoresUseCase
import cz.sic.utils.BaseViewModel
import cz.sic.utils.UiStateAware
import cz.sic.utils.fold
import cz.sic.utils.getOrNull
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScoresListViewModel(
    val getScoresUseCase: GetAllScoresUseCase,
    val deleteScoreUseCase: DeleteScoreUseCase,
): BaseViewModel<
        ScoresListContract.UiAction,
        ScoresListContract.UiEvent,
        ScoresListContract.UiData
        >(
            initialState = UiStateAware.UiState(
                isLoading = true,
                uiData = ScoresListContract.UiData()
            )
        ) {

    private var observingJob: Job? = null

    override suspend fun handleUiAction(action: ScoresListContract.UiAction) {
        when (action) {
            is ScoresListContract.UiAction.OnAppear -> onAppeared()
            is ScoresListContract.UiAction.OnScoreClick -> onScoreClick(action.id, action.store)
            is ScoresListContract.UiAction.OnStoreSelect -> onStoreSelect(action.store)
            ScoresListContract.UiAction.OnAddScoreClick -> onAddStoreClick()
            is ScoresListContract.UiAction.OnDeleteClick -> deleteScore(action.id, action.store)
        }
    }

    private fun onAppeared() {
        observeScores(_uiState.value.uiData.selectedStore)
    }

    private fun onScoreClick(id: Long, store: Store) {
        updateUiEvents(
            uiEvents = { it + ScoresListContract.UiEvent.ShowDetail(id, store) }
        )
    }

    private fun onStoreSelect(store: Store) {
        updateUi(
            isLoading = false,
            uiData = {
                it.copy(selectedStore = store)
            }
        )
        observeScores(store)
    }

    private fun onAddStoreClick() {
        updateUiEvents(
            uiEvents = { it + ScoresListContract.UiEvent.ShowAddScreen }
        )
    }

    private fun deleteScore(id: Long, store: Store) {
        viewModelScope.launch {
            runCatching { deleteScoreUseCase(id, store) }
                .onFailure { t ->
                    updateUiEvents(
                        uiEvents = { it + ScoresListContract.UiEvent.ShowError("Error: ${t.message}") }
                    )
                }
        }
    }

    private fun observeScores(store: Store = Store.Any) {
        _uiState.update { it.copy(isLoading = true) }
        observingJob?.cancel()
        observingJob = getScoresUseCase.observeScoresByStore(store)
            .onEach { data ->
                data.fold(
                    success = {
                        updateUi(
                            isLoading = false,
                            uiData = { it.copy(scores = data.getOrNull().orEmpty()) }
                        )
                    },
                    error = { error ->
                        updateUiEvents(
                            isLoading = false,
                            uiEvents = { it + ScoresListContract.UiEvent.ShowError("Error loading data") }
                        )
                    }
                )
            }
            .catch { t ->
                updateUiEvents(
                    isLoading = false,
                    uiEvents = { it + ScoresListContract.UiEvent.ShowError("Error loading data: ${t.message}") }
                )
            }
            .launchIn(viewModelScope)
    }
}