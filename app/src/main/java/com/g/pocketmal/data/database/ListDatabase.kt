package com.g.pocketmal.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import com.g.pocketmal.data.database.dao.RecordDao
import com.g.pocketmal.data.database.dao.TitleDetailsDao
import com.g.pocketmal.data.database.dao.UserDao
import com.g.pocketmal.data.database.model.DbListRecord
import com.g.pocketmal.data.database.model.TitleDetailsTable
import com.g.pocketmal.data.database.model.UserProfileTable

@Database(entities = [UserProfileTable::class, DbListRecord::class, TitleDetailsTable::class], version = 1)
@TypeConverters(Converter::class)
abstract class ListDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao
    abstract fun titleDetailsDao(): TitleDetailsDao
    abstract fun userDao(): UserDao
}
