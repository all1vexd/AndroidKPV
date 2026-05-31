package ru.itis.hw6.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.itis.hw6.BuildConfig
import ru.itis.hw6.data.SongRepository
import ru.itis.hw6.data.SongRepositoryImpl
import ru.itis.hw6.data.network.AuthorizationInterceptor
import ru.itis.hw6.data.network.GeniusApi
import ru.itis.hw6.domain.GetSongDetailsUseCase
import ru.itis.hw6.domain.SearchSongUseCase
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(AuthorizationInterceptor(BuildConfig.GENIUS_API_KEY))
        .build()

    @Provides
    @Singleton
    fun provideGeniusApi(client: OkHttpClient): GeniusApi = Retrofit.Builder()
        .baseUrl(BuildConfig.GENIUS_API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(GeniusApi::class.java)

    @Provides
    @Singleton
    fun provideSongRepository(api: GeniusApi): SongRepository = SongRepositoryImpl(api)

    @Provides
    fun provideSearchSongUseCase(repository: SongRepository): SearchSongUseCase =
        SearchSongUseCase(repository)

    @Provides
    fun provideGetSongDetailsUseCase(repository: SongRepository): GetSongDetailsUseCase =
        GetSongDetailsUseCase(repository)
}
