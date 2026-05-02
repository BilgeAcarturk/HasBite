package com.hasbite.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hasbite.app.R
import com.hasbite.app.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

private val PassCreamBg = Color(0xFFF6EFE7)
private val PassOrange = Color(0xFFE47A2E)
private val PassCardBg = Color(0xFFF9F3ED)

@Composable
fun ChangePasswordScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    val viewModel: AuthViewModel = viewModel()

    val loading by viewModel.loading.collectAsState()
    val message by viewModel.message.collectAsState()

    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PassCreamBg)
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
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 14.dp, bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            item {
                PassHeader(onBackClick)
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(28.dp)),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = PassCardBg)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {

                        PasswordField("New Password", newPassword) {
                            newPassword = it
                        }

                        Spacer(Modifier.height(12.dp))

                        PasswordField("Confirm Password", confirmPassword) {
                            confirmPassword = it
                        }

                        Spacer(Modifier.height(22.dp))

                        Button(
                            onClick = {
                                if (newPassword == confirmPassword) {
                                    viewModel.changePassword(newPassword)
                                } else {
                                    // local error
                                }
                            },
                            enabled = !loading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(22.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PassOrange)
                        ) {
                            if (loading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(22.dp)
                                )
                            } else {
                                Text("Change Password", fontWeight = FontWeight.Bold)
                            }
                        }

                        message?.let {
                            Spacer(Modifier.height(12.dp))

                            Text(
                                text = it,
                                color = if (it.contains("success", true))
                                    Color(0xFF2E7D32)
                                else
                                    Color.Red
                            )

                            LaunchedEffect(it) {
                                delay(3000)
                                viewModel.clearMessage()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PassHeader(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(Icons.Default.ArrowBack, contentDescription = null)
        }

        Text(
            text = "Change Password",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun PasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(label)

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}