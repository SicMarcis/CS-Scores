package cz.sic.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<
        A: UiActionAware.UiAction,
        E: UiStateAware.UiEvent,
        D: UiStateAware.UiData>(
    initialState: UiStateAware.UiState<D,E>
): ViewModel() {

    protected val _uiState = MutableStateFlow(
        initialState
    )
    val uiState = _uiState.asStateFlow()

    protected abstract suspend fun handleUiAction(action: A)

    fun onUiAction(action: A) {
        viewModelScope.launch {
            handleUiAction(action)
        }
    }

    protected fun updateUi(
        isLoading: Boolean?,
        uiData: ((uiData: D) -> D)? = null,
        uiEvents: ((uiEvents: List<E>) -> List<E>)? = null
    ) {
        _uiState.update { uiState ->
            uiState.copy(
                isLoading = isLoading ?: uiState.isLoading,
                uiData = uiData?.invoke(uiState.uiData) ?: uiState.uiData,
                uiEvents = uiEvents?.invoke(uiState.uiEvents) ?: uiState.uiEvents
            )
        }
    }

    protected fun updateIsLoading(isLoading: Boolean) {
        updateUi(isLoading = isLoading)
    }

    protected fun updateUiEvents(isLoading: Boolean? = null, uiEvents: (uiEvents: List<E>) -> List<E>) {
        updateUi(
            isLoading = isLoading,
            uiEvents = uiEvents
        )
    }

    fun onUiEventConsumed(uiEvent: E) {
        _uiState.update { it.copyWithEventConsumed(uiEvent) }
    }

    protected fun UiStateAware.UiState<D, E>.copyWithEventConsumed(uiEvent: E): UiStateAware.UiState<D, E> {
        return copy(
            uiEvents = uiEvents - uiEvent
        )
    }
}