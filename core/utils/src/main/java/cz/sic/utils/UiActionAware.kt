package cz.sic.utils

interface UiActionAware<T : UiActionAware.UiAction> {

    fun onUiAction(uiAction: T)

    interface UiAction
}