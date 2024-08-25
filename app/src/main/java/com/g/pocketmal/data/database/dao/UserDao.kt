package com.g.pocketmal.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.g.pocketmal.data.database.model.UserProfileTable

@Dao
interface UserDao {

    @Query("SELECT * FROM user_profile WHERE id = :id")
    suspend fun getUserById(id: Int): UserProfileTable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserProfileTable)
}
