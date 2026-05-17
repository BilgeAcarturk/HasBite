package com.hasbite.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hasbite.app.data.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FavoritesViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds

    init {
        listenFavorites()
    }

    private fun listenFavorites() {

        val uid = auth.currentUser?.uid ?: return

        db.collection("users")
            .document(uid)
            .collection("favorites")
            .addSnapshotListener { snapshot, error ->

                if (error != null || snapshot == null) {
                    return@addSnapshotListener
                }

                val list = snapshot.documents.mapNotNull { doc ->

                    doc.toObject(Recipe::class.java)
                }

                _recipes.value = list

                _favoriteIds.value = list.map { it.id }.toSet()
            }
    }

    fun addFavorite(recipe: Recipe) {

        val uid = auth.currentUser?.uid ?: return

        db.collection("users")
            .document(uid)
            .collection("favorites")
            .document(recipe.id)
            .set(recipe)

        db.collection("recipes")
            .document(recipe.id)
            .update(
                "saveCount",
                com.google.firebase.firestore.FieldValue.increment(1)
            )
    }

    fun removeFavorite(recipeId: String) {

        val uid = auth.currentUser?.uid ?: return

        db.collection("users")
            .document(uid)
            .collection("favorites")
            .document(recipeId)
            .delete()

        db.collection("recipes")
            .document(recipeId)
            .update(
                "saveCount",
                com.google.firebase.firestore.FieldValue.increment(-1)
            )
    }

    fun toggleFavorite(recipe: Recipe) {

        if (isFavorite(recipe.id)) {
            removeFavorite(recipe.id)
        } else {
            addFavorite(recipe)
        }
    }

    fun isFavorite(recipeId: String): Boolean {

        return _favoriteIds.value.contains(recipeId)
    }
}