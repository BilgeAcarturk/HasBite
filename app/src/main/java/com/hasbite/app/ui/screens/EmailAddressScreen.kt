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

private val EmailCreamBg = Color(0xFFF6EFE7)
private val EmailOrange = Color(0xFFE47A2E)
private val EmailTextDark = Color(0xFF2A2A2A)
private val EmailTextMuted = Color(0xFF8A8A8A)
private val EmailCardBg = Color(0xFFF9F3ED)

private fun TextStyle.emailNoFontPad(): TextStyle =
    copy(platformStyle = PlatformTextStyle(includeFontPadding = false))

@Composable
fun EmailAddressScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("jane.doe@email.com") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(EmailCreamBg)
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
                EmailHeader(onBackClick = onBackClick)
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(28.dp)),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = EmailCardBg)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Email Address",
                            style = MaterialTheme.typography.bodyMedium.emailNoFontPad(),
                            color = EmailTextMuted,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(Modifier.height(6.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White.copy(alpha = 0.65f),
                                unfocusedContainerColor = Color.White.copy(alpha = 0.65f),
                                focusedBorderColor = EmailOrange,
                                unfocusedBorderColor = Color(0xFFE7DED4)
                            )
                        )

                        Spacer(Modifier.height(22.dp))

                        Button(
                            onClick = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(22.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = EmailOrange)
                        ) {
                            Text("Update Email", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmailHeader(onBackClick: () -> Unit) {
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
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = EmailTextDark)
            }
        }

        Spacer(Modifier.width(12.dp))

        Column {
            Text(
                text = "Email Address",
                style = MaterialTheme.typography.headlineMedium.emailNoFontPad(),
                fontWeight = FontWeight.ExtraBold,
                color = EmailTextDark
            )
            Text(
                text = "Update your email information",
                style = MaterialTheme.typography.bodyLarge.emailNoFontPad(),
                color = EmailTextMuted
            )
        }
    }
}