package com.hasbite.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hasbite.app.R
import com.hasbite.app.ui.viewmodel.AuthViewModel

@Composable
fun ForgotPasswordScreen(
    onBackToLoginClick: () -> Unit
) {

    val viewModel: AuthViewModel = viewModel()

    var email by remember { mutableStateOf("") }

    val loading by viewModel.loading.collectAsState()
    val message by viewModel.message.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.login_food_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x00000000),
                            Color(0xB3FF6A00),
                            Color(0xE6FF6A00)
                        )
                    )
                )
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            color = Color.White,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            tonalElevation = 0.dp,
            shadowElevation = 10.dp
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 22.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Forgot Password",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Enter your email to reset your password",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF666666)
                )

                Spacer(Modifier.height(18.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(Modifier.height(18.dp))

                Button(
                    onClick = {
                        viewModel.sendReset(email)
                    },
                    enabled = !loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE96A1A)
                    )
                ) {
                    if (loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(22.dp)
                        )
                    } else {
                        Text("Send Reset Link", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(Modifier.height(12.dp))

                TextButton(onClick = onBackToLoginClick) {
                    Text("Back to login")
                }

                Spacer(Modifier.height(6.dp))

                // 🔥 MESSAGE
                message?.let {
                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = it,
                        color = if (it.contains("sent")) Color(0xFF2E7D32) else Color.Red
                    )

                    LaunchedEffect(it) {
                        kotlinx.coroutines.delay(3000)
                        viewModel.clearMessage()
                    }
                }
            }
        }
    }
}