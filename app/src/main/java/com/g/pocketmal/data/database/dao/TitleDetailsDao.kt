package com.g.pocketmal.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.g.pocketmal.data.database.model.TitleDetailsTable
import com.g.pocketmal.domain.TitleType

@Dao
interface TitleDetailsDao {

    @Query("SELECT * FROM title_details WHERE id = :id AND title_type = :type")
    suspend fun getDetailsById(id: Int, type: TitleType): TitleDetailsTable

    @Query("DELETE FROM title_details WHERE id = :id AND title_type = :type")
    suspend fun delete(id: Int, type: TitleType)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(titleDetails: TitleDetailsTable)
}
