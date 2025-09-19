package cz.sic.detail.presentation.vm

import cz.sic.utils.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScoreDetailViewModel: BaseViewModel<ScoreDetailContract.UiAction, ScoreDetailContract.UiEvent>() {

    private val _uiState = MutableStateFlow (ScoreDetailContract.UiState())
    val uiState: StateFlow<ScoreDetailContract.UiState> =
        _uiState.asStateFlow()

    override fun onUiEventConsumed(uiEvent: ScoreDetailContract.UiEvent) {
        when (uiEvent) {
            is ScoreDetailContract.UiEvent.ShowError -> {
                _uiState.value = _uiState.value.copy(
                    events = _uiState.value.events - uiEvent
                )
            }
        }
    }

    override suspend fun handleUiAction(action: ScoreDetailContract.UiAction) {
        when (action) {
            is ScoreDetailContract.UiAction.OnAppear -> {
                loadScore()
            }
        }
    }

    private fun loadScore() {

    }
}