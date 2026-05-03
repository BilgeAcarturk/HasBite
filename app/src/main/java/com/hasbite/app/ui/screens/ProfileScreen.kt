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
            item { ProfileHeader() }

            item {
                ProfileCard(
                    user = user,
                    onEditProfile = onOpenEditProfile
                )
            }

            item { ProfileStats() }

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
private fun ProfileHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.displaySmall.noFontPad(),
            fontWeight = FontWeight.ExtraBold,
            color = TextDark,
            modifier = Modifier.weight(1f)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            CircleIconButton(Icons.Outlined.NotificationsNone) {}
            CircleIconButton(Icons.Filled.Search) {}
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
                    painter = painterResource(R.drawable.profile_picture),
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
private fun ProfileStats() {
    val shape = RoundedCornerShape(20.dp)
    Card(
        modifier = Modifier.fillMaxWidth().shadow(10.dp, shape),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F3ED))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem("62", "Saved")
            StatItem("18", "Reviews")
            StatItem("137", "Photos")
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
