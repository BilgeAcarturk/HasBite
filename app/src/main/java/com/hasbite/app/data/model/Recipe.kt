package com.hasbite.app.data.model

data class Recipe(
    val id: String = "",
    val title: String = "",
    val category: String = "Dinner", // Bilge'nin kullandığı alan adı
    val minutes: Int = 0,
    val rating: Double = 4.5,
    val imageUrl: String = "", // İnternet adresi
    val ingredients: List<String> = emptyList(),
    val steps: List<String> = emptyList(),
    val content: String = "", // AI'dan gelen ham metni tutmak için
    val saveCount: Int = 0
)