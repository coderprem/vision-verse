package com.example.visionverse.domain.usecase.manga

import com.example.visionverse.data.local.manga.MangaEntity
import com.example.visionverse.domain.repository.MangaRepository
import javax.inject.Inject

class FetchMangaUseCase @Inject constructor(
    private val repository: MangaRepository
) {
    suspend operator fun invoke(page: Int): List<MangaEntity> = repository.fetchManga(page)
}