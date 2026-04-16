package com.hasbite.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hasbite.app.R

private val Cream = Color(0xFFF6EFE7)          // ana arka plan
private val CreamTop = Color(0xE6F6EFE7)       // üstte daha transparan
private val CreamBottom = Color(0xFFF6EFE7)    // altta tam cream
private val Orange = Color(0xFFE47A2E)         // ana turuncu
private val ChipBg = Color(0xFFFFE7D3)         // chip bg
private val SearchBarBg = Color(0xFFF3EEF0)    // arama çubuğu + bottom bar bg
private val CardBg = Color(0xFFF3EEF0)         // kart bg (soft gri-mor)

private data class HomeRecipe(
    val title: String,
    val category: String,
    val imageRes: Int,
    val recipeId: String
)

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onOpenAI: () -> Unit = {},
    onOpenRecipeDetail: (String) -> Unit = {}
) {
    var selectedCategory by remember { mutableStateOf("Breakfast") }

    val categories = listOf("All", "Breakfast", "Dessert", "Chicken", "Fit", "Vegan")

    val recipes = listOf(
        HomeRecipe(
            title = "10 Minute Fit Omelette",
            category = "Breakfast",
            imageRes = R.drawable.recipe_omelet,
            recipeId = "omelette"
        ),
        HomeRecipe(
            title = "Air fryer Chicken",
            category = "Chicken",
            imageRes = R.drawable.recipe_chicken,
            recipeId = "chicken"
        ),
        HomeRecipe(
            title = "Chocolate Cake",
            category = "Dessert",
            imageRes = R.drawable.recipe_chocolate_cake,
            recipeId = "cake"
        ),
        HomeRecipe(
            title = "Protein Pancakes",
            category = "Fit",
            imageRes = R.drawable.recipe_pancake,
            recipeId = "pancake"
        ),
        HomeRecipe(
            title = "Avocado Toast",
            category = "Vegan",
            imageRes = R.drawable.recipe_avokado_toast,
            recipeId = "avocado"
        )
    )

    val filteredRecipes = if (selectedCategory == "All") {
        recipes
    } else {
        recipes.filter { it.category == selectedCategory }
    }

    Box(modifier = modifier.fillMaxSize()) {

        // 1) Background image (blur hissi için full + üstüne overlay)
        Image(
            painter = painterResource(R.drawable.login_food_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.35f // ÖNEMLİ: foto çok baskın olmasın ama görülsün
        )

        // 2) Cream overlay (blur/soft look burada)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            CreamTop,               // üst: transparan cream
                            Color(0xCCF6EFE7),      // orta: biraz daha yoğun
                            CreamBottom             // alt: tam cream
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Discover",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E2E2E)
                    )
                    Text(
                        "What should we cook today?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF7A7A7A)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFE0C7)),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = onOpenAI) {
                        Icon(
                            Icons.Default.Restaurant,
                            contentDescription = null,
                            tint = Orange
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // SEARCH BAR (rengi bottom bar ile aynı: SearchBarBg)
            Surface(
                color = SearchBarBg,
                shape = RoundedCornerShape(28.dp),
                tonalElevation = 0.dp,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Search, null, tint = Color(0xFF5B5B5B))
                    Spacer(Modifier.width(10.dp))
                    Text(
                        "Search recipes (pasta, chicken...)",
                        color = Color(0xFF7A7A7A),
                        modifier = Modifier.weight(1f)
                    )
                    Icon(Icons.Default.Mic, null, tint = Orange)
                }
            }

            Spacer(Modifier.height(18.dp))

            // CATEGORY CHIPS
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(categories) { category ->
                    val isSelected = category == selectedCategory

                    Surface(
                        onClick = { selectedCategory = category },
                        shape = RoundedCornerShape(50),
                        color = if (isSelected) Orange else ChipBg,
                        tonalElevation = 0.dp,
                        shadowElevation = 0.dp
                    ) {
                        Text(
                            text = category,
                            color = if (isSelected) Color.White else Orange,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(22.dp))

            Text(
                "Featured Recipes",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E2E2E)
            )

            Spacer(Modifier.height(12.dp))

            filteredRecipes.forEach { recipe ->
                RecipeCard(
                    title = recipe.title,
                    image = recipe.imageRes,
                    onViewClick = { onOpenRecipeDetail(recipe.recipeId) }
                )
            }
        }
    }
}

@Composable
fun RecipeCard(
    title: String,
    image: Int,
    onViewClick: () -> Unit = {}
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier
                    .size(84.dp)
                    .clip(RoundedCornerShape(18.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF3A3A3A)
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    "Fast • Easy • Popular",
                    color = Color(0xFF7A7A7A),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(
                onClick = onViewClick,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Orange),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp)
            ) {
                Text("View", fontWeight = FontWeight.Bold)
            }
        }
    }
}