package com.g.pocketmal.data.keyvalue

import android.content.Context
import androidx.core.content.edit
import at.favre.lib.armadillo.Armadillo
import com.g.pocketmal.domain.entity.BaseUserEntity
import com.google.gson.Gson

class SessionManager(context: Context) {

    private val preferencesKey = "session_storage"

    private val gson = Gson()
    private val preferences = Armadillo.create(context, preferencesKey)
                    .encryptionFingerprint(context)
                    .build()

    init {
        migrate()
    }

    fun saveTokenData(accessToken: String?, refreshToken: String?, expirationTime: Long) {
        preferences.edit {
            putString(KEY_ACCESS_TOKEN, accessToken)
            putString(KEY_REFRESH_TOKEN, refreshToken)
            putLong(KEY_EXPIRATION_TIME, expirationTime * 1000)
        }
    }

    fun saveUser(user: BaseUserEntity) {
        preferences.edit {
            putString(KEY_USER, gson.toJson(user))
        }
    }

    val user: BaseUserEntity?
        get() = gson.fromJson(preferences.getString(KEY_USER, ""), BaseUserEntity::class.java)

    val accessToken: String?
        get() = preferences.getString(KEY_ACCESS_TOKEN, null)

    val refreshToken: String?
        get() = preferences.getString(KEY_REFRESH_TOKEN, null)

    val expirationTime: Long
        get() = preferences.getLong(KEY_EXPIRATION_TIME, 0)

    val isUserLoggedIn: Boolean
        get() = accessToken != null && user != null

    private fun migrate() {
        val lastVersion = preferences.getInt(KEY_VERSION, 0)
        if (lastVersion == VERSION) {
            return
        }
        if (lastVersion < 2) {
            preferences.edit {
                remove("mal_user_email")
                remove("mal_user_password")
                remove("mal_user_id")
            }
        }
        preferences.edit {
            putInt(KEY_VERSION, VERSION)
        }
    }

    fun logout() {
        preferences.edit {
            clear()
        }
    }

    companion object {
        private const val VERSION = 2
        private const val KEY_VERSION = "storage_version"
        private const val KEY_USER = "user"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_EXPIRATION_TIME = "expiration_time"
    }
}
