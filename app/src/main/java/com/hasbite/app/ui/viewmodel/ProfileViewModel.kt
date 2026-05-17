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

    fun updateUser(name: String, age: Int, bio: String) {
        val uid = auth.currentUser?.uid ?: return

        val updates = mapOf(
            "name" to name,
            "age" to age,
            "bio" to bio
        )

        db.collection("users")
            .document(uid)
            .update(updates)
    }

    fun refreshUser() {
        loadUser()
    }

    fun updateAvatar(avatarName: String) {

        val uid = auth.currentUser?.uid ?: return

        db.collection("users")
            .document(uid)
            .update("avatar", avatarName)

        refreshUser()
    }

    private val _searchResults = MutableStateFlow<List<User>>(emptyList())
    val searchResults: StateFlow<List<User>> = _searchResults

    fun searchUsers(query: String) {

        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        db.collection("users")
            .whereGreaterThanOrEqualTo("name", query)
            .whereLessThanOrEqualTo("name", query + "\uf8ff")
            .get()
            .addOnSuccessListener { snapshot ->

                val users = snapshot.documents.mapNotNull { doc ->

                    val user = doc.toObject(User::class.java)

                    user?.copy(uid = doc.id)

                }.filter {

                    !it.privateAccount
                }

                _searchResults.value = users
            }
    }

    fun updatePrivateAccount(isPrivate: Boolean) {

        val uid = auth.currentUser?.uid ?: return

        db.collection("users")
            .document(uid)
            .update("privateAccount", isPrivate)

        refreshUser()
    }
}