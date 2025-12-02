package com.ifpe.recife.marcapagina.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ifpe.recife.marcapagina.data.api.RetrofitClient
import com.ifpe.recife.marcapagina.data.model.BookDto
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val _books = mutableStateListOf<BookDto>()
    val books: List<BookDto>
        get() = _books

    var isLoading = mutableStateListOf<Boolean>()

    fun search(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            try {
                _books.clear()

                val response = RetrofitClient.service.searchBooks(query)

                _books.addAll(response.docs)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}