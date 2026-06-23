package com.nawaitu.app.data.local

import androidx.room.*
import com.nawaitu.app.data.model.TodoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos WHERE userId = :userId ORDER BY isDone ASC, createdAt DESC")
    fun getAllByUser(userId: Long): Flow<List<TodoItem>>

    @Insert
    suspend fun insert(todo: TodoItem): Long

    @Update
    suspend fun update(todo: TodoItem)

    @Delete
    suspend fun delete(todo: TodoItem)

    @Query("UPDATE todos SET isDone = :isDone WHERE id = :id")
    suspend fun updateDone(id: Long, isDone: Boolean)
}
