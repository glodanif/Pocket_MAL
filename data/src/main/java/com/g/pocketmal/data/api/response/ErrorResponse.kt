package com.g.pocketmal.data.api.response

data class ErrorResponse(
        val message: String?,
        val error: String?
) {
    companion object {
        const val INVALID_TOKEN = "invalid_token"
    }
}
