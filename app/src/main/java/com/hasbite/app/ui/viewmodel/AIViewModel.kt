package com.hasbite.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasbite.app.network.GeminiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AIViewModel : ViewModel() {

    private val _result = MutableStateFlow("")
    val result: StateFlow<String> = _result

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun generate(prompt: String) {
        if (prompt.isBlank()) return

        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            _error.value = null
            try {
                val response = GeminiService.generateRecipe(prompt)
                if (response.isNullOrBlank()) {
                    _error.value = "No response"
                } else {
                    _result.value = response
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

}

data class ParsedRecipe(
    val title: String,
    val ingredients: List<String>,
    val steps: List<String>
)
fun parseRecipe(text: String): ParsedRecipe {

    val cleanedText = text
        .replace("**", "")
        .replace("*", "")
        .replace("#", "")

    val lines = cleanedText
        .lines()
        .map { it.trim() }
        .filter { it.isNotBlank() }

    var title = "AI Recipe"

    val ingredients = mutableListOf<String>()
    val steps = mutableListOf<String>()

    var mode = ""

    for (line in lines) {

        val lower = line.lowercase()

        // TITLE

        if (
            title == "AI Recipe" &&
            (
                    lower.startsWith("recipe name:") ||
                            lower.startsWith("title:")
                    )
        ) {

            title = line
                .substringAfter(":")
                .trim()

            continue
        }

        // INGREDIENT MODE

        if (lower.contains("ingredients")) {
            mode = "ING"
            continue
        }

        // STEP MODE

        if (
            lower.contains("steps") ||
            lower.contains("instructions")
        ) {

            mode = "STEP"
            continue
        }

        // INGREDIENTS

        if (mode == "ING") {

            val ingredient = line
                .removePrefix("-")
                .removePrefix("•")
                .trim()

            if (
                ingredient.isNotBlank() &&
                !ingredient.lowercase().contains("steps")
            ) {

                ingredients.add(ingredient)
            }
        }

        // STEPS

        if (mode == "STEP") {

            val cleanedStep = line
                .replace(Regex("^\\d+\\."), "")
                .trim()

            if (cleanedStep.isNotBlank()) {
                steps.add(cleanedStep)
            }
        }
    }

    return ParsedRecipe(
        title = title,
        ingredients = ingredients,
        steps = steps
    )
}