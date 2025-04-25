package com.example.visionverse.domain.usecase.manga

import com.example.visionverse.data.local.manga.MangaEntity
import com.example.visionverse.domain.repository.MangaRepository
import javax.inject.Inject

class SearchMangaUseCase @Inject constructor(
    private val repository: MangaRepository
) {
    suspend operator fun invoke(query: String): List<MangaEntity> = repository.searchManga(query)
}