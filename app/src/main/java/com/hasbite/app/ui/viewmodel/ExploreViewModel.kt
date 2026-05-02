package com.hasbite.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.hasbite.app.data.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ExploreViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    // UI'ın dinleyeceği canlı liste
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    init {
        fetchGlobalRecipes()
    }

    fun fetchGlobalRecipes() {
        viewModelScope.launch {
            try {
                // Senin oluşturacağın ana "recipes" koleksiyonuna bağlanıyoruz
                val snapshot = db.collection("recipes").get().await()
                val recipeList = snapshot.toObjects(Recipe::class.java)
                _recipes.value = recipeList
            } catch (e: Exception) {
                android.util.Log.e("FIREBASE_ERROR", "Veri çekilemedi: ${e.message}")
            }
        }
    }
}