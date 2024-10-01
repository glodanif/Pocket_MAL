package com.g.pocketmal.data.database

import androidx.room.TypeConverter
import com.g.pocketmal.data.DataTitleType
import com.g.pocketmal.data.api.response.MangaMagazineRelationEdge
import com.g.pocketmal.data.api.response.PersonRoleEdge

import com.g.pocketmal.data.api.response.RelatedTitleEdge
import com.g.pocketmal.data.common.Broadcast
import com.g.pocketmal.data.common.Company
import com.g.pocketmal.data.common.Genre
import com.g.pocketmal.data.common.StartSeason
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

import java.util.ArrayList

class Converter {
    @TypeConverter
    fun seasonFromGson(value: String?): StartSeason? {
        return if (value == null) null else gson.fromJson(value, StartSeason::class.java)
    }

    @TypeConverter
    fun seasonToGson(value: StartSeason?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun broadcastFromGson(value: String?): Broadcast? {
        return if (value == null) null else gson.fromJson(value, Broadcast::class.java)
    }

    @TypeConverter
    fun broadcastToGson(value: Broadcast?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun genresFromGson(value: String?): List<Genre>? {
        val type = object : TypeToken<List<Genre>>() {

        }.type
        return if (value == null) ArrayList() else gson.fromJson(value, type)
    }

    @TypeConverter
    fun genresToGson(value: List<Genre>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun companiesToGson(value: List<Company>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun companiesFromGson(value: String?): List<Company>? {
        val type = object : TypeToken<List<Company>>() {

        }.type
        return if (value == null) ArrayList() else gson.fromJson(value, type)
    }

    @TypeConverter
    fun authorsToGson(value: List<PersonRoleEdge>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun authorsFromGson(value: String?): List<PersonRoleEdge>? {
        val type = object : TypeToken<List<PersonRoleEdge>>() {

        }.type
        return if (value == null) ArrayList() else gson.fromJson(value, type)
    }

    @TypeConverter
    fun magazinesToGson(value: List<MangaMagazineRelationEdge>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun magazinesFromGson(value: String?): List<MangaMagazineRelationEdge>? {
        val type = object : TypeToken<List<MangaMagazineRelationEdge>>() {

        }.type
        return if (value == null) ArrayList() else gson.fromJson(value, type)
    }

    @TypeConverter
    fun relatedTitlesToGson(value: List<RelatedTitleEdge>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun relatedTitlesFromGson(value: String?): List<RelatedTitleEdge>? {
        val type = object : TypeToken<List<RelatedTitleEdge>>() {

        }.type
        return if (value == null) ArrayList() else gson.fromJson(value, type)
    }

    @TypeConverter
    fun stringsToGson(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun stringsFromGson(value: String?): List<String> {
        val type = object : TypeToken<List<String>>() {
        }.type
        return if (value == null) ArrayList() else gson.fromJson(value, type)
    }

    @TypeConverter
    fun typeToInt(value: DataTitleType): Int {
        return value.type
    }

    @TypeConverter
    fun intToType(value: Int): DataTitleType {
        return DataTitleType.from(value)
    }

    @TypeConverter
    fun statusToString(value: Status): String {
        return value.status
    }

    @TypeConverter
    fun stringToStatus(value: String): Status {
        return Status.from(value)
    }

    @TypeConverter
    fun longToDate(value: Long): Date? {
        return Date(value)
    }

    @TypeConverter
    fun dateToLong(value: Date?): Long {
        return value?.time ?: 0L
    }

    @TypeConverter
    fun countsToGson(value: ListCounts): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun countsFromGson(value: String?): ListCounts {
        return gson.fromJson(value, ListCounts::class.java)
    }

    companion object {
        private val gson = Gson()
    }
}
