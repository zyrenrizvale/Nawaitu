package com.nawaitu.app.data.local

import androidx.room.*
import com.nawaitu.app.data.model.CommunityPost
import kotlinx.coroutines.flow.Flow

@Dao
interface CommunityPostDao {
    @Query("SELECT * FROM community_posts ORDER BY createdAt DESC")
    fun getAll(): Flow<List<CommunityPost>>

    @Insert
    suspend fun insert(post: CommunityPost): Long

    @Update
    suspend fun update(post: CommunityPost)

    @Delete
    suspend fun delete(post: CommunityPost)
}
