package com.g.pocketmal.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.g.pocketmal.data.database.model.DbListRecord
import com.g.pocketmal.data.util.TitleType

@Dao
interface RecordDao {

    @Query("SELECT * FROM record WHERE series_id = :id AND title_type = :type")
    suspend fun getRecordById(id: Int, type: TitleType): DbListRecord

    @Query("SELECT * FROM record WHERE title_type = :type")
    suspend fun getRecordsByType(type: TitleType): List<DbListRecord>

    @Query("SELECT * FROM record WHERE my_status = :status AND title_type = :type")
    suspend fun getRecordsByStatus(status: Int, type: TitleType): List<DbListRecord>

    @Query("DELETE FROM record")
    suspend fun deleteAll()

    @Query("DELETE FROM record WHERE title_type = :type")
    suspend fun deleteAllByType(type: TitleType)

    @Query("DELETE FROM record WHERE series_id = :id AND title_type = :type")
    suspend fun deleteById(id: Int, type: TitleType)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: DbListRecord)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: List<DbListRecord>)

    @Query("DELETE FROM record")
    suspend fun dropTable()
}
