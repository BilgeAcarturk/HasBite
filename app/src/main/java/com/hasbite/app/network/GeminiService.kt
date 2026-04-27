package com.hasbite.app.network

import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject  
import com.hasbite.app.BuildConfig
import java.util.concurrent.TimeUnit

object GeminiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    // BuildConfig'ten API key'i al
    private val API_KEY = BuildConfig.GEMINI_API_KEY

    fun generateRecipe(prompt: String): String? {
        return try {
            val model = "gemini-2.5-flash-lite"

            val json = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", "Give me a clear recipe with ingredients and steps for: $prompt")
                            })
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.7)
                    put("maxOutputTokens", 800)
                })
            }

            val body = json.toString()
                .toRequestBody("application/json".toMediaType())

            val url = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$API_KEY"

            Log.d("GEMINI_URL", url)
            Log.d("GEMINI_REQUEST", json.toString())

            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

            client.newCall(request).execute().use { response ->
                val responseBody = response.body?.string()
                Log.d("GEMINI_RESPONSE_CODE", response.code.toString())
                Log.d("GEMINI_RESPONSE_BODY", responseBody ?: "null")

                if (!response.isSuccessful) {
                    Log.e("GEMINI_HTTP_ERROR", "HTTP ${response.code}: $responseBody")
                    return null
                }

                if (responseBody == null) {
                    Log.e("GEMINI_ERROR", "Response body is null")
                    return null
                }

                val jsonRes = JSONObject(responseBody)

                // Hata kontrolü
                if (jsonRes.has("error")) {
                    val errorObj = jsonRes.getJSONObject("error")
                    Log.e("GEMINI_API_ERROR", "Code: ${errorObj.optInt("code")}, Message: ${errorObj.optString("message")}")
                    return null
                }

                // Response'u parse et
                val candidates = jsonRes.optJSONArray("candidates")
                if (candidates == null || candidates.length() == 0) {
                    Log.e("GEMINI_PARSE", "No candidates found in response")
                    return null
                }

                val candidate = candidates.getJSONObject(0)
                val content = candidate.optJSONObject("content")
                if (content == null) {
                    Log.e("GEMINI_PARSE", "No content in candidate")
                    return null
                }

                val parts = content.optJSONArray("parts")
                if (parts == null || parts.length() == 0) {
                    Log.e("GEMINI_PARSE", "No parts in content")
                    return null
                }

                val text = parts.getJSONObject(0).optString("text")
                if (text.isEmpty()) {
                    Log.e("GEMINI_PARSE", "Empty text in response")
                    return null
                }

                Log.d("GEMINI_SUCCESS", "Recipe generated successfully")
                text
            }
        } catch (e: Exception) {
            Log.e("GEMINI_EXCEPTION", "Error: ${e.message}", e)
            null
        }
    }
}