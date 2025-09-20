package cz.sic.detail.presentation.vm

import cz.sic.detail.presentation.model.Mode
import cz.sic.domain.model.Score
import cz.sic.domain.model.ScoreWithStore
import cz.sic.domain.model.Store
import cz.sic.utils.UiActionAware
import cz.sic.utils.UiStateAware

class ScoreDetailContract {
    data class UiState(
        val isLoading: Boolean = true,
        val score: ScoreWithStore? = null,
        val editEnabled: Boolean = false,
        val mode: Mode = Mode.View,
        val events: List<UiEvent> = emptyList()
    ): UiStateAware.UiData

    sealed interface UiAction: UiActionAware.UiAction {
        data class LoadScore(val id: Long?, val store: Store?, val mode: Mode) : UiAction

        data object AddScore : UiAction
        data object OnAppear : UiAction

        data object SaveScore : UiAction

        sealed class ValueChange<T>(val value: T): UiAction {
            data class Name(val name: String): ValueChange<String>(name)
            data class Address(val address: String): ValueChange<String>(address)
            data class Duration(val duration: String): ValueChange<String>(duration)
            data class Location(val store: Store): ValueChange<Store>(store)
        }
    }

    sealed interface UiEvent: UiStateAware.UiEvent {
        data class ShowError(val message: String): UiEvent

        data object ScoreSaved: UiEvent
    }
}