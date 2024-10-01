package com.g.pocketmal.data.database

import android.content.Context
import androidx.room.Room

class ListDbStorage(private val context: Context) {

    val db = Room.databaseBuilder(context, ListDatabase::class.java, "list_database").build()

    fun migrateLegacy() {
        context.deleteDatabase("anime_list")
    }
}
