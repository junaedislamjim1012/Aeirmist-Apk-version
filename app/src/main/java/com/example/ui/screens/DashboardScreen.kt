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
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.data.model.User
import com.example.ui.components.AeirmistAvatar
import com.example.ui.components.AeirmistCard
import com.example.ui.components.VerifiedShieldBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.AeirmistViewModel

@Composable
fun DashboardScreen(
    viewModel: AeirmistViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val metrics = state.matrixMetrics

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AeirmistBg)
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp)
    ) {
        // Screen Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                Text(
                    text = "NEURAL MATRIX",
                    color = AeirmistCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "Connections & Resonance",
                    color = AeirmistTextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        // Main Resonance HUD Card
        item {
            AeirmistCard(
                borderBrush = Brush.linearGradient(
                    listOf(AeirmistCyan, AeirmistMagenta, AeirmistLime)
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ACTIVE RESONANCE SCORE",
                            color = AeirmistTextSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "${metrics.resonanceScore}",
                            color = AeirmistTextPrimary,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(AeirmistCyan.copy(alpha = 0.3f), Color.Transparent)
                                )
                            )
                            .border(1.5.dp, AeirmistCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ElectricBolt,
                            contentDescription = "Resonance Spark",
                            tint = AeirmistCyan,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 3-Metric Telemetry Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    HUDMetricItem(
                        label = "DAILY PULSE",
                        value = "+${metrics.dailyPulse}",
                        accentColor = AeirmistLime
                    )
                    HUDMetricItem(
                        label = "NEURAL SYNC",
                        value = "${metrics.neuralSyncPct}%",
                        accentColor = AeirmistCyan
                    )
                    HUDMetricItem(
                        label = "NETWORK TIER",
                        value = metrics.networkTier.split(" ").lastOrNull() ?: "V",
                        accentColor = AeirmistMagenta
                    )
                }
            }
        }

        // Section Title: Network Nodes
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SYNCHRONIZED NODES",
                    color = AeirmistCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "${state.networkUsers.size} Available",
                    color = AeirmistTextMuted,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        // Connected Network Users List
        items(state.networkUsers, key = { it.id }) { user ->
            ConnectionNodeCard(
                user = user,
                onToggleFollow = { viewModel.toggleFollowUser(user.id) }
            )
        }
    }
}

@Composable
private fun HUDMetricItem(
    label: String,
    value: String,
    accentColor: Color
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(AeirmistSurfaceElevated.copy(alpha = 0.8f))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = AeirmistTextMuted,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text = value,
            color = accentColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
private fun ConnectionNodeCard(
    user: User,
    onToggleFollow: () -> Unit
) {
    AeirmistCard(
        modifier = Modifier.testTag("connection_card_${user.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                AeirmistAvatar(
                    drawableRes = user.avatarDrawable,
                    size = 46.dp,
                    isOnline = true
                )

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = user.displayName,
                            color = AeirmistTextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        // Always use ShieldCheck icon when verified (per AGENTS.md rule!)
                        if (user.isVerified) {
                            VerifiedShieldBadge()
                        }
                    }
                    Text(
                        text = "@${user.username}",
                        color = AeirmistCyan,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = user.rankTitle,
                        color = AeirmistTextMuted,
                        fontSize = 10.sp
                    )
                }
            }

            // Follow / Connect Action
            Button(
                onClick = onToggleFollow,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (user.isFollowing) AeirmistSurfaceElevated else AeirmistCyan,
                    contentColor = if (user.isFollowing) AeirmistTextSecondary else AeirmistBg
                ),
                shape = RoundedCornerShape(10.dp),
                border = if (user.isFollowing) BorderStroke(1.dp, AeirmistBorderSubtle) else null,
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Icon(
                    imageVector = if (user.isFollowing) Icons.Default.PersonRemove else Icons.Default.PersonAdd,
                    contentDescription = if (user.isFollowing) "Unlink" else "Link",
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (user.isFollowing) "LINKED" else "SYNC",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
