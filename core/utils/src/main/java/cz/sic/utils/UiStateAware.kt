package cz.sic.utils

interface UiStateAware {

    interface UiData

    interface UiEvent

    interface OutputEvent

    data class UiState<T : UiData, S : UiEvent>(
        val isLoading: Boolean = true,
        val uiData: T,
        val uiEvents: List<S> = emptyList()
    )
}