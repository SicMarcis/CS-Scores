package cz.sic.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

abstract class BaseViewModel<A,E>: ViewModel() {

    abstract fun onUiEventConsumed(uiEvent: E)

    protected abstract suspend fun handleUiAction(action: A)

    fun onUiAction(action: A) {
        viewModelScope.launch {
            handleUiAction(action)
        }
    }
}

