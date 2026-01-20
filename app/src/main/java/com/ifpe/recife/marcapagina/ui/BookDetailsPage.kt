package com.ifpe.recife.marcapagina.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ifpe.recife.marcapagina.data.model.SavedBook
import com.ifpe.recife.marcapagina.ui.viewmodel.LibraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsPage(
    navController: NavController,
    title: String,
    author: String,
    coverId: String,
    viewModel: LibraryViewModel = viewModel()
) {
    val context = LocalContext.current

    val coverUrl = if (coverId != "-1" && coverId != "null") {
        "https://covers.openlibrary.org/b/id/$coverId-L.jpg"
    } else {
        "https://via.placeholder.com/300x450.png?text=Sem+Capa"
    }

    val statusOptions = listOf("Quero Ler", "Lendo", "Lido")
    var selectedStatus by remember { mutableStateOf("Quero Ler") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do Livro") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                AsyncImage(
                    model = coverUrl,
                    contentDescription = "Capa do livro $title",
                    modifier = Modifier
                        .width(200.dp)
                        .height(300.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = title,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Por: $author",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Status da Leitura:",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                statusOptions.forEach { status ->
                    val isSelected = status == selectedStatus

                    OutlinedButton(
                        onClick = { selectedStatus = status },
                        colors = if (isSelected) {
                            ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        } else {
                            ButtonDefaults.outlinedButtonColors()
                        }
                    ) {
                        Text(text = status)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val bookToSave = SavedBook(
                        id = "$title-$author".hashCode().toString(),
                        title = title,
                        author = author,
                        coverUrl = coverUrl,
                        status = selectedStatus
                    )

                    viewModel.saveBook(
                        book = bookToSave,
                        onSuccess = {
                            Toast.makeText(context, "Livro salvo em '$selectedStatus'!", Toast.LENGTH_SHORT).show()
                        },
                        onFailure = { erro ->
                            Toast.makeText(context, "Erro ao salvar: $erro", Toast.LENGTH_LONG).show()
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Adicionar à Minha Estante", fontSize = 18.sp)
            }
        }
    }
}