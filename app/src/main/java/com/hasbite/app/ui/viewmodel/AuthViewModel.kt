package com.hasbite.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _loginState = MutableStateFlow<String?>(null)
    val loginState: StateFlow<String?> = _loginState

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginState.value = "SUCCESS"
                } else {
                    _loginState.value = task.exception?.message
                }
            }
    }

    fun register(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {

                val user = auth.currentUser ?: return@addOnSuccessListener

                val userData = hashMapOf(
                    "name" to name,
                    "email" to email,
                    "age" to 20
                )

                db.collection("users")
                    .document(user.uid)
                    .set(userData)
                    .addOnSuccessListener {
                        _loginState.value = "REGISTER_SUCCESS"
                    }
                    .addOnFailureListener {
                        _loginState.value = it.message
                    }
            }
            .addOnFailureListener {
                _loginState.value = it.message
            }
    }
}