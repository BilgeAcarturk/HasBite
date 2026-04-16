package com.hasbite.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.PersonOutline
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
fun AccountSettingsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onOpenPersonalInformation: () -> Unit = {},
    onOpenEmailAddress: () -> Unit = {},
    onOpenChangePassword: () -> Unit = {}
) {
    var emailNotifications by remember { mutableStateOf(true) }
    var privateAccount by remember { mutableStateOf(false) }

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
                SettingsHeader(
                    title = "Account Settings",
                    subtitle = "Manage your account preferences",
                    onBackClick = onBackClick
                )
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
                            .padding(vertical = 10.dp)
                    ) {
                        SettingOptionRow(
                            icon = { Icon(Icons.Outlined.PersonOutline, contentDescription = null, tint = TextDark) },
                            title = "Personal Information",
                            subtitle = "Update your name and profile data",
                            onClick = onOpenPersonalInformation
                        )

                        Divider(color = Color(0xFFE8DED3))

                        SettingOptionRow(
                            icon = { Icon(Icons.Outlined.Email, contentDescription = null, tint = TextDark) },
                            title = "Email Address",
                            subtitle = "jane.doe@email.com",
                            onClick = onOpenEmailAddress
                        )

                        Divider(color = Color(0xFFE8DED3))

                        SettingOptionRow(
                            icon = { Icon(Icons.Outlined.Lock, contentDescription = null, tint = TextDark) },
                            title = "Change Password",
                            subtitle = "Update your password securely",
                            onClick = onOpenChangePassword
                        )

                        Divider(color = Color(0xFFE8DED3))

                        SettingToggleRow(
                            icon = { Icon(Icons.Outlined.Visibility, contentDescription = null, tint = TextDark) },
                            title = "Private Account",
                            checked = privateAccount,
                            onCheckedChange = { privateAccount = it }
                        )

                        Divider(color = Color(0xFFE8DED3))

                        SettingToggleRow(
                            icon = { Icon(Icons.Outlined.Email, contentDescription = null, tint = TextDark) },
                            title = "Email Notifications",
                            checked = emailNotifications,
                            onCheckedChange = { emailNotifications = it }
                        )
                    }
                }
            }

            item {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Orange)
                ) {
                    Text("Save Settings", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun SettingsHeader(
    title: String,
    subtitle: String,
    onBackClick: () -> Unit
) {
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
                text = title,
                style = MaterialTheme.typography.headlineMedium.noFontPad(),
                fontWeight = FontWeight.ExtraBold,
                color = TextDark
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge.noFontPad(),
                color = TextMuted
            )
        }
    }
}

@Composable
private fun SettingOptionRow(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()

        Spacer(Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.noFontPad(),
                color = TextDark,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium.noFontPad(),
                color = TextMuted
            )
        }

        Text("›", color = Orange)
    }
}

@Composable
private fun SettingToggleRow(
    icon: @Composable () -> Unit,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()

        Spacer(Modifier.width(14.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.noFontPad(),
            color = TextDark,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Orange
            )
        )
    }
}