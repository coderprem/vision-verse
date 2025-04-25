package com.example.visionverse.domain.usecase.manga

import com.example.visionverse.data.local.manga.MangaEntity
import com.example.visionverse.domain.repository.MangaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCachedMangaUseCase @Inject constructor(
    private val repository: MangaRepository
) {
    operator fun invoke(): Flow<List<MangaEntity>> = repository.getCachedManga()
}