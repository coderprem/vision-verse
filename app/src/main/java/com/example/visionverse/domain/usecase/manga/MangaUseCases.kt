package com.example.visionverse.domain.usecase.manga

data class MangaUseCases(
    val fetchManga: FetchMangaUseCase,
    val searchManga: SearchMangaUseCase,
    val getCachedManga: GetCachedMangaUseCase
)