package com.g.pocketmal.data.api

class NetworkException(
        val code: Int,
        val errorMessage: String = ""
) : Exception("($code) $errorMessage")
