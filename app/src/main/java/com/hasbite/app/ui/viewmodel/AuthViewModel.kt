package com.hasbite.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // LOGIN STATE
    private val _loginState = MutableStateFlow<String?>(null)
    val loginState: StateFlow<String?> = _loginState

    // 🔥 NEW STATES
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    // LOGIN
    fun login(email: String, password: String) {
        _loading.value = true

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _loading.value = false
                if (task.isSuccessful) {

                    // 🔥 BURAYA EKLİYORSUN
                    val user = auth.currentUser

                    if (user != null && user.isEmailVerified) {
                        _loginState.value = "SUCCESS"
                    } else {
                        _loginState.value = "Please verify your email before logging in"
                    }

                } else {
                    _loginState.value = task.exception?.message
                }
            }
    }

    // REGISTER
    fun register(name: String, email: String, password: String) {

        _loading.value = true

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {

                val user = auth.currentUser ?: return@addOnSuccessListener

                // 🔥 EMAIL VERIFICATION GÖNDER
                user.sendEmailVerification()

                val userData = hashMapOf(
                    "name" to name,
                    "email" to email,
                    "age" to 20
                )

                db.collection("users")
                    .document(user.uid)
                    .set(userData)
                    .addOnSuccessListener {

                        // 🔥 BURASI DEĞİŞTİ
                        _loginState.value = "VERIFY_EMAIL"
                        _loading.value = false
                    }
                    .addOnFailureListener {
                        _loginState.value = it.message
                        _loading.value = false
                    }
            }
            .addOnFailureListener {
                _loginState.value = it.message
                _loading.value = false
            }
    }

    // 🔥 FORGOT PASSWORD
    fun sendReset(email: String) {
        if (email.isBlank()) {
            _message.value = "Email boş olamaz"
            return
        }

        _loading.value = true

        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                _message.value = "Reset maili gönderildi"
                _loading.value = false
            }
            .addOnFailureListener {
                _message.value = it.message ?: "Hata oluştu"
                _loading.value = false
            }
    }

    // 🔥 CHANGE PASSWORD
    fun changePassword(newPassword: String) {

        val user = auth.currentUser

        if (user == null) {
            _message.value = "User not logged in"
            return
        }

        if (newPassword.length < 6) {
            _message.value = "Password must be at least 6 characters"
            return
        }

        _loading.value = true

        user.updatePassword(newPassword)
            .addOnSuccessListener {
                _message.value = "Password updated successfully"
                _loading.value = false
            }
            .addOnFailureListener {
                _message.value = it.message ?: "Error occurred"
                _loading.value = false
            }
    }

    // CLEAR MESSAGE
    fun clearMessage() {
        _message.value = null
    }
}