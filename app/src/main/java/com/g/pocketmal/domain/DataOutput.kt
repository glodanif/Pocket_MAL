package com.g.pocketmal.domain

sealed class DataOutput<out T> {

    data class Success<T>(val output: T) : DataOutput<T>()

    data class Error(val message: String, val code: Int) : DataOutput<Nothing>()
}
