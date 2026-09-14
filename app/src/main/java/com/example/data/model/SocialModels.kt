package com.example.data.model

data class User(
    val id: String,
    val username: String,
    val displayName: String,
    val avatarDrawable: Int,
    val bio: String,
    val isVerified: Boolean = false,
    val resonanceScore: Int = 1200,
    val rankTitle: String = "Neural Node",
    val postsCount: Int = 12,
    val connectionsCount: Int = 340,
    val isFollowing: Boolean = false,
    val activeStatus: String = "Synchronized"
)

data class PollOption(
    val id: String,
    val text: String,
    val votes: Int
)

data class PollData(
    val question: String,
    val options: List<PollOption>,
    val userVotedOptionId: String? = null
) {
    val totalVotes: Int get() = options.sumOf { it.votes }
}

enum class PostMediaType {
    NONE,
    HERO_IMAGE,
    POLL
}

data class Post(
    val id: String,
    val author: User,
    val content: String,
    val mediaType: PostMediaType = PostMediaType.NONE,
    val mediaDrawable: Int? = null,
    val timestamp: String,
    val resonanceCount: Int,
    val commentCount: Int,
    val isResonated: Boolean = false,
    val isBookmarked: Boolean = false,
    val poll: PollData? = null,
    val tags: List<String> = emptyList(),
    val comments: List<Comment> = emptyList()
)

data class Comment(
    val id: String,
    val author: User,
    val text: String,
    val timestamp: String,
    val resonanceCount: Int = 0
)

data class Story(
    val id: String,
    val user: User,
    val hasUnseen: Boolean = true,
    val timestamp: String = "2h ago",
    val caption: String = ""
)

data class ChatMessage(
    val id: String,
    val senderId: String,
    val text: String,
    val timestamp: String,
    val isMine: Boolean,
    val isVoiceNote: Boolean = false,
    val voiceDurationSec: Int = 0,
    val isVanishMode: Boolean = false
)

data class Conversation(
    val id: String,
    val participant: User,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int = 0,
    val isOnline: Boolean = true,
    val messages: List<ChatMessage> = emptyList()
)

data class VideoReel(
    val id: String,
    val creator: User,
    val title: String,
    val audioTrack: String,
    val likesCount: Int,
    val commentsCount: Int,
    val sharesCount: Int,
    val isLiked: Boolean = false,
    val coverDrawable: Int
)

enum class NotificationType {
    RESONANCE,
    COMMENT,
    CONNECTION,
    SYSTEM
}

data class NotificationItem(
    val id: String,
    val type: NotificationType,
    val actor: User?,
    val title: String,
    val description: String,
    val timestamp: String,
    val isRead: Boolean = false
)

data class MarketplaceItem(
    val id: String,
    val title: String,
    val description: String,
    val priceCredits: Int,
    val category: String,
    val seller: User,
    val rating: Float,
    val imageDrawable: Int
)

data class MatrixMetrics(
    val resonanceScore: Int,
    val dailyPulse: Int,
    val neuralSyncPct: Int,
    val networkTier: String,
    val activeSignalsCount: Int,
    val totalConnections: Int
)
