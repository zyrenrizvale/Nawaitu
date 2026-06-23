package com.nawaitu.app.data.repository

import com.nawaitu.app.data.local.NawaitDatabase
import com.nawaitu.app.data.model.Priority
import com.nawaitu.app.data.model.TodoItem
import kotlinx.coroutines.flow.Flow

class TodoRepository(private val database: NawaitDatabase) {
    fun getAllTodos(userId: Long): Flow<List<TodoItem>> =
        database.todoDao().getAllByUser(userId)

    suspend fun addTodo(
        userId: Long,
        title: String,
        description: String,
        priority: Priority
    ) {
        database.todoDao().insert(
            TodoItem(
                userId = userId,
                title = title,
                description = description,
                priority = priority
            )
        )
    }

    suspend fun toggleDone(id: Long, currentDone: Boolean) {
        database.todoDao().updateDone(id, !currentDone)
    }

    suspend fun deleteTodo(todo: TodoItem) = database.todoDao().delete(todo)

    suspend fun updateTodo(todo: TodoItem) = database.todoDao().update(todo)
}
