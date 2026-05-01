package com.hasbite.app.ui.viewmodel
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hasbite.app.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    init {
        loadUser()
    }

    private fun loadUser() {
        val uid = auth.currentUser?.uid ?: return

        println("UID: $uid")

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->

                println("DOC EXISTS: ${doc.exists()}")
                println("RAW DATA: ${doc.data}")

                val user = doc.toObject(User::class.java)

                println("PARSED USER: $user")

                _user.value = user
            }
            .addOnFailureListener {
                println("ERROR: ${it.message}")
            }
    }

    fun updateUser(name: String, age: Int, bio: String, email: String) {
        val uid = auth.currentUser?.uid ?: return

        val updates = mapOf(
            "name" to name,
            "age" to age,
            "bio" to bio,
            "email" to email
        )

        db.collection("users")
            .document(uid)
            .update(updates)
    }

    fun refreshUser() {
        loadUser()
    }
}