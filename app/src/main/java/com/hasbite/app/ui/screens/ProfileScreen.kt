package com.hasbite.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hasbite.app.R
import com.hasbite.app.data.model.User
import com.hasbite.app.ui.viewmodel.ProfileViewModel

private val CreamBg = Color(0xFFF6EFE7)
private val Orange = Color(0xFFE47A2E)
private val TextDark = Color(0xFF2A2A2A)
private val TextMuted = Color(0xFF8A8A8A)

private fun TextStyle.noFontPad(): TextStyle =
    copy(platformStyle = PlatformTextStyle(includeFontPadding = false))

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onOpenEditProfile: () -> Unit = {},
    onOpenAccountSettings: () -> Unit = {},
    onOpenCollections: () -> Unit = {},
    onOpenShoppingList: () -> Unit = {},
    onOpenInviteFriends: () -> Unit = {},
    onLogout: () -> Unit = {} // Çıkış yapma aksiyonu
) {
    val viewModel: ProfileViewModel = viewModel()
    val user by viewModel.user.collectAsState()
    var notificationsEnabled by remember { mutableStateOf(true) }
    var searchText by remember { mutableStateOf("") }

    val searchResults by viewModel.searchResults.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refreshUser()
    }

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
            contentPadding = PaddingValues(top = 14.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                ProfileHeader(
                    searchText = searchText,
                    onSearchChange = {
                        searchText = it
                        viewModel.searchUsers(it)
                    }
                )
            }

            item {
                ProfileCard(
                    user = user,
                    onEditProfile = onOpenEditProfile
                )
            }

            if (searchText.isNotBlank()) {

                item {

                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        searchResults.forEach { foundUser ->

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .offset(y = (-1).dp),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFF9F3ED)
                                )
                            ) {

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Box(
                                        modifier = Modifier
                                            .size(54.dp)
                                            .clip(CircleShape)
                                            .background(Orange.copy(alpha = 0.15f))
                                            .padding(3.dp)
                                    ) {

                                        Image(
                                            painter = painterResource(
                                                getAvatarRes(foundUser.avatar)
                                            ),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(14.dp))

                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {

                                        Text(
                                            text = foundUser.name,
                                            fontWeight = FontWeight.Bold,
                                            color = TextDark
                                        )

                                        Text(
                                            text = foundUser.email,
                                            color = TextMuted,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                SettingsCard(
                    notificationsEnabled = notificationsEnabled,
                    onToggle = { notificationsEnabled = it },
                    onOpenAccountSettings = onOpenAccountSettings,
                    onOpenCollections = onOpenCollections,
                    onOpenShoppingList = onOpenShoppingList,
                    onOpenInviteFriends = onOpenInviteFriends
                )
            }

            // 🔥 ÇIKIŞ BUTONU
            item {
                LogoutButton(onClick = onLogout)
            }
        }
    }
}

@Composable
private fun ProfileHeader(
    searchText: String,
    onSearchChange: (String) -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Profile",
            style = MaterialTheme.typography.displaySmall.noFontPad(),
            fontWeight = FontWeight.ExtraBold,
            color = TextDark,
            modifier = Modifier.weight(1f)
        )

        Surface(
            modifier = Modifier
                .weight(1.5f)
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color.White.copy(alpha = 0.82f),
            shadowElevation = 8.dp
        ) {

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = TextMuted
                )

                Spacer(modifier = Modifier.width(6.dp))

                TextField(
                    value = searchText,
                    onValueChange = onSearchChange,
                    placeholder = {
                        Text(
                            text = "Search Profile",
                            color = TextMuted,
                            style = MaterialTheme.typography.bodyMedium.noFontPad()
                        )
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.noFontPad(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Orange
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-1).dp)
                )
            }
        }
    }
}

@Composable
private fun ProfileCard(user: User?, onEditProfile: () -> Unit) {
    val shape = RoundedCornerShape(28.dp)
    Card(
        modifier = Modifier.fillMaxWidth().shadow(14.dp, shape),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F3ED))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 26.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(120.dp).clip(CircleShape).background(Orange.copy(alpha = 0.18f)).padding(4.dp)
            ) {
                Image(
                    painter = painterResource(
                        getAvatarRes(user?.avatar ?: "avatar1")
                    ),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().clip(CircleShape).graphicsLayer {
                        scaleX = 1.35f
                        scaleY = 1.35f
                    },
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
            }
            Spacer(Modifier.height(14.dp))
            Text(text = user?.name ?: "Loading...", style = MaterialTheme.typography.headlineMedium.noFontPad(), fontWeight = FontWeight.Bold, color = TextDark)
            Text(text = "${user?.email ?: ""} • ${user?.age ?: ""}", style = MaterialTheme.typography.bodyLarge.noFontPad(), color = TextMuted)
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onEditProfile,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Orange),
                contentPadding = PaddingValues(horizontal = 30.dp, vertical = 12.dp)
            ) {
                Text("Edit Profile", fontWeight = FontWeight.Bold)
            }
        }
    }

}

@Composable
private fun StatItem(number: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = number, style = MaterialTheme.typography.headlineSmall.noFontPad(), fontWeight = FontWeight.Bold, color = TextDark)
        Text(text = label, style = MaterialTheme.typography.bodyMedium.noFontPad(), color = TextMuted)
    }
}

@Composable
private fun SettingsCard(
    notificationsEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
    onOpenAccountSettings: () -> Unit,
    onOpenCollections: () -> Unit,
    onOpenShoppingList: () -> Unit,
    onOpenInviteFriends: () -> Unit
) {
    val shape = RoundedCornerShape(26.dp)
    Card(
        modifier = Modifier.fillMaxWidth().shadow(12.dp, shape),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F3ED))
    ) {
        Column {
            SettingsRow(Icons.Outlined.Person, "Account Settings", onClick = onOpenAccountSettings)
            SettingsRow(Icons.Outlined.Folder, "My Collections", onClick = onOpenCollections)
            SettingsRow(Icons.Outlined.List, "Shopping List", onClick = onOpenShoppingList)
            SettingsRow(Icons.Outlined.PersonAdd, "Invite Friends", onClick = onOpenInviteFriends)

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.Notifications, contentDescription = null)
                Spacer(Modifier.width(14.dp))
                Text(text = "Notifications", style = MaterialTheme.typography.bodyLarge.noFontPad(), modifier = Modifier.weight(1f))
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = onToggle,
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Orange)
                )
            }
        }
    }
}

@Composable
private fun SettingsRow(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 18.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null)
        Spacer(Modifier.width(14.dp))
        Text(text = title, style = MaterialTheme.typography.bodyLarge.noFontPad(), modifier = Modifier.weight(1f))
        Text("›", color = Orange, fontWeight = FontWeight.Bold)
    }
}

// 🛑 LOGOUT BUTONU BİLEŞENİ
@Composable
private fun LogoutButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(8.dp, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFD32F2F) // Koyu Kırmızı
        )
    ) {
        Icon(Icons.Outlined.Logout, contentDescription = null, tint = Color.White)
        Spacer(Modifier.width(10.dp))
        Text(
            text = "Logout",
            style = MaterialTheme.typography.bodyLarge.noFontPad(),
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

private fun getAvatarRes(name: String): Int {
    return when (name) {
        "avatar1" -> R.drawable.w1
        "avatar2" -> R.drawable.m1
        "avatar3" -> R.drawable.w2
        else -> R.drawable.w1
    }
}

@Composable
private fun CircleIconButton(icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.size(44.dp),
        shape = CircleShape,
        color = Color.White.copy(alpha = 0.75f),
        shadowElevation = 8.dp
    ) {
        IconButton(onClick = onClick) {
            Icon(icon, contentDescription = null, tint = TextDark)
        }
    }
}