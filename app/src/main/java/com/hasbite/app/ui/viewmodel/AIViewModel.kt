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
    val lines = text.lines().map { it.trim() }.filter { it.isNotBlank() }

    var title = "AI Recipe"
    val ingredients = mutableListOf<String>()
    val steps = mutableListOf<String>()
    var mode = ""

    for (line in lines) {
        if (title == "AI Recipe") {
            val lowerLine = line.lowercase()
            // Eğer satır "##" ile başlıyorsa veya "here's" içermeyen kısa bir isimse başlık yap
            if (line.startsWith("##")) {
                title = line.replace("#", "").trim()
            } else if (line.length < 50 && !lowerLine.contains("here's")) {
                title = line
            }
        }

        // 2. MOD BELİRLEME
        if (line.lowercase().contains("ingredients")) {
            mode = "ING"
            continue
        }
        if (line.lowercase().contains("instructions") || line.lowercase().contains("steps")) {
            mode = "STEP"
            continue
        }

        // 3. VERİLERİ TOPLAMA
        if (mode == "ING" && (line.startsWith("*") || line.startsWith("-") || line.startsWith("•"))) {
            ingredients.add(line.removePrefix("*").removePrefix("-").removePrefix("•").trim())
        }

        if (mode == "STEP" && (line.firstOrNull()?.isDigit() == true)) {
            steps.add(line)
        }
    }

    return ParsedRecipe(title, ingredients, steps)
}