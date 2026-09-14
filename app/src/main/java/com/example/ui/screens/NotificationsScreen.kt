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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NotificationItem
import com.example.data.model.NotificationType
import com.example.ui.components.AeirmistAvatar
import com.example.ui.components.AeirmistCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.AeirmistViewModel

@Composable
fun NotificationsScreen(
    viewModel: AeirmistViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AeirmistBg)
            .padding(horizontal = 14.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(vertical = 12.dp),
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

                Column {
                    Text(
                        text = "NOTIFICATIONS",
                        color = AeirmistCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "Matrix Alerts",
                        color = AeirmistTextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            TextButton(
                onClick = { viewModel.markAllNotificationsRead() },
                colors = ButtonDefaults.textButtonColors(contentColor = AeirmistCyan)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Mark Read",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "MARK ALL READ",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            items(state.notifications, key = { it.id }) { notif ->
                NotificationRowCard(
                    notif = notif,
                    onClick = { viewModel.markNotificationRead(notif.id) }
                )
            }
        }
    }
}

@Composable
private fun NotificationRowCard(
    notif: NotificationItem,
    onClick: () -> Unit
) {
    val iconVector = when (notif.type) {
        NotificationType.RESONANCE -> Icons.Default.ElectricBolt
        NotificationType.COMMENT -> Icons.Outlined.ChatBubbleOutline
        NotificationType.CONNECTION -> Icons.Default.PersonAdd
        NotificationType.SYSTEM -> Icons.Default.Security
    }

    val iconTint = when (notif.type) {
        NotificationType.RESONANCE -> AeirmistMagenta
        NotificationType.COMMENT -> AeirmistCyan
        NotificationType.CONNECTION -> AeirmistLime
        NotificationType.SYSTEM -> AeirmistPurple
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (notif.isRead) AeirmistSurfaceCard else AeirmistSurfaceElevated)
            .border(
                1.dp,
                if (notif.isRead) AeirmistBorderSubtle else AeirmistCyan.copy(alpha = 0.5f),
                RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(14.dp)
            .testTag("notif_row_${notif.id}"),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconTint.copy(alpha = 0.15f))
                .border(1.dp, iconTint.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = iconVector,
                contentDescription = notif.title,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = notif.title,
                    color = AeirmistTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = notif.timestamp,
                    color = AeirmistTextMuted,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = notif.description,
                color = AeirmistTextSecondary,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }

        if (!notif.isRead) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(AeirmistCyan)
            )
        }
    }
}
