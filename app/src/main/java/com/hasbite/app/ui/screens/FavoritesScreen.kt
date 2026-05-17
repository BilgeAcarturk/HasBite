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
import androidx.compose.material3.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hasbite.app.data.model.Recipe
import com.hasbite.app.ui.viewmodel.FavoritesViewModel
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
import coil.compose.AsyncImage
import com.hasbite.app.R

// ---------- Palette (match Explore) ----------
private val CreamBg = Color(0xFFF6EFE7)
private val Orange = Color(0xFFE47A2E)
private val TextDark = Color(0xFF2A2A2A)
private val TextMuted = Color(0xFF8A8A8A)

private fun TextStyle.noFontPad(): TextStyle =
    copy(platformStyle = PlatformTextStyle(includeFontPadding = false))

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    onOpenRecipeDetail: (String) -> Unit = {}
) {

    val listState = rememberLazyListState()
    var selectedTab by remember { mutableStateOf(0) }

    val favoritesViewModel: FavoritesViewModel = viewModel()

    val recipes by favoritesViewModel.recipes.collectAsState()

    val tabs = listOf(
        "Breakfast",
        "Dinner",
        "Dessert",
        "Healthy"
    )

    val selectedCategory = tabs[selectedTab]

    val filteredItems = remember(recipes, selectedTab) {

        recipes.filter {

            it.category.equals(
                selectedCategory,
                ignoreCase = true
            )
        }
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
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(
                top = 14.dp,
                bottom = 120.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            item {

                FavoritesHeader(
                    title = "Favorites",
                    subtitle = "Your favorite recipes"
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
    subtitle: String
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

        Brush.verticalGradient(
            listOf(
                Color(0xFFFF8A3D),
                Orange
            )
        )

    } else {

        Brush.verticalGradient(
            listOf(
                Color(0xFFFFFBF8),
                Color(0xFFFFFBF8)
            )
        )
    }

    val textColor =
        if (selected) Color.White
        else TextDark

    Box(
        modifier = Modifier
            .height(46.dp)
            .shadow(
                if (selected) 10.dp else 7.dp,
                shape,
                clip = false
            )
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
    items: List<Recipe>,
    onOpenRecipeDetail: (String) -> Unit
) {

    val shape = RoundedCornerShape(26.dp)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(14.dp, shape),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFBF8)
        )
    ) {

        Column(modifier = Modifier.fillMaxWidth()) {

            items.forEachIndexed { index, item ->

                FavoriteRow(
                    item = item,
                    onClick = {
                        if (item.id.isNotBlank()) {
                            onOpenRecipeDetail(item.id)
                        }
                    }
                )

                if (index != items.lastIndex) {

                    Divider(
                        modifier = Modifier.padding(
                            start = 112.dp,
                            end = 14.dp
                        ),
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
    item: Recipe,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = 14.dp,
                vertical = 14.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = item.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(86.dp)
                .clip(RoundedCornerShape(18.dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.default_recipe),
            error = painterResource(R.drawable.default_recipe)
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

            Spacer(Modifier.height(6.dp))

            Text(
                text = "${item.minutes} min",
                style = MaterialTheme.typography.bodyLarge.noFontPad(),
                color = TextMuted
            )

            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "★ ${item.rating}",
                    style = MaterialTheme.typography.bodyLarge.noFontPad(),
                    color = TextMuted,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.width(10.dp))

                Text(
                    text = item.category,
                    style = MaterialTheme.typography.bodyLarge.noFontPad(),
                    color = TextMuted
                )
            }
        }
    }
}