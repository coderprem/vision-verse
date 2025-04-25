package com.example.visionverse.data.local.manga

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.jvm.java

@Parcelize
@Serializable
@Entity(tableName = "manga")
data class MangaEntity(
    @PrimaryKey val id: String,
    val title: String,
    val thumb: String,
    val summary: String,
    val sub_title: String,
    val genres: List<String>,
    val nsfw: Boolean,
) : Parcelable

val MangaEntityData = object : NavType<MangaEntity>(
    isNullableAllowed = false
) {
    override fun get(bundle: Bundle, key: String): MangaEntity? {
        return return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, MangaEntity::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): MangaEntity {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: MangaEntity) {
        bundle.putParcelable(key, value)
    }

    override fun serializeAsValue(value: MangaEntity): String {
        return Uri.encode(Json.encodeToString<MangaEntity>(value))
    }
}