package cz.sic.utils

sealed class Result<out T : Any?> {

    data class Success<out T : Any?>(val data: T) : Result<T>()

    data class Error<out T : Any?>(val error: ErrorResult, val data: T? = null) : Result<T>()
}

abstract class ErrorResult(val message: ErrorMessage, open val throwable: Throwable? = null) {
    sealed interface ErrorMessage {
        data class Plain(val text: String) : ErrorMessage
        data class Resource(val textResId: Int) : ErrorMessage
        data object Empty : ErrorMessage
    }

    data class General(
        override val throwable: Throwable,
    ) : ErrorResult(message = ErrorMessage.Resource(R.string.unknown_error), throwable = throwable)
}