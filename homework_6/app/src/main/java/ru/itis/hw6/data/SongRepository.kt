package ru.itis.hw6.data

import kotlinx.coroutines.flow.Flow
import ru.itis.hw6.domain.Song
import ru.itis.hw6.domain.SongDetails

interface SongRepository {

    fun searchSong(query: String): Flow<List<Song>>

    suspend fun getSongDetails(id: Long): SongDetails

}