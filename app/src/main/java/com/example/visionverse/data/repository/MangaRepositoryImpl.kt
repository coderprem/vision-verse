package com.example.visionverse.data.repository

import com.example.visionverse.data.local.manga.MangaDao
import com.example.visionverse.data.local.manga.MangaEntity
import com.example.visionverse.data.remote.api.MangaApi
import com.example.visionverse.domain.repository.MangaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MangaRepositoryImpl @Inject constructor(
    private val api: MangaApi,
    private val dao: MangaDao
) : MangaRepository {

    override suspend fun fetchManga(page: Int): List<MangaEntity> {
        return try {
            val response = api.fetchManga(page = page)
            val mangaEntities = response.data.map {
                MangaEntity(
                    id = it.id,
                    title = it.title,
                    thumb = it.thumb,
                    summary = it.summary,
                    sub_title = it.sub_title,
                    genres = it.genres,
                    nsfw = it.nsfw
                )
            }
            dao.insertAll(mangaEntities)
            mangaEntities
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun searchManga(query: String): List<MangaEntity> {
        return try {
            val response = api.searchManga(query)
            response.data.map {
                MangaEntity(
                    id = it.id,
                    title = it.title,
                    thumb = it.thumb,
                    summary = it.summary,
                    sub_title = it.sub_title,
                    genres = it.genres,
                    nsfw = it.nsfw
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun getCachedManga(): Flow<List<MangaEntity>> = dao.getAllManga()
}
