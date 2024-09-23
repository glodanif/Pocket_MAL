package com.g.pocketmal.data.common

enum class Status(val status: String) {
    NOT_IN_LIST("not_listed"),
    GENERAL("general"),
    IN_PROGRESS("watching"),
    COMPLETED("completed"),
    ON_HOLD("on_hold"),
    DROPPED("dropped"),
    PLANNED("plan_to_watch");

    companion object {
        fun from(status: String?) = entries.find { value -> value.status == status } ?: NOT_IN_LIST
    }
}