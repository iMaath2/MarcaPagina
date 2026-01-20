package com.ifpe.recife.marcapagina.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
    val tabs = listOf("Todos", "Quero Ler", "Lendo", "Lido")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    var showDialog by remember { mutableStateOf(false) }
    var selectedBookForUpdate by remember { mutableStateOf<SavedBook?>(null) }

    val filteredBooks = if (selectedTabIndex == 0) {
        viewModel.savedBooks
    } else {
        viewModel.savedBooks.filter { it.status == tabs[selectedTabIndex] }
    }

    if (showDialog && selectedBookForUpdate != null) {
        UpdateProgressDialog(
            book = selectedBookForUpdate!!,
            onDismiss = { showDialog = false },
            onConfirm = { current, total ->
                viewModel.updateBookProgress(selectedBookForUpdate!!.id, current, total)
                showDialog = false
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD))
    ) {
        Column(modifier = Modifier.background(Color.White)) {
            Text(
                text = "Minhas Estantes",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                edgePadding = 16.dp,
                containerColor = Color.White,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            if (filteredBooks.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhum livro aqui.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp)
                ) {
                    items(filteredBooks) { book ->
                        SavedBookItem(
                            book = book,
                            onDelete = {
                                viewModel.removeBook(book.id, {}, {})
                            },
                            onUpdateClick = {
                                selectedBookForUpdate = book
                                showDialog = true
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SavedBookItem(
    book: SavedBook,
    onDelete: () -> Unit,
    onUpdateClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = book.coverUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .width(60.dp)
                        .height(90.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = book.title, fontWeight = FontWeight.Bold, maxLines = 1)
                    Text(text = book.author, fontSize = 14.sp, color = Color.Gray, maxLines = 1)

                    Spacer(modifier = Modifier.height(4.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = book.status,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }

                Column {
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Remover", tint = Color.Red)
                    }
                    if (book.status == "Lendo") {
                        IconButton(onClick = onUpdateClick) {
                            Icon(Icons.Default.Edit, contentDescription = "Atualizar Progresso", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }

            if (book.status == "Lendo" && book.totalPages > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Column {
                    val progress = book.currentPage.toFloat() / book.totalPages.toFloat()
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "${(progress * 100).toInt()}%", fontSize = 12.sp, color = Color.Gray)
                        Text(text = "${book.currentPage} / ${book.totalPages} pág", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun UpdateProgressDialog(
    book: SavedBook,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    var currentText by remember { mutableStateOf(book.currentPage.toString()) }
    var totalText by remember { mutableStateOf(if (book.totalPages > 0) book.totalPages.toString() else "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Atualizar Leitura") },
        text = {
            Column {
                Text(text = book.title, fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = currentText,
                    onValueChange = { currentText = it },
                    label = { Text("Página Atual") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = totalText,
                    onValueChange = { totalText = it },
                    label = { Text("Total de Páginas") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val c = currentText.toIntOrNull() ?: 0
                    val t = totalText.toIntOrNull() ?: 0
                    if (t > 0 && c <= t) {
                        onConfirm(c, t)
                    }
                }
            ) {
                Text("Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}