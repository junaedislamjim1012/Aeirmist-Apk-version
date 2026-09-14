package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AeirmistCard
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var neonGlowEnabled by remember { mutableStateOf(true) }
    var vanishAutoDelete by remember { mutableStateOf(true) }
    var pushNotificationsEnabled by remember { mutableStateOf(true) }
    var hapticFeedbackEnabled by remember { mutableStateOf(true) }

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
                    text = "SYSTEM PROTOCOLS",
                    color = AeirmistCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "Settings & Security",
                    color = AeirmistTextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // Security Score Card
            item {
                AeirmistCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "NODE SECURITY SCORE",
                                color = AeirmistTextSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "98% ENCRYPTED",
                                color = AeirmistLime,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = "Verified Security",
                            tint = AeirmistLime,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Hardware KeyStore active • End-to-end Vanish crypto verified • Peer node handshake established.",
                        color = AeirmistTextMuted,
                        fontSize = 11.sp
                    )
                }
            }

            // Appearance & Sensory Sector
            item {
                SectionHeader("APPEARANCE & SENSORY")
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(AeirmistSurfaceCard)
                        .border(1.dp, AeirmistBorderSubtle, RoundedCornerShape(16.dp))
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SettingToggleRow(
                        icon = Icons.Default.BrightnessMedium,
                        title = "Neon Glow Resonance",
                        subtitle = "High-luminescence cyber borders & aura lighting",
                        checked = neonGlowEnabled,
                        onCheckedChange = { neonGlowEnabled = it }
                    )
                    HorizontalDivider(color = AeirmistBorderSubtle)
                    SettingToggleRow(
                        icon = Icons.Default.Vibration,
                        title = "Haptic Frequency Feedback",
                        subtitle = "Tactile impulse on resonance upvotes and messages",
                        checked = hapticFeedbackEnabled,
                        onCheckedChange = { hapticFeedbackEnabled = it }
                    )
                }
            }

            // Privacy & Transmissions Sector
            item {
                SectionHeader("PRIVACY & TRANSMISSIONS")
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(AeirmistSurfaceCard)
                        .border(1.dp, AeirmistBorderSubtle, RoundedCornerShape(16.dp))
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SettingToggleRow(
                        icon = Icons.Default.VisibilityOff,
                        title = "Auto-Purge Vanish Messages",
                        subtitle = "Destroy ephemeral signal packets after recipient opens",
                        checked = vanishAutoDelete,
                        onCheckedChange = { vanishAutoDelete = it }
                    )
                    HorizontalDivider(color = AeirmistBorderSubtle)
                    SettingToggleRow(
                        icon = Icons.Default.Notifications,
                        title = "Neural Link Alerts",
                        subtitle = "Receive notifications when other nodes resonate",
                        checked = pushNotificationsEnabled,
                        onCheckedChange = { pushNotificationsEnabled = it }
                    )
                }
            }

            // Platform Node Sync Details
            item {
                SectionHeader("FIREBASE & CLOUD LINK")
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(AeirmistSurfaceCard)
                        .border(1.dp, AeirmistBorderSubtle, RoundedCornerShape(16.dp))
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SettingInfoRow(label = "PROJECT ID", value = "aeirmist-d4dd8")
                    SettingInfoRow(label = "AUTH DOMAIN", value = "aeirmist-d4dd8.firebaseapp.com")
                    SettingInfoRow(label = "STORAGE BUCKET", value = "aeirmist-d4dd8.firebasestorage.app")
                    SettingInfoRow(label = "PLATFORM VERSION", value = "Aeirmist 2.0 Native Android (Jetpack Compose)")
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        color = AeirmistCyan,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp,
        fontFamily = FontFamily.Monospace,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
private fun SettingToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
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
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = AeirmistCyan,
                modifier = Modifier.size(22.dp)
            )
            Column {
                Text(
                    text = title,
                    color = AeirmistTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    color = AeirmistTextMuted,
                    fontSize = 11.sp
                )
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = AeirmistBg,
                checkedTrackColor = AeirmistCyan,
                uncheckedThumbColor = AeirmistTextMuted,
                uncheckedTrackColor = AeirmistSurfaceElevated
            )
        )
    }
}

@Composable
private fun SettingInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = AeirmistTextMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text = value,
            color = AeirmistTextSecondary,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}
