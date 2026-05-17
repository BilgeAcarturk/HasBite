package com.hasbite.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hasbite.app.R
import com.hasbite.app.ui.viewmodel.AIViewModel
import com.hasbite.app.ui.viewmodel.parseRecipe

private val Cream = Color(0xFFF6EFE7)
private val CreamTop = Color(0xE6F6EFE7)
private val CreamBottom = Color(0xFFF6EFE7)
private val Orange = Color(0xFFE47A2E)
private val SearchBarBg = Color(0xFFF3EEF0)
private val TextDark = Color(0xFF2E2E2E)
private val TextMuted = Color(0xFF7A7A7A)

private fun TextStyle.noFontPad(): TextStyle =
    copy(platformStyle = PlatformTextStyle(includeFontPadding = false))

data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

@Composable
fun AIRecipeScreen(
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    query: String = ""
) {

    val viewModel: AIViewModel = viewModel()

    val result by viewModel.result.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    var inputText by remember { mutableStateOf("") }

    val messages = remember {
        mutableStateListOf<ChatMessage>()
    }

    val parsed = remember(result) {
        if (result.isNotBlank()) parseRecipe(result) else null
    }

    val isValidRecipe =
        result.isNotBlank() &&
                !result.contains("INVALID_REQUEST")

    var isSaving by remember { mutableStateOf(false) }

    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    var selectedCategory by remember { mutableStateOf("Dinner") }
    var expanded by remember { mutableStateOf(false) }

    val categories = listOf(
        "Breakfast",
        "Dinner",
        "Dessert",
        "Healthy"
    )

    fun sendMessage(message: String) {

        val cleanMessage = message.trim()

        if (cleanMessage.isBlank()) return

        messages.add(
            ChatMessage(
                text = cleanMessage,
                isUser = true
            )
        )

        viewModel.generate(cleanMessage)

        inputText = ""
    }

    LaunchedEffect(query) {
        if (query.isNotBlank()) {
            sendMessage(query)
        }
    }

    LaunchedEffect(result) {

        if (result.isNotBlank()) {

            val alreadyExists = messages.any {
                !it.isUser && it.text == result
            }

            if (
                !alreadyExists &&
                !result.contains("INVALID_REQUEST")
            ) {

                messages.add(
                    ChatMessage(
                        text = result,
                        isUser = false
                    )
                )
            }

        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(R.drawable.login_food_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.35f
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            CreamTop,
                            Color(0xCCF6EFE7),
                            CreamBottom
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .navigationBarsPadding() .padding(16.dp)
        ) {

            // HEADER

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (onBackClick != null) {

                    Surface(
                        modifier = Modifier.size(42.dp),
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.88f),
                        shadowElevation = 8.dp
                    ) {

                        IconButton(
                            onClick = onBackClick
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = TextDark
                            )
                        }
                    }

                    Spacer(Modifier.width(12.dp))
                }

                Column {

                    Text(
                        text = "AI Recipe",
                        style = MaterialTheme.typography.headlineLarge.noFontPad(),
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    Text(
                        text = "Assistant",
                        style = MaterialTheme.typography.headlineMedium.noFontPad(),
                        color = TextMuted
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            // CHAT AREA

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {

                items(messages) { chat ->

                    if (chat.isUser) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {

                            Surface(
                                shape = RoundedCornerShape(22.dp),
                                color = Color(0xFFFFE7D3),
                                shadowElevation = 4.dp
                            ) {

                                Text(
                                    text = chat.text,
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 12.dp
                                    ),
                                    style = MaterialTheme.typography.bodyLarge.noFontPad(),
                                    color = TextDark
                                )
                            }
                        }

                    }
                }

                // LOADING

                if (loading) {

                    item {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Surface(
                                modifier = Modifier.size(44.dp),
                                shape = CircleShape,
                                color = Color(0xFFFFE0C7)
                            ) {

                                Box(
                                    contentAlignment = Alignment.Center
                                ) {

                                    Icon(
                                        Icons.Default.SmartToy,
                                        contentDescription = null,
                                        tint = Orange
                                    )
                                }
                            }

                            Spacer(Modifier.width(10.dp))

                            Surface(
                                shape = RoundedCornerShape(24.dp),
                                color = Color(0xFFFDFDFD), shadowElevation = 2.dp
                            ) {

                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    CircularProgressIndicator(
                                        modifier = Modifier.size(18.dp),
                                        strokeWidth = 2.dp,
                                        color = Orange
                                    )

                                    Spacer(Modifier.width(10.dp))

                                    Text(
                                        "Thinking...",
                                        color = TextMuted
                                    )
                                }
                            }
                        }
                    }
                }

                if (result.contains("INVALID_REQUEST")) {

                    item {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {

                            Surface(
                                shape = RoundedCornerShape(18.dp),
                                color = Color(0xFFFFE0E0)
                            ) {

                                Text(
                                    text = "Please ask for a food recipe.",
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 12.dp
                                    ),
                                    color = Color.Red,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }

// ERROR

                error?.let {

                    item {

                        Text(
                            text = it,
                            color = Color.Red
                        )
                    }
                }


                // RECIPE CARD

                if (isValidRecipe) {

                    parsed?.let { recipe ->

                        item {

                            Card(
                                modifier = Modifier .fillMaxWidth() .clip(RoundedCornerShape(26.dp)), shape = RoundedCornerShape(26.dp), colors = CardDefaults.cardColors( containerColor = Color(0xFFFDFBF9) ), elevation = CardDefaults.cardElevation(2.dp)
                            ) {

                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {

                                    Text(
                                        text = recipe.title,
                                        style = MaterialTheme.typography.headlineSmall.noFontPad(),
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark
                                    )

                                    Spacer(Modifier.height(12.dp))

                                    Text(
                                        "Ingredients",
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(Modifier.height(6.dp))

                                    recipe.ingredients.forEach { ingredient ->

                                        Text(
                                            "• $ingredient",
                                            color = TextDark
                                        )
                                    }

                                    Spacer(Modifier.height(12.dp))

                                    Text(
                                        "Steps",
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(Modifier.height(6.dp))

                                    recipe.steps.forEach { step ->

                                        Text(
                                            text = step
                                                .replace("**", "")
                                                .replace("*", ""),
                                            color = TextDark
                                        )
                                    }

                                    Spacer(Modifier.height(18.dp))

                                    Box {

                                        OutlinedButton(
                                            onClick = {
                                                expanded = true
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {

                                            Text("Category: $selectedCategory")
                                        }

                                        DropdownMenu(
                                            expanded = expanded,
                                            onDismissRequest = {
                                                expanded = false
                                            }
                                        ) {

                                            categories.forEach { category ->

                                                DropdownMenuItem(
                                                    text = {
                                                        Text(category)
                                                    },
                                                    onClick = {
                                                        selectedCategory = category
                                                        expanded = false
                                                    }
                                                )
                                            }
                                        }
                                    }

                                    Button(
                                        onClick = {

                                            val uid = auth.currentUser?.uid
                                                ?: return@Button

                                            isSaving = true

                                            val rawTitle = parsed.title

                                            val encodedTitle =
                                                java.net.URLEncoder.encode(
                                                    rawTitle,
                                                    "UTF-8"
                                                )

                                            val finalImageUrl =
                                                "https://source.unsplash.com/featured/?$encodedTitle"


                                            val recipeId = parsed.title
                                                .lowercase()
                                                .replace(" ", "_")
                                                .replace(Regex("[^a-z0-9_]"), "")

                                            val recipeData = hashMapOf(
                                                "id" to recipeId,
                                                "title" to rawTitle,
                                                "content" to result,
                                                "category" to selectedCategory,
                                                "ingredients" to recipe.ingredients,
                                                "steps" to recipe.steps,
                                                "imageUrl" to finalImageUrl,
                                                "minutes" to (15..45).random(),
                                                "rating" to 4.5,
                                                "saveCount" to 0
                                            )

                                            val docRef =
                                                db.collection("recipes")
                                                    .document(recipeId)

                                            docRef.set(recipeData)
                                                .addOnSuccessListener {

                                                    db.collection("users")
                                                        .document(uid)
                                                        .collection("saved_recipes")
                                                        .document(recipeId)
                                                        .set(recipeData)
                                                }
                                                .addOnCompleteListener {
                                                    isSaving = false
                                                }
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        enabled = !isSaving,
                                        shape = RoundedCornerShape(20.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Orange
                                        )
                                    ) {

                                        if (isSaving) {

                                            CircularProgressIndicator(
                                                modifier = Modifier.size(20.dp),
                                                color = Color.White,
                                                strokeWidth = 2.dp
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
                }
            }

            // INPUT AREA

            Surface(
                color = SearchBarBg,
                shape = RoundedCornerShape(28.dp),
                tonalElevation = 0.dp,
                shadowElevation = 8.dp
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 14.dp,
                            vertical = 10.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = Color(0xFF5B5B5B)
                    )

                    Spacer(Modifier.width(10.dp))

                    TextField(
                        value = inputText,
                        onValueChange = {
                            inputText = it
                        },
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text("Type your ingredients...")
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            focusedTextColor = TextDark,
                            unfocusedTextColor = TextDark
                        ),
                        singleLine = true
                    )

                    Icon(
                        Icons.Default.Mic,
                        contentDescription = null,
                        tint = TextDark
                    )

                    Spacer(Modifier.width(10.dp))

                    Surface(
                        modifier = Modifier
                            .size(42.dp)
                            .clickable {
                                sendMessage(inputText)
                            },
                        shape = CircleShape,
                        color = Orange
                    ) {

                        Box(
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                Icons.Default.Send,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
