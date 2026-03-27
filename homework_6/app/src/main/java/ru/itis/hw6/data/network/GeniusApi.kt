package ru.itis.hw6.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.itis.hw6.data.SearchResponseById
import ru.itis.hw6.data.SearchResponseByQuery

interface GeniusApi {

    @GET("/search")
    suspend fun getSongs(
        @Query("q") query: String
    ): SearchResponseByQuery

    @GET("/songs/{id}")
    suspend fun getSongById(
        @Path("id") id: Long
    ): SearchResponseById

}