package ru.itis.hw6.domain

import ru.itis.hw6.data.SongRepository

class GetSongDetailsUseCase(
    val repository: SongRepository
) {

    suspend operator fun invoke(
        id: Long
    ): SongDetails {
        return repository.getSongDetails(id)
    }
}