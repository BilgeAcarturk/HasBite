package com.hasbite.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.hasbite.app.R

private val CreamBg = Color(0xFFF6EFE7)
private val Orange = Color(0xFFE47A2E)
private val TextDark = Color(0xFF2A2A2A)
private val TextMuted = Color(0xFF8A8A8A)
private val CardBg = Color(0xFFF9F3ED)

private fun TextStyle.noFontPad(): TextStyle =
    copy(platformStyle = PlatformTextStyle(includeFontPadding = false))

data class ShoppingItem(
    val name: String,
    var checked: Boolean = false
)

@Composable
fun ShoppingListScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    var newItem by remember { mutableStateOf("") }
    val shoppingItems = remember {
        mutableStateListOf(
            ShoppingItem("Eggs"),
            ShoppingItem("Milk"),
            ShoppingItem("Olive oil"),
            ShoppingItem("Chicken breast"),
            ShoppingItem("Tomatoes")
        )
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
                                        shoppingItems.add(ShoppingItem(newItem.trim()))
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

                        shoppingItems.forEachIndexed { index, item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 18.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = item.checked,
                                    onCheckedChange = { checked ->
                                        shoppingItems[index] = item.copy(checked = checked)
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Orange,
                                        uncheckedColor = TextMuted
                                    )
                                )

                                Spacer(Modifier.width(10.dp))

                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.bodyLarge.noFontPad(),
                                    color = if (item.checked) TextMuted else TextDark
                                )
                            }

                            if (index != shoppingItems.lastIndex) {
                                Divider(
                                    color = Color(0xFFE8DED3),
                                    modifier = Modifier.padding(horizontal = 18.dp)
                                )
                            }
                        }
                    }
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