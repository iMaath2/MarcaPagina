package com.ifpe.recife.marcapagina.data.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    val docs: List<BookDto>
)

data class BookDto(
    @SerializedName("key") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("author_name") val authorNames: List<String>?,
    @SerializedName("cover_i") val coverId: Long?
) {
    fun getCoverUrl(): String {
        return if (coverId != null) {
            "https://covers.openlibrary.org/b/id/$coverId-M.jpg"
        } else {
            "https://via.placeholder.com/150x220.png?text=Sem+Capa"
        }
    }

    fun getAuthors(): String {
        return authorNames?.joinToString(", ") ?: "Autor Desconhecido"
    }
}