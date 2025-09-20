package cz.sic.detail.presentation.vm

import androidx.lifecycle.viewModelScope
import cz.sic.detail.presentation.model.Mode
import cz.sic.domain.model.Score
import cz.sic.domain.model.ScoreWithStore
import cz.sic.domain.model.Store
import cz.sic.domain.usecase.GetAllScoresUseCase
import cz.sic.domain.usecase.GetScoreItemUseCase
import cz.sic.utils.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScoreDetailViewModel(
    val getScoreItemUseCase: GetScoreItemUseCase
): BaseViewModel<ScoreDetailContract.UiAction, ScoreDetailContract.UiEvent>() {

    private val _uiState = MutableStateFlow (ScoreDetailContract.UiState())
    val uiState: StateFlow<ScoreDetailContract.UiState> =
        _uiState.asStateFlow()

    override suspend fun handleUiAction(action: ScoreDetailContract.UiAction) {
        when (action) {
            is ScoreDetailContract.UiAction.OnAppear -> {}
            is ScoreDetailContract.UiAction.LoadScore -> {
                if(action.mode == Mode.View) {
                    _uiState.update { it.copy(editEnabled = false) }
                    loadScore(action.id!!, action.store!!)
                } else {
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
                            mode = action.mode
                        )
                    }
                }

            }
            is ScoreDetailContract.UiAction.SaveScore -> {}
        }
    }

    private fun loadScore(id: Long, store: Store) {
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

    override fun onUiEventConsumed(uiEvent: ScoreDetailContract.UiEvent) {
        when (uiEvent) {
            is ScoreDetailContract.UiEvent.ShowError -> {
                _uiState.value = _uiState.value.copy(
                    events = _uiState.value.events - uiEvent
                )
            }
        }
    }
}