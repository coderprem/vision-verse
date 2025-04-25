package com.example.visionverse.presentation.screen.home

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import com.example.visionverse.R
import com.example.visionverse.data.local.manga.MangaEntity
import com.example.visionverse.presentation.viewmodels.MangaViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MangaScreen(
    modifier: Modifier = Modifier,
    viewModel: MangaViewModel = hiltViewModel(),
    onMangaClick: (MangaEntity) -> Unit = {}
) {
    val mangaList = viewModel.mangaList
    val isLoading = viewModel.isLoading
    var searchQuery = viewModel.searchQuery

    val gridState = rememberLazyGridState()

    Column(modifier = modifier.fillMaxSize()) {

        // 🔍 Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                viewModel.onSearchQueryChange(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            placeholder = { Text("Search manga...") },
            singleLine = true,
            trailingIcon = {
                Row {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = {
                            viewModel.clearSearch()
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                    IconButton(onClick = {
                        viewModel.searchManga()
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    viewModel.searchManga()
                }
            )
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            state = gridState,
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(mangaList) { manga ->
                MangaGridItem(manga = manga, onClick = { onMangaClick(manga) })
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }

    // Load cached manga on first launch
    LaunchedEffect(Unit) {
        if (!viewModel.isSearchActive) {
            viewModel.observeCachedManga()
        }
    }

    // Pagination logic: detect end-of-grid
    LaunchedEffect(gridState.firstVisibleItemIndex) {
        if (gridState.layoutInfo.totalItemsCount - gridState.firstVisibleItemIndex <= 5) {
            if (!viewModel.isSearchActive) {
                viewModel.loadManga()
            }
        }
    }
}

@Composable
fun MangaGridItem(manga: MangaEntity, onClick: () -> Unit) {
    val context = LocalContext.current

    val request = ImageRequest.Builder(context)
        .data(manga.thumb)
        .crossfade(true)
        .allowHardware(false)
        .listener(
            onSuccess = { _, _ -> Log.d("CoilSuccess", "Image loaded") },
            onError = { _, throwable ->
                Log.e("CoilError", "Failed: ${throwable.throwable?.message}")
                Log.e("CoilError", "URL: ${manga.thumb}")
            }
        )
        .build()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        AsyncImage(
            model = request,
            contentDescription = manga.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.ic_launcher_background),
            error = painterResource(id = R.drawable.ic_launcher_background)
        )

    }
}
