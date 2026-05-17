package com.hasbite.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hasbite.app.R
import com.hasbite.app.data.model.Recipe
import com.hasbite.app.ui.viewmodel.FavoritesViewModel

private val Orange = Color(0xFFE47A2E)
private val TextDark = Color(0xFF2E2E2E)

@Composable
fun RecipeDetailScreen(
    recipeId: String,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    isSavedRecipe: Boolean = false
) {

    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    var recipe by remember { mutableStateOf<Recipe?>(null) }
    var loading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }

    val favoritesViewModel: FavoritesViewModel = viewModel()
    val favoriteIds by favoritesViewModel.favoriteIds.collectAsState()

    // 🔥 DATA FETCH
    LaunchedEffect(recipeId) {

        db.collection("recipes")
            .document(recipeId)
            .get()
            .addOnSuccessListener {

                recipe = it.toObject(Recipe::class.java)
                    ?.copy(id = it.id)

                loading = false
            }
            .addOnFailureListener {

                loading = false
            }
    }

    // 🔥 LOADING SCREEN
    if (loading) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            CircularProgressIndicator(color = Orange)
        }

        return
    }

    // 🔥 RECIPE NOT FOUND
    if (recipe == null) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "Recipe not found",
                color = TextDark
            )
        }

        return
    }

    recipe?.let { data ->

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFF6EFE7))
        ) {

            // 🔥 BACKGROUND IMAGE
            Image(
                painter = painterResource(id = R.drawable.login_food_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.35f
            )

            // 🔥 GRADIENT OVERLAY
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xE6F6EFE7),
                                Color(0xCCF6EFE7),
                                Color(0xFFF6EFE7)
                            )
                        )
                    )
            )

            // 🔥 CONTENT
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(
                    top = 14.dp,
                    bottom = 120.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // HEADER
                item {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {

                            IconButton(onClick = onBackClick) {

                                Icon(
                                    Icons.Default.ArrowBack,
                                    contentDescription = null
                                )
                            }

                            Text(
                                text = "Recipe Detail",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        IconButton(
                            onClick = {
                                favoritesViewModel.toggleFavorite(data)
                            }
                        ) {

                            Icon(
                                imageVector =
                                    if (favoriteIds.contains(recipeId))
                                        Icons.Filled.Favorite
                                    else
                                        Icons.Outlined.FavoriteBorder,
                                contentDescription = null,
                                tint =
                                    if (favoriteIds.contains(recipeId))
                                        Color.Red
                                    else
                                        TextDark
                            )
                        }
                    }
                }

                // TITLE
                item {

                    Text(
                        text = data.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }

                // INFO
                item {

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        Text("⭐ ${data.rating}")
                        Text("🔥 ${data.minutes} min")
                    }
                }

                // IMAGE
                item {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.LightGray)
                    ) {

                        if (
                            data.imageUrl.isNotBlank() &&
                            data.imageUrl.startsWith("http")
                        ) {

                            AsyncImage(
                                model = data.imageUrl,
                                contentDescription = "Recipe image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                error = painterResource(R.drawable.default_recipe)
                            )

                        } else {

                            Image(
                                painter = painterResource(R.drawable.default_recipe),
                                contentDescription = "Default recipe image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                // INGREDIENTS
                item {

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.9f)
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                "Ingredients",
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(8.dp))

                            data.ingredients.forEach {

                                Text("• $it")
                            }
                        }
                    }
                }

                // STEPS
                item {

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.9f)
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                "Steps",
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(8.dp))

                            data.steps.forEachIndexed { i, step ->

                                Text("${i + 1}. $step")

                                Spacer(Modifier.height(6.dp))
                            }
                        }
                    }
                }
            }

            // 🔥 SAVE BUTTON
            if (!isSavedRecipe) {

                Button(
                    onClick = {

                        val uid =
                            auth.currentUser?.uid
                                ?: return@Button

                        isSaving = true

                        db.collection("users")
                            .document(uid)
                            .collection("saved_recipes")
                            .document(recipeId)
                            .set(data)
                            .addOnSuccessListener {

                                isSaving = false
                            }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(54.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Orange
                    )
                ) {

                    if (isSaving) {

                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White
                        )

                    } else {

                        Text(
                            "Save Recipe",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}