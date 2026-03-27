package ru.itis.hw6.data

import ru.itis.hw6.domain.Song
import ru.itis.hw6.domain.SongDetails

fun SongResponseModel.toEntity(): Song {
    return Song(
        id = id,
        title = title,
        author = artistName,
        thumbnailUrl = headerImageThumbnailUrl
    )
}

fun SongDetailsResponseModel.toEntity(lyrics: String): SongDetails {
    return SongDetails(
        id = id,
        title = title,
        author = artistName,
        album = album.name,
        releaseDate = releaseDate,
        imageUrl = headerImageThumbnailUrl,
        lyrics = lyrics
    )
}

