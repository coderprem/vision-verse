package com.example.visionverse.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.visionverse.data.local.manga.MangaDao
import com.example.visionverse.data.local.manga.MangaEntity
import com.example.visionverse.data.local.user.UserDao
import com.example.visionverse.data.local.user.UserEntity
import com.example.visionverse.utils.Converters

@Database(entities = [UserEntity::class, MangaEntity::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun mangaDao(): MangaDao
}