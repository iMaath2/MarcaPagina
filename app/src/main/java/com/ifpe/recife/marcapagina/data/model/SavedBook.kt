package com.ifpe.recife.marcapagina.data.model

data class SavedBook(
    val id: String = "",
    val title: String = "",
    val author: String = "",
    val coverUrl: String = "",
    val userId: String = "",
    val status: String = "Quero ler",
    val currentPage: Int = 0,
    val totalPages: Int = 0

)