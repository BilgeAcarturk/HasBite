package com.hasbite.app.ui.screens

// 🔥 COMPOSE
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.ArrowBack
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

// 🔥 IMAGE
import coil.compose.AsyncImage

// 🔥 FIREBASE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun CollectionDetailScreen(
    category: String,
    onBackClick: () -> Unit,
    onOpenRecipe: (String, Boolean) -> Unit
) {

    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    var recipes by remember { mutableStateOf<List<Pair<String, Map<String, Any>>>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    // 🔥 FETCH
    LaunchedEffect(category) {
        val uid = auth.currentUser?.uid ?: return@LaunchedEffect

        db.collection("users")
            .document(uid)
            .collection("saved_recipes")
            .whereEqualTo("category", category)
            .get()
            .addOnSuccessListener { result ->
                recipes = result.documents.mapNotNull { doc ->
                    val data = doc.data
                    if (data != null) doc.id to data else null
                }
                loading = false
            }
            .addOnFailureListener {
                loading = false
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6EFE7))
    ) {

        // 🔥 BACKGROUND IMAGE
        Image(
            painter = painterResource(id = com.hasbite.app.R.drawable.login_food_bg),
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

        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color(0xFFE47A2E),
                    modifier = Modifier.size(48.dp)
                )
            }
            return@Box
        }

        // 🔥 EMPTY STATE
        if (recipes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "📭",
                        fontSize = MaterialTheme.typography.displayLarge.fontSize
                    )
                    Text(
                        text = "No recipes found in '$category'",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Button(
                        onClick = onBackClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE47A2E)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Go Back")
                    }
                }
            }
            return@Box
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 32.dp
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // HEADER
            item {
                Row(
                    modifier = Modifier.padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF2E2E2E)
                        )
                    }

                    Text(
                        text = category,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E2E2E)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Recipe count badge
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFE47A2E).copy(alpha = 0.2f),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "${recipes.size}",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE47A2E)
                            )
                        }
                    }
                }
            }

            // 🔥 RECIPES LIST
            items(recipes) { (id, data) ->

                val title = data["title"] as? String ?: "Untitled Recipe"
                val imageUrl = data["imageUrl"] as? String ?: ""
                val minutes = data["minutes"]?.toString() ?: "?"
                val rating = data["rating"] as? Double ?: 0.0

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenRecipe(id, true) },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Row(
                        modifier = Modifier.height(120.dp)
                    ) {
                        // 🔥 IMAGE SECTION
                        Box(
                            modifier = Modifier
                                .width(120.dp)
                                .fillMaxHeight()
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 20.dp,
                                        bottomStart = 20.dp
                                    )
                                )
                                .background(Color(0xFFFF8A3D).copy(alpha = 0.3f))
                        ) {
                            when {
                                imageUrl.isNotBlank() && imageUrl.startsWith("http") -> {
                                    AsyncImage(
                                        model = imageUrl,
                                        contentDescription = title,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop,
                                        error = painterResource(id = com.hasbite.app.R.drawable.default_recipe)
                                    )
                                }
                                else -> {
                                    Image(
                                        painter = painterResource(id = com.hasbite.app.R.drawable.default_recipe),
                                        contentDescription = "Default recipe image",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }

                        // 🔥 CONTENT SECTION
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = title,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color(0xFF2E2E2E),
                                    maxLines = 2
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                // Rating stars
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "⭐",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = String.format("%.1f", rating),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color(0xFFE47A2E),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "⏱ $minutes min",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Gray
                                    )
                                }
                            }

                            // Category chip
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFE47A2E).copy(alpha = 0.15f),
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text(
                                    text = category,
                                    fontSize = MaterialTheme.typography.labelSmall.fontSize,
                                    color = Color(0xFFE47A2E),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}