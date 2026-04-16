package com.hasbite.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hasbite.app.R

// ---------- Palette (match Explore) ----------
private val CreamBg = Color(0xFFF6EFE7)
private val Orange = Color(0xFFE47A2E)
private val TextDark = Color(0xFF2A2A2A)
private val TextMuted = Color(0xFF8A8A8A)

private fun TextStyle.noFontPad(): TextStyle =
    copy(platformStyle = PlatformTextStyle(includeFontPadding = false))

// ---------- Models ----------
private enum class FavCategory { Breakfast, Lunch, Dinner, Dessert }

private data class FavItem(
    val title: String,
    val minutesText: String? = null,
    val rating: Double,
    val reviewsText: String,
    val tag: String? = null,          // pill text
    val category: FavCategory,         // filter için
    val imageRes: Int
)

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    onOpenNotifications: () -> Unit = {},
    onOpenSearch: () -> Unit = {},
    onOpenRecipeDetail: (String) -> Unit = {}
) {
    val listState = rememberLazyListState()
    var selectedTab by remember { mutableStateOf(0) }

    val tabs = listOf("Breakfast", "Lunch", "Dinner", "Dessert")

    // Demo content (same as mock)
    val items = listOf(
        FavItem(
            title = "Avocado Toast",
            minutesText = "10 min",
            rating = 4.8,
            reviewsText = "980",
            tag = "Quick & Easy",
            category = FavCategory.Breakfast,
            imageRes = R.drawable.recipe_avokado_toast
        ),
        FavItem(
            title = "Chocolate Cake",
            minutesText = "45 min",
            rating = 4.9,
            reviewsText = "2.1k",
            tag = "Sweet",
            category = FavCategory.Dessert,
            imageRes = R.drawable.recipe_chocolate_cake
        ),
        FavItem(
            title = "Grilled Chicken",
            minutesText = "30 min",
            rating = 4.9,
            reviewsText = "1.2k",
            tag = "High Protein",
            category = FavCategory.Dinner,
            imageRes = R.drawable.recipe_chicken
        ),
        FavItem(
            title = "Quinoa Chicken Salad",
            minutesText = "20 min",
            rating = 4.6,
            reviewsText = "640",
            tag = "Light & Fresh",
            category = FavCategory.Lunch,
            imageRes = R.drawable.recipe_quinoa_salad
        ),
        FavItem(
            title = "Protein Pancakes",
            minutesText = null,
            rating = 4.7,
            reviewsText = "750",
            tag = "Healthy",
            category = FavCategory.Breakfast,
            imageRes = R.drawable.recipe_pancake
        )
    )
    val selectedCategory = when (selectedTab) {
        0 -> FavCategory.Breakfast
        1 -> FavCategory.Lunch
        2 -> FavCategory.Dinner
        else -> FavCategory.Dessert
    }

    val filteredItems = remember(selectedTab) {
        items.filter { it.category == selectedCategory }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBg)
    ) {
        // soft blurred-ish background
        Image(
            painter = painterResource(R.drawable.login_food_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.16f
        )

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 14.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                FavoritesHeader(
                    title = "Favorites",
                    subtitle = "Your saved recipes",
                    onOpenNotifications = onOpenNotifications,
                    onOpenSearch = onOpenSearch
                )
            }

            item {
                FavoritesTabs(
                    tabs = tabs,
                    selectedIndex = selectedTab,
                    onSelect = { selectedTab = it }
                )
            }

            item {
                FavoritesListCard(
                    items = filteredItems,
                    onOpenRecipeDetail = onOpenRecipeDetail
                )
            }
        }
    }
}

// ---------- Header ----------
@Composable
private fun FavoritesHeader(
    title: String,
    subtitle: String,
    onOpenNotifications: () -> Unit,
    onOpenSearch: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.displaySmall.noFontPad(),
                fontWeight = FontWeight.ExtraBold,
                color = TextDark
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge.noFontPad(),
                color = TextMuted
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            CircleIconButton(icon = Icons.Outlined.NotificationsNone, onClick = onOpenNotifications)
            CircleIconButton(icon = Icons.Filled.Search, onClick = onOpenSearch)
        }
    }
}

@Composable
private fun CircleIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.size(44.dp),
        shape = CircleShape,
        color = Color.White.copy(alpha = 0.75f),
        tonalElevation = 0.dp,
        shadowElevation = 8.dp
    ) {
        IconButton(onClick = onClick) {
            Icon(imageVector = icon, contentDescription = null, tint = TextDark)
        }
    }
}

// ---------- Tabs row ----------
@Composable
private fun FavoritesTabs(
    tabs: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 4.dp)
    ) {
        items(tabs.indices.toList()) { idx ->
            val selected = idx == selectedIndex
            FavoritesTabChip(
                title = tabs[idx],
                selected = selected,
                onClick = { onSelect(idx) }
            )
        }
    }
}

@Composable
private fun FavoritesTabChip(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(22.dp)

    val bg = if (selected) {
        Brush.verticalGradient(listOf(Color(0xFFFF8A3D), Orange))
    } else {
        Brush.verticalGradient(
            listOf(
                Color.White.copy(alpha = 0.88f),
                Color.White.copy(alpha = 0.88f)
            )
        )
    }

    val textColor = if (selected) Color.White else TextDark

    Box(
        modifier = Modifier
            .height(46.dp)
            .shadow(if (selected) 10.dp else 7.dp, shape, clip = false)
            .clip(shape)
            .background(bg)
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.noFontPad(),
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}

// ---------- Big list card ----------
@Composable
private fun FavoritesListCard(
    items: List<FavItem>,
    onOpenRecipeDetail: (String) -> Unit
) {
    val shape = RoundedCornerShape(26.dp)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(14.dp, shape),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.78f))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            items.forEachIndexed { index, item ->
                FavoriteRow(
                    item = item,
                    onClick = {
                        val recipeId = when (item.title) {
                            "Avocado Toast" -> "avocado"
                            "Chocolate Cake" -> "cake"
                            "Grilled Chicken" -> "chicken"
                            "Protein Pancakes" -> "pancake"
                            "Quinoa Chicken Salad" -> "quinoa_salad"
                            else -> "default"
                        }
                        onOpenRecipeDetail(recipeId)
                    }
                )

                if (index != items.lastIndex) {
                    Divider(
                        modifier = Modifier
                            .padding(start = 112.dp, end = 14.dp), // start after image like mock
                        color = Color.Black.copy(alpha = 0.08f),
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun FavoriteRow(
    item: FavItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(item.imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(86.dp)
                .clip(RoundedCornerShape(18.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.headlineSmall.noFontPad(),
                fontWeight = FontWeight.ExtraBold,
                color = TextDark,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (item.minutesText != null) {
                Spacer(Modifier.height(6.dp))
                Text(
                    text = item.minutesText,
                    style = MaterialTheme.typography.bodyLarge.noFontPad(),
                    color = TextMuted
                )
            } else {
                Spacer(Modifier.height(8.dp))
            }

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (item.tag != null) {
                    TagPill(text = item.tag)
                    Spacer(Modifier.width(10.dp))
                }

                Text(
                    text = "★ ${item.rating}",
                    style = MaterialTheme.typography.bodyLarge.noFontPad(),
                    color = TextMuted,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.width(10.dp))

                // small "download" looking icon in mock -> we mimic with text glyph
                Text(
                    text = "⇩ ${item.reviewsText}",
                    style = MaterialTheme.typography.bodyLarge.noFontPad(),
                    color = TextMuted
                )
            }
        }
    }
}

@Composable
private fun TagPill(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFDCEAD5))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.noFontPad(),
            color = Color(0xFF3E6B3E),
            fontWeight = FontWeight.SemiBold
        )
    }
}