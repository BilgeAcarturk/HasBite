package com.hasbite.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.hasbite.app.data.model.Recipe

class FavoritesViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    init {
        loadRecipes()
    }

    private fun loadRecipes() {
        val uid = auth.currentUser?.uid ?: return

        db.collection("users")
            .document(uid)
            .collection("favorites")
            .get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.documents.mapNotNull {
                    it.toObject(Recipe::class.java)
                }
                _recipes.value = list
            }
    }
}