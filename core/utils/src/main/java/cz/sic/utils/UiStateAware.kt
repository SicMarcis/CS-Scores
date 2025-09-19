package cz.sic.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface UiStateAware<T : UiStateAware.UiData, S : UiStateAware.UiEvent> {
    var uiState: StateFlow<UiState<T, S>>
    val currentUiState: UiState<T, S>
    val currentUiData: T
    val currentUiEvents: List<S>

    fun updateUi(
        isLoading: Boolean? = null,
        uiData: ((uiData: T) -> T)? = null,
        uiEvents: ((uiEvents: List<S>) -> List<S>)? = null
    )

    fun updateIsLoading(isLoading: Boolean)

    fun updateUiData(
        isLoading: Boolean? = null,
        uiData: (uiData: T) -> T
    )

    fun updateUiEvents(
        isLoading: Boolean? = null,
        uiEvents: (uiEvents: List<S>) -> List<S>
    )

    fun onUiEventConsumed(uiEvent: S)

    fun clearUi()

    interface UiData

    interface UiEvent

    interface OutputEvent

    data class UiState<T : UiData, S : UiEvent>(
        val isLoading: Boolean = true,
        val uiData: T,
        val uiEvents: List<S> = emptyList()
    )
}

class UiStateAwareImpl<T : UiStateAware.UiData, S : UiStateAware.UiEvent>(
    private val initialData: T,
    private val initialUiEvents: List<S> = emptyList(),
    private val initialLoading: Boolean = true
) : UiStateAware<T, S> {
    private val _uiState = MutableStateFlow(
        UiStateAware.UiState(
            uiData = initialData,
            uiEvents = initialUiEvents,
            isLoading = initialLoading
        )
    )

    override var uiState: StateFlow<UiStateAware.UiState<T, S>> = _uiState.asStateFlow()
    override val currentUiState: UiStateAware.UiState<T, S>
        get() = uiState.value
    override val currentUiData: T
        get() = currentUiState.uiData
    override val currentUiEvents: List<S>
        get() = currentUiState.uiEvents

    override fun updateUi(
        isLoading: Boolean?,
        uiData: ((uiData: T) -> T)?,
        uiEvents: ((uiEvents: List<S>) -> List<S>)?
    ) {
        _uiState.update { uiState ->
            uiState.copy(
                isLoading = isLoading ?: uiState.isLoading,
                uiData = uiData?.invoke(uiState.uiData) ?: uiState.uiData,
                uiEvents = uiEvents?.invoke(uiState.uiEvents) ?: uiState.uiEvents
            )
        }
    }

    override fun updateIsLoading(isLoading: Boolean) {
        updateUi(isLoading = isLoading)
    }

    override fun clearUi() {
        _uiState.update {
            UiStateAware.UiState(
                uiData = initialData,
                uiEvents = initialUiEvents,
                isLoading = initialLoading
            )
        }
    }

    override fun updateUiData(isLoading: Boolean?, uiData: (uiData: T) -> T) {
        updateUi(
            isLoading = isLoading,
            uiData = uiData
        )
    }

    override fun updateUiEvents(isLoading: Boolean?, uiEvents: (uiEvents: List<S>) -> List<S>) {
        updateUi(
            isLoading = isLoading,
            uiEvents = uiEvents
        )
    }

    override fun onUiEventConsumed(uiEvent: S) {
        _uiState.update { uiState ->
            uiState.copy(uiEvents = uiState.uiEvents - uiEvent)
        }
    }
}

fun <T : UiStateAware.UiEvent> List<T>.add(
    uiEventToAdd: T
): List<T> {
    return this + uiEventToAdd
}

fun <T : UiStateAware.UiEvent> List<T>.addIfNotNull(
    uiEventToAdd: T?
): List<T> {
    uiEventToAdd?.let {
        return this + it
    }
    return this
}