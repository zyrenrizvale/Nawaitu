package com.nawaitu.app.data.repository

import com.nawaitu.app.data.local.NawaitDatabase
import com.nawaitu.app.data.model.CommunityPost
import kotlinx.coroutines.flow.Flow

class CommunityRepository(private val database: NawaitDatabase) {
    fun getAllPosts(): Flow<List<CommunityPost>> =
        database.communityPostDao().getAll()

    suspend fun addPost(authorId: Long, authorName: String, content: String) {
        database.communityPostDao().insert(
            CommunityPost(
                authorId = authorId,
                authorName = authorName,
                content = content
            )
        )
    }

    suspend fun toggleLike(post: CommunityPost) {
        val updated = if (post.isLikedByMe) {
            post.copy(likes = maxOf(0, post.likes - 1), isLikedByMe = false)
        } else {
            post.copy(likes = post.likes + 1, isLikedByMe = true)
        }
        database.communityPostDao().update(updated)
    }

    suspend fun deletePost(post: CommunityPost) = database.communityPostDao().delete(post)
}
