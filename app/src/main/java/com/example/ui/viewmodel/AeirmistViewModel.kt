package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.data.model.Story
import com.example.data.repository.AeirmistRepository
import com.example.data.repository.AeirmistState
import kotlinx.coroutines.flow.StateFlow

class AeirmistViewModel(
    private val repository: AeirmistRepository = AeirmistRepository()
) : ViewModel() {

    val uiState: StateFlow<AeirmistState> = repository.state

    fun createPost(content: String, tags: List<String>, pollQuestion: String? = null, pollOptions: List<String> = emptyList()) {
        repository.createPost(content, tags, pollQuestion, pollOptions)
    }

    fun toggleResonance(postId: String) {
        repository.toggleResonance(postId)
    }

    fun toggleBookmark(postId: String) {
        repository.toggleBookmark(postId)
    }

    fun votePoll(postId: String, optionId: String) {
        repository.votePoll(postId, optionId)
    }

    fun addComment(postId: String, text: String) {
        repository.addComment(postId, text)
    }

    fun sendMessage(conversationId: String, text: String, isVanish: Boolean = false) {
        repository.sendMessage(conversationId, text, isVanish)
    }

    fun selectConversation(conversationId: String?) {
        repository.selectConversation(conversationId)
    }

    fun toggleFollowUser(userId: String) {
        repository.toggleFollowUser(userId)
    }

    fun toggleVideoLike(videoId: String) {
        repository.toggleVideoLike(videoId)
    }

    fun markNotificationRead(notifId: String) {
        repository.markNotificationRead(notifId)
    }

    fun markAllNotificationsRead() {
        repository.markAllNotificationsRead()
    }

    fun setFeedFilter(filter: String) {
        repository.setFeedFilter(filter)
    }

    fun setSearchQuery(query: String) {
        repository.setSearchQuery(query)
    }

    fun viewStory(story: Story?) {
        repository.viewStory(story)
    }

    fun updateProfile(displayName: String, bio: String) {
        repository.updateProfile(displayName, bio)
    }
}
