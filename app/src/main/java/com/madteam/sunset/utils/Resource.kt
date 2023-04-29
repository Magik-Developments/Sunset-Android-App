package com.madteam.sunset.utils

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(value: T) : Resource<T>(data = value)
    class Error<T>(message: String) : Resource<T>(message = message)
    class Loading<T>() : Resource<T>()
}
