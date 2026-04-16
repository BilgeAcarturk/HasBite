package com.hasbite.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hasbite.app.R

private val CreamBg = Color(0xFFF6EFE7)
private val Orange = Color(0xFFE47A2E)
private val TextDark = Color(0xFF2A2A2A)
private val TextMuted = Color(0xFF8A8A8A)
private val CardBg = Color(0xFFF9F3ED)

private fun TextStyle.noFontPad(): TextStyle =
    copy(platformStyle = PlatformTextStyle(includeFontPadding = false))

@Composable
fun InviteFriendsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBg)
    ) {
        Image(
            painter = painterResource(R.drawable.login_food_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.16f
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 14.dp, bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                InviteHeader(onBackClick = onBackClick)
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(28.dp)),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            modifier = Modifier.size(72.dp),
                            shape = CircleShape,
                            color = Orange.copy(alpha = 0.14f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Outlined.PersonAdd,
                                    contentDescription = null,
                                    tint = Orange,
                                    modifier = Modifier.size(34.dp)
                                )
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = "Invite Your Friends",
                            style = MaterialTheme.typography.headlineSmall.noFontPad(),
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = "Share HasBite with your friends and cook smarter together",
                            style = MaterialTheme.typography.bodyLarge.noFontPad(),
                            color = TextMuted
                        )

                        Spacer(Modifier.height(20.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Enter email address") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.MailOutline,
                                    contentDescription = null
                                )
                            },
                            shape = RoundedCornerShape(18.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White.copy(alpha = 0.7f),
                                unfocusedContainerColor = Color.White.copy(alpha = 0.7f),
                                focusedBorderColor = Orange,
                                unfocusedBorderColor = Color(0xFFE7DED4)
                            )
                        )

                        Spacer(Modifier.height(16.dp))

                        Button(
                            onClick = { },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Orange)
                        ) {
                            Text("Send Invite", fontWeight = FontWeight.Bold)
                        }

                        Spacer(Modifier.height(22.dp))

                        Divider(color = Color(0xFFE8DED3))

                        Spacer(Modifier.height(18.dp))

                        InviteActionRow(
                            icon = Icons.Outlined.ContentCopy,
                            title = "Copy Invite Link"
                        )

                        Spacer(Modifier.height(12.dp))

                        InviteActionRow(
                            icon = Icons.Outlined.Share,
                            title = "Share via Apps"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InviteHeader(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            modifier = Modifier.size(44.dp),
            shape = CircleShape,
            color = Color.White.copy(alpha = 0.75f),
            shadowElevation = 8.dp
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = TextDark
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        Column {
            Text(
                text = "Invite Friends",
                style = MaterialTheme.typography.headlineMedium.noFontPad(),
                fontWeight = FontWeight.ExtraBold,
                color = TextDark
            )
            Text(
                text = "Share HasBite with others",
                style = MaterialTheme.typography.bodyLarge.noFontPad(),
                color = TextMuted
            )
        }
    }
}

@Composable
private fun InviteActionRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color.White.copy(alpha = 0.7f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Orange
            )

            Spacer(Modifier.width(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.noFontPad(),
                color = TextDark,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )

            Text("›", color = Orange)
        }
    }
}