package com.madteam.sunset.utils

sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error<T>(val error: Throwable) : Result<T>()
    class Loading<T> : Result<T>()
}

inline fun <R, T> Result<T>.fold(
    onSuccess: (data: T) -> R,
    onError: (exception: Throwable) -> R,
    onLoading: () -> R
): R =
    when (this) {
        is Result.Success -> onSuccess(data)
        is Result.Error -> onError(error)
        is Result.Loading -> onLoading()
    }


inline fun <R> runCatchingException(block: () -> R): Result<R> =
    try {
        Result.Success(data = block())
    } catch (ex: Exception) {
        Result.Error(ex)
    }
