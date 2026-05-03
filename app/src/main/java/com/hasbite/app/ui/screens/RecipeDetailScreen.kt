package com.hasbite.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private val Orange = Color(0xFFE47A2E)
private val TextDark = Color(0xFF2E2E2E)

@Composable
fun RecipeDetailScreen(
    recipeId: String,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    var recipe by remember { mutableStateOf<Map<String, Any>?>(null) }
    var loading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }

    // 🔥 DATA FETCH
    LaunchedEffect(recipeId) {
        db.collection("recipes").document(recipeId).get()
            .addOnSuccessListener {
                recipe = it.data
                loading = false
            }
    }

    // 🔥 LOADING SCREEN
    if (loading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Orange)
        }
        return
    }

    recipe?.let { data ->

        val title = data["title"] as? String ?: ""
        val imageUrl = data["imageUrl"] as? String ?: ""
        val ingredients = data["ingredients"] as? List<String> ?: emptyList()
        val steps = data["steps"] as? List<String> ?: emptyList()
        val minutes = data["minutes"]?.toString() ?: "-"
        val rating = data["rating"]?.toString() ?: "-"
        val content = data["content"] as? String ?: ""

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFF6EFE7))
        ) {

            // 🔥 BACKGROUND IMAGE
            Image(
                painter = androidx.compose.ui.res.painterResource(id = com.hasbite.app.R.drawable.login_food_bg),
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
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 14.dp, bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // HEADER
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null)
                        }
                        Text(
                            text = "Recipe Detail",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // TITLE
                item {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }

                // INFO
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("⭐ $rating")
                        Text("🔥 $minutes min")
                    }
                }

                // IMAGE
                item {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                // INGREDIENTS
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.9f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            Text("Ingredients", fontWeight = FontWeight.Bold)

                            Spacer(Modifier.height(8.dp))

                            ingredients.forEach {
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
                        Column(modifier = Modifier.padding(16.dp)) {

                            Text("Steps", fontWeight = FontWeight.Bold)

                            Spacer(Modifier.height(8.dp))

                            steps.forEachIndexed { i, step ->
                                Text("${i + 1}. $step")
                                Spacer(Modifier.height(6.dp))
                            }
                        }
                    }
                }

                // FULL AI CONTENT
                if (content.isNotBlank()) {
                    item {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.9f)
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {

                                Text("Full Recipe", fontWeight = FontWeight.Bold)

                                Spacer(Modifier.height(8.dp))

                                val cleanContent = content
                                    .replace("*", "")
                                    .replace("#", "")

                                Text(
                                    text = cleanContent,
                                    color = TextDark
                                )
                            }
                        }
                    }
                }
            }

            // 🔥 SAVE BUTTON
            Button(
                onClick = {
                    val uid = auth.currentUser?.uid ?: return@Button
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
                colors = ButtonDefaults.buttonColors(containerColor = Orange)
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White
                    )
                } else {
                    Text("Save Recipe", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}