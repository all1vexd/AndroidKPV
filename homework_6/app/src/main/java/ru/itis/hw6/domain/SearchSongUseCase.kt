package ru.itis.hw6.domain

import kotlinx.coroutines.flow.Flow
import ru.itis.hw6.data.SongRepository

class SearchSongUseCase(
    private val repository: SongRepository
) {

    operator fun invoke(query: String): Flow<List<Song>> {
        return repository.searchSong(query)
    }
}