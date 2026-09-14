package com.example.data.repository

import com.example.R
import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

data class AeirmistState(
    val currentUser: User,
    val posts: List<Post>,
    val stories: List<Story>,
    val conversations: List<Conversation>,
    val activeConversationId: String? = null,
    val videoReels: List<VideoReel>,
    val notifications: List<NotificationItem>,
    val marketplaceItems: List<MarketplaceItem>,
    val networkUsers: List<User>,
    val matrixMetrics: MatrixMetrics,
    val searchQuery: String = "",
    val activeFeedFilter: String = "ALL",
    val activeStory: Story? = null
)

class AeirmistRepository {

    private val currentUser = User(
        id = "usr_junaed",
        username = "junaed_islam_jim9",
        displayName = "Junaed Islam Jim",
        avatarDrawable = R.drawable.img_avatar_cyber,
        bio = "Lead Architect at Aeirmist Matrix. Shaping next-generation neural resonance & cybernetic interfaces.",
        isVerified = true,
        resonanceScore = 9420,
        rankTitle = "Matrix Architect • Tier V",
        postsCount = 48,
        connectionsCount = 1840,
        isFollowing = false,
        activeStatus = "Transmitting"
    )

    private val userAura = User(
        id = "usr_aura",
        username = "aura_protocol",
        displayName = "Aura Neural",
        avatarDrawable = R.drawable.img_app_icon,
        bio = "Distributed cybernetic collective. Bridging organic consciousness with digital sanctuaries.",
        isVerified = true,
        resonanceScore = 7850,
        rankTitle = "Resonance Sentinel",
        postsCount = 89,
        connectionsCount = 3120,
        isFollowing = true,
        activeStatus = "Synchronized"
    )

    private val userValkyrie = User(
        id = "usr_valk",
        username = "valkyrie_synth",
        displayName = "Valkyrie Ray",
        avatarDrawable = R.drawable.img_hero_cyber,
        bio = "Audio visual synthwave artist & 3D hologram designer. Living in the high-frequency continuum.",
        isVerified = true,
        resonanceScore = 5230,
        rankTitle = "Signal Shaper",
        postsCount = 34,
        connectionsCount = 980,
        isFollowing = false,
        activeStatus = "In Studio"
    )

    private val userNova = User(
        id = "usr_nova",
        username = "nova_core",
        displayName = "Nova Vance",
        avatarDrawable = R.drawable.img_avatar_cyber,
        bio = "Quantum security researcher and decentralized node operator.",
        isVerified = false,
        resonanceScore = 3410,
        rankTitle = "Cipher Operator",
        postsCount = 19,
        connectionsCount = 450,
        isFollowing = true,
        activeStatus = "Encrypted"
    )

    private val initialPosts = listOf(
        Post(
            id = "post_1",
            author = currentUser,
            content = "The new Aeirmist 2.0 native Android architecture is live! Real-time neural resonance, glassmorphic HUD telemetry, and zero latency signal flow. Step into the matrix. ⚡💎",
            mediaType = PostMediaType.HERO_IMAGE,
            mediaDrawable = R.drawable.img_hero_cyber,
            timestamp = "10m ago",
            resonanceCount = 342,
            commentCount = 28,
            isResonated = true,
            isBookmarked = true,
            tags = listOf("Aeirmist2", "NativeMatrix", "Cyberpunk", "JetpackCompose"),
            comments = listOf(
                Comment("c1", userAura, "The glassmorphism and responsiveness are astonishing!", "8m ago", 12),
                Comment("c2", userValkyrie, "Transmitting on pure 120Hz frequency. Massive upgrade!", "4m ago", 7)
            )
        ),
        Post(
            id = "post_2",
            author = userAura,
            content = "Community Signal Poll: Which neural frequency should we activate for tomorrow's synchronized meditation and matrix broadcast?",
            mediaType = PostMediaType.POLL,
            timestamp = "1h ago",
            resonanceCount = 614,
            commentCount = 45,
            isResonated = false,
            poll = PollData(
                question = "Broadcast Frequency Channel",
                options = listOf(
                    PollOption("opt_1", "432 Hz Solfeggio Resonance", 380),
                    PollOption("opt_2", "528 Hz DNA Repair Wave", 295),
                    PollOption("opt_3", "963 Hz Crown Transcendence", 184)
                ),
                userVotedOptionId = "opt_1"
            ),
            tags = listOf("AuraPoll", "Frequency", "Transcendence")
        ),
        Post(
            id = "post_3",
            author = userValkyrie,
            content = "Dropped a new synth-matrix visual soundscape into the Video stream! Turn up the haptics and sync your audio drivers. 🎧✨",
            mediaType = PostMediaType.HERO_IMAGE,
            mediaDrawable = R.drawable.img_app_icon,
            timestamp = "3h ago",
            resonanceCount = 890,
            commentCount = 62,
            isResonated = false,
            tags = listOf("Soundscape", "Synthetix", "Reels")
        ),
        Post(
            id = "post_4",
            author = userNova,
            content = "Quantum cryptography protocol verified across all node clusters. Vanish mode messaging is fully peer-to-peer end-to-end encrypted.",
            mediaType = PostMediaType.NONE,
            timestamp = "5h ago",
            resonanceCount = 175,
            commentCount = 14,
            isResonated = false,
            tags = listOf("Cryptography", "Privacy", "Security")
        )
    )

    private val initialStories = listOf(
        Story("st_0", currentUser, hasUnseen = false, timestamp = "Just now", caption = "Deploying Matrix 2.0"),
        Story("st_1", userAura, hasUnseen = true, timestamp = "25m ago", caption = "Neural Pulse sync: 98%"),
        Story("st_2", userValkyrie, hasUnseen = true, timestamp = "1h ago", caption = "Studio session live ✨"),
        Story("st_3", userNova, hasUnseen = true, timestamp = "3h ago", caption = "Cipher mesh updated")
    )

    private val initialConversations = listOf(
        Conversation(
            id = "conv_aura",
            participant = userAura,
            lastMessage = "The neural sync telemetry looks pristine on device!",
            timestamp = "12:44 PM",
            unreadCount = 2,
            isOnline = true,
            messages = listOf(
                ChatMessage("m1", "usr_aura", "Greetings Architect Junaed. All nodes report green status.", "12:40 PM", false),
                ChatMessage("m2", "usr_junaed", "Excellent. Jetpack Compose UI rendering is delivering 120 FPS.", "12:42 PM", true),
                ChatMessage("m3", "usr_aura", "The neural sync telemetry looks pristine on device!", "12:44 PM", false)
            )
        ),
        Conversation(
            id = "conv_valk",
            participant = userValkyrie,
            lastMessage = "Sent you the audio stems for the new story stream.",
            timestamp = "Yesterday",
            unreadCount = 0,
            isOnline = false,
            messages = listOf(
                ChatMessage("m4", "usr_valk", "Hey Junaed! Checking in on the visualizer.", "Yesterday", false),
                ChatMessage("m5", "usr_junaed", "Added the dynamic neon waveform haptics!", "Yesterday", true),
                ChatMessage("m6", "usr_valk", "Sent you the audio stems for the new story stream.", "Yesterday", false)
            )
        ),
        Conversation(
            id = "conv_nova",
            participant = userNova,
            lastMessage = "Encrypted handshake established.",
            timestamp = "Sep 12",
            unreadCount = 0,
            isOnline = true,
            messages = listOf(
                ChatMessage("m7", "usr_nova", "Encrypted handshake established.", "Sep 12", false)
            )
        )
    )

    private val initialVideos = listOf(
        VideoReel(
            id = "vid_1",
            creator = userValkyrie,
            title = "High-velocity Cyberpunk Flight Simulation through Neo-Dhaka Skyline 🌆✨",
            audioTrack = "Valkyrie Synth • Neon Dreams (Matrix Mix)",
            likesCount = 4210,
            commentsCount = 312,
            sharesCount = 590,
            isLiked = true,
            coverDrawable = R.drawable.img_hero_cyber
        ),
        VideoReel(
            id = "vid_2",
            creator = currentUser,
            title = "Aeirmist Matrix HUD Interactive Hologram Breakdown",
            audioTrack = "Aeirmist Audio • Solfeggio 528Hz Ambient",
            likesCount = 8940,
            commentsCount = 740,
            sharesCount = 1200,
            isLiked = false,
            coverDrawable = R.drawable.img_app_icon
        ),
        VideoReel(
            id = "vid_3",
            creator = userAura,
            title = "Organic Consciousness meets AI Neural Network Architecture",
            audioTrack = "Aura Sound Lab • Quantum Field",
            likesCount = 3150,
            commentsCount = 198,
            sharesCount = 340,
            isLiked = false,
            coverDrawable = R.drawable.img_avatar_cyber
        )
    )

    private val initialNotifications = listOf(
        NotificationItem(
            id = "notif_1",
            type = NotificationType.RESONANCE,
            actor = userAura,
            title = "Resonance Triggered",
            description = "Aura Neural resonated with your signal 'The new Aeirmist 2.0 native Android architecture is live!' (+25 Resonance)",
            timestamp = "5m ago",
            isRead = false
        ),
        NotificationItem(
            id = "notif_2",
            type = NotificationType.COMMENT,
            actor = userValkyrie,
            title = "New Matrix Comment",
            description = "Valkyrie Ray transmitted: 'Transmitting on pure 120Hz frequency. Massive upgrade!'",
            timestamp = "15m ago",
            isRead = false
        ),
        NotificationItem(
            id = "notif_3",
            type = NotificationType.CONNECTION,
            actor = userNova,
            title = "New Neural Link",
            description = "Nova Vance established a synchronized link with your Matrix ID.",
            timestamp = "2h ago",
            isRead = true
        ),
        NotificationItem(
            id = "notif_4",
            type = NotificationType.SYSTEM,
            actor = null,
            title = "Matrix Tier Promotion",
            description = "Resonance benchmark exceeded: Rank upgraded to Matrix Architect • Tier V.",
            timestamp = "1d ago",
            isRead = true
        )
    )

    private val initialMarketplace = listOf(
        MarketplaceItem(
            id = "mkt_1",
            title = "Holo-Prism Neon Shader Pack",
            description = "Ultra-luminescent real-time UI shaders for matrix profiles and HUD cards.",
            priceCredits = 450,
            category = "Visual Shaders",
            seller = userValkyrie,
            rating = 4.9f,
            imageDrawable = R.drawable.img_hero_cyber
        ),
        MarketplaceItem(
            id = "mkt_2",
            title = "Quantum Neural Encryption Key",
            description = "Hardware-grade RSA cipher token for high-security Vanish rooms.",
            priceCredits = 1200,
            category = "Security",
            seller = userNova,
            rating = 5.0f,
            imageDrawable = R.drawable.img_app_icon
        ),
        MarketplaceItem(
            id = "mkt_3",
            title = "Aeirmist 528Hz Binaural Audio Master",
            description = "Lossless acoustic frequencies engineered for mental clarity and focus.",
            priceCredits = 300,
            category = "Audio Frequencies",
            seller = userAura,
            rating = 4.8f,
            imageDrawable = R.drawable.img_avatar_cyber
        )
    )

    private val initialMetrics = MatrixMetrics(
        resonanceScore = 9420,
        dailyPulse = 840,
        neuralSyncPct = 96,
        networkTier = "Tier V Architect",
        activeSignalsCount = 142,
        totalConnections = 1840
    )

    private val _state = MutableStateFlow(
        AeirmistState(
            currentUser = currentUser,
            posts = initialPosts,
            stories = initialStories,
            conversations = initialConversations,
            videoReels = initialVideos,
            notifications = initialNotifications,
            marketplaceItems = initialMarketplace,
            networkUsers = listOf(userAura, userValkyrie, userNova),
            matrixMetrics = initialMetrics
        )
    )

    val state: StateFlow<AeirmistState> = _state.asStateFlow()

    fun createPost(content: String, tags: List<String>, pollQuestion: String? = null, pollOptions: List<String> = emptyList()) {
        val hasPoll = !pollQuestion.isNullOrBlank() && pollOptions.size >= 2
        val poll = if (hasPoll) {
            PollData(
                question = pollQuestion!!,
                options = pollOptions.filter { it.isNotBlank() }.mapIndexed { idx, opt ->
                    PollOption("opt_${idx + 1}", opt, 0)
                }
            )
        } else null

        val newPost = Post(
            id = "post_${UUID.randomUUID()}",
            author = _state.value.currentUser,
            content = content,
            mediaType = if (hasPoll) PostMediaType.POLL else PostMediaType.NONE,
            timestamp = "Just now",
            resonanceCount = 1,
            commentCount = 0,
            isResonated = true,
            poll = poll,
            tags = tags
        )

        _state.update { curr ->
            curr.copy(
                posts = listOf(newPost) + curr.posts,
                currentUser = curr.currentUser.copy(
                    postsCount = curr.currentUser.postsCount + 1,
                    resonanceScore = curr.currentUser.resonanceScore + 10
                ),
                matrixMetrics = curr.matrixMetrics.copy(
                    resonanceScore = curr.matrixMetrics.resonanceScore + 10,
                    activeSignalsCount = curr.matrixMetrics.activeSignalsCount + 1
                )
            )
        }
    }

    fun toggleResonance(postId: String) {
        _state.update { curr ->
            val updatedPosts = curr.posts.map { post ->
                if (post.id == postId) {
                    val newResonated = !post.isResonated
                    val countDiff = if (newResonated) 1 else -1
                    post.copy(
                        isResonated = newResonated,
                        resonanceCount = maxOf(0, post.resonanceCount + countDiff)
                    )
                } else post
            }
            curr.copy(posts = updatedPosts)
        }
    }

    fun toggleBookmark(postId: String) {
        _state.update { curr ->
            val updatedPosts = curr.posts.map { post ->
                if (post.id == postId) {
                    post.copy(isBookmarked = !post.isBookmarked)
                } else post
            }
            curr.copy(posts = updatedPosts)
        }
    }

    fun votePoll(postId: String, optionId: String) {
        _state.update { curr ->
            val updatedPosts = curr.posts.map { post ->
                if (post.id == postId && post.poll != null && post.poll.userVotedOptionId == null) {
                    val updatedOptions = post.poll.options.map { opt ->
                        if (opt.id == optionId) opt.copy(votes = opt.votes + 1) else opt
                    }
                    post.copy(
                        poll = post.poll.copy(
                            options = updatedOptions,
                            userVotedOptionId = optionId
                        )
                    )
                } else post
            }
            curr.copy(posts = updatedPosts)
        }
    }

    fun addComment(postId: String, text: String) {
        if (text.isBlank()) return
        val newComment = Comment(
            id = "c_${UUID.randomUUID()}",
            author = _state.value.currentUser,
            text = text,
            timestamp = "Just now"
        )
        _state.update { curr ->
            val updatedPosts = curr.posts.map { post ->
                if (post.id == postId) {
                    post.copy(
                        commentCount = post.commentCount + 1,
                        comments = post.comments + newComment
                    )
                } else post
            }
            curr.copy(posts = updatedPosts)
        }
    }

    fun sendMessage(conversationId: String, text: String, isVanish: Boolean = false) {
        if (text.isBlank()) return
        val newMessage = ChatMessage(
            id = "m_${UUID.randomUUID()}",
            senderId = _state.value.currentUser.id,
            text = text,
            timestamp = "Just now",
            isMine = true,
            isVanishMode = isVanish
        )
        _state.update { curr ->
            val updatedConversations = curr.conversations.map { conv ->
                if (conv.id == conversationId) {
                    conv.copy(
                        lastMessage = text,
                        timestamp = "Just now",
                        messages = conv.messages + newMessage
                    )
                } else conv
            }
            curr.copy(conversations = updatedConversations)
        }
    }

    fun selectConversation(conversationId: String?) {
        _state.update { curr ->
            if (conversationId != null) {
                val updatedConversations = curr.conversations.map { conv ->
                    if (conv.id == conversationId) conv.copy(unreadCount = 0) else conv
                }
                curr.copy(activeConversationId = conversationId, conversations = updatedConversations)
            } else {
                curr.copy(activeConversationId = null)
            }
        }
    }

    fun toggleFollowUser(userId: String) {
        _state.update { curr ->
            val updatedNetwork = curr.networkUsers.map { user ->
                if (user.id == userId) {
                    val newFollowing = !user.isFollowing
                    user.copy(
                        isFollowing = newFollowing,
                        connectionsCount = if (newFollowing) user.connectionsCount + 1 else maxOf(0, user.connectionsCount - 1)
                    )
                } else user
            }
            val user = curr.networkUsers.find { it.id == userId }
            val diff = if (user?.isFollowing == true) -1 else 1
            curr.copy(
                networkUsers = updatedNetwork,
                currentUser = curr.currentUser.copy(
                    connectionsCount = maxOf(0, curr.currentUser.connectionsCount + diff)
                )
            )
        }
    }

    fun toggleVideoLike(videoId: String) {
        _state.update { curr ->
            val updatedVideos = curr.videoReels.map { reel ->
                if (reel.id == videoId) {
                    val newLiked = !reel.isLiked
                    val diff = if (newLiked) 1 else -1
                    reel.copy(
                        isLiked = newLiked,
                        likesCount = maxOf(0, reel.likesCount + diff)
                    )
                } else reel
            }
            curr.copy(videoReels = updatedVideos)
        }
    }

    fun markNotificationRead(notifId: String) {
        _state.update { curr ->
            val updated = curr.notifications.map {
                if (it.id == notifId) it.copy(isRead = true) else it
            }
            curr.copy(notifications = updated)
        }
    }

    fun markAllNotificationsRead() {
        _state.update { curr ->
            val updated = curr.notifications.map { it.copy(isRead = true) }
            curr.copy(notifications = updated)
        }
    }

    fun setFeedFilter(filter: String) {
        _state.update { it.copy(activeFeedFilter = filter) }
    }

    fun setSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun viewStory(story: Story?) {
        _state.update { it.copy(activeStory = story) }
    }

    fun updateProfile(displayName: String, bio: String) {
        _state.update { curr ->
            curr.copy(
                currentUser = curr.currentUser.copy(
                    displayName = displayName.ifBlank { curr.currentUser.displayName },
                    bio = bio.ifBlank { curr.currentUser.bio }
                )
            )
        }
    }
}
