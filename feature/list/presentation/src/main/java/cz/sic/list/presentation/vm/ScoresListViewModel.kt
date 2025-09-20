package cz.sic.list.presentation.vm

import androidx.lifecycle.viewModelScope
import cz.sic.domain.model.Score
import cz.sic.domain.model.Store
import cz.sic.domain.usecase.DeleteScoreUseCase
import cz.sic.domain.usecase.GetAllScoresUseCase
import cz.sic.list.domain.usecase.TestDataUseCase
import cz.sic.utils.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScoresListViewModel(
    val getScoresUseCase: GetAllScoresUseCase,
    val deleteScoreUseCase: DeleteScoreUseCase,
    val testScoresUseCase: TestDataUseCase
): BaseViewModel<ScoresListContract.UiAction, ScoresListContract.UiEvent>() {

    private val _uiState = MutableStateFlow (ScoresListContract.UiState())
    val uiState: StateFlow<ScoresListContract.UiState> = _uiState.asStateFlow()

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
        observeScores(_uiState.value.selectedStore)
    }

    private fun onScoreClick(id: Long, store: Store) {
        _uiState.update { it.copy(events = it.events + ScoresListContract.UiEvent.ShowDetail(id, store)) }
    }

    private fun onStoreSelect(store: Store) {
        _uiState.update { it.copy(selectedStore = store) }
        observeScores(store)
    }

    private fun onAddStoreClick() {
        _uiState.update {
            it.copy(events = it.events + ScoresListContract.UiEvent.ShowAddScreen)
        }
    }

    private fun deleteScore(id: Long, store: Store) {
        viewModelScope.launch {
            runCatching { deleteScoreUseCase(id, store) }
                .onFailure { t ->
                    _uiState.update {
                        it.copy(
                            events = it.events + ScoresListContract.UiEvent.ShowError("Error: ${t.message}")
                        )
                    }
                }

        }
    }


    private fun refreshScores(store: Store) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            runCatching { getScoresUseCase.getScoresByStore(store) }
                .fold(
                    onSuccess = { result ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                scores = result
                            )
                        }
                    },
                    onFailure = {
                        _uiState.update { it.copy(isLoading = false, events = it.events + ScoresListContract.UiEvent.ShowError("Error loading scores")) }
                    }
                )
        }

    }

    private fun observeScores(store: Store = Store.Any) {
        _uiState.update { it.copy(isLoading = true) }
        observingJob?.cancel()
        observingJob = getScoresUseCase.observeScoresByStore(store)
            .onEach { data ->
                _uiState.update { it.copy(isLoading = false, scores = data) }
            }

            .launchIn(viewModelScope)
    }

    private fun loadScores(store: Store) {
        viewModelScope.launch {
            //insertTestData()
            runCatching {
                getScoresUseCase.getScoresByStore(store)
            }.fold(
                onSuccess = { result ->
                    _uiState.update { it.copy(scores = result) }
                },
                onFailure = {
                    _uiState.update { it.copy(events = it.events + ScoresListContract.UiEvent.ShowError("Error loading scores")) }
                }
            )
        }
    }

    private suspend fun insertTestData() {
        testScoresUseCase.deleteAllScores()
        testScoresUseCase.saveScore(
            Score(
                name = "Test score",
                address = "Test address",
                duration = 1234
            ),
            Store.Local
        )

        testScoresUseCase.saveScore(
            Score(
                name = "Test score 2",
                address = "Test address 2",
                duration = 4321
            ),
            Store.Local
        )

        testScoresUseCase.saveScore(
            Score(
                name = "Beh do konce",
                address = "Mount Everest",
                duration = System.currentTimeMillis().toInt()
            ),
            Store.Remote
        )

    }

    override fun onUiEventConsumed(event: ScoresListContract.UiEvent) {
        _uiState.update { currentState ->
            currentState.copy(
                events = currentState.events - event
            )
        }
    }
}