package com.hasbite.app.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth // 1. BU EKLENDİ
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.hasbite.app.ui.screens.ShoppingItem

class ShoppingViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance() // 2. BU EKLENDİ

    var items = mutableStateListOf<ShoppingItem>()
        private set

    // 3. SABİT userId SİLİNDİ, YERİNE BU GELDİ:
    private val userId: String?
        get() = auth.currentUser?.uid


    // 🔥 REAL-TIME LISTENER
    fun observeItems(listName: String) {

        val currentUid = userId ?: return

        db.collection("users")
            .document(currentUid)
            .collection("shoppingList")
            .whereEqualTo("listName", listName)
            .addSnapshotListener { snapshot, _ ->

                val list = snapshot?.documents?.map {

                    ShoppingItem(
                        id = it.id,
                        name = it.getString("name") ?: "",
                        checked = it.getBoolean("checked") ?: false
                    )

                } ?: emptyList()

                items.clear()
                items.addAll(list)
            }
    }

    // ➕ ITEM EKLE
    fun addItem(name: String, listName: String) {
        val currentUid = userId ?: return
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return

        db.collection("users")
            .document(currentUid)
            .collection("shoppingList")
            .whereEqualTo("name", trimmed)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    val item = hashMapOf(
                        "name" to trimmed,
                        "checked" to false,
                        "listName" to listName,
                        "createdAt" to FieldValue.serverTimestamp()
                    )

                    db.collection("users")
                        .document(currentUid)
                        .collection("shoppingList")
                        .add(item)
                }
            }
    }

    // ✔️ CHECKBOX
    // ✔️ CHECKBOX GÜNCELLEME
    fun toggleItem(item: ShoppingItem) {
        val currentUid = userId ?: return

        db.collection("users")
            .document(currentUid)
            .collection("shoppingList")
            .document(item.id) // Artık 'document' referansını tanıyacaktır
            .update("checked", !item.checked)
    }

    // 🗑️ TEK SİL
    fun deleteItem(item: ShoppingItem) {
        val currentUid = userId ?: return
        db.collection("users")
            .document(currentUid)
            .collection("shoppingList")
            .document(item.id)
            .delete()
    }

    // 🧹 COMPLETED TEMİZLE
    fun clearCompleted() {
        val currentUid = userId ?: return
        db.collection("users")
            .document(currentUid)
            .collection("shoppingList")
            .whereEqualTo("checked", true)
            .get()
            .addOnSuccessListener { result ->
                result.documents.forEach {
                    it.reference.delete()
                }
            }
    }
}