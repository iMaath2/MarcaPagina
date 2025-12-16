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

    val coverUrl = if (coverId != "-1") {
        "https://covers.openlibrary.org/b/id/$coverId-L.jpg"
    } else {
        "https://via.placeholder.com/300x450.png?text=Sem+Capa"
    }

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
            Card(elevation = CardDefaults.cardElevation(8.dp), shape = RoundedCornerShape(8.dp)) {
                AsyncImage(
                    model = coverUrl,
                    contentDescription = null,
                    modifier = Modifier.width(200.dp).height(300.dp).clip(RoundedCornerShape(8.dp)),
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

            Button(
                onClick = {
                    // Cria objeto para salvar
                    val bookToSave = SavedBook(
                        id = "$title-$author".hashCode().toString(),
                        title = title,
                        author = author,
                        coverUrl = coverUrl
                    )
                    viewModel.saveBook(bookToSave) {
                        Toast.makeText(context, "Livro adicionado à estante!", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Adicionar à Minha Estante")
            }
        }
    }
}