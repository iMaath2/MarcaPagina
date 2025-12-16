package com.ifpe.recife.marcapagina.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.ifpe.recife.marcapagina.data.model.SavedBook
import com.ifpe.recife.marcapagina.ui.viewmodel.LibraryViewModel

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    viewModel: LibraryViewModel = viewModel()
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "Minha Estante",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (viewModel.savedBooks.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Sua estante está vazia.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(viewModel.savedBooks) { book ->
                        SavedBookItem(
                            book = book,
                            onDelete = {
                                viewModel.removeBook(
                                    bookId = book.id,
                                    onSuccess = {
                                        Toast.makeText(context, "Livro removido com sucesso!", Toast.LENGTH_SHORT).show()
                                    },
                                    onFailure = { erro ->
                                        Toast.makeText(context, "Erro: $erro", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SavedBookItem(book: SavedBook, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().height(100.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = book.coverUrl,
                contentDescription = null,
                modifier = Modifier
                    .width(60.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = book.title, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(text = book.author, fontSize = 14.sp, color = Color.Gray, maxLines = 1)
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Remover", tint = Color.Red)
            }
        }
    }
}