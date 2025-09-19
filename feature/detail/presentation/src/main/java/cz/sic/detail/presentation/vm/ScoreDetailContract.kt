package cz.sic.detail.presentation.vm

import cz.sic.utils.UiActionAware
import cz.sic.utils.UiStateAware

class ScoreDetailContract {
    data class UiState(
        val isLoading: Boolean = true,
        val events: List<UiEvent> = emptyList()
    ): UiStateAware.UiData

    sealed interface UiAction: UiActionAware.UiAction {
        data object OnAppear : UiAction
    }

    sealed interface UiEvent: UiStateAware.UiEvent {
        data class ShowError(val message: String): UiEvent
    }
}