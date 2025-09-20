package cz.sic.detail.presentation.vm

import cz.sic.domain.model.Score
import cz.sic.utils.UiActionAware
import cz.sic.utils.UiStateAware

class ScoreDetailContract {
    data class UiState(
        val isLoading: Boolean = true,
        val score: Score? = null,
        val events: List<UiEvent> = emptyList()
    ): UiStateAware.UiData

    sealed interface UiAction: UiActionAware.UiAction {
        data class LoadScore(val id: Long) : UiAction
        data object OnAppear : UiAction

        data class SaveScore(val score: Score) : UiAction
    }

    sealed interface UiEvent: UiStateAware.UiEvent {
        data class ShowError(val message: String): UiEvent
    }
}