package com.example.ui.screens

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
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ShoppingBag
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
import com.example.data.model.MarketplaceItem
import com.example.ui.components.AeirmistCard
import com.example.ui.components.CyberChip
import com.example.ui.components.VerifiedShieldBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.AeirmistViewModel

@Composable
fun DiscoverScreen(
    viewModel: AeirmistViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("ALL") }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    val trendingTags = listOf(
        "#AeirmistMatrix",
        "#CyberPulse",
        "#NeuralArchitecture",
        "#QuantumSecurity",
        "#SynthFrequency",
        "#VanishMode"
    )

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
                text = "DISCOVER MATRIX",
                color = AeirmistCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "Frequencies & Marketplace",
                color = AeirmistTextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        // Search Input
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search nodes, assets, frequencies...", color = AeirmistTextMuted, fontSize = 13.sp) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = AeirmistCyan
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
                .padding(bottom = 12.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // Trending Tags Section
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "TRENDING FREQUENCIES",
                        color = AeirmistCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(trendingTags) { tag ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(AeirmistSurfaceCard)
                                    .border(1.dp, AeirmistBorderSubtle, RoundedCornerShape(10.dp))
                                    .clickable { searchQuery = tag }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = tag,
                                    color = AeirmistTextPrimary,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // Marketplace Section Header & Filter
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ShoppingBag,
                                contentDescription = "Marketplace",
                                tint = AeirmistLime,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "DIGITAL SANCTUARY ASSETS",
                                color = AeirmistLime,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    // Categories
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("ALL", "Visual Shaders", "Security", "Audio Frequencies").forEach { cat ->
                            CyberChip(
                                text = cat,
                                isSelected = selectedCategory == cat,
                                onClick = { selectedCategory = cat }
                            )
                        }
                    }
                }
            }

            // Filtered Marketplace Items
            val filteredItems = state.marketplaceItems.filter { item ->
                (selectedCategory == "ALL" || item.category == selectedCategory) &&
                (searchQuery.isBlank() || item.title.contains(searchQuery, ignoreCase = true) || item.category.contains(searchQuery, ignoreCase = true))
            }

            items(filteredItems, key = { it.id }) { item ->
                MarketplaceItemCard(
                    item = item,
                    onAcquire = { snackbarMessage = "Acquired '${item.title}' for ${item.priceCredits} credits!" }
                )
            }
        }
    }

    // Feedback Toast / Banner
    snackbarMessage?.let { msg ->
        LaunchedEffect(msg) {
            kotlinx.coroutines.delay(2500)
            snackbarMessage = null
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                color = AeirmistSurfaceCard,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, AeirmistCyan),
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = msg,
                    color = AeirmistCyan,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }
        }
    }
}

@Composable
private fun MarketplaceItemCard(
    item: MarketplaceItem,
    onAcquire: () -> Unit
) {
    AeirmistCard(
        modifier = Modifier.testTag("marketplace_item_${item.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, AeirmistBorderSubtle, RoundedCornerShape(14.dp))
            ) {
                androidx.compose.foundation.Image(
                    painter = painterResource(id = item.imageDrawable),
                    contentDescription = item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = item.title,
                        color = AeirmistTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = item.description,
                    color = AeirmistTextSecondary,
                    fontSize = 11.sp,
                    maxLines = 2,
                    lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = AeirmistLime,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "${item.rating}",
                            color = AeirmistLime,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Text(
                        text = "by @${item.seller.username}",
                        color = AeirmistTextMuted,
                        fontSize = 10.sp
                    )
                }
            }

            // Price & Acquire Action
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "${item.priceCredits} ⚡",
                    color = AeirmistCyan,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace
                )

                Button(
                    onClick = onAcquire,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AeirmistCyan,
                        contentColor = AeirmistBg
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text(
                        text = "GET",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}
