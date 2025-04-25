package com.example.visionverse.data.remote.api

import com.example.visionverse.data.remote.dto.MangaResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MangaApi {

    @GET("manga/fetch")
    suspend fun fetchManga(
        @Query("page") page: Int,
        @Query("genres") genres: String = "Comedy,Action",
        @Query("nsfw") nsfw: Boolean = false,
        @Query("type") type: String = "all"
    ): MangaResponse

    @GET("manga/search")
    suspend fun searchManga(
        @Query("text") query: String,
        @Query("nsfw") nsfw: Boolean = true,
        @Query("type") type: String = "all"
    ): MangaResponse
}
