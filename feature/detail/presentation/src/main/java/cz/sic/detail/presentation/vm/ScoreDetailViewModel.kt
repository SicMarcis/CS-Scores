package cz.sic.detail.presentation.vm

import androidx.lifecycle.viewModelScope
import cz.sic.detail.domain.usecase.ChangeScoresUseCase
import cz.sic.detail.presentation.model.Mode
import cz.sic.domain.model.Score
import cz.sic.domain.model.ScoreWithStore
import cz.sic.domain.model.Store
import cz.sic.domain.usecase.GetScoreItemUseCase
import cz.sic.utils.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScoreDetailViewModel(
    val getScoreItemUseCase: GetScoreItemUseCase,
    val changeScoresUseCase: ChangeScoresUseCase
): BaseViewModel<ScoreDetailContract.UiAction, ScoreDetailContract.UiEvent>() {

    private val _uiState = MutableStateFlow (ScoreDetailContract.UiState())
    val uiState: StateFlow<ScoreDetailContract.UiState> =
        _uiState.asStateFlow()

    override suspend fun handleUiAction(action: ScoreDetailContract.UiAction) {
        when (action) {
            is ScoreDetailContract.UiAction.OnAppear -> {}
            is ScoreDetailContract.UiAction.LoadScore -> loadScore(action.id!!, action.store!!)
            is ScoreDetailContract.UiAction.SaveScore -> saveScore()
            is ScoreDetailContract.UiAction.AddScore -> handleAddScore()
            is ScoreDetailContract.UiAction.ValueChange<*> -> updateValue(action)
        }
    }

    private fun handleAddScore() {
        _uiState.update {
            it.copy(
                isLoading = false,
                editEnabled = true,
                score = ScoreWithStore(
                    score = Score(
                        name = "",
                        address = "",
                        duration = 0,
                    ),
                    store = Store.Any
                ),
                mode = Mode.Add
            )
        }
    }
    private fun updateValue(change: ScoreDetailContract.UiAction.ValueChange<*>) {
        when (change) {
            is ScoreDetailContract.UiAction.ValueChange.Name -> {
                val current = _uiState.value.score ?: return
                _uiState.update {
                    it.copy(
                        score = current.copy(
                            score = current.score.copy(
                                name = change.value
                            )
                        )
                    )
                }
            }
            is ScoreDetailContract.UiAction.ValueChange.Address -> {
                val current = _uiState.value.score ?: return
                _uiState.update {
                    it.copy(
                        score = current.copy(
                            score = current.score.copy(
                                address = change.value
                            )
                        )
                    )
                }
            }
            is ScoreDetailContract.UiAction.ValueChange.Duration -> {
                val current = _uiState.value.score ?: return
                val duration = change.value.toIntOrNull() ?: 0
                _uiState.update {
                    it.copy(
                        score = current.copy(
                            score = current.score.copy(
                                duration = duration
                            )
                        )
                    )
                }
            }
            is ScoreDetailContract.UiAction.ValueChange.Location -> {
                val current = _uiState.value.score ?: return
                _uiState.update {
                    it.copy(
                        score = current.copy(
                            store = change.value
                        )
                    )
                }
            }
        }
    }
    private fun loadScore(id: Long, store: Store) {
        _uiState.update { it.copy(editEnabled = false) }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            runCatching { getScoreItemUseCase(id, store) }
                .fold(
                    onSuccess = { result ->
                        when (result) {
                            null -> _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    events = it.events + ScoreDetailContract.UiEvent.ShowError("Score not found")
                                )
                            }
                            else -> _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    score = result
                                )
                            }
                        }
                    },
                    onFailure = { t ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                events = it.events + ScoreDetailContract.UiEvent.ShowError(t.message ?: "Unknown error")
                            )
                        }
                    }
                )
        }
    }

    private fun saveScore() {
        val score = _uiState.value.score
        val store = score?.store
        if(score == null || store == null) {
            _uiState.update {
                it.copy(events = it.events + ScoreDetailContract.UiEvent.ShowError("Input form not valid"))
            }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            runCatching { changeScoresUseCase.saveScore(score.score, store) }
                .fold(
                    onSuccess = {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                events = it.events + ScoreDetailContract.UiEvent.ScoreSaved
                            )
                        }
                    },
                    onFailure = { t ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                events = it.events + ScoreDetailContract.UiEvent.ShowError("Error saving: ${t.message}")
                            )
                        }
                    }
                )
        }
    }

    override fun onUiEventConsumed(uiEvent: ScoreDetailContract.UiEvent) {
        _uiState.update { it.copy(events = it.events - uiEvent) }
    }
}