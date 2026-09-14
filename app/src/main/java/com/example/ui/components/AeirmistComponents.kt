package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun VerifiedShieldBadge(
    modifier: Modifier = Modifier,
    size: Dp = 15.dp
) {
    Icon(
        imageVector = Icons.Default.Shield,
        contentDescription = "Verified Identity",
        tint = AeirmistCyan,
        modifier = modifier.size(size)
    )
}

@Composable
fun AeirmistAvatar(
    drawableRes: Int,
    size: Dp = 42.dp,
    hasStoryRing: Boolean = false,
    isOnline: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .size(size)
            .then(
                if (onClick != null) Modifier.clickable { onClick() } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        val ringModifier = if (hasStoryRing) {
            Modifier
                .size(size)
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(AeirmistCyan, AeirmistMagenta, AeirmistLime)
                    ),
                    shape = CircleShape
                )
                .padding(3.dp)
        } else {
            Modifier
                .size(size)
                .border(
                    width = 1.dp,
                    color = AeirmistBorder,
                    shape = CircleShape
                )
        }

        Box(
            modifier = ringModifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(AeirmistSurfaceElevated)
        ) {
            androidx.compose.foundation.Image(
                painter = painterResource(id = drawableRes),
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        if (isOnline) {
            Box(
                modifier = Modifier
                    .size(size * 0.28f)
                    .align(Alignment.BottomEnd)
                    .background(AeirmistLime, CircleShape)
                    .border(1.5.dp, AeirmistBg, CircleShape)
            )
        }
    }
}

@Composable
fun AeirmistCard(
    modifier: Modifier = Modifier,
    borderBrush: Brush = Brush.linearGradient(
        colors = listOf(AeirmistBorderActive.copy(alpha = 0.4f), AeirmistBorderSubtle)
    ),
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(
                BorderStroke(1.dp, borderBrush),
                RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        color = AeirmistSurfaceCard.copy(alpha = 0.92f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun ResonancePill(
    score: Int,
    modifier: Modifier = Modifier,
    label: String = "RESONANCE"
) {
    Row(
        modifier = modifier
            .background(
                Brush.horizontalGradient(
                    listOf(AeirmistCyan.copy(alpha = 0.15f), AeirmistMagenta.copy(alpha = 0.12f))
                ),
                RoundedCornerShape(12.dp)
            )
            .border(
                BorderStroke(1.dp, AeirmistCyan.copy(alpha = 0.35f)),
                RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "⚡",
            fontSize = 10.sp
        )
        Text(
            text = "$score $label",
            color = AeirmistCyan,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 0.8.sp
        )
    }
}

@Composable
fun CyberChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = if (isSelected) AeirmistCyan.copy(alpha = 0.18f) else AeirmistSurfaceElevated
    val border = if (isSelected) AeirmistCyan else AeirmistBorderSubtle
    val textClr = if (isSelected) AeirmistCyan else AeirmistTextSecondary

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .border(1.dp, border, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textClr,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp
        )
    }
}
