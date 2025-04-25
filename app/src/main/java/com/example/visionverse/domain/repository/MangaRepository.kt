package com.example.visionverse.domain.repository

import com.example.visionverse.data.local.manga.MangaEntity
import kotlinx.coroutines.flow.Flow

interface MangaRepository {
    suspend fun fetchManga(page: Int): List<MangaEntity>
    suspend fun searchManga(query: String): List<MangaEntity>
    fun getCachedManga(): Flow<List<MangaEntity>>
}