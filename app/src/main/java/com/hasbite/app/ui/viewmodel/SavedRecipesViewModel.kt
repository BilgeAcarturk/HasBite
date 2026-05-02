package com.hasbite.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class SavedRecipe(
    val title: String = "",
    val content: String = "",
    val category: String = ""
)

class SavedRecipesViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _recipes = MutableStateFlow<List<SavedRecipe>>(emptyList())
    val recipes: StateFlow<List<SavedRecipe>> = _recipes

    init {
        loadRecipes()
    }

    private fun loadRecipes() {
        val uid = auth.currentUser?.uid ?: return

        db.collection("users")
            .document(uid)
            .collection("saved_recipes")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val list = snapshot.documents.map {
                        SavedRecipe(
                            title = it.getString("title") ?: "",
                            content = it.getString("content") ?: "",
                            category = it.getString("category") ?: ""
                        )
                    }
                    _recipes.value = list
                }
            }
    }
}