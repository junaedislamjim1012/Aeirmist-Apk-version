package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.AeirmistViewModel

enum class MainTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    FEED("Feed", Icons.Filled.DynamicFeed, Icons.Outlined.DynamicFeed),
    MATRIX("Matrix", Icons.Filled.Hub, Icons.Outlined.Hub),
    STREAMS("Streams", Icons.Filled.PlayCircle, Icons.Outlined.PlayCircleOutline),
    MESSAGES("Inbox", Icons.Filled.Forum, Icons.Outlined.Forum),
    DISCOVER("Discover", Icons.Filled.Explore, Icons.Outlined.Explore),
    PROFILE("Profile", Icons.Filled.Person, Icons.Outlined.PersonOutline)
}

enum class SubScreen {
    NONE,
    NOTIFICATIONS,
    SETTINGS
}

@Composable
fun MainScreen(
    viewModel: AeirmistViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(MainTab.FEED) }
    var currentSubScreen by remember { mutableStateOf(SubScreen.NONE) }
    var showCreatePostSheet by remember { mutableStateOf(false) }

    val state by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = AeirmistBg,
        bottomBar = {
            if (currentSubScreen == SubScreen.NONE && state.activeConversationId == null) {
                AeirmistBottomNavigation(
                    currentTab = selectedTab,
                    onSelectTab = { selectedTab = it },
                    unreadMessages = state.conversations.sumOf { it.unreadCount }
                )
            }
        },
        floatingActionButton = {
            if (currentSubScreen == SubScreen.NONE && state.activeConversationId == null && selectedTab == MainTab.FEED) {
                FloatingActionButton(
                    onClick = { showCreatePostSheet = true },
                    containerColor = AeirmistCyan,
                    contentColor = AeirmistBg,
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(bottom = 72.dp)
                        .testTag("fab_create_signal")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New Signal",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (currentSubScreen == SubScreen.NONE && state.activeConversationId == null) 0.dp else paddingValues.calculateBottomPadding())
        ) {
            when (currentSubScreen) {
                SubScreen.NOTIFICATIONS -> {
                    NotificationsScreen(
                        viewModel = viewModel,
                        onBack = { currentSubScreen = SubScreen.NONE }
                    )
                }
                SubScreen.SETTINGS -> {
                    SettingsScreen(
                        onBack = { currentSubScreen = SubScreen.NONE }
                    )
                }
                SubScreen.NONE -> {
                    when (selectedTab) {
                        MainTab.FEED -> HomeScreen(
                            viewModel = viewModel,
                            onOpenNotifications = { currentSubScreen = SubScreen.NOTIFICATIONS },
                            onOpenMessenger = { selectedTab = MainTab.MESSAGES }
                        )
                        MainTab.MATRIX -> DashboardScreen(viewModel = viewModel)
                        MainTab.STREAMS -> VideosScreen(viewModel = viewModel)
                        MainTab.MESSAGES -> MessengerScreen(viewModel = viewModel)
                        MainTab.DISCOVER -> DiscoverScreen(viewModel = viewModel)
                        MainTab.PROFILE -> ProfileScreen(
                            viewModel = viewModel,
                            onOpenSettings = { currentSubScreen = SubScreen.SETTINGS }
                        )
                    }
                }
            }
        }
    }

    // Modal Create Post Bottom Sheet
    if (showCreatePostSheet) {
        CreatePostSheet(
            onDismiss = { showCreatePostSheet = false },
            onPublish = { content, tags, pollQ, pollOpts ->
                viewModel.createPost(content, tags, pollQ, pollOpts)
                showCreatePostSheet = false
            }
        )
    }

    // Story Viewer
    state.activeStory?.let { story ->
        StoryViewerDialog(
            story = story,
            onDismiss = { viewModel.viewStory(null) }
        )
    }
}

@Composable
private fun AeirmistBottomNavigation(
    currentTab: MainTab,
    onSelectTab: (MainTab) -> Unit,
    unreadMessages: Int
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(24.dp))
            .border(
                1.dp,
                Brush.horizontalGradient(
                    listOf(AeirmistCyan.copy(alpha = 0.4f), AeirmistMagenta.copy(alpha = 0.3f), AeirmistLime.copy(alpha = 0.2f))
                ),
                RoundedCornerShape(24.dp)
            ),
        color = AeirmistSurfaceGlass,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MainTab.values().forEach { tab ->
                val isSelected = currentTab == tab
                val icon = if (isSelected) tab.selectedIcon else tab.unselectedIcon
                val tint = if (isSelected) AeirmistCyan else AeirmistTextMuted

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onSelectTab(tab) }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("nav_tab_${tab.name.lowercase()}")
                ) {
                    BadgedBox(
                        badge = {
                            if (tab == MainTab.MESSAGES && unreadMessages > 0) {
                                Badge(
                                    containerColor = AeirmistMagenta,
                                    contentColor = AeirmistBg
                                ) {
                                    Text(unreadMessages.toString(), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = tab.title,
                            tint = tint,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Text(
                        text = tab.title,
                        color = tint,
                        fontSize = 9.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 0.5.sp
                    )

                    // Active dot
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(AeirmistCyan)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}
