package com.hasbite.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

private val DetailCream = Color(0xFFF6EFE7)
private val DetailCreamTop = Color(0xE6F6EFE7)
private val DetailCreamBottom = Color(0xFFF6EFE7)
private val DetailOrange = Color(0xFFE47A2E)
private val DetailTextDark = Color(0xFF2E2E2E)
private val DetailTextMuted = Color(0xFF7A7A7A)

private fun TextStyle.noFontPad(): TextStyle =
    copy(platformStyle = PlatformTextStyle(includeFontPadding = false))
private data class RecipeDetailData(
    val title: String,
    val imageRes: Int,
    val rating: String,
    val kcal: String,
    val time: String,
    val ingredients: List<String>,
    val steps: List<String>
)

private fun getRecipeDetail(recipeId: String): RecipeDetailData {
    return when (recipeId) {
        "omelette" -> RecipeDetailData(
            title = "10 Minute Fit Omelette",
            imageRes = R.drawable.recipe_omelet,
            rating = "4.7",
            kcal = "280 kcal",
            time = "10 min",
            ingredients = listOf(
                "2 eggs",
                "1 tbsp milk",
                "1 tsp olive oil",
                "Salt and pepper",
                "Fresh herbs"
            ),
            steps = listOf(
                "Whisk the eggs with milk, salt and pepper.",
                "Heat olive oil in a non-stick pan.",
                "Pour the mixture and cook for 3-4 minutes.",
                "Fold gently and serve warm."
            )
        )

        "chicken" -> RecipeDetailData(
            title = "Grilled Chicken",
            imageRes = R.drawable.recipe_chicken,
            rating = "4.8",
            kcal = "420 kcal",
            time = "20 min",
            ingredients = listOf(
                "2 chicken breasts",
                "1 tbsp olive oil",
                "1 tsp paprika",
                "Salt and pepper",
                "Garlic powder"
            ),
            steps = listOf(
                "Season the chicken with oil and spices.",
                "Preheat the air fryer.",
                "Cook for 18-20 minutes, flipping halfway.",
                "Serve hot with your favorite side."
            )
        )

        "avocado" -> RecipeDetailData(
            title = "Avocado Toast",
            imageRes = R.drawable.recipe_avokado_toast,
            rating = "4.8",
            kcal = "320 kcal",
            time = "10 min",
            ingredients = listOf(
                "2 slices whole grain bread",
                "1 ripe avocado",
                "1 tsp lemon juice",
                "Salt and pepper",
                "Chili flakes"
            ),
            steps = listOf(
                "Toast the bread slices until golden.",
                "Mash the avocado with lemon juice, salt and pepper.",
                "Spread the avocado mixture over the toast.",
                "Top with chili flakes and serve immediately."
            )
        )

        "pasta" -> RecipeDetailData(
            title = "Creamy Tomato Pasta",
            imageRes = R.drawable.recipe_pasta,
            rating = "4.8",
            kcal = "450 kcal",
            time = "20 min",
            ingredients = listOf(
                "200 g pasta",
                "1 cup tomato sauce",
                "1/2 cup cream",
                "2 cloves garlic",
                "Parmesan cheese"
            ),
            steps = listOf(
                "Boil the pasta until al dente.",
                "Cook garlic in a pan for 1 minute.",
                "Add tomato sauce and cream, then simmer.",
                "Mix in pasta and top with parmesan."
            )
        )

        "pancake" -> RecipeDetailData(
            title = "Protein Pancakes",
            imageRes = R.drawable.recipe_pancake,
            rating = "4.9",
            kcal = "310 kcal",
            time = "15 min",
            ingredients = listOf(
                "1 banana",
                "2 eggs",
                "1 scoop protein powder",
                "2 tbsp oats",
                "1 tsp cinnamon"
            ),
            steps = listOf(
                "Blend all ingredients until smooth.",
                "Heat a non-stick pan lightly.",
                "Pour small portions and cook both sides.",
                "Serve with berries or honey."
            )
        )

        "cake" -> RecipeDetailData(
            title = "Chocolate Cake",
            imageRes = R.drawable.recipe_chocolate_cake,
            rating = "4.9",
            kcal = "510 kcal",
            time = "45 min",
            ingredients = listOf(
                "2 cups flour",
                "1 cup cocoa powder",
                "2 eggs",
                "1 cup milk",
                "1 cup sugar"
            ),
            steps = listOf(
                "Preheat the oven to 180°C.",
                "Mix flour, cocoa powder and sugar in a bowl.",
                "Add eggs and milk, then whisk until smooth.",
                "Pour into a cake pan and bake for 35-40 minutes."
            )
        )

        "quinoa_salad" -> RecipeDetailData(
            title = "Quinoa Chicken Salad",
            imageRes = R.drawable.recipe_quinoa_salad,
            rating = "4.6",
            kcal = "360 kcal",
            time = "20 min",
            ingredients = listOf(
                "1 cup cooked quinoa",
                "1 grilled chicken breast",
                "1 cucumber",
                "1 tomato",
                "1 tbsp olive oil",
                "1 tsp lemon juice",
                "Salt and pepper"
            ),
            steps = listOf(
                "Cook the quinoa and let it cool.",
                "Dice the grilled chicken, cucumber and tomato.",
                "Mix all ingredients in a large bowl.",
                "Add olive oil, lemon juice, salt and pepper.",
                "Toss well and serve fresh."
            )
        )

        else -> RecipeDetailData(
            title = "Creamy Chicken Rice",
            imageRes = R.drawable.recipe_chicken,
            rating = "4.8",
            kcal = "450 kcal",
            time = "20 min",
            ingredients = listOf(
                "1 cup rice",
                "2 chicken breasts, diced",
                "1 cup chicken broth",
                "1/2 cup cream"
            ),
            steps = listOf(
                "Cook the rice and set aside.",
                "Sauté diced chicken until golden.",
                "Add broth, cream and rice together."
            )
        )
    }
}

@Composable
fun RecipeDetailScreen(
    recipeId: String,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
){
    val recipe = getRecipeDetail(recipeId)
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DetailCream)
    ) {
        Image(
            painter = painterResource(R.drawable.login_food_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.18f
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            DetailCreamTop,
                            Color(0xCCF6EFE7),
                            DetailCreamBottom
                        )
                    )
                )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 14.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                DetailHeader(onBackClick = onBackClick)
            }

            item {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.headlineLarge.noFontPad(),
                    fontWeight = FontWeight.Bold,
                    color = DetailTextDark
                )
            }

            item {
                DetailInfoRow(
                    rating = recipe.rating,
                    kcal = recipe.kcal,
                    time = recipe.time
                )
            }

            item {
                Image(
                    painter = painterResource(recipe.imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(28.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            item {
                DetailSectionTitle("Ingredients")
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    recipe.ingredients.forEach { ingredient ->
                        IngredientItem(ingredient)
                    }
                }
            }

            item {
                DetailSectionTitle("Steps")
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    recipe.steps.forEachIndexed { index, step ->
                        StepItem(index + 1, step)
                    }
                }
            }

            item {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DetailOrange)
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

@Composable
private fun DetailHeader(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = Color.White.copy(alpha = 0.78f),
            shadowElevation = 8.dp
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = DetailTextDark
                )
            }
        }

        Spacer(Modifier.weight(1f))

        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = Color.White.copy(alpha = 0.78f),
            shadowElevation = 8.dp
        ) {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = DetailOrange
                )
            }
        }
    }
}

@Composable
private fun DetailInfoRow(
    rating: String,
    kcal: String,
    time: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        InfoItem(
            icon = { Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB800)) },
            text = rating
        )
        InfoItem(
            icon = { Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = DetailOrange) },
            text = kcal
        )
        InfoItem(
            icon = { Icon(Icons.Default.Timer, contentDescription = null, tint = DetailOrange) },
            text = time
        )
    }
}

@Composable
private fun InfoItem(
    icon: @Composable () -> Unit,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Spacer(Modifier.width(6.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.noFontPad(),
            color = DetailTextMuted
        )
    }
}

@Composable
private fun DetailSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall.noFontPad(),
        fontWeight = FontWeight.Bold,
        color = DetailTextDark
    )
}

@Composable
private fun IngredientItem(text: String) {
    Text(
        text = "• $text",
        style = MaterialTheme.typography.bodyLarge.noFontPad(),
        color = DetailTextDark
    )
}

@Composable
private fun StepItem(number: Int, text: String) {
    Text(
        text = "$number. $text",
        style = MaterialTheme.typography.bodyLarge.noFontPad(),
        color = DetailTextDark
    )
}