package com.example.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.data.model.Conversation
import com.example.ui.components.AeirmistAvatar
import com.example.ui.components.AeirmistCard
import com.example.ui.components.VerifiedShieldBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.AeirmistViewModel

@Composable
fun MessengerScreen(
    viewModel: AeirmistViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val activeConv = state.conversations.find { it.id == state.activeConversationId }

    if (activeConv != null) {
        ChatDetailScreen(
            conversation = activeConv,
            onBack = { viewModel.selectConversation(null) },
            onSendMessage = { text, isVanish -> viewModel.sendMessage(activeConv.id, text, isVanish) }
        )
    } else {
        ConversationListScreen(
            conversations = state.conversations,
            onSelectConversation = { viewModel.selectConversation(it.id) },
            modifier = modifier
        )
    }
}

@Composable
private fun ConversationListScreen(
    conversations: List<Conversation>,
    onSelectConversation: (Conversation) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AeirmistBg)
            .padding(horizontal = 14.dp)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(vertical = 12.dp)
        ) {
            Text(
                text = "NEURAL INBOX",
                color = AeirmistCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "Encrypted Transmissions",
                color = AeirmistTextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        // Search Bar
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search encrypted frequencies...", color = AeirmistTextMuted, fontSize = 13.sp) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Search",
                    tint = AeirmistTextMuted
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = AeirmistSurfaceElevated,
                unfocusedContainerColor = AeirmistSurfaceElevated,
                focusedIndicatorColor = AeirmistCyan,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = AeirmistTextPrimary,
                unfocusedTextColor = AeirmistTextPrimary
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp)
        )

        // Active Statuses / Notes Strip
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            items(conversations) { conv ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.clickable { onSelectConversation(conv) }
                ) {
                    AeirmistAvatar(
                        drawableRes = conv.participant.avatarDrawable,
                        size = 50.dp,
                        isOnline = conv.isOnline,
                        hasStoryRing = conv.unreadCount > 0
                    )
                    Text(
                        text = conv.participant.displayName.split(" ").firstOrNull() ?: "",
                        color = AeirmistTextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Conversation List
        val filtered = if (searchQuery.isBlank()) conversations else conversations.filter {
            it.participant.displayName.contains(searchQuery, ignoreCase = true) ||
            it.participant.username.contains(searchQuery, ignoreCase = true)
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            items(filtered, key = { it.id }) { conv ->
                ConversationItemCard(
                    conversation = conv,
                    onClick = { onSelectConversation(conv) }
                )
            }
        }
    }
}

@Composable
private fun ConversationItemCard(
    conversation: Conversation,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AeirmistSurfaceCard)
            .border(1.dp, AeirmistBorderSubtle, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(14.dp)
            .testTag("conv_item_${conversation.id}"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AeirmistAvatar(
            drawableRes = conversation.participant.avatarDrawable,
            size = 46.dp,
            isOnline = conversation.isOnline
        )

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = conversation.participant.displayName,
                        color = AeirmistTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (conversation.participant.isVerified) {
                        VerifiedShieldBadge()
                    }
                }
                Text(
                    text = conversation.timestamp,
                    color = AeirmistTextMuted,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = conversation.lastMessage,
                color = if (conversation.unreadCount > 0) AeirmistTextPrimary else AeirmistTextSecondary,
                fontSize = 12.sp,
                fontWeight = if (conversation.unreadCount > 0) FontWeight.SemiBold else FontWeight.Normal,
                maxLines = 1
            )
        }

        if (conversation.unreadCount > 0) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(AeirmistCyan),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${conversation.unreadCount}",
                    color = AeirmistBg,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

@Composable
fun ChatDetailScreen(
    conversation: Conversation,
    onBack: () -> Unit,
    onSendMessage: (String, Boolean) -> Unit
) {
    var messageText by remember { mutableStateOf("") }
    var isVanishMode by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isVanishMode) Color(0xFF030308) else AeirmistBg)
    ) {
        // Chat Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .background(AeirmistSurfaceGlass)
                .border(1.dp, AeirmistBorderSubtle)
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = AeirmistTextPrimary
                    )
                }

                AeirmistAvatar(
                    drawableRes = conversation.participant.avatarDrawable,
                    size = 38.dp,
                    isOnline = conversation.isOnline
                )

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = conversation.participant.displayName,
                            color = AeirmistTextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (conversation.participant.isVerified) {
                            VerifiedShieldBadge(size = 13.dp)
                        }
                    }
                    Text(
                        text = if (isVanishMode) "Vanish Mode Active 🔒" else "@${conversation.participant.username}",
                        color = if (isVanishMode) AeirmistMagenta else AeirmistCyan,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            // Vanish Mode Toggle
            IconButton(
                onClick = { isVanishMode = !isVanishMode },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (isVanishMode) AeirmistMagenta.copy(alpha = 0.2f) else AeirmistSurfaceElevated)
            ) {
                Icon(
                    imageVector = Icons.Outlined.VisibilityOff,
                    contentDescription = "Toggle Vanish Mode",
                    tint = if (isVanishMode) AeirmistMagenta else AeirmistTextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Messages Thread
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 14.dp)
        ) {
            items(conversation.messages, key = { it.id }) { msg ->
                ChatMessageBubble(msg = msg)
            }
        }

        // Transmission Input Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .background(AeirmistSurfaceCard)
                .border(1.dp, AeirmistBorderSubtle)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = messageText,
                onValueChange = { messageText = it },
                placeholder = {
                    Text(
                        text = if (isVanishMode) "Transmit ephemeral signal..." else "Send encrypted transmission...",
                        color = AeirmistTextMuted,
                        fontSize = 12.sp
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = AeirmistSurface,
                    unfocusedContainerColor = AeirmistSurface,
                    focusedIndicatorColor = if (isVanishMode) AeirmistMagenta else AeirmistCyan,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = AeirmistTextPrimary,
                    unfocusedTextColor = AeirmistTextPrimary
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = {
                    if (messageText.isNotBlank()) {
                        onSendMessage(messageText, isVanishMode)
                        messageText = ""
                    }
                },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (isVanishMode) AeirmistMagenta else AeirmistCyan)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = AeirmistBg,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun ChatMessageBubble(msg: ChatMessage) {
    val alignment = if (msg.isMine) Alignment.End else Alignment.Start
    val bg = if (msg.isMine) {
        if (msg.isVanishMode) AeirmistMagenta.copy(alpha = 0.35f) else AeirmistCyan.copy(alpha = 0.25f)
    } else AeirmistSurfaceElevated

    val border = if (msg.isMine) {
        if (msg.isVanishMode) AeirmistMagenta.copy(alpha = 0.7f) else AeirmistCyan.copy(alpha = 0.6f)
    } else AeirmistBorderSubtle

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(bg)
                .border(1.dp, border, RoundedCornerShape(16.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = msg.text,
                    color = AeirmistTextPrimary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
                Text(
                    text = msg.timestamp + (if (msg.isVanishMode) " • Vanish" else ""),
                    color = AeirmistTextMuted,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}
