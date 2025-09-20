package cz.sic.list.presentation.vm

import cz.sic.domain.model.Store
import cz.sic.domain.model.ScoreWithStore
import cz.sic.utils.UiActionAware
import cz.sic.utils.UiStateAware

class ScoresListContract {
    data class UiState(
        val isLoading: Boolean = true,
        val scores: List<ScoreWithStore> = emptyList(),
        val selectedStore: Store = Store.Any,
        val events: List<UiEvent> = emptyList()
    ): UiStateAware.UiData

    sealed interface UiAction: UiActionAware.UiAction {
        data object OnAppear : UiAction
        data class OnScoreClick(val id: Long, val store: Store) : UiAction

        data class OnStoreSelect(val store: Store) : UiAction

        data object OnAddScoreClick : UiAction
    }

    sealed interface UiEvent: UiStateAware.UiEvent {
        data class ShowError(val message: String): UiEvent
        data class ShowDetail(val id: Long, val store: Store): UiEvent

        data object ShowAddScreen: UiEvent
    }

    sealed interface OutputEvent: UiStateAware.OutputEvent {

    }
}