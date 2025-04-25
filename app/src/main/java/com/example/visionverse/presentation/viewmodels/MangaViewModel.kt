package com.example.visionverse.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visionverse.data.local.manga.MangaEntity
import com.example.visionverse.domain.usecase.manga.MangaUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MangaViewModel @Inject constructor(
    private val useCases: MangaUseCases
) : ViewModel() {

    var mangaList by mutableStateOf<List<MangaEntity>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var searchQuery by mutableStateOf("")
        private set

    var isSearchActive by mutableStateOf(false)
        private set

    private var currentPage = 1

    init {
        loadManga()
    }

    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
        if (newQuery.isBlank()) {
            isSearchActive = false
            mangaList = emptyList()
            loadManga()
        }
    }

    fun loadManga() {
        if (isSearchActive) return // 🚫 Don't load default manga during search

        viewModelScope.launch {
            isLoading = true
            val newManga = useCases.fetchManga(currentPage)
            mangaList = mangaList + newManga
            currentPage++
            isLoading = false
        }
    }

    fun observeCachedManga() {
        if (isSearchActive) return

        viewModelScope.launch {
            useCases.getCachedManga().collect {
                mangaList = it
            }
        }
    }

    fun searchManga() {
        viewModelScope.launch {
            isLoading = true
            isSearchActive = true
            val searchResult = useCases.searchManga(searchQuery.trim())
            mangaList = searchResult
            isLoading = false
        }
    }

    fun clearSearch() {
        searchQuery = ""
        isSearchActive = false
        mangaList = emptyList()
        currentPage = 1
        loadManga()
    }
}
