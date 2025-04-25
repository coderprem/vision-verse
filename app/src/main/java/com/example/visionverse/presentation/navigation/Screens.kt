package com.example.visionverse.presentation.navigation

import com.example.visionverse.data.local.manga.MangaEntity
import kotlinx.serialization.Serializable

sealed class Screens {
    @Serializable
    data object SignInScreen : Screens()

    @Serializable
    data object SignUpScreen : Screens()

    @Serializable
    data object HomeScreen: Screens()

    @Serializable
    data object FaceRecognitionScreen: Screens()

    @Serializable
    data object MangaScreen: Screens()

    @Serializable
    data class MangaDetailScreen(val manga: MangaEntity): Screens()
}