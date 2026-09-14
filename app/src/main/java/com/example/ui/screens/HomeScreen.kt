package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PollData
import com.example.data.model.Post
import com.example.data.model.PostMediaType
import com.example.data.model.Story
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.AeirmistViewModel

@Composable
fun HomeScreen(
    viewModel: AeirmistViewModel,
    onOpenNotifications: () -> Unit,
    onOpenMessenger: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val unreadNotifs = state.notifications.count { !it.isRead }
    val unreadMessages = state.conversations.sumOf { it.unreadCount }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AeirmistBg)
    ) {
        // Top App Bar
        HomeTopBar(
            unreadNotifs = unreadNotifs,
            unreadMessages = unreadMessages,
            onOpenNotifications = onOpenNotifications,
            onOpenMessenger = onOpenMessenger
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // Stories Strip
            item {
                StoriesSection(
                    stories = state.stories,
                    onStoryClick = { viewModel.viewStory(it) }
                )
            }

            // Feed Filter Chips
            item {
                FeedFilterChips(
                    activeFilter = state.activeFeedFilter,
                    onSelectFilter = { viewModel.setFeedFilter(it) }
                )
            }

            // Filtered Posts
            val filteredPosts = when (state.activeFeedFilter) {
                "TRENDING" -> state.posts.sortedByDescending { it.resonanceCount }
                "NEURAL" -> state.posts.filter { it.poll != null }
                "SAVED" -> state.posts.filter { it.isBookmarked }
                else -> state.posts
            }

            items(filteredPosts, key = { it.id }) { post ->
                PostCard(
                    post = post,
                    onToggleResonance = { viewModel.toggleResonance(post.id) },
                    onToggleBookmark = { viewModel.toggleBookmark(post.id) },
                    onVotePoll = { optId -> viewModel.votePoll(post.id, optId) },
                    onAddComment = { commentText -> viewModel.addComment(post.id, commentText) }
                )
            }
        }
    }
}

@Composable
private fun HomeTopBar(
    unreadNotifs: Int,
    unreadMessages: Int,
    onOpenNotifications: () -> Unit,
    onOpenMessenger: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.sweepGradient(
                            listOf(AeirmistCyan, AeirmistMagenta, AeirmistLime, AeirmistCyan)
                        )
                    )
                    .padding(1.5.dp)
                    .clip(CircleShape)
                    .background(AeirmistBg),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Æ",
                    color = AeirmistCyan,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Column {
                Text(
                    text = "AEIRMIST",
                    color = AeirmistTextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "NEURAL RESONANCE MATRIX",
                    color = AeirmistCyan,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.2.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onOpenNotifications,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AeirmistSurfaceElevated)
                    .border(1.dp, AeirmistBorderSubtle, CircleShape)
                    .testTag("home_alerts_button")
            ) {
                BadgedBox(
                    badge = {
                        if (unreadNotifs > 0) {
                            Badge(
                                containerColor = AeirmistCyan,
                                contentColor = AeirmistBg
                            ) {
                                Text(unreadNotifs.toString(), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Alerts",
                        tint = AeirmistTextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            IconButton(
                onClick = onOpenMessenger,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AeirmistSurfaceElevated)
                    .border(1.dp, AeirmistBorderSubtle, CircleShape)
                    .testTag("home_messages_button")
            ) {
                BadgedBox(
                    badge = {
                        if (unreadMessages > 0) {
                            Badge(
                                containerColor = AeirmistMagenta,
                                contentColor = AeirmistBg
                            ) {
                                Text(unreadMessages.toString(), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = "Messenger",
                        tint = AeirmistTextPrimary,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun StoriesSection(
    stories: List<Story>,
    onStoryClick: (Story) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.clickable { }
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(AeirmistSurfaceElevated)
                            .border(1.dp, AeirmistCyan.copy(alpha = 0.6f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Story",
                            tint = AeirmistCyan,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Text(
                        text = "Your Signal",
                        color = AeirmistTextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            items(stories) { story ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.clickable { onStoryClick(story) }
                ) {
                    AeirmistAvatar(
                        drawableRes = story.user.avatarDrawable,
                        size = 56.dp,
                        hasStoryRing = story.hasUnseen,
                        isOnline = story.user.isFollowing
                    )
                    Text(
                        text = story.user.displayName.split(" ").firstOrNull() ?: story.user.username,
                        color = AeirmistTextPrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun FeedFilterChips(
    activeFilter: String,
    onSelectFilter: (String) -> Unit
) {
    val filters = listOf("ALL", "TRENDING", "NEURAL", "SAVED")
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        filters.forEach { filter ->
            CyberChip(
                text = filter,
                isSelected = activeFilter == filter,
                onClick = { onSelectFilter(filter) }
            )
        }
    }
}

@Composable
fun PostCard(
    post: Post,
    onToggleResonance: () -> Unit,
    onToggleBookmark: () -> Unit,
    onVotePoll: (String) -> Unit,
    onAddComment: (String) -> Unit
) {
    var isCommentsOpen by remember { mutableStateOf(false) }
    var commentInput by remember { mutableStateOf("") }

    AeirmistCard(
        modifier = Modifier.testTag("post_card_${post.id}")
    ) {
        // Author Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AeirmistAvatar(
                    drawableRes = post.author.avatarDrawable,
                    size = 40.dp
                )

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = post.author.displayName,
                            color = AeirmistTextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        // ALWAYS display ShieldCheck icon for verified identities (Per AGENTS.md rule!)
                        if (post.author.isVerified) {
                            VerifiedShieldBadge()
                        }
                    }
                    Text(
                        text = "@${post.author.username} • ${post.timestamp}",
                        color = AeirmistTextMuted,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            ResonancePill(
                score = post.author.resonanceScore,
                label = "NODE"
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Content Text
        Text(
            text = post.content,
            color = AeirmistTextPrimary,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.2.sp
        )

        // Tags
        if (post.tags.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                post.tags.take(3).forEach { tag ->
                    Text(
                        text = "#$tag",
                        color = AeirmistCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // Media Content
        if (post.mediaType == PostMediaType.HERO_IMAGE && post.mediaDrawable != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, AeirmistBorderSubtle, RoundedCornerShape(14.dp))
            ) {
                androidx.compose.foundation.Image(
                    painter = painterResource(id = post.mediaDrawable),
                    contentDescription = "Post Media",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else if (post.mediaType == PostMediaType.POLL && post.poll != null) {
            Spacer(modifier = Modifier.height(12.dp))
            PollView(
                poll = post.poll,
                onVote = onVotePoll
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Action Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Resonance / Upvote Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onToggleResonance() }
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                    .testTag("resonance_btn_${post.id}")
            ) {
                Icon(
                    imageVector = if (post.isResonated) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Resonate",
                    tint = if (post.isResonated) AeirmistMagenta else AeirmistTextSecondary,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "${post.resonanceCount}",
                    color = if (post.isResonated) AeirmistMagenta else AeirmistTextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            // Comments Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { isCommentsOpen = !isCommentsOpen }
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                    .testTag("comments_btn_${post.id}")
            ) {
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = "Comments",
                    tint = AeirmistTextSecondary,
                    modifier = Modifier.size(17.dp)
                )
                Text(
                    text = "${post.commentCount}",
                    color = AeirmistTextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            // Share Button
            IconButton(
                onClick = { },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = "Share",
                    tint = AeirmistTextSecondary,
                    modifier = Modifier.size(17.dp)
                )
            }

            // Bookmark Button
            IconButton(
                onClick = onToggleBookmark,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = if (post.isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint = if (post.isBookmarked) AeirmistCyan else AeirmistTextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Expanded Comments Section
        AnimatedVisibility(visible = isCommentsOpen) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .background(AeirmistSurfaceElevated.copy(alpha = 0.7f), RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "TRANSMISSIONS (${post.comments.size})",
                    color = AeirmistCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    fontFamily = FontFamily.Monospace
                )

                post.comments.forEach { comment ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AeirmistAvatar(
                            drawableRes = comment.author.avatarDrawable,
                            size = 28.dp
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = comment.author.displayName,
                                    color = AeirmistTextPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                if (comment.author.isVerified) {
                                    VerifiedShieldBadge(size = 12.dp)
                                }
                                Text(
                                    text = "• ${comment.timestamp}",
                                    color = AeirmistTextMuted,
                                    fontSize = 9.sp
                                )
                            }
                            Text(
                                text = comment.text,
                                color = AeirmistTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                // Add comment row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextField(
                        value = commentInput,
                        onValueChange = { commentInput = it },
                        placeholder = { Text("Transmit frequency...", fontSize = 11.sp, color = AeirmistTextMuted) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = AeirmistSurface,
                            unfocusedContainerColor = AeirmistSurface,
                            focusedIndicatorColor = AeirmistCyan,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = AeirmistTextPrimary,
                            unfocusedTextColor = AeirmistTextPrimary
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp)
                    )

                    IconButton(
                        onClick = {
                            if (commentInput.isNotBlank()) {
                                onAddComment(commentInput)
                                commentInput = ""
                            }
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(AeirmistCyan)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send Comment",
                            tint = AeirmistBg,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PollView(
    poll: PollData,
    onVote: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(AeirmistSurfaceElevated.copy(alpha = 0.8f))
            .border(1.dp, AeirmistBorderSubtle, RoundedCornerShape(14.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = poll.question,
            color = AeirmistTextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        poll.options.forEach { option ->
            val isVoted = poll.userVotedOptionId == option.id
            val hasVoted = poll.userVotedOptionId != null
            val pct = if (poll.totalVotes > 0) (option.votes.toFloat() / poll.totalVotes) else 0f

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(AeirmistSurfaceCard)
                    .border(
                        1.dp,
                        if (isVoted) AeirmistCyan else AeirmistBorderSubtle,
                        RoundedCornerShape(10.dp)
                    )
                    .clickable(enabled = !hasVoted) { onVote(option.id) }
            ) {
                // Percentage Progress Bar
                if (hasVoted) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(pct)
                            .background(
                                if (isVoted) AeirmistCyan.copy(alpha = 0.25f) else AeirmistSurfaceElevated
                            )
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = option.text,
                        color = if (isVoted) AeirmistCyan else AeirmistTextPrimary,
                        fontSize = 12.sp,
                        fontWeight = if (isVoted) FontWeight.Bold else FontWeight.Medium
                    )

                    if (hasVoted) {
                        Text(
                            text = "${(pct * 100).toInt()}% (${option.votes})",
                            color = if (isVoted) AeirmistCyan else AeirmistTextMuted,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Text(
            text = "${poll.totalVotes} neural votes cast",
            color = AeirmistTextMuted,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}
