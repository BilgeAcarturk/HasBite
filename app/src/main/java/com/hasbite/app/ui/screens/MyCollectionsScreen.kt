package com.hasbite.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.RestaurantMenu
import androidx.compose.material3.*
import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hasbite.app.ui.viewmodel.SavedRecipesViewModel
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

private data class CollectionItem(
    val title: String,
    val subtitle: String,
    val imageRes: Int,
    val iconType: String
)

@Composable
fun MyCollectionsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onOpenCollection: (String) -> Unit
) {

    val viewModel: SavedRecipesViewModel = viewModel()
    val recipes by viewModel.recipes.collectAsState()

    // 🔥 Firestore’dan gelen veriye göre sayım
    val healthyCount = recipes.count { it.category == "Healthy" }
    val dessertCount = recipes.count { it.category == "Dessert" }
    val dinnerCount = recipes.count { it.category == "Dinner" }
    val breakfastCount = recipes.count { it.category == "Breakfast" }

    val collections = listOf(
        CollectionItem("Healthy Meals", "$healthyCount recipes", R.drawable.recipe_omelet, "Healthy"),
        CollectionItem("Dessert Picks", "$dessertCount recipes", R.drawable.recipe_chocolate_cake, "Dessert"),
        CollectionItem("Quick Dinners", "$dinnerCount recipes", R.drawable.recipe_chicken, "Dinner"),
        CollectionItem("Breakfast Ideas", "$breakfastCount recipes", R.drawable.recipe_pancake, "Breakfast")
    )

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
                .statusBarsPadding()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 14.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                CollectionsHeader(onBackClick = onBackClick)
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
                            .padding(18.dp)
                    ) {
                        Text(
                            text = "Saved Recipe Collections",
                            style = MaterialTheme.typography.titleLarge.noFontPad(),
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = "Group your favorite meals by category and mood",
                            style = MaterialTheme.typography.bodyMedium.noFontPad(),
                            color = TextMuted
                        )

                        Spacer(Modifier.height(18.dp))

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(420.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            userScrollEnabled = false
                        ) {
                            items(collections) { item ->
                                CollectionCard(
                                    item = item,
                                    onClick = {
                                        onOpenCollection(item.iconType) // 🔥 category gönderiyoruz
                                    }
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
private fun CollectionsHeader(onBackClick: () -> Unit) {
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
                text = "My Collections",
                style = MaterialTheme.typography.headlineMedium.noFontPad(),
                fontWeight = FontWeight.ExtraBold,
                color = TextDark
            )
            Text(
                text = "Organize your saved recipes",
                style = MaterialTheme.typography.bodyLarge.noFontPad(),
                color = TextMuted
            )
        }
    }
}

@Composable
private fun CollectionCard(item: CollectionItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .shadow(8.dp, RoundedCornerShape(22.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFCFA)
        )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(105.dp)
            ) {
                Image(
                    painter = painterResource(item.imageRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Surface(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopEnd),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.88f)
                ) {
                    Box(
                        modifier = Modifier.padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        when (item.iconType) {
                            "healthy" -> Icon(Icons.Outlined.RestaurantMenu, null, tint = Orange)
                            "dessert" -> Icon(Icons.Outlined.FavoriteBorder, null, tint = Orange)
                            else -> Icon(Icons.Outlined.BookmarkBorder, null, tint = Orange)
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium.noFontPad(),
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodyMedium.noFontPad(),
                    color = TextMuted
                )
            }
        }
    }
}