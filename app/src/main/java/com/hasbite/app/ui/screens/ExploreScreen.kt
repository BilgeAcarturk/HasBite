package com.hasbite.app.ui.screens

import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hasbite.app.R
import androidx.compose.ui.text.style.TextOverflow

private val CreamBg = Color(0xFFF6EFE7)
private val Orange = Color(0xFFE47A2E)
private val TextDark = Color(0xFF2A2A2A)
private val TextMuted = Color(0xFF8A8A8A)

private fun TextStyle.noFontPad(): TextStyle =
    copy(platformStyle = PlatformTextStyle(includeFontPadding = false))

data class ExploreFilter(
    val key: String,
    val title: String,
    val emoji: String? = null
)
data class PopularItem(
    val title: String,
    val categoryKey: String,
    val minutes: Int,
    val rating: Double,
    val reviews: String,
    val imageRes: Int
)
data class CategoryTile(val title: String, val subtitle: String, val imageRes: Int, val tint: Color)
data class RecommendedItem(val title: String, val meta: String, val rating: Double, val imageRes: Int)

@Composable
fun ExploreScreen(
    modifier: Modifier = Modifier,
    onOpenRecipeDetail: (String) -> Unit = {}
) {
    val listState = rememberLazyListState()
    var selectedFilter by remember { mutableStateOf(0) }

    val filters = listOf(
        ExploreFilter("all", "All", "🍽️"),
        ExploreFilter("breakfast", "Breakfast", "🍳"),
        ExploreFilter("lunch", "Lunch", "🥗"),
        ExploreFilter("dinner", "Dinner", "🍝"),
        ExploreFilter("dessert", "Dessert", "🍰"),
        ExploreFilter("salad", "Salads", "🥬")
    )

    val popular = listOf(
        PopularItem("Grilled Chicken", "dinner", 30, 4.9, "1.2k", R.drawable.recipe_chicken),
        PopularItem("Avocado Toast", "breakfast", 10, 4.8, "980", R.drawable.recipe_avokado_toast),
        PopularItem("Chocolate Cake", "dessert", 16, 4.9, "2.1k", R.drawable.recipe_chocolate_cake),
        PopularItem("Quinoa Chicken Salad", "lunch", 20, 4.6, "640", R.drawable.recipe_quinoa_salad)
    )

    val categories = listOf(
        CategoryTile("Quick & Easy", "< 20 min", R.drawable.recipe_pancake, Color(0xFFFFD7B8)),
        CategoryTile("Healthy", "Light meals", R.drawable.recipe_omelet, Color(0xFFCFE8D6)),
        CategoryTile("Italian", "Pasta & more", R.drawable.recipe_pasta, Color(0xFFFFD1D1)),
        CategoryTile("Asian", "Noodles, rice", R.drawable.recipe_asian, Color(0xFFD9D0FF))
    )

    val recommended = RecommendedItem(
        title = "Protein Pancakes",
        meta = "Healthy • 15 min",
        rating = 4.7,
        imageRes = R.drawable.recipe_pancake
    )
    val selectedFilterKey = filters[selectedFilter].key

    val filteredPopular = if (selectedFilterKey == "all") {
        popular
    } else {
        popular.filter { it.categoryKey == selectedFilterKey }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBg)
    ) {
        // Soft background image (blur hissi için düşük alpha)
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
            // Bottom bar üstüne binmesin + rahat scroll
            contentPadding = PaddingValues(top = 14.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { ExploreHeader(title = "Explore", subtitle = "Discover delicious recipes") }

            item {
                FilterRow(
                    filters = filters,
                    selectedIndex = selectedFilter,
                    onSelect = { selectedFilter = it }
                )
            }

            item {
                TodaySpecialBanner(
                    title = "Creamy Sun-Dried\nTomato Pasta",
                    meta = "25 min • Medium",
                    onViewRecipe = { onOpenRecipeDetail("pasta") }
                )
            }

            item {
                SectionTitle(title = "Popular Recipes", action = "See All", onAction = { /* later */ })
            }

            item {
                PopularRow(
                    popular = filteredPopular,
                    onOpenRecipeDetail = onOpenRecipeDetail
                )
            }

            item {
                Text(
                    text = "Categories",
                    style = MaterialTheme.typography.titleLarge.noFontPad(),
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            }

            item { CategoriesGrid(categories = categories) }

            item {
                Text(
                    text = "Recommended for You",
                    style = MaterialTheme.typography.titleLarge.noFontPad(),
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            }

            item {
                RecommendedCard(
                    item = recommended,
                    onTry = { onOpenRecipeDetail("pancake") }
                )
            }
        }
    }
}

@Composable
private fun ExploreHeader(title: String, subtitle: String) {
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

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge.noFontPad(),
                color = TextMuted
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            CircleIconButton(icon = Icons.Outlined.NotificationsNone, onClick = { /* later */ })
            CircleIconButton(icon = Icons.Filled.Search, onClick = { /* later */ })
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

@Composable
private fun FilterRow(
    filters: List<ExploreFilter>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 4.dp)
    ) {
        items(filters.indices.toList()) { idx ->
            val item = filters[idx]
            FilterChip(
                title = item.title,
                emoji = item.emoji,
                selected = idx == selectedIndex,
                onClick = { onSelect(idx) }
            )
        }
    }
}

@Composable
private fun FilterChip(
    title: String,
    emoji: String?,
    selected: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(18.dp)

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
    val iconColor = if (selected) Color.White.copy(alpha = 0.95f) else TextMuted

    Box(
        modifier = Modifier
            .height(44.dp) // ✅ hizalamayı toparlayan ana şey
            .shadow(if (selected) 10.dp else 6.dp, shape, clip = false)
            .clip(shape)
            .background(bg)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (!emoji.isNullOrBlank()) {
                Text(
                    text = emoji,
                    color = iconColor,
                    style = MaterialTheme.typography.bodyLarge.noFontPad()
                )
                Spacer(Modifier.width(8.dp))
            }
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.noFontPad(),
                color = textColor,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun TodaySpecialBanner(
    title: String,
    meta: String,
    onViewRecipe: () -> Unit
) {
    val shape = RoundedCornerShape(24.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(shape)
            .shadow(12.dp, shape)
    ) {
        Image(
            painter = painterResource(R.drawable.recipe_pasta),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.62f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "←  Today’s Special",
                style = MaterialTheme.typography.labelLarge.noFontPad(),
                fontWeight = FontWeight.SemiBold,
                color = Color.White.copy(alpha = 0.95f)
            )

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium.noFontPad(),
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = meta,
                    style = MaterialTheme.typography.bodyLarge.noFontPad(),
                    color = Color.White.copy(alpha = 0.9f)
                )

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = onViewRecipe,
                    modifier = Modifier.height(44.dp), // ✅ sabit yükseklik: yazı kırpılmasın
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Orange),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 0.dp) // ✅ dikey padding'i sıfırla
                ) {
                    Text(
                        text = "View Recipe  →",
                        style = MaterialTheme.typography.labelLarge.noFontPad(),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String, action: String, onAction: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.noFontPad(),
            fontWeight = FontWeight.Bold,
            color = TextDark,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = action,
            style = MaterialTheme.typography.bodyLarge.noFontPad(),
            color = Orange,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable(onClick = onAction)
        )
    }
}

@Composable
private fun PopularRow(
    popular: List<PopularItem>,
    onOpenRecipeDetail: (String) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 4.dp)
    ) {
        items(popular) { item ->
            PopularCard(
                item = item,
                onClick = {
                    val recipeId = when (item.title) {
                        "Grilled Chicken" -> "chicken"
                        "Avocado Toast" -> "avocado"
                        "Chocolate Cake" -> "cake"
                        "Quinoa Chicken Salad" -> "quinoa_salad"
                        else -> "default"
                    }
                    onOpenRecipeDetail(recipeId)
                }
            )
        }
    }
}

@Composable
private fun PopularCard(
    item: PopularItem,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(18.dp)

    Box(
        modifier = Modifier
            .width(180.dp)
            .clip(shape)
            .shadow(10.dp, shape)
            .background(Color.White.copy(alpha = 0.82f))
            .clickable(onClick = onClick)
    ) {
        Column {
            Box(modifier = Modifier.height(110.dp)) {
                Image(
                    painter = painterResource(item.imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.35f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        "${item.minutes} min",
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium.noFontPad()
                    )
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    item.title,
                    style = MaterialTheme.typography.titleMedium.noFontPad(),
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Text(
                    "★ ${item.rating}  (${item.reviews})",
                    style = MaterialTheme.typography.bodyMedium.noFontPad(),
                    color = TextMuted
                )
            }
        }
    }
}

@Composable
private fun CategoriesGrid(categories: List<CategoryTile>) {
    // ✅ Grid kendi içinde scroll yapmıyor, ana LazyColumn scroll yapıyor.
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        userScrollEnabled = false
    ) {
        items(categories) { c -> CategoryCard(tile = c) }
    }
}

@Composable
private fun CategoryCard(tile: CategoryTile) {
    val shape = RoundedCornerShape(18.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(86.dp)
            .clip(shape)
            .shadow(10.dp, shape)
            .background(tile.tint.copy(alpha = 0.75f))
    ) {
        Image(
            painter = painterResource(tile.imageRes),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .width(80.dp),
            contentScale = ContentScale.Crop,
            alpha = 0.88f
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 12.dp, end = 88.dp)
        ) {
            Text(tile.title, style = MaterialTheme.typography.titleMedium.noFontPad(), fontWeight = FontWeight.ExtraBold, color = TextDark, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(tile.subtitle, style = MaterialTheme.typography.bodyMedium.noFontPad(), color = Orange, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun RecommendedCard(item: RecommendedItem, onTry: () -> Unit) {
    val shape = RoundedCornerShape(18.dp)

    Card(
        modifier = Modifier.shadow(10.dp, shape),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.82f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(item.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(item.title, style = MaterialTheme.typography.titleMedium.noFontPad(), fontWeight = FontWeight.Bold, color = TextDark)
                Text(item.meta, style = MaterialTheme.typography.bodyMedium.noFontPad(), color = TextMuted)
                Text("★ ${item.rating}", style = MaterialTheme.typography.bodyMedium.noFontPad(), color = TextMuted, fontWeight = FontWeight.SemiBold)
            }

            Button(
                onClick = onTry,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Orange),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 12.dp)
            ) {
                Text("Try", fontWeight = FontWeight.Bold)
            }
        }
    }
}