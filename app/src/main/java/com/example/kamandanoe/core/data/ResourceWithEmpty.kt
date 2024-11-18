package com.example.kamandanoe.core.data

sealed class ResourceWithEmpty<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : ResourceWithEmpty<T>(data)
    class Loading<T>(data: T? = null) : ResourceWithEmpty<T>(data)
    class Error<T>(message: String, data: T? = null) : ResourceWithEmpty<T>(data, message)
    class Empty<T>(message: String) : ResourceWithEmpty<T>(null, message)
}
