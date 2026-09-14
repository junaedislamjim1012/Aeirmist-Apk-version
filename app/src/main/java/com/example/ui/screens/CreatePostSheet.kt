package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Poll
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CyberChip
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostSheet(
    onDismiss: () -> Unit,
    onPublish: (content: String, tags: List<String>, pollQuestion: String?, pollOptions: List<String>) -> Unit
) {
    var content by remember { mutableStateOf("") }
    var selectedTag by remember { mutableStateOf("AeirmistMatrix") }
    var isPollActive by remember { mutableStateOf(false) }
    var pollQuestion by remember { mutableStateOf("") }
    var pollOption1 by remember { mutableStateOf("") }
    var pollOption2 by remember { mutableStateOf("") }

    val defaultTags = listOf("AeirmistMatrix", "CyberPulse", "NeuralSync", "Quantum")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = AeirmistSurfaceCard,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .width(44.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(AeirmistBorderActive)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 8.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Sheet Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TRANSMIT NEW SIGNAL",
                    color = AeirmistCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    fontFamily = FontFamily.Monospace
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = AeirmistTextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Post Content Text Field
            TextField(
                value = content,
                onValueChange = { content = it },
                placeholder = {
                    Text(
                        "Broadcast frequency to the Aeirmist Matrix...",
                        color = AeirmistTextMuted,
                        fontSize = 14.sp
                    )
                },
                minLines = 3,
                maxLines = 6,
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
                    .testTag("create_post_input")
            )

            // Tag Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                defaultTags.forEach { tag ->
                    CyberChip(
                        text = "#$tag",
                        isSelected = selectedTag == tag,
                        onClick = { selectedTag = tag }
                    )
                }
            }

            // Poll Toggle Action
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { isPollActive = !isPollActive }
                    .background(if (isPollActive) AeirmistCyan.copy(alpha = 0.12f) else Color.Transparent)
                    .border(
                        1.dp,
                        if (isPollActive) AeirmistCyan else AeirmistBorderSubtle,
                        RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Poll,
                    contentDescription = "Add Poll",
                    tint = if (isPollActive) AeirmistCyan else AeirmistTextSecondary,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = if (isPollActive) "Neural Poll Attached" else "Attach Neural Poll",
                    color = if (isPollActive) AeirmistCyan else AeirmistTextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Monospace
                )
            }

            // Expanded Poll Fields
            AnimatedVisibility(visible = isPollActive) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextField(
                        value = pollQuestion,
                        onValueChange = { pollQuestion = it },
                        placeholder = { Text("Poll Question...", fontSize = 12.sp, color = AeirmistTextMuted) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = AeirmistSurfaceElevated,
                            unfocusedContainerColor = AeirmistSurfaceElevated,
                            focusedTextColor = AeirmistTextPrimary,
                            unfocusedTextColor = AeirmistTextPrimary,
                            focusedIndicatorColor = AeirmistCyan
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    TextField(
                        value = pollOption1,
                        onValueChange = { pollOption1 = it },
                        placeholder = { Text("Option 1...", fontSize = 12.sp, color = AeirmistTextMuted) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = AeirmistSurfaceElevated,
                            unfocusedContainerColor = AeirmistSurfaceElevated,
                            focusedTextColor = AeirmistTextPrimary,
                            unfocusedTextColor = AeirmistTextPrimary,
                            focusedIndicatorColor = AeirmistCyan
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    TextField(
                        value = pollOption2,
                        onValueChange = { pollOption2 = it },
                        placeholder = { Text("Option 2...", fontSize = 12.sp, color = AeirmistTextMuted) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = AeirmistSurfaceElevated,
                            unfocusedContainerColor = AeirmistSurfaceElevated,
                            focusedTextColor = AeirmistTextPrimary,
                            unfocusedTextColor = AeirmistTextPrimary,
                            focusedIndicatorColor = AeirmistCyan
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Publish Button
            Button(
                onClick = {
                    if (content.isNotBlank()) {
                        val pollQ = if (isPollActive && pollQuestion.isNotBlank()) pollQuestion else null
                        val pollOpts = if (isPollActive) listOf(pollOption1, pollOption2) else emptyList()
                        onPublish(content, listOf(selectedTag), pollQ, pollOpts)
                    }
                },
                enabled = content.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AeirmistCyan,
                    contentColor = AeirmistBg,
                    disabledContainerColor = AeirmistSurfaceElevated,
                    disabledContentColor = AeirmistTextMuted
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("publish_post_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Publish",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "TRANSMIT SIGNAL",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
