package com.hasbite.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

private val PassCreamBg = Color(0xFFF6EFE7)
private val PassOrange = Color(0xFFE47A2E)
private val PassTextDark = Color(0xFF2A2A2A)
private val PassTextMuted = Color(0xFF8A8A8A)
private val PassCardBg = Color(0xFFF9F3ED)

private fun TextStyle.passNoFontPad(): TextStyle =
    copy(platformStyle = PlatformTextStyle(includeFontPadding = false))

@Composable
fun ChangePasswordScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PassCreamBg)
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
                PassHeader(onBackClick = onBackClick)
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(28.dp)),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = PassCardBg)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        PasswordField("Current Password", currentPassword) { currentPassword = it }
                        Spacer(Modifier.height(12.dp))
                        PasswordField("New Password", newPassword) { newPassword = it }
                        Spacer(Modifier.height(12.dp))
                        PasswordField("Confirm Password", confirmPassword) { confirmPassword = it }

                        Spacer(Modifier.height(22.dp))

                        Button(
                            onClick = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(22.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PassOrange)
                        ) {
                            Text("Change Password", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PassHeader(onBackClick: () -> Unit) {
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
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = PassTextDark)
            }
        }

        Spacer(Modifier.width(12.dp))

        Column {
            Text(
                text = "Change Password",
                style = MaterialTheme.typography.headlineMedium.passNoFontPad(),
                fontWeight = FontWeight.ExtraBold,
                color = PassTextDark
            )
            Text(
                text = "Update your password securely",
                style = MaterialTheme.typography.bodyLarge.passNoFontPad(),
                color = PassTextMuted
            )
        }
    }
}

@Composable
private fun PasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.passNoFontPad(),
            color = PassTextMuted,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(6.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(alpha = 0.65f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.65f),
                focusedBorderColor = PassOrange,
                unfocusedBorderColor = Color(0xFFE7DED4)
            )
        )
    }
}