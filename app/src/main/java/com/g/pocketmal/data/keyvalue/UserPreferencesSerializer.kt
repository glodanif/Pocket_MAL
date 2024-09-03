package com.g.pocketmal.data.keyvalue

import androidx.datastore.core.Serializer
import com.google.gson.Gson
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.nio.charset.StandardCharsets

class UserPreferencesSerializer(private val gson: Gson) : Serializer<UserPreferences> {

    override val defaultValue: UserPreferences = UserPreferences.defaultPreferences

    override suspend fun readFrom(input: InputStream): UserPreferences {
        val reader = InputStreamReader(input)
        return gson.fromJson(reader, UserPreferences::class.java)
    }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
        val preferencesString = gson.toJson(t)
        output.apply {
            write(preferencesString.toByteArray(StandardCharsets.UTF_8))
            flush()
            close()
        }
    }
}
