package com.hasbite.app.ui.viewmodel

import android.util.Log
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

    // 🔥 Yeni: Yüklenme durumunu takip ediyoruz
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchGlobalRecipes()
    }

    fun fetchGlobalRecipes() {
        viewModelScope.launch {
            _isLoading.value = true // Yükleme başladı
            try {
                // Firebase'den verileri 'saveCount' değerine göre büyükten küçüğe çekiyoruz
                db.collection("recipes")
                    .orderBy("saveCount", com.google.firebase.firestore.Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener { result ->
                        val recipeList = result.map { document ->
                            document.toObject(com.hasbite.app.data.model.Recipe::class.java)
                                .copy(id = document.id)
                        }
                        _recipes.value = recipeList
                        _isLoading.value = false // Yükleme bitti
                    }
                    .addOnFailureListener { exception ->
                        Log.e("FIREBASE_ERROR", "Veri çekilemedi: ", exception)
                        _isLoading.value = false
                    }
            } catch (e: Exception) {
                Log.e("FIREBASE_ERROR", "Hata: ${e.message}")
                _isLoading.value = false
            }
        }
    }
}