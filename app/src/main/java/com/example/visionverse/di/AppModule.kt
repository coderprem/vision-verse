package com.example.visionverse.di

import android.content.Context
import androidx.room.Room
import com.example.visionverse.data.local.AppDatabase
import com.example.visionverse.data.local.DataStoreManager
import com.example.visionverse.data.local.manga.MangaDao
import com.example.visionverse.data.local.user.UserDao
import com.example.visionverse.data.remote.api.MangaApi
import com.example.visionverse.data.repository.AuthRepositoryImpl
import com.example.visionverse.data.repository.MangaRepositoryImpl
import com.example.visionverse.domain.repository.AuthRepository
import com.example.visionverse.domain.repository.MangaRepository
import com.example.visionverse.domain.usecase.manga.FetchMangaUseCase
import com.example.visionverse.domain.usecase.manga.GetCachedMangaUseCase
import com.example.visionverse.domain.usecase.manga.MangaUseCases
import com.example.visionverse.domain.usecase.manga.SearchMangaUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMangaApi(): MangaApi {
        return Retrofit.Builder()
            .baseUrl("https://mangaverse-api.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder().addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("x-rapidapi-host", "mangaverse-api.p.rapidapi.com")
                        .addHeader("x-rapidapi-key", "d8257964c6msh42024ba15274f1fp159453jsn1e8729bf2e6f")
                        .build()
                    chain.proceed(request)
                }.build()
            )
            .build()
            .create(MangaApi::class.java)
    }

    // Provide Room Database
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "manga_app_db"
        ).fallbackToDestructiveMigration(false) // 💥 This will wipe and recreate DB on schema change
            .build()
    }

    // Provide UserDao
    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    // Provide AuthRepository
    @Provides
    @Singleton
    fun provideAuthRepository(userDao: UserDao): AuthRepository {
        return AuthRepositoryImpl(userDao)
    }

    // Provide DataStoreManager
    @Provides
    @Singleton
    fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager {
        return DataStoreManager(context)
    }

    @Provides
    fun provideMangaDao(appDatabase: AppDatabase): MangaDao {
        return appDatabase.mangaDao()
    }

    @Provides
    fun provideMangaRepository(api: MangaApi, dao: MangaDao): MangaRepository {
        return MangaRepositoryImpl(api, dao)
    }

    @Provides
    @Singleton
    fun provideMangaUseCases(repository: MangaRepository): MangaUseCases {
        return MangaUseCases(
            fetchManga = FetchMangaUseCase(repository),
            searchManga = SearchMangaUseCase(repository),
            getCachedManga = GetCachedMangaUseCase(repository),
        )
    }
}
