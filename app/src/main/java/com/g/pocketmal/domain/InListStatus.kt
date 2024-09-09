package com.g.pocketmal.domain

enum class InListStatus {
    NOT_IN_LIST,
    IN_PROGRESS,
    COMPLETED,
    ON_HOLD,
    DROPPED,
    PLANNED;

    companion object {
        fun parse(status: String?): InListStatus {
            return when (status) {
                "watching",
                "reading" -> IN_PROGRESS

                "completed" -> COMPLETED

                "on_hold" -> ON_HOLD

                "dropped" -> DROPPED

                "plan_to_watch",
                "plan_to_read" -> PLANNED

                else -> NOT_IN_LIST
            }
        }
    }
}
