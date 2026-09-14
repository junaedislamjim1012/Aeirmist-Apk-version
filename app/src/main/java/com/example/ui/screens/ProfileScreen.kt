package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
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
import com.example.R
import com.example.ui.components.AeirmistAvatar
import com.example.ui.components.AeirmistCard
import com.example.ui.components.CyberChip
import com.example.ui.components.VerifiedShieldBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.AeirmistViewModel

@Composable
fun ProfileScreen(
    viewModel: AeirmistViewModel,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val user = state.currentUser
    var isEditDialogOpen by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf("POSTS") }

    val userPosts = state.posts.filter { it.author.id == user.id }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AeirmistBg),
        contentPadding = PaddingValues(bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Banner & Avatar Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                androidx.compose.foundation.Image(
                    painter = painterResource(id = R.drawable.img_hero_cyber),
                    contentDescription = "Profile Cover",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, AeirmistBg)
                            )
                        )
                )

                // Settings icon in top right
                IconButton(
                    onClick = onOpenSettings,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .statusBarsPadding()
                        .padding(12.dp)
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(AeirmistSurfaceElevated.copy(alpha = 0.8f))
                        .border(1.dp, AeirmistBorderSubtle, CircleShape)
                        .testTag("profile_settings_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = AeirmistTextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Profile Details Card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
                    .offset(y = (-36).dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    AeirmistAvatar(
                        drawableRes = user.avatarDrawable,
                        size = 76.dp,
                        hasStoryRing = true,
                        isOnline = true
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { isEditDialogOpen = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AeirmistSurfaceElevated,
                                contentColor = AeirmistTextPrimary
                            ),
                            border = BorderStroke(1.dp, AeirmistBorder),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("EDIT ID", fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        }

                        IconButton(
                            onClick = { },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(AeirmistSurfaceElevated)
                                .border(1.dp, AeirmistBorder, RoundedCornerShape(12.dp))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share ID",
                                tint = AeirmistTextPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Display Name + Verified Shield Check Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = user.displayName,
                        color = AeirmistTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    // Always use ShieldCheck icon when verified (Mandatory user rule in AGENTS.md!)
                    if (user.isVerified) {
                        VerifiedShieldBadge(size = 18.dp)
                    }
                }

                Text(
                    text = "@${user.username}",
                    color = AeirmistCyan,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Rank Title Capsule
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(AeirmistSurfaceElevated)
                        .border(1.dp, AeirmistBorderSubtle, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = "👑", fontSize = 11.sp)
                    Text(
                        text = user.rankTitle,
                        color = AeirmistLime,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = user.bio,
                    color = AeirmistTextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Stats Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(AeirmistSurfaceCard)
                        .border(1.dp, AeirmistBorderSubtle, RoundedCornerShape(14.dp))
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ProfileStatItem(label = "POSTS", count = "${user.postsCount}")
                    ProfileStatItem(label = "CONNECTIONS", count = "${user.connectionsCount}")
                    ProfileStatItem(label = "RESONANCE", count = "${user.resonanceScore}", isAccent = true)
                }
            }
        }

        // Tab Row: Posts / Quarts / Vault
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
                    .offset(y = (-20).dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("POSTS", "SIGNALS", "VAULT 🔒").forEach { tab ->
                    CyberChip(
                        text = tab,
                        isSelected = selectedTab == tab,
                        onClick = { selectedTab = tab }
                    )
                }
            }
        }

        // Posts List under profile
        if (selectedTab == "POSTS") {
            items(userPosts, key = { it.id }) { post ->
                Box(modifier = Modifier.padding(horizontal = 14.dp)) {
                    PostCard(
                        post = post,
                        onToggleResonance = { viewModel.toggleResonance(post.id) },
                        onToggleBookmark = { viewModel.toggleBookmark(post.id) },
                        onVotePoll = { optId -> viewModel.votePoll(post.id, optId) },
                        onAddComment = { commentText -> viewModel.addComment(post.id, commentText) }
                    )
                }
            }
        } else if (selectedTab == "VAULT 🔒") {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Encrypted Vault",
                        tint = AeirmistCyan,
                        modifier = Modifier.size(36.dp)
                    )
                    Text(
                        text = "ENCRYPTED VAULT",
                        color = AeirmistTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "Private transmissions, ephemeral stories, and stored keys are locked with hardware keystore.",
                        color = AeirmistTextMuted,
                        fontSize = 11.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }

    // Edit Profile Modal
    if (isEditDialogOpen) {
        var editName by remember { mutableStateOf(user.displayName) }
        var editBio by remember { mutableStateOf(user.bio) }

        AlertDialog(
            onDismissRequest = { isEditDialogOpen = false },
            containerColor = AeirmistSurfaceCard,
            title = {
                Text(
                    text = "EDIT MATRIX IDENTITY",
                    color = AeirmistCyan,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    TextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Display Name", color = AeirmistTextMuted) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = AeirmistSurfaceElevated,
                            unfocusedContainerColor = AeirmistSurfaceElevated,
                            focusedTextColor = AeirmistTextPrimary,
                            unfocusedTextColor = AeirmistTextPrimary,
                            focusedIndicatorColor = AeirmistCyan
                        )
                    )

                    TextField(
                        value = editBio,
                        onValueChange = { editBio = it },
                        label = { Text("Bio", color = AeirmistTextMuted) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = AeirmistSurfaceElevated,
                            unfocusedContainerColor = AeirmistSurfaceElevated,
                            focusedTextColor = AeirmistTextPrimary,
                            unfocusedTextColor = AeirmistTextPrimary,
                            focusedIndicatorColor = AeirmistCyan
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateProfile(editName, editBio)
                        isEditDialogOpen = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AeirmistCyan, contentColor = AeirmistBg)
                ) {
                    Text("SAVE", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { isEditDialogOpen = false }) {
                    Text("CANCEL", color = AeirmistTextMuted, fontFamily = FontFamily.Monospace)
                }
            }
        )
    }
}

@Composable
private fun ProfileStatItem(
    label: String,
    count: String,
    isAccent: Boolean = false
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            color = if (isAccent) AeirmistCyan else AeirmistTextPrimary,
            fontSize = 17.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text = label,
            color = AeirmistTextMuted,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}
