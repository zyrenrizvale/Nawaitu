package com.nawaitu.app.data.local

import androidx.room.*
import com.nawaitu.app.data.model.AlarmItem
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarms WHERE userId = :userId ORDER BY hour ASC, minute ASC")
    fun getAllByUser(userId: Long): Flow<List<AlarmItem>>

    @Query("SELECT * FROM alarms WHERE userId = :userId")
    suspend fun getAllByUserOnce(userId: Long): List<AlarmItem>

    @Insert
    suspend fun insert(alarm: AlarmItem): Long

    @Update
    suspend fun update(alarm: AlarmItem)

    @Delete
    suspend fun delete(alarm: AlarmItem)

    @Query("UPDATE alarms SET isEnabled = :isEnabled WHERE id = :id")
    suspend fun updateEnabled(id: Long, isEnabled: Boolean)
}
