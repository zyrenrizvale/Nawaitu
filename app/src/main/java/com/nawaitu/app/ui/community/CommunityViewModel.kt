package com.nawaitu.app.ui.community

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nawaitu.app.data.local.NawaitDatabase
import com.nawaitu.app.data.local.SessionManager
import com.nawaitu.app.data.model.CommunityPost
import com.nawaitu.app.data.repository.CommunityRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CommunityViewModel(application: Application) : AndroidViewModel(application) {
    private val database = NawaitDatabase.getDatabase(application)
    private val sessionManager = SessionManager(application)
    private val communityRepository = CommunityRepository(database)

    val posts: StateFlow<List<CommunityPost>> = communityRepository.getAllPosts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _userId = MutableStateFlow(-1L)
    private val _userName = MutableStateFlow("")

    val userName: StateFlow<String> = _userName

    init {
        viewModelScope.launch {
            sessionManager.currentUserId.collect { id -> _userId.value = id }
        }
        viewModelScope.launch {
            sessionManager.currentUserName.collect { name -> _userName.value = name }
        }
    }

    fun addPost(content: String) {
        val userId = _userId.value
        val userName = _userName.value
        if (userId < 0 || content.isBlank()) return
        viewModelScope.launch {
            communityRepository.addPost(userId, userName, content.trim())
        }
    }

    fun toggleLike(post: CommunityPost) {
        viewModelScope.launch {
            communityRepository.toggleLike(post)
        }
    }

    fun deletePost(post: CommunityPost) {
        viewModelScope.launch {
            communityRepository.deletePost(post)
        }
    }

    fun isMyPost(post: CommunityPost) = post.authorId == _userId.value
}
