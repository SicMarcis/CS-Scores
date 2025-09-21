package cz.sic.utils

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/** Transform Result data object to Result<Unit> */
fun <T : Any?> Result<T>.toEmptyResult(): Result<Unit> = map { }

/**
 * Creates a [Result.Success] from [A], the receiver.
 */
fun <A : Any?> A.success(): Result<A> = Result.Success(this)

/**
 * Creates a [Result.Error] from the receiver.
 */
fun <A : Any?> ErrorResult.error(): Result<A> = Result.Error(this)

/** Fold Result data object */
@OptIn(ExperimentalContracts::class)
inline fun <T : Any?, R> Result<T>.fold(
    success: (data: T) -> R,
    error: (error: ErrorResult) -> R,
): R {
    contract {
        callsInPlace(success, InvocationKind.AT_MOST_ONCE)
        callsInPlace(error, InvocationKind.AT_MOST_ONCE)
    }

    return when (this) {
        is Result.Success -> success(data)
        is Result.Error -> error(this.error)
    }
}

/** Transform Result data object */
inline fun <T : Any?, R : Any?> Result<T>.map(transform: (T) -> R) =
    flatMap { Result.Success(transform(it)) }

/** Transform Result data object */
inline fun <T : Any?, R : Any?> Result<T>.flatMap(transform: (T) -> Result<R>) =
    fold(success = { transform(it) }, error = { Result.Error(it) })

/**
 * Runs the [action] if the [Result] is s [Result.Success].
 * @return The original result
 */
inline fun <T : Any?> Result<T>.alsoOnSuccess(action: (T) -> Unit): Result<T> =
    apply { if (this is Result.Success) action(data) }

/**
 * Runs the [action] if the [Result] is an [Result.Error].
 * @return The original result
 */
inline fun <T : Any?> Result<T>.alsoOnError(action: (ErrorResult) -> Unit): Result<T> =
    apply { if (this is Result.Error) action(error) }

/**
 * Returns this [Result] if it's a [Result.Success] or returns [recover] when this [Result] is [Result.Error]
 */
inline fun <T : Any?> Result<T>.getOrElse(recover: (ErrorResult) -> T): T =
    fold(success = { it }, error = { recover(it) })

/**
 * Returns the [data] if this [Result] is a [Result.Success] or null otherwise.
 */
fun <T : Any?> Result<T>.getOrNull(): T? = fold(success = { it }, error = { null })

/**
 * Returns the [errorResult] if this [Result] is a [Result.Error] or null otherwise.
 */
fun <T : Any?> Result<T>.getErrorOrNull(): ErrorResult? = fold(success = { null }, error = { it })

/**
 * Returns this [Result] if it's a [Result.Success] or returns the [Result] of [recover] when this [Result] is [Result.Error]
 */
inline fun <T : Any?> Result<T>.recover(recover: (ErrorResult) -> Result<T>): Result<T> =
    fold(success = { Result.Success(it) }, error = { recover(it) })

/**
 * Returns the inner [Result] if the outer one is a [Result.Success] or the outer one if it is an [Result.Error]
 */
fun <T : Any?> Result<Result<T>>.flatten(): Result<T> =
    fold(success = { it }, error = { Result.Error(it) })

/**
 * Returns a [Result] containing a [Pair] of values from [resultA] and [resultB] if both are [Result.success].
 * If either is a [Result.failure], returns the first encountered failure.
 */
fun <A : Any, B : Any> combineResults(
    resultA: Result<A>,
    resultB: Result<B>,
): Result<Pair<A, B>> =
    resultA.flatMap { a ->
        resultB.map { b ->
            a to b
        }
    }

/**
 * Executes [block] and wraps the result in a [Result].
 *
 * Returns [Result.Success] if the block completes normally,
 * or [Result.Error] with an [ErrorResult.General] if an exception occurs.
 *
 * @param T The type of the successful result.
 */
inline fun <T : Any> runTryCatching(block: () -> T): Result<T> {
    return try {
        Result.Success(block())
    } catch (throwable: Throwable) {
        Result.Error(error = ErrorResult.General(throwable))
    }
}

suspend inline fun <T1, T2, R> combine(
    result: Result<T1>,
    result2: Result<T2>,
    crossinline transform: suspend (T1, T2) -> R
): Result<R> =
    result.flatMap { r1 ->
        result2.map { r2 ->
            transform(r1, r2)
        }
    }

suspend inline fun <T1, T2, T3, R> combine(
    result: Result<T1>,
    result2: Result<T2>,
    result3: Result<T3>,
    crossinline transform: suspend (T1, T2, T3) -> R
): Result<R> =
    result.flatMap { r1 ->
        result2.flatMap { r2 ->
            result3.map { r3 ->
                transform(r1, r2, r3)
            }
        }
    }

suspend inline fun <T1, T2, T3, T4, R> combine(
    result: Result<T1>,
    result2: Result<T2>,
    result3: Result<T3>,
    result4: Result<T4>,
    crossinline transform: suspend (T1, T2, T3, T4) -> R
): Result<R> =
    result.flatMap { r1 ->
        result2.flatMap { r2 ->
            result3.flatMap { r3 ->
                result4.map { r4 ->
                    transform(r1, r2, r3, r4)
                }
            }
        }
    }

suspend inline fun <T1, T2, T3, T4, T5, R> combine(
    result: Result<T1>,
    result2: Result<T2>,
    result3: Result<T3>,
    result4: Result<T4>,
    result5: Result<T5>,
    crossinline transform: suspend (T1, T2, T3, T4, T5) -> R
): Result<R> =
    result.flatMap { r1 ->
        result2.flatMap { r2 ->
            result3.flatMap { r3 ->
                result4.flatMap { r4 ->
                    result5.map { r5 ->
                        transform(r1, r2, r3, r4, r5)
                    }
                }
            }
        }
    }

suspend inline fun <T1, T2, T3, T4, T5, T6, R> combine(
    result: Result<T1>,
    result2: Result<T2>,
    result3: Result<T3>,
    result4: Result<T4>,
    result5: Result<T5>,
    result6: Result<T6>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6) -> R
): Result<R> =
    result.flatMap { r1 ->
        result2.flatMap { r2 ->
            result3.flatMap { r3 ->
                result4.flatMap { r4 ->
                    result5.flatMap { r5 ->
                        result6.map { r6 ->
                            transform(r1, r2, r3, r4, r5, r6)
                        }
                    }
                }
            }
        }
    }
