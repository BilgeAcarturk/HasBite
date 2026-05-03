package com.hasbite.app.ui.screens

// 🔥 COMPOSE
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
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
    onOpenRecipe: (String) -> Unit
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
                recipes = result.documents.mapNotNull {
                    it.id to (it.data ?: return@mapNotNull null)
                }
                loading = false
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6EFE7))
    ) {

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

        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
            return
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // HEADER
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, null)
                    }

                    Text(
                        text = category,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // LIST
            items(recipes) { (id, data) ->

                val title = data["title"] as? String ?: ""
                val image = data["imageUrl"] as? String ?: ""
                val minutes = data["minutes"]?.toString() ?: "-"

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenRecipe(id) },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFF8A3D) // 🔥 turuncu
                    ),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {

                    Row {
                        AsyncImage(
                            model = image,
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(20.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Column(
                            modifier = Modifier
                                .padding(12.dp)
                                .weight(1f)
                        ) {
                            Text(title, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                            Text("⏱ $minutes min")
                        }
                    }
                }
            }
        }
    }
}