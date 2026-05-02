package com.hasbite.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.rememberDismissState
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ShoppingCart
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hasbite.app.R
import com.hasbite.app.ui.viewmodel.ShoppingViewModel

import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.ExperimentalMaterialApi

private val CreamBg = Color(0xFFF6EFE7)
private val Orange = Color(0xFFE47A2E)
private val TextDark = Color(0xFF2A2A2A)
private val TextMuted = Color(0xFF8A8A8A)
private val CardBg = Color(0xFFF9F3ED)

private fun TextStyle.noFontPad(): TextStyle =
    copy(platformStyle = PlatformTextStyle(includeFontPadding = false))

data class ShoppingItem(
    val id: String = "",
    val name: String,
    val checked: Boolean = false
)

@OptIn(ExperimentalMaterialApi::class)

@Composable

fun ShoppingListScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    val viewModel: ShoppingViewModel = viewModel()
    val items = viewModel.items

    var newItem by remember { mutableStateOf("") }

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
                ScreenHeader(
                    title = "Shopping List",
                    subtitle = "Organize your ingredients",
                    onBackClick = onBackClick
                )
            }

            // ADD ITEM
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(28.dp)),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = newItem,
                            onValueChange = { newItem = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Add new item") },
                            shape = RoundedCornerShape(18.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White.copy(alpha = 0.7f),
                                unfocusedContainerColor = Color.White.copy(alpha = 0.7f),
                                focusedBorderColor = Orange,
                                unfocusedBorderColor = Color(0xFFE7DED4)
                            )
                        )

                        Spacer(Modifier.width(10.dp))

                        Surface(
                            modifier = Modifier.size(46.dp),
                            shape = CircleShape,
                            color = Orange,
                            shadowElevation = 6.dp
                        ) {
                            IconButton(
                                onClick = {
                                    if (newItem.isNotBlank()) {
                                        viewModel.addItem(newItem)
                                        newItem = ""
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Add,
                                    contentDescription = "Add item",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // HEADER CARD (liste başlığı)
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
                            .padding(vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 18.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ShoppingCart,
                                contentDescription = null,
                                tint = Orange
                            )

                            Spacer(Modifier.width(10.dp))

                            Text(
                                text = "Your Items",
                                style = MaterialTheme.typography.titleMedium.noFontPad(),
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                        }

                        Divider(color = Color(0xFFE8DED3))
                    }
                }
            }

            itemsIndexed(items) { index, item ->
                val dismissState = rememberDismissState()

                if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                    LaunchedEffect(item.id) {
                        viewModel.deleteItem(item)
                    }
                }

                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(DismissDirection.EndToStart),
                    modifier = Modifier.padding(vertical = 4.dp), // Baloncuklar arası boşluk
                    background = {
                        // Kaydırırken arkada çıkan kırmızı silme alanı (isteğe bağlı)
                        Box(
                            Modifier
                                .fillMaxSize()
                                .padding(horizontal = 2.dp)
                                .background(Color.Red.copy(alpha = 0.7f), RoundedCornerShape(20.dp)),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                androidx.compose.material.icons.Icons.Default.Delete,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.padding(end = 16.dp)
                            )
                        }
                    }
                ) {
                    // Baloncuk Görünümlü Kart
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(4.dp, RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.9f) // Açık renkli baloncuk
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = item.checked,
                                onCheckedChange = {
                                    viewModel.toggleItem(item)
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Orange,
                                    uncheckedColor = TextMuted
                                )
                            )

                            Spacer(Modifier.width(8.dp))

                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.bodyLarge.noFontPad(),
                                color = if (item.checked) TextMuted else TextDark,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // CLEAR BUTTON
            item {
                Button(
                    onClick = { viewModel.clearCompleted() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Orange)
                ) {
                    Text("Clear Completed", color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun ScreenHeader(
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