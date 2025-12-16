package com.ifpe.recife.marcapagina.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ifpe.recife.marcapagina.data.model.SavedBook

class LibraryViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _savedBooks = mutableStateListOf<SavedBook>()
    val savedBooks: List<SavedBook> get() = _savedBooks

    init {
        fetchBooks()
    }

    private fun fetchBooks() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("users").document(userId).collection("books")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("LibraryViewModel", "Erro ao ouvir livros", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    _savedBooks.clear()
                    for (doc in snapshot) {
                        val book = doc.toObject(SavedBook::class.java)
                        _savedBooks.add(book)
                    }
                }
            }
    }

    fun saveBook(book: SavedBook, onSuccess: () -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        val bookToSave = book.copy(userId = userId)

        db.collection("users").document(userId).collection("books")
            .document(book.id)
            .set(bookToSave)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.w("LibraryViewModel", "Erro ao salvar", e)
            }
    }

    fun removeBook(bookId: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val userId = auth.currentUser?.uid ?: return

        db.collection("users").document(userId).collection("books")
            .document(bookId)
            .delete()
            .addOnSuccessListener {
                onSuccess() //
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Erro ao remover livro")
            }
    }
}
