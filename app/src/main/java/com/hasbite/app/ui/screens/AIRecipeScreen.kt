package com.hasbite.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hasbite.app.R

private val Cream = Color(0xFFF6EFE7)
private val CreamTop = Color(0xE6F6EFE7)
private val CreamBottom = Color(0xFFF6EFE7)
private val Orange = Color(0xFFE47A2E)
private val SearchBarBg = Color(0xFFF3EEF0)
private val TextDark = Color(0xFF2E2E2E)
private val TextMuted = Color(0xFF7A7A7A)

private fun TextStyle.noFontPad(): TextStyle =
    copy(platformStyle = PlatformTextStyle(includeFontPadding = false))

@Composable
fun AIRecipeScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    var message by remember { mutableStateOf("") }

    Box(modifier = modifier.fillMaxSize()) {

        Image(
            painter = painterResource(R.drawable.login_food_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.35f
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            CreamTop,
                            Color(0xCCF6EFE7),
                            CreamBottom
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // HEADER
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(42.dp),
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.78f),
                            shadowElevation = 8.dp
                        ) {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = TextDark
                                )
                            }
                        }

                        Spacer(Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "AI Recipe",
                                style = MaterialTheme.typography.headlineLarge.noFontPad(),
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Text(
                                text = "Assistant",
                                style = MaterialTheme.typography.headlineMedium.noFontPad(),
                                color = TextMuted
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(18.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Surface(
                            shape = RoundedCornerShape(22.dp),
                            color = Color(0xFFFFE7D3),
                            shadowElevation = 4.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "I have chicken and rice",
                                    style = MaterialTheme.typography.bodyLarge.noFontPad(),
                                    color = TextDark
                                )
                            }
                        }
                    }
                }

                item {
                    Row(
                        verticalAlignment = Alignment.Top
                    ) {
                        Surface(
                            modifier = Modifier.size(44.dp),
                            shape = CircleShape,
                            color = Color(0xFFFFE0C7),
                            shadowElevation = 4.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.SmartToy,
                                    contentDescription = null,
                                    tint = Orange
                                )
                            }
                        }

                        Spacer(Modifier.width(10.dp))

                        Surface(
                            shape = RoundedCornerShape(24.dp),
                            color = Color.White.copy(alpha = 0.78f),
                            shadowElevation = 6.dp
                        ) {
                            Text(
                                text = "Sure! Here is a recipe suggestion for a creamy chicken and rice dish:",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyLarge.noFontPad(),
                                color = TextDark
                            )
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(26.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.82f)),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Creamy Chicken Rice",
                                style = MaterialTheme.typography.headlineSmall.noFontPad(),
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = "⭐ 4.8 • 17 reviews",
                                style = MaterialTheme.typography.bodyMedium.noFontPad(),
                                color = TextMuted
                            )

                            Spacer(Modifier.height(14.dp))

                            Image(
                                painter = painterResource(R.drawable.recipe_chicken),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(20.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(Modifier.height(14.dp))

                            Text(
                                text = "Ingredients",
                                style = MaterialTheme.typography.titleMedium.noFontPad(),
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )

                            Spacer(Modifier.height(8.dp))

                            Text("• 1 cup rice", color = TextDark)
                            Text("• 2 chicken breasts, diced", color = TextDark)
                            Text("• 1 cup chicken broth", color = TextDark)
                            Text("• 1/2 cup cream", color = TextDark)

                            Spacer(Modifier.height(14.dp))

                            Text(
                                text = "Steps",
                                style = MaterialTheme.typography.titleMedium.noFontPad(),
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )

                            Spacer(Modifier.height(8.dp))

                            Text("1. Cook the rice and set aside.", color = TextDark)
                            Text("2. Sauté diced chicken until golden.", color = TextDark)
                            Text("3. Add broth, cream and rice together.", color = TextDark)

                            Spacer(Modifier.height(18.dp))

                            Button(
                                onClick = {},
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Orange),
                                contentPadding = PaddingValues(vertical = 14.dp)
                            ) {
                                Text(
                                    text = "Save Recipe",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Surface(
                color = SearchBarBg,
                shape = RoundedCornerShape(28.dp),
                tonalElevation = 0.dp,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Search, null, tint = Color(0xFF5B5B5B))
                    Spacer(Modifier.width(10.dp))

                    TextField(
                        value = message,
                        onValueChange = { message = it },
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text("Type your ingredients...")
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        singleLine = true
                    )

                    Icon(
                        Icons.Default.Mic,
                        contentDescription = null,
                        tint = TextDark
                    )

                    Spacer(Modifier.width(10.dp))

                    Surface(
                        modifier = Modifier.size(42.dp),
                        shape = CircleShape,
                        color = Orange
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.Send,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}