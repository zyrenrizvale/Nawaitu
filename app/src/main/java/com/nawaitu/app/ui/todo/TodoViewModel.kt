package com.nawaitu.app.ui.todo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nawaitu.app.data.local.NawaitDatabase
import com.nawaitu.app.data.local.SessionManager
import com.nawaitu.app.data.model.Priority
import com.nawaitu.app.data.model.TodoItem
import com.nawaitu.app.data.repository.TodoRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val database = NawaitDatabase.getDatabase(application)
    private val sessionManager = SessionManager(application)
    private val todoRepository = TodoRepository(database)

    private val _userId = MutableStateFlow(-1L)

    val todos: StateFlow<List<TodoItem>> = _userId
        .flatMapLatest { uid ->
            if (uid > 0) todoRepository.getAllTodos(uid)
            else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            sessionManager.currentUserId.collect { id ->
                _userId.value = id
            }
        }
    }

    fun addTodo(title: String, description: String, priority: Priority) {
        val userId = _userId.value
        if (userId < 0 || title.isBlank()) return
        viewModelScope.launch {
            todoRepository.addTodo(userId, title.trim(), description.trim(), priority)
        }
    }

    fun toggleDone(todo: TodoItem) {
        viewModelScope.launch {
            todoRepository.toggleDone(todo.id, todo.isDone)
        }
    }

    fun deleteTodo(todo: TodoItem) {
        viewModelScope.launch {
            todoRepository.deleteTodo(todo)
        }
    }
}
